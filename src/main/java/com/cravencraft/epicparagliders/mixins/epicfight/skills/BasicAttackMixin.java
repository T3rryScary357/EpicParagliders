package com.cravencraft.epicparagliders.mixins.epicfight.skills;

import com.cravencraft.epicparagliders.utils.MathUtils;
import com.cravencraft.epicparagliders.capabilities.StaminaOverride;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.forge.capability.PlayerMovementProvider;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.BasicAttack;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(BasicAttack.class)
public abstract class BasicAttackMixin extends Skill {

    //TODO: Major issue with attacking stamina and dodging stamina. Look into later
    public BasicAttackMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    // TODO: Mainly working now. Isn't working for the first swing, but the second. Maybe it has to do with combo count?
    //       Or set attacking to true?
    @Inject(method = "executeOnServer", at = @At("TAIL"), remap = false)
    private void getPlayerPatch(ServerPlayerPatch executor, FriendlyByteBuf args, CallbackInfo ci) {
        EpicFightMod.LOGGER.info("BASIC ATTACK STAMINA COST: {}", MathUtils.getAttackStaminaCost(executor.getOriginal()));
        StaminaOverride botwStamina = ((StaminaOverride) PlayerMovementProvider.of(executor.getOriginal()).stamina());
        EpicFightMod.LOGGER.info("AFTER TRYING TO GET STAMINA: ");
        EpicFightMod.LOGGER.info("AFTER TRYING TO GET STAMINA: {}", botwStamina.isAttacking());

        botwStamina.attacking(true);
        botwStamina.setActionStaminaCost(MathUtils.getAttackStaminaCost(executor.getOriginal()));
    }
}
