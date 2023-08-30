package net.cravencraft.epicparagliders.mixins.capabilities;

import net.cravencraft.epicparagliders.gameasset.ExhaustionAnimations;
import net.cravencraft.epicparagliders.gameasset.ExhaustionMotions;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import static yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch.STAMINA;

@Mixin(PlayerPatch.class)
public abstract class PlayerPatchMixin<T extends Player> extends LivingEntityPatch<T> {

    @Inject(method = "onConstructed", at = @At("TAIL"), cancellable = true, remap = false)
    private void constructStamina(CallbackInfo ci) {
        (this.original).getEntityData().set(STAMINA, 15.0f);
    }

    @Inject(method = "setStamina", at = @At("HEAD"), cancellable = true, remap = false)
    private void setStamina(float value, CallbackInfo ci) {

        ci.cancel();
    }

    @OnlyIn(Dist.CLIENT)
    @Inject(method = "initAnimator", at = @At("HEAD"), remap = false)
    private void addExhaustionAnimations(ClientAnimator clientAnimator, CallbackInfo ci) {
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE, ExhaustionAnimations.EXHAUSTED_IDLE);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK, ExhaustionAnimations.EXHAUSTED_WALK);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE_CROSSBOW, ExhaustionAnimations.EXHAUSTED_IDLE_CROSSBOW);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_CROSSBOW, ExhaustionAnimations.EXHAUSTED_WALK_CROSSBOW);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE_GREATSWORD, ExhaustionAnimations.EXHAUSTED_IDLE_GREATSWORD);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_GREATSWORD, ExhaustionAnimations.EXHAUSTED_WALK_GREATSWORD);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE_TACHI, ExhaustionAnimations.EXHAUSTED_IDLE_TACHI);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_TACHI, ExhaustionAnimations.EXHAUSTED_WALK_TACHI);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE_SPEAR, ExhaustionAnimations.EXHAUSTED_IDLE_SPEAR);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_SPEAR, ExhaustionAnimations.EXHAUSTED_WALK_SPEAR);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE_LIECHTENAUER, ExhaustionAnimations.EXHAUSTED_IDLE_LIECHTENAUER);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_LIECHTENAUER, ExhaustionAnimations.EXHAUSTED_WALK_LIECHTENAUER);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE_SHEATH, ExhaustionAnimations.EXHAUSTED_IDLE_SHEATH);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_SHEATH, ExhaustionAnimations.EXHAUSTED_WALK_SHEATH);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_IDLE_UNSHEATH, ExhaustionAnimations.EXHAUSTED_IDLE_UNSHEATH);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_UNSHEATH, ExhaustionAnimations.EXHAUSTED_WALK_UNSHEATH);
        clientAnimator.addLivingAnimation(ExhaustionMotions.EXHAUSTED_WALK_KATANA, ExhaustionAnimations.EXHAUSTED_WALK_KATANA);
    }
}
