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

//TODO: Reimport skill. To add back how it was originally done. Also, remove the unecessary setter calls in the ReRegister class after
public class NewTechnicianSkill extends Skill {
	private static final UUID EVENT_UUID = UUID.fromString("99e5c782-fdaf-11eb-9a03-0242ac130003");
	private static final SkillDataKey<Boolean> CURRENTLY_ACTIVATED = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private final UpdatedServerPlayerMovement updatedServerPlayerMovement;

	public NewTechnicianSkill(Builder builder, UpdatedServerPlayerMovement updatedServerPlayerMovement) {
		super(builder);
		this.updatedServerPlayerMovement = updatedServerPlayerMovement;
	}

	@Override
	public void onInitiate(SkillContainer container) {
		container.getExecuter();

		container.getDataManager().registerData(CURRENTLY_ACTIVATED);
		UpdatedServerPlayerMovement.instance.serverPlayerPatch.getEventListener().addEventListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
			if (event.getAnimation() instanceof DodgeAnimation) {
				container.getDataManager().setData(CURRENTLY_ACTIVATED, false);
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.HURT_EVENT_PRE, EVENT_UUID, (event) -> {
			ServerPlayerPatch executer = event.getPlayerPatch();

			if (executer.getAnimator().getPlayerFor(null).getAnimation() instanceof DodgeAnimation) {
				DamageSource damageSource = event.getDamageSource();

				if (executer.getEntityState().invulnerableTo(damageSource)) {
					if (!container.getDataManager().getDataValue(CURRENTLY_ACTIVATED)) {
						EpicParaglidersMod.LOGGER.info("Technician consumption: " + container.getSkill().getConsumption());
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