package net.cravencraft.epicparagliders.mixins.epicfight.skills;

import net.cravencraft.epicparagliders.capabilities.StaminaOverride;
import net.cravencraft.epicparagliders.utils.MathUtils;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.forge.capability.PlayerMovementProvider;
import yesman.epicfight.skill.AirAttack;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(AirAttack.class)
public abstract class AirAttackMixin extends Skill {

    public AirAttackMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "executeOnServer", at = @At("TAIL"), remap = false)
    private void getPlayerPatch(ServerPlayerPatch executer, FriendlyByteBuf args, CallbackInfo ci) {

        StaminaOverride botwStamina = ((StaminaOverride) PlayerMovementProvider.of(executer.getOriginal()).stamina());

        botwStamina.attacking(true);
        botwStamina.setActionStaminaCost(MathUtils.getAttackStaminaCost(executer.getOriginal()) + 3);
    }
}
