package com.cravencraft.epicparagliders.mixins.epicfight.skills;

import com.cravencraft.epicparagliders.utils.MathUtils;
import com.cravencraft.epicparagliders.capabilities.StaminaOverride;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.forge.capability.PlayerMovementProvider;
import yesman.epicfight.skill.BasicAttack;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(BasicAttack.class)
public abstract class BasicAttackMixin extends Skill {

    public BasicAttackMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "executeOnServer", at = @At("TAIL"), remap = false)
    private void getPlayerPatch(ServerPlayerPatch executor, FriendlyByteBuf args, CallbackInfo ci) {
        StaminaOverride botwStamina = ((StaminaOverride) PlayerMovementProvider.of(executor.getOriginal()).stamina());

        botwStamina.attacking(true);
        botwStamina.setActionStaminaCost(MathUtils.getAttackStaminaCost(executor.getOriginal()));
    }
}
