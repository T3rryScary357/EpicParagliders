package com.cravencraft.epicparagliders.mixins.epicfight.skills.passive;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.passive.EnduranceSkill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(EnduranceSkill.class)
public abstract class EnduranceSkillMixin extends PassiveSkill {

    public EnduranceSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    /**
     * Redirects the max stun shield for the Endurance skill. Uses the Paragliders
     * stamina system now along with some extra math to ensure it's fairly balanced.
     *
     * @param playerPatch
     * @param staminaConsumed
     */
    @Redirect(at = @At(value = "INVOKE", target = "Lyesman/epicfight/world/capabilities/entitypatch/player/ServerPlayerPatch;setMaxStunShield(F)V"), remap = false, method = "executeOnServer")
    private void modifyMaxStunShield(ServerPlayerPatch playerPatch, float staminaConsumed) {
        playerPatch.setMaxStunShield((15.0F / playerPatch.getMaxStamina()) * staminaConsumed);
    }

    /**
     * Redirects the max stun shield for the Endurance skill. Uses the Paragliders
     * stamina system now along with some extra math to ensure it's fairly balanced.
     *
     * @param playerPatch
     * @param staminaConsumed
     */
    @Redirect(at = @At(value = "INVOKE", target = "Lyesman/epicfight/world/capabilities/entitypatch/player/ServerPlayerPatch;setStunShield(F)V"), remap = false, method = "executeOnServer")
    private void modifyStunShield(ServerPlayerPatch playerPatch, float staminaConsumed) {
        playerPatch.setStunShield((15.0F / playerPatch.getMaxStamina()) * staminaConsumed);
    }
}