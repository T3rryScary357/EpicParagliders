package com.cravencraft.epicparagliders.mixins.paragliders.client;

import com.cravencraft.epicparagliders.config.ConfigManager;
import com.cravencraft.epicparagliders.capabilities.StaminaOverride;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.api.movement.Movement;
import tictim.paraglider.api.stamina.Stamina;
import tictim.paraglider.client.render.InGameStaminaWheelRenderer;
import tictim.paraglider.client.render.StaminaWheelRenderer;
import tictim.paraglider.forge.capability.PlayerMovementProvider;
import tictim.paraglider.impl.stamina.BotWStamina;
import yesman.epicfight.client.ClientEngine;

import static tictim.paraglider.ParagliderUtils.ms;
import static tictim.paraglider.client.render.StaminaWheelConstants.*;

@Mixin(InGameStaminaWheelRenderer.class)
public abstract class InGameStaminaWheelRendererMixin extends StaminaWheelRenderer {

    private boolean staminaBarShowing;
    @Shadow private boolean full;
    @Shadow protected abstract void makeFullWheel(@NotNull Wheel wheel, int stamina);

    @Inject(at = @At("HEAD"), remap = false, cancellable = true, method = "makeWheel")
    private void epicFightStaminaWheelSupport(Player player, Wheel wheel, CallbackInfo ci) {

        if (PlayerMovementProvider.of(player) != null) {
            BotWStamina botWStamina = ((BotWStamina) PlayerMovementProvider.of(player).stamina());
            if (ConfigManager.CLIENT_CONFIG.useStaminaWheel()) {
                Stamina s = Stamina.get(player);
                int maxStamina = s.maxStamina();
                int stamina = Math.min(maxStamina, s.stamina());
                if(stamina>=maxStamina){
                    this.makeFullWheel(wheel, stamina);
                }
                else {
                    this.full = false;
                    boolean depleted = s.isDepleted();
                    int color = FastColor.ARGB32.lerp(cycle(ms(), depleted ? DEPLETED_BLINK : BLINK), DEPLETED_1, DEPLETED_2);
                    int gainStamColor = FastColor.ARGB32.lerp(cycle(ms(), depleted ? DEPLETED_BLINK : BLINK), FastColor.ARGB32.color(255, 2, 2, 150), FastColor.ARGB32.color(255, 2, 150, 255));

                    Movement movement = Movement.get(player);

                    int totalActionStaminaCost = ((StaminaOverride) botWStamina).getTotalActionStaminaCost();
                    int staminaDelta = movement.getActualStaminaDelta();
                    staminaDelta = (staminaDelta < 0) ? staminaDelta - totalActionStaminaCost : -totalActionStaminaCost;

                    wheel.fill(0, maxStamina, EMPTY);
                    if (depleted) {
                        wheel.fill(0, stamina, color);
                    }
                    else {
                        wheel.fill(0, stamina, IDLE);
                        if (staminaDelta < 0){
                            wheel.fill(stamina + staminaDelta * 10, stamina, color);
                        }
                        if (totalActionStaminaCost < 0) {
                            int totalProportion = stamina + (-totalActionStaminaCost) * 10;
                            // If the total proportion is greater than the max stamina, just set it to the missing stamina.
                            if (totalProportion > botWStamina.maxStamina()) {
                                totalProportion = stamina + (botWStamina.maxStamina() - stamina);
                            }
                            wheel.fill(stamina, totalProportion, gainStamColor);
                        }
                    }
                }
            }
            else if (!ClientEngine.getInstance().getPlayerPatch().isBattleMode()) {

                if (botWStamina.stamina() == botWStamina.maxStamina() && staminaBarShowing) {
                    ClientEngine.getInstance().renderEngine.downSlideSkillUI();
                    staminaBarShowing = false;
                }
                //TODO: Small bug here where the stamina bar goes up and down if in battle mode
                //      using it then switching to mining mode.
                else if (botWStamina.stamina() < botWStamina.maxStamina() && !staminaBarShowing) {
                    ClientEngine.getInstance().renderEngine.upSlideSkillUI();
                    staminaBarShowing = true;
                }
            }
        }
        ci.cancel();
    }
}