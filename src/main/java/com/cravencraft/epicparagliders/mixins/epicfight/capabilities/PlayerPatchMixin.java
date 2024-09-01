package com.cravencraft.epicparagliders.mixins.epicfight.capabilities;

import com.cravencraft.epicparagliders.EpicParaglidersAttributes;
import com.cravencraft.epicparagliders.capabilities.StaminaOverride;
import com.cravencraft.epicparagliders.config.ConfigManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tictim.paraglider.api.stamina.Stamina;
import tictim.paraglider.forge.capability.PlayerMovementProvider;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.ChargeableSkill;
import yesman.epicfight.skill.mover.DemolitionLeapSkill;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

@Mixin(PlayerPatch.class)
public abstract class PlayerPatchMixin<T extends Player> extends LivingEntityPatch<T> {
    @Shadow public abstract float getStamina();

    @Shadow protected ChargeableSkill chargingSkill;

    /**
     * Modifies the getMaxStamina method to return the max stamina from the Paragliders
     * stamina system.
     *
     * @param cir
     */
    @Inject(method = "getMaxStamina", at = @At("RETURN"), cancellable = true, remap = false)
    private void getMaxStamina(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue((float) Stamina.get(this.getOriginal()).maxStamina());
    }

    /**
     * Modifies the getStamina method to return the current stamina from the Paragliders
     * stamina system.
     *
     * @param cir
     */
    @Inject(method = "getStamina", at = @At("HEAD"), cancellable = true, remap = false)
    private void getStamina(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue((float) Stamina.get(this.getOriginal()).stamina());
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
        float attenuation = (float)Mth.clamp(this.original.level().getGameRules().getInt(EpicFightGamerules.WEIGHT_PENALTY), 0, 100) / 100.0F;
        float weight = this.getWeight();
        float modifiedConsumption = ((weight / 40.0F - 1.0F) * 0.3F * attenuation + 1.0F) * amount;

        if (this.chargingSkill instanceof DemolitionLeapSkill) {
            cir.setReturnValue((float) (modifiedConsumption * ConfigManager.SERVER_CONFIG.demolitionLeapStaminaMultiplier()));
        }
        else {
            cir.setReturnValue((float) Math.round(modifiedConsumption * ConfigManager.SERVER_CONFIG.baseDodgeStaminaMultiplier() *
                    this.getOriginal().getAttributeValue(EpicParaglidersAttributes.DODGE_STAMINA_REDUCTION.get())));
        }
    }
}
