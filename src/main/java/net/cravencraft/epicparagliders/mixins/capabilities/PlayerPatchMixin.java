package net.cravencraft.epicparagliders.mixins.capabilities;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.ClientPlayerMovement;
import tictim.paraglider.capabilities.PlayerMovement;
import tictim.paraglider.capabilities.ServerPlayerMovement;
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
//        EpicParaglidersMod.LOGGER.info("HOW MUCH IS REMOVED: " + value);
//        PlayerMovement playerMovement = PlayerMovement.of(this.original);
//
//        if (playerMovement instanceof ServerPlayerMovement) {
//            ((PlayerMovementInterface) playerMovement).setTotalActionStaminaCostServerSide((int) value);
//            ((PlayerMovementInterface) playerMovement).setActionStaminaNeedsSync(true);
//        }
//        else if (playerMovement instanceof ClientPlayerMovement clientPlayerMovement) {
//            ((PlayerMovementInterface) clientPlayerMovement).setTotalActionStaminaCostClientSide((int) value);
//        }

        ci.cancel();
    }
}
