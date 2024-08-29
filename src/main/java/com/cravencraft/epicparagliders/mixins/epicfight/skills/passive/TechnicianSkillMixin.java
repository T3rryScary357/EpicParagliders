package com.cravencraft.epicparagliders.mixins.epicfight.skills.passive;

import com.cravencraft.epicparagliders.capabilities.StaminaOverride;
import com.cravencraft.epicparagliders.config.ConfigManager;
import com.cravencraft.epicparagliders.utils.MathUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tictim.paraglider.forge.capability.PlayerMovementProvider;
import tictim.paraglider.impl.movement.PlayerMovement;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.skill.passive.TechnicianSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(TechnicianSkill.class)
public abstract class TechnicianSkillMixin extends PassiveSkill {

    public TechnicianSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    /**
     * Redirects the setStamina method call in the Technician skill to here. Uses the Paragliders stamina system along
     * with server config values to determine how much stamina should be drained or returned.
     */
    @Redirect(at = @At(value = "INVOKE", target = "Lyesman/epicfight/world/capabilities/entitypatch/player/PlayerPatch;setStamina(F)V"), remap = false, method = "lambda$onInitiate$0")
    private static void modifyConsumedStamina(PlayerPatch playerPatch, float originalValue) {
        PlayerMovement playerMovement = PlayerMovementProvider.of(playerPatch.getOriginal());
        StaminaOverride botwStamina = ((StaminaOverride) playerMovement.stamina());

        int technicianConsumption = botwStamina.getTotalActionStaminaCost();
        double technicianPercentModifier = ConfigManager.SERVER_CONFIG.technicianPercentModifier() * 0.01;

        // If the player is successful with the dodge, use one of these formulas depending on if the technician skill is set to drain stamina in the config.
        if (ConfigManager.SERVER_CONFIG.technicianDrain()) {
            technicianConsumption *= (1 - technicianPercentModifier);
        }
        else {
            int trueTotalMissing = (int) (MathUtils.calculateTriangularNumber(botwStamina.getTotalActionStaminaCost()) + (playerMovement.stamina().maxStamina() - playerMovement.stamina().stamina()));
            technicianConsumption = -(int) (MathUtils.calculateModifiedTriangularRoot(trueTotalMissing, technicianPercentModifier));
        }

        botwStamina.performingAction(true);
        playerPatch.setStamina(technicianConsumption);
    }
}
