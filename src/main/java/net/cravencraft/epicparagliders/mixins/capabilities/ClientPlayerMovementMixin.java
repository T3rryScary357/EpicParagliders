package net.cravencraft.epicparagliders.mixins.capabilities;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.cravencraft.epicparagliders.network.ModNet;
import net.cravencraft.epicparagliders.network.SyncActionToServerMsg;
import net.cravencraft.epicparagliders.utils.MathUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.ClientPlayerMovement;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.item.GreatswordItem;

@Mixin(ClientPlayerMovement.class)
public abstract class ClientPlayerMovementMixin extends PlayerMovement implements PlayerMovementInterface {
    private int totalActionStaminaCost;
    private boolean isAttacking;

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
//        EpicParaglidersMod.LOGGER.info("INIT CLIENT TOTAL: " + totalActionStaminaCost);
        if (!ClientEngine.instance.getPlayerPatch().getEntityState().movementLocked()){
            this.isAttacking = false;
        }
        else if (ClientEngine.instance.getPlayerPatch().getEntityState().attacking() && !this.isAttacking) {
            EpicParaglidersMod.LOGGER.info("INSIDE ATTACK CLIENT TOTAL: " + totalActionStaminaCost);
            this.totalActionStaminaCost = (int) MathUtils.calculateTriangularRoot((MathUtils.calculateTriangularNumber(this.totalActionStaminaCost)
                                        + MathUtils.calculateTriangularNumber(getAttackStaminaCost(this.player.getMainHandItem().getItem()))));
            EpicParaglidersMod.LOGGER.info("INSIDE ATTACK AFTER CLIENT TOTAL: " + totalActionStaminaCost);
            this.isAttacking = true;
        }

        if (this.totalActionStaminaCost > 0) {
            this.totalActionStaminaCost--;
            ModNet.NET.sendToServer(new SyncActionToServerMsg(this.totalActionStaminaCost));
        }
        else if (this.totalActionStaminaCost < 0) {
            this.totalActionStaminaCost++;
            ModNet.NET.sendToServer(new SyncActionToServerMsg(this.totalActionStaminaCost));
        }
//        EpicParaglidersMod.LOGGER.info("CLIENT TOTAL: " + totalActionStaminaCost);
        this.setTotalActionStaminaCost(this.totalActionStaminaCost);
    }

    /**
     * TODO: Compare with BetterParagliders stamina algorithm.
     *
     * Some math done here to determine how much stamina should be consumed from each weapon type.
     * Takes in the attack delay of the weapon as well as its attack strength to create a balanced
     * stamina cost. Also, factors in the extra damage that Epic Fight applies to certain weapons.
     *
     * @param item The weapon that is attacking
     * @return The amount of stamina that should be drained from the attacking weapon
     */
    private int getAttackStaminaCost(Item item) {
        int itemAttackStrDelay = (int) this.player.getCurrentItemAttackStrengthDelay();
        if (item instanceof SwordItem swordItem) {
            //TODO: May have to factor this in. Test with a datapack in the future.
//            if (this.player.getMainHandItem().getTag().contains("damage_bonus")) {
//                EpicParaglidersMod.LOGGER.info(swordItem.getRegistryName() + " contains a damage bonus!");
//            }

            if (swordItem instanceof GreatswordItem) {
                return (int) (itemAttackStrDelay + (swordItem.getDamage() + 12) / 2);
            }
            else {
                return (int) (itemAttackStrDelay + (swordItem.getDamage() + 1));
            }
        }
        else if (item instanceof AxeItem axeItem) {
            return (int) ((itemAttackStrDelay / 2) + (axeItem.getAttackDamage() + 1));
        }
        else {
            return itemAttackStrDelay;
        }
    }

    public int getTotalActionStaminaCost() {
        return this.totalActionStaminaCost;
    }

    @Override
    public void setTotalActionStaminaCostClientSide(int totalActionStaminaCost) {
        this.totalActionStaminaCost = totalActionStaminaCost;
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
