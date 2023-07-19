package net.cravencraft.epicparagliders.skills;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.UpdatedPlayerMovement;
import net.cravencraft.epicparagliders.capabilities.UpdatedServerPlayerMovement;
import net.cravencraft.epicparagliders.utils.MathUtils;
import yesman.epicfight.skill.PassiveSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

import java.util.UUID;

public class NewStaminaPillagerSkill extends PassiveSkill {
	private static final double STAMINA_PERCENTAGE_RETURNED = 0.3F;
	private static final UUID EVENT_UUID = UUID.fromString("20807880-fd30-11eb-9a03-0242ac130003");

	public NewStaminaPillagerSkill(Builder<? extends Skill> builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID, (event) -> {
			if (!event.getTarget().isAlive()) {
				float stamina = UpdatedServerPlayerMovement.instance.serverPlayerMovement.getStamina();
				float missingStamina = UpdatedServerPlayerMovement.instance.serverPlayerMovement.getMaxStamina() - stamina;
				UpdatedServerPlayerMovement.instance.skillStaminaGain = (int) MathUtils.calculateModifiedTriangularRoot(missingStamina, STAMINA_PERCENTAGE_RETURNED);
				UpdatedServerPlayerMovement.instance.actionStaminaNeedsSync = true;
			}
		});
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID);
	}
}