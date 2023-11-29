package net.cravencraft.epicparagliders.mixins.paragliders.stamina;

import net.cravencraft.epicparagliders.capabilities.StaminaOverride;
import net.cravencraft.epicparagliders.config.ConfigManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.api.Copy;
import tictim.paraglider.api.Serde;
import tictim.paraglider.api.movement.Movement;
import tictim.paraglider.api.movement.ParagliderPlayerStates;
import tictim.paraglider.api.movement.PlayerState;
import tictim.paraglider.api.stamina.Stamina;
import tictim.paraglider.impl.movement.PlayerMovement;
import tictim.paraglider.impl.movement.ServerPlayerMovement;
import tictim.paraglider.impl.stamina.BotWStamina;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(BotWStamina.class)
public abstract class BotWStaminaMixin implements Stamina, Copy, Serde, StaminaOverride {

    @Shadow public abstract boolean isDepleted();

    private int totalActionStaminaCost;
    private int eldenStaminaDelay;

    @Override
    public int getTotalActionStaminaCost() {
        return this.totalActionStaminaCost;
    }

    @Override
    public void setTotalActionStaminaCost(int totalActionStaminaCost) {
        this.totalActionStaminaCost = totalActionStaminaCost;
    }

    @Inject(at = @At("HEAD"), remap = false, cancellable = true, method = "update")
    private void injectEpicFightStaminaValues(@NotNull Movement movement, CallbackInfo ci) {

        PlayerMovement playerMovement = (PlayerMovement) movement;
        PlayerPatch playerPatch = (PlayerPatch) playerMovement.player().getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null);

        PlayerState state = movement.state();
        int recoveryDelay = movement.recoveryDelay();
        int newRecoveryDelay = recoveryDelay;

        if (state.staminaDelta() < 0 || this.totalActionStaminaCost > 0) {
            int staminaDelta;

            if (ConfigManager.SERVER_CONFIG.eldenStaminaSystem()) {
                if (playerPatch.isBattleMode()) {
                    eldenStaminaDelay = 60;
                    staminaDelta = state.staminaDelta();
                }
                else if (state.id().equals(ParagliderPlayerStates.RUNNING) || state.id().equals(ParagliderPlayerStates.SWIMMING)) {
                    if (eldenStaminaDelay > 0) {
                        eldenStaminaDelay = 60;
                        staminaDelta = state.staminaDelta();
                    }
                    else {
                        staminaDelta = 0;
                    }

                }
                else {
                    staminaDelta = state.staminaDelta();
                }
            }
            else {
                staminaDelta = state.staminaDelta();
            }

            if (!isDepleted()) {
                //TODO: This probably needs to be redone with the triangular numbers formula
                staminaDelta = (state.staminaDelta() < 0) ? staminaDelta - this.totalActionStaminaCost : -this.totalActionStaminaCost;
                takeStamina(-staminaDelta, false, false);
            }
        }
        else {
            if (recoveryDelay > 0) {
                newRecoveryDelay--;
            }
            else if (state.staminaDelta() > 0) {

                giveStamina(state.staminaDelta(), false);
            }

            if (ConfigManager.SERVER_CONFIG.eldenStaminaSystem() && this.eldenStaminaDelay > 0) {
                --this.eldenStaminaDelay;
            }
        }

        if (this.totalActionStaminaCost > 0) {
            movement.setRecoveryDelay(10);
            this.totalActionStaminaCost--;
        }

        //TODO: Maybe there's a reason I put this outside of the else statement
//        if (ConfigManager.SERVER_CONFIG.eldenStaminaSystem() && this.eldenStaminaDelay > 0) {
//            --this.eldenStaminaDelay;
//        }

        if (movement instanceof ServerPlayerMovement) {
            this.setTotalActionStaminaCostServerSide(this.totalActionStaminaCost);
        }

        //noinspection DataFlowIssue
        newRecoveryDelay = Math.max(0, Math.max(newRecoveryDelay, state.recoveryDelay()));
        if (recoveryDelay!=newRecoveryDelay) {
            movement.setRecoveryDelay(newRecoveryDelay);
        }

        ci.cancel();
    }
}