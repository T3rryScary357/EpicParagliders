package net.cravencraft.epicparagliders.skills;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.UpdatedServerPlayerMovement;
import net.minecraft.world.damagesource.DamageSource;
import yesman.epicfight.api.animation.types.DodgeAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.math.Formulars;
import yesman.epicfight.skill.*;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

import java.util.UUID;

public class NewTechnicianSkill extends PassiveSkill {
	private static final UUID EVENT_UUID = UUID.fromString("99e5c782-fdaf-11eb-9a03-0242ac130003");
	private static final SkillDataKey<Boolean> CURRENTLY_ACTIVATED = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private final UpdatedServerPlayerMovement updatedServerPlayerMovement;

	public NewTechnicianSkill(Builder<? extends Skill> builder, UpdatedServerPlayerMovement updatedServerPlayerMovement) {
		super(builder);
		this.updatedServerPlayerMovement = updatedServerPlayerMovement;
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		EpicParaglidersMod.LOGGER.info("IN TECH SKILL");
		container.getDataManager().registerData(CURRENTLY_ACTIVATED);
		container.getExecuter().getEventListener().addEventListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
			if (event.getAnimation() instanceof DodgeAnimation) {
				container.getDataManager().setData(CURRENTLY_ACTIVATED, false);
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.HURT_EVENT_PRE, EVENT_UUID, (event) -> {
			ServerPlayerPatch executer = event.getPlayerPatch();
			EpicParaglidersMod.LOGGER.info("IN TECH CLASS");
			if (executer.getAnimator().getPlayerFor(null).getAnimation() instanceof DodgeAnimation) {
				DamageSource damageSource = event.getDamageSource();
				EpicParaglidersMod.LOGGER.info("PAST FIRST IF");
				if (executer.getEntityState().invulnerableTo(damageSource)) {
					EpicParaglidersMod.LOGGER.info("PAST SECOND IF");
					if (!container.getDataManager().getDataValue(CURRENTLY_ACTIVATED)) {
						EpicParaglidersMod.LOGGER.info("Technician consumption: " + container.getSkill().getConsumption());
//						float consumption = Formulars.getStaminarConsumePenalty(executer.getWeight(), container.getSkill().getConsumption(), executer);
//						EpicParaglidersMod.LOGGER.info("Current weight: " + executer.getWeight());
						updatedServerPlayerMovement.skillStaminaCost = (int) Formulars.getStaminarConsumePenalty(executer.getWeight(), getConsumption(), executer);
						EpicParaglidersMod.LOGGER.info("Skill Consumption: " + updatedServerPlayerMovement.skillStaminaCost);
						updatedServerPlayerMovement.actionStaminaNeedsSync = true;
//						executer.setStamina(executer.getStamina() + consumption);
						container.getDataManager().setData(CURRENTLY_ACTIVATED, true);
						event.setCanceled(true);
						event.setResult(AttackResult.ResultType.FAILED);
					}
				}
			}
		});
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.HURT_EVENT_PRE, EVENT_UUID);
	}
}