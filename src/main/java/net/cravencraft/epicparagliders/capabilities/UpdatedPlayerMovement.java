package net.cravencraft.epicparagliders.capabilities;

import net.cravencraft.epicparagliders.UpdatedModCfg;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import tictim.paraglider.capabilities.PlayerMovement;
import tictim.paraglider.capabilities.PlayerState;

public abstract class UpdatedPlayerMovement {

    public PlayerMovement playerMovement;
    public int actionStaminaCost;
    public boolean setNewSkill;
    public boolean isAttacking;

    public UpdatedPlayerMovement(PlayerMovement playerMovement) {
        this.playerMovement = playerMovement;
    }

    public abstract void update();

    public void updateStamina() {
        PlayerState state = playerMovement.getState();
        if (this.actionStaminaCost > 0 || state.isConsume() ) {
            playerMovement.setRecoveryDelay(playerMovement.RECOVERY_DELAY);
            int stateChange = (state.isConsume()) ? state.change() : -this.actionStaminaCost;

            if (!playerMovement.isDepleted() && ((state.isParagliding() ? UpdatedModCfg.paraglidingConsumesStamina() : UpdatedModCfg.runningConsumesStamina()) || this.actionStaminaCost > 0)) {
                // TODO: Double check this area if you're getting an increasing value with attacks.
                playerMovement.setStamina(Math.max(0, playerMovement.getStamina() + stateChange));
            }
        }
    }

    protected void addEffects() {
        if(!playerMovement.player.isCreative()&&playerMovement.isDepleted()){
            playerMovement.player.addEffect(new MobEffectInstance(MobEffect.byId(18))); // Adds weakness
        }
    }
}