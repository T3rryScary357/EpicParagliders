package net.cravencraft.epicparagliders.mixins.epicfight.capabilities;

import net.cravencraft.epicparagliders.EPModCfg;
import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.cravencraft.epicparagliders.gameasset.ExhaustionAnimations;
import net.cravencraft.epicparagliders.gameasset.ExhaustionMotions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

import static yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch.STAMINA;

@Mixin(PlayerPatch.class)
public abstract class PlayerPatchMixin<T extends Player> extends LivingEntityPatch<T> {
    @Shadow public abstract float getStamina();

    @Inject(method = "onConstructed", at = @At("TAIL"), cancellable = true, remap = false)
    private void constructStamina(CallbackInfo ci) {
        (this.original).getEntityData().set(STAMINA, 15.0f);
    }

    /**
     * Modifies the getMaxStamina method to return the max stamina from the Paragliders
     * stamina system.
     *
     * @param cir
     */
    @Inject(method = "getMaxStamina", at = @At("RETURN"), cancellable = true, remap = false)
    private void getMaxStamina(CallbackInfoReturnable<Float> cir) {
        PlayerMovement playerMovement = PlayerMovement.of(this.getOriginal());
        cir.setReturnValue((float) playerMovement.getMaxStamina());
    }

    /**
     * Modifies the getStamina method to return the current stamina from the Paragliders
     * stamina system.
     *
     * @param cir
     */
    @Inject(method = "getStamina", at = @At("HEAD"), cancellable = true, remap = false)
    private void getStamina(CallbackInfoReturnable<Float> cir) {
        PlayerMovement playerMovement = PlayerMovement.of(this.getOriginal());
        cir.setReturnValue((float) playerMovement.getStamina());
    }

    /**
     * Set stamina now sets stamina for the Paragliders stamina system, and only if
     * the player is performing a skill action or an attack action.
     *
     * @param value
     * @param ci
     */
    @Inject(method = "setStamina", at = @At("HEAD"), cancellable = true, remap = false)
    private void setStamina(float value, CallbackInfo ci) {
        PlayerMovement playerMovement = PlayerMovement.of(this.getOriginal());

        // Easy way to ensure only my stamina values are being applied.
        // So I don't have to edit 5+ different methods.
        if (((PlayerMovementInterface) playerMovement).isPerformingActionServerSide() || ((PlayerMovementInterface) playerMovement).isAttackingServerSide()) {
            EpicParaglidersMod.LOGGER.info("PLAYER PATCH STAMINA USED: " + value);
            ((PlayerMovementInterface) playerMovement).setActionStaminaCostServerSide((int) value);
        }
        ci.cancel();
    }

    /**
     * Need to modify the stamina consumption math slightly in order to properly work with
     * Paraglider's stamina system & values.
     *
     * @param amount
     * @param cir
     */
    @Inject(method = "getModifiedStaminaConsume", at = @At("RETURN"), remap = false, cancellable = true)
    private void modifyStaminaConsumption(float amount, CallbackInfoReturnable<Float> cir) {
        float attenuation = Mth.clamp(this.original.level.getGameRules().getInt(EpicFightGamerules.WEIGHT_PENALTY), 0, 100) / 100.0F;
        float weight = this.getWeight();

        cir.setReturnValue((float) (((weight / 40.0F - 1.0F) * 0.3F * attenuation + 1.0F) * amount * EPModCfg.baseDodgeStaminaMultiplier()));
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
