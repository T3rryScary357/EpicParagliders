package com.cravencraft.epicparagliders.mixins.epicfight.skills.passive;

import com.cravencraft.epicparagliders.config.ConfigManager;
import com.cravencraft.epicparagliders.capabilities.StaminaOverride;
import com.cravencraft.epicparagliders.utils.MathUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.forge.capability.PlayerMovementProvider;
import tictim.paraglider.impl.movement.PlayerMovement;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.skill.passive.StaminaPillagerSkill;
import yesman.epicfight.world.entity.eventlistener.DealtDamageEvent;

@Mixin(StaminaPillagerSkill.class)
public abstract class StaminaPillagerSkillMixin extends PassiveSkill {

    public StaminaPillagerSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    /**
     * Works like the original skill, but now is integrated with the new system.
     * Return percentage is now based on the server config.
     *
     * @param event
     * @param ci
     */
    @Inject(method = "lambda$onInitiate$0", at = @At("HEAD"), remap = false, cancellable = true)
    private void getPlayerPatch(DealtDamageEvent.Damage event, CallbackInfo ci) {
        if (!event.getTarget().isAlive()) {
            PlayerMovement playerMovement = PlayerMovementProvider.of(event.getPlayerPatch().getOriginal());
            StaminaOverride botwStamina = ((StaminaOverride) playerMovement.stamina());
            double staminaPillagerPercentModifier = ConfigManager.SERVER_CONFIG.staminaPillagerPercentModifier() * 0.01;

            int trueTotalMissing = (int) (MathUtils.calculateTriangularNumber(botwStamina.getTotalActionStaminaCost()) + (playerMovement.stamina().maxStamina() - playerMovement.stamina().stamina()));
            botwStamina.performingAction(true);
            event.getPlayerPatch().setStamina(-(int) (MathUtils.calculateModifiedTriangularRoot(trueTotalMissing, staminaPillagerPercentModifier)));
        }
        ci.cancel();
    }
}