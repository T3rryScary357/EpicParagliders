package net.cravencraft.epicparagliders.mixins.epicfight.skills.weaponinnate;

import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.cravencraft.epicparagliders.utils.MathUtils;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.weaponinnate.SimpleWeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(SimpleWeaponInnateSkill.class)
public abstract class SimpleSpecialAttackSkillMixin extends Skill {

    public SimpleSpecialAttackSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "executeOnServer", at = @At("HEAD"), remap = false)
    private void getPlayerPatch(ServerPlayerPatch executer, FriendlyByteBuf args, CallbackInfo ci) {
        PlayerMovement playerMovement = PlayerMovement.of(executer.getOriginal());
        int specialAttackStaminaConsumption = MathUtils.getAttackStaminaCost(executer.getOriginal());
        ((PlayerMovementInterface) playerMovement).setActionStaminaCostServerSide(specialAttackStaminaConsumption);
        ((PlayerMovementInterface) playerMovement).isAttackingServerSide(true);
    }
}
