package net.cravencraft.epicparagliders.mixins.capabilities;

import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.ClientPlayerMovement;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;

@Mixin(ClientPlayerMovement.class)
public abstract class ClientPlayerMovementMixin extends PlayerMovement implements PlayerMovementInterface {
    private int totalActionStaminaCost;
    private boolean isAttacking;
    private boolean isPerformingAction;

    public ClientPlayerMovementMixin(Player player) {
        super(player);
    }

    /**
     * Updates the client side player by doing a few things. If the player is attacking, then the amount of
     * stamina to drain will be calculated. If there is an overlap between a skill being used (e.g., rolling)
     * and an attack (e.g., rolling immediately after an attack to cancel the animation), then that will be
     * accounted for in the total stamina cost so that there isn't any uneven amount of stamina drained.
     */
    @Inject(method = "update", at = @At(value = "HEAD"), remap=false)
    public void update(CallbackInfo ci) {
        this.setTotalActionStaminaCost(this.totalActionStaminaCost);
    }

    public int getTotalActionStaminaCost() {
        return this.totalActionStaminaCost;
    }

    @Override
    public void setTotalActionStaminaCostClientSide(int totalActionStaminaCost) {
        this.totalActionStaminaCost = totalActionStaminaCost;
    }

    @Override
    public void performingActionClientSide(boolean isPerformingAction) {
        this.isPerformingAction = isPerformingAction;
    }

//    private void calculateRangeStaminaCost() {
//        //TODO: Maybe I want to pass in the projectileWeaponItem instead of LocalPlayer?
//        if (player.getUseItem().getItem() instanceof  ProjectileWeaponItem projectileWeaponItem) {
//            this.totalActionStaminaCost = CalculateStaminaUtils.calculateRangeStaminaCost((LocalPlayer) this.player);
//        }
//    }

    /**
     * Will disable Epic Fight's battle mode if the player is paragliding.
     * This fixes the awkward issue where the player can still attack while gliding.
     */
    private void disableAttackIfParagliding() {
        LocalPlayerPatch localPlayerPatch = ClientEngine.instance.getPlayerPatch();
        if (this.isParagliding() && localPlayerPatch.isBattleMode()) {
            localPlayerPatch.toggleMode();
        }
    }
}
