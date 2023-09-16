package net.cravencraft.epicparagliders.mixins.client;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.gameasset.ExhaustionMotions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.item.*;

@Mixin(ClientAnimator.class)
public abstract class ClientAnimatorMixin extends Animator {

    @Shadow private LivingMotion currentMotion;

    @Inject(method = "poseTick", at = @At("TAIL"), remap = false)
    private void addExhaustedAnimations(CallbackInfo ci) {
        if (entitypatch instanceof PlayerPatch playerPatch) {

            boolean isExhausted = false;
            PlayerMovement playerMovement = PlayerMovement.of(playerPatch.getOriginal());

            for (MobEffectInstance mobEffectInstance : playerMovement.player.getActiveEffects()) {
                if (isExhausted = mobEffectInstance.getEffect().getDescriptionId().contains("exhausted")) {
                    break;
                }
            }
            if (isExhausted) {
                this.entitypatch.currentLivingMotion = switchToExhaustedAnimations(this.entitypatch);
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"), remap = false, cancellable = true)
    private void testMotion(CallbackInfo ci) {
        if (this.entitypatch instanceof PlayerPatch) {
            EpicParaglidersMod.LOGGER.info("CURRENT ENTITY MOTION: " + this.entitypatch.currentLivingMotion);
            EpicParaglidersMod.LOGGER.info("CURRENT MOTION: " + this.currentMotion);
            EpicParaglidersMod.LOGGER.info("CURRENT COMPOSITE MOTION: " + this.entitypatch.currentCompositeMotion);
        }
    }

    private LivingMotion switchToExhaustedAnimations(LivingEntityPatch<?> entityPatch) {
        Item item = entityPatch.getOriginal().getMainHandItem().getItem();
        Style weaponStyle = EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getMainHandItem()).getStyle(entityPatch);

        if (entityPatch.currentLivingMotion.equals(LivingMotions.IDLE) && !entityPatch.getEntityState().inaction()) {
            if (item instanceof CrossbowItem) {
                EpicParaglidersMod.LOGGER.info("RETURNING EXHAUSTED_IDLE_CROSSBOW ");
                return ExhaustionMotions.EXHAUSTED_IDLE_CROSSBOW;
            }
            else if (item instanceof LongswordItem) {
                if (weaponStyle.equals(CapabilityItem.Styles.LIECHTENAUER)) {
                    EpicParaglidersMod.LOGGER.info("RETURNING LongswordItem EXHAUSTED_IDLE_LIECHTENAUER ");
                    return ExhaustionMotions.EXHAUSTED_IDLE_LIECHTENAUER;
                }
                else {
                    EpicParaglidersMod.LOGGER.info("RETURNING LongswordItem EXHAUSTED_IDLE_GREATSWORD ");
                    return ExhaustionMotions.EXHAUSTED_IDLE_GREATSWORD;
                }
            }
            else if (item instanceof GreatswordItem) {
                EpicParaglidersMod.LOGGER.info("RETURNING GreatswordItem EXHAUSTED_IDLE_GREATSWORD ");
                return ExhaustionMotions.EXHAUSTED_IDLE_GREATSWORD;
            }
            else if (item instanceof SpearItem) {
                if (weaponStyle.equals(CapabilityItem.Styles.TWO_HAND)) {
                    EpicParaglidersMod.LOGGER.info("RETURNING EXHAUSTED_IDLE_SPEAR ");
                    return ExhaustionMotions.EXHAUSTED_IDLE_SPEAR;
                }
                else {
                    EpicParaglidersMod.LOGGER.info("RETURNING SpearItem IDLE ");
                    return ExhaustionMotions.EXHAUSTED_IDLE;
                }
            }
            else if (item instanceof TachiItem) {
                EpicParaglidersMod.LOGGER.info("RETURNING EXHAUSTED_IDLE_TACHI");
                return ExhaustionMotions.EXHAUSTED_IDLE_TACHI;
            }
            else if (item instanceof KatanaItem) {
                if (weaponStyle.equals(CapabilityItem.Styles.SHEATH)) {
                    EpicParaglidersMod.LOGGER.info("RETURNING EXHAUSTED_IDLE_SHEATH");
                    return ExhaustionMotions.EXHAUSTED_IDLE_SHEATH;
                }
                else {
                    EpicParaglidersMod.LOGGER.info("RETURNING IDLE KatanaItem");
                    return ExhaustionMotions.EXHAUSTED_IDLE;
                }
            }
            else {
                EpicParaglidersMod.LOGGER.info("RETURNING IDLE ");
                return ExhaustionMotions.EXHAUSTED_IDLE;
            }
        }
        else if (entityPatch.currentLivingMotion.equals(LivingMotions.WALK)) {
            if (item instanceof CrossbowItem) {
                return ExhaustionMotions.EXHAUSTED_WALK_CROSSBOW;
            }
            else if (item instanceof LongswordItem) {
                if (weaponStyle.equals(CapabilityItem.Styles.LIECHTENAUER)) {
                    return ExhaustionMotions.EXHAUSTED_WALK_LIECHTENAUER;
                }
                else {
                    return ExhaustionMotions.EXHAUSTED_WALK_GREATSWORD;
                }
            }
            else if (item instanceof GreatswordItem) {
                return ExhaustionMotions.EXHAUSTED_WALK_GREATSWORD;
            }
            else if (item instanceof SpearItem) {
                return ExhaustionMotions.EXHAUSTED_WALK_SPEAR;
            }
            else if (item instanceof TachiItem) {
                return ExhaustionMotions.EXHAUSTED_WALK_TACHI;
            }
            else if (item instanceof KatanaItem) {
                if (weaponStyle.equals(CapabilityItem.Styles.SHEATH)) {
                    return ExhaustionMotions.EXHAUSTED_WALK_SHEATH;
                }
                else {
                    return ExhaustionMotions.EXHAUSTED_WALK_KATANA;
                }
            }
            else {
                return ExhaustionMotions.EXHAUSTED_WALK;
            }
        }
        else {
            return entityPatch.currentLivingMotion;
        }
    }
}