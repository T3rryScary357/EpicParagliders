package com.cravencraft.epicparagliders.mixins.paragliders.stamina;

import com.cravencraft.epicparagliders.EpicParaglidersMod;
import com.cravencraft.epicparagliders.capabilities.StaminaOverride;
import com.cravencraft.epicparagliders.config.ConfigManager;
import com.cravencraft.epicparagliders.network.ModNet;
import com.cravencraft.epicparagliders.network.SyncActionToClientMsg;
import com.cravencraft.epicparagliders.utils.EpicParagliderUtils;
import com.cravencraft.epicparagliders.utils.MathUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.api.movement.Movement;
import tictim.paraglider.api.vessel.VesselContainer;
import tictim.paraglider.impl.movement.ServerPlayerMovement;
import tictim.paraglider.impl.stamina.BotWStamina;
import tictim.paraglider.impl.stamina.ServerBotWStamina;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(ServerBotWStamina.class)
public abstract class ServerBotWStaminaMixin extends BotWStamina implements StaminaOverride {

    private int totalActionStaminaCost;
    private int currentActionStaminaCost;
    private boolean isAttacking;
    private boolean isPerformingAction;
    private Player player;

    public ServerBotWStaminaMixin(@NotNull VesselContainer vessels) {
        super(vessels);
    }

    @Override
    public void performingAction(boolean isPerformingAction) {
        this.isPerformingAction = isPerformingAction;
    }

    @Override
    public boolean isPerformingAction() {
        return this.isPerformingAction;
    }

    @Override
    public void attacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    @Override
    public boolean isAttacking() {
        return this.isAttacking;
    }

    @Override
    public void setTotalActionStaminaCostServerSide(int totalActionStaminaCost) {
        this.totalActionStaminaCost = totalActionStaminaCost;
    }

    @Override
    public void setActionStaminaCost(int actionStaminaCost) {
        this.currentActionStaminaCost = actionStaminaCost;
    }

    /**
     * Updates the server side player by doing a few things. If the player is attacking, then the amount of
     * stamina to drain will be calculated. If there is an overlap between a skill being used (e.g., rolling)
     * and an attack (e.g., rolling immediately after an attack to cancel the animation), then that will be
     * accounted for in the total stamina cost so that there isn't any uneven amount of stamina drained.
     *
     * @param movement
     * @param ci
     */
    @Inject(at = @At("HEAD"), remap = false, cancellable = true, method = "update")
    private void updateServerSideMovement(Movement movement, CallbackInfo ci) {

//        EpicParaglidersMod.LOGGER.info("INSIDE SERVER BOTW STAMINA");
        if (movement instanceof ServerPlayerMovement serverPlayerMovement) {
            this.player = serverPlayerMovement.player();
            ServerPlayerPatch serverPlayerPatch = (ServerPlayerPatch) this.player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null);

            if (EpicParagliderUtils.isParagliding(this.player) && serverPlayerPatch.isBattleMode()) {
                serverPlayerPatch.toggleMode();
            }

//            EpicParaglidersMod.LOGGER.info("IS ATTACKING: {} | IS PERFORMING ACTION: {}", this.isAttacking, this.isPerformingAction);
//            EpicParaglidersMod.LOGGER.info("IS ATTACK ANIMATION: {}", serverPlayerPatch.getEntityState().inaction());

            //TODO: Would like to organize these better.
            checkShieldDisable();
            calculateRangeStaminaCost();

            if (isAttacking) {

                EpicParaglidersMod.LOGGER.info("INSIDE SERVER IS ATTACKING BLOCK");
                this.isAttacking = false;
                isPerformingAction = true;

                this.totalActionStaminaCost = (int) MathUtils.calculateTriangularRoot(
                        (MathUtils.calculateTriangularNumber(this.totalActionStaminaCost) + MathUtils.calculateTriangularNumber(currentActionStaminaCost)));
            }
            else if (isPerformingAction) {
                //TODO: Do this for greater than 0 too, so it doesn't go over. Maybe have it a little less to always ensure
                //      it'll be in the negative. Actually, check it not going into the negative. Could be a cool feature
                //      like what Storm wants.
                //TODO: Double check regular stamina consumption from attacks. Could need a rework as well, but more than
                //      likely they are fine.
                if (this.currentActionStaminaCost < 0) {
                    int totalStaminaCostTriangularNumber = (int) MathUtils.calculateTriangularNumber(this.totalActionStaminaCost);
                    int currentStaminaCostTriangularNumber = (int) MathUtils.calculateTriangularNumber(this.currentActionStaminaCost);
                    this.totalActionStaminaCost = (int) MathUtils.calculateTriangularRoot(totalStaminaCostTriangularNumber + currentStaminaCostTriangularNumber);
                }
                else {
                    this.totalActionStaminaCost = (int) MathUtils.calculateTriangularRoot((MathUtils.calculateTriangularNumber(this.totalActionStaminaCost)
                            + MathUtils.calculateTriangularNumber(currentActionStaminaCost)));
                }
        }

        if(this.isPerformingAction) {
            serverPlayerMovement.markMovementChanged();
            BotWStamina botwStaminaSystem = (BotWStamina) serverPlayerMovement.stamina();
            ((StaminaOverride) botwStaminaSystem).setTotalActionStaminaCost(this.totalActionStaminaCost);

            ModNet.NET.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayerMovement) movement).player()), new SyncActionToClientMsg(this.totalActionStaminaCost)); //TODO: Change all to total and not current
            this.currentActionStaminaCost = 0;
            this.isPerformingAction = false;
        }
        }
    }

    /**
     * Calculates ranged attacks from bows and crossbows. Currently, this just uses a base amount of 6
     * to drain constantly while the use button is held down. Will flesh this out in a later version.
     */
    private void calculateRangeStaminaCost() {
        //TODO: Flesh out the math for this, then add it to the MathUtils class.
        //      Also, this will need attribute support as well.
        //      Can probably even check if the weapon is being pulled back or not using 'projectileWeaponItem'
        if (this.player.getUseItem().getItem() instanceof  ProjectileWeaponItem projectileWeaponItem) {
            this.currentActionStaminaCost = (int) (6 * ConfigManager.SERVER_CONFIG.baseRangedStaminaMultiplier());
            this.isPerformingAction = true;
        }
    }

    /**
     * Checks if the player is currently holding a shield item. If so, then the modifyShieldCooldown method is called
     * to determine what to do with the shield.
     */
    private void checkShieldDisable() {
        if (this.player.getOffhandItem().getItem() instanceof ShieldItem offhandShieldItem) {
            modifyShieldCooldown(offhandShieldItem);
        }
        else if (this.player.getMainHandItem().getItem() instanceof ShieldItem mainHandShieldItem) {
            modifyShieldCooldown(mainHandShieldItem);
        }
    }

    /**
     * Disables the shield cooldown UNLESS the player's current stamina is fully depleted. If the stamina is depleted,
     * then the cooldown is set to the amount of ticks remaining until the player's stamina is fully replenished. Has
     * some additional checks to ensure the shield cooldown time stays in sync with the stamina replenish time as well.
     *
     * @param shieldItem A main hand or offhand shield being held by the player
     */
    private void modifyShieldCooldown(ShieldItem shieldItem) {
        if (this.player.getCooldowns().isOnCooldown(shieldItem) && !this.isDepleted()) {
            this.player.getCooldowns().removeCooldown(shieldItem);
        }
        else if (this.isDepleted() && !player.getCooldowns().isOnCooldown(shieldItem)) {
            ServerPlayerPatch serverPlayerPatch = (ServerPlayerPatch) this.player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null);
            if (serverPlayerPatch.getServerAnimator().animationPlayer.getAnimation().toString().contains("hit_shield")) {
                this.player.getCooldowns().addCooldown(shieldItem, 40);
            }
        }
    }
}