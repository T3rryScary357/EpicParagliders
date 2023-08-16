package net.cravencraft.epicparagliders.mixins.capabilities;
import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.cravencraft.epicparagliders.network.ModNet;
import net.cravencraft.epicparagliders.network.SyncActionToClientMsg;
import net.cravencraft.epicparagliders.utils.MathUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import tictim.paraglider.capabilities.ServerPlayerMovement;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(ServerPlayerMovement.class)
public abstract class ServerPlayerMovementMixin extends PlayerMovement implements PlayerMovementInterface {
    @Shadow @Final private ServerPlayer serverPlayer;
    private int totalActionStaminaCost;
    private int currentActionStaminaCost;
    private boolean isAttacking;
    private boolean isPerformingAction;

    public ServerPlayerMovementMixin(Player player) { super(player); }

    @Inject(method = "update", at = @At(value = "HEAD"),  remap=false)
    public void update(CallbackInfo ci) {
        //TODO: Maybe hit the ServerPlayerPatch mixin and just set this value there via the interface?
        ServerPlayerPatch serverPlayerPatch = (ServerPlayerPatch) player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null);
//        EpicParaglidersMod.LOGGER.info("SERVER PLAYER PATCH INITIALIZED: " + this.totalActionStaminaCost);
//        EpicParaglidersMod.LOGGER.info("IS ATTACKING: " + this.isAttacking);

        /**
         * TODO: Put into own method when the above TODO for the ServerPlayerPatch is done.
         *
         * Will disable Epic Fight's battle mode if the player is paragliding.
         * This fixes the awkward issue where the player can still attack while gliding.
         */
        if (this.isParagliding() && serverPlayerPatch.isBattleMode()) {
            serverPlayerPatch.toggleMode();
        }

        calculateRangeStaminaCost();
        if (isAttacking && serverPlayerPatch.getEntityState().attacking()) {
            EpicParaglidersMod.LOGGER.info("INSIDE ATTACK CONDITION TOTAL: " + this.currentActionStaminaCost);
            this.totalActionStaminaCost = (int) MathUtils.calculateTriangularRoot((MathUtils.calculateTriangularNumber(this.totalActionStaminaCost)
                    + MathUtils.calculateTriangularNumber(currentActionStaminaCost)));
            EpicParaglidersMod.LOGGER.info("INSIDE ATTACK CONDITION TOTAL: " + this.totalActionStaminaCost);
            isAttacking = false;
            isPerformingAction = true;
//            ModNet.NET.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncActionToClientMsg(this.totalActionStaminaCost, this.isPerformingAction));
        }
        else if (isPerformingAction) {
            EpicParaglidersMod.LOGGER.info("INSIDE ACTION CONDITION: " + this.currentActionStaminaCost);
            EpicParaglidersMod.LOGGER.info("TOTAL ACTION STAMINA: " + this.totalActionStaminaCost);
            this.totalActionStaminaCost = (int) MathUtils.calculateTriangularRoot((MathUtils.calculateTriangularNumber(this.totalActionStaminaCost)
                    + MathUtils.calculateTriangularNumber(currentActionStaminaCost)));
            EpicParaglidersMod.LOGGER.info("INSIDE ACTION CONDITION TOTAL: " + this.totalActionStaminaCost);
        }

        //TODO: Is this necessary?
        if(!this.player.isCreative() && this.isDepleted()){
            this.player.addEffect(new MobEffectInstance(MobEffect.byId(18))); // Adds weakness
        }

        if(this.isPerformingAction) {
            ModNet.NET.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncActionToClientMsg(this.totalActionStaminaCost, this.isPerformingAction));
            this.currentActionStaminaCost = 0;
            this.isPerformingAction = false;
        }
        else if (this.totalActionStaminaCost < 0) {
            this.totalActionStaminaCost++;
            ModNet.NET.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncActionToClientMsg(this.totalActionStaminaCost, this.isPerformingAction));
        }
        this.setTotalActionStaminaCost(this.totalActionStaminaCost);
    }

        private void calculateRangeStaminaCost() {
        //TODO: Flesh out the math for this, then add it to the MathUtils class.
        //      Also, this will need attribute support as well.
        //      Can probably even check if the weapon is being pulled back or not using 'projectileWeaponItem'
        if (player.getUseItem().getItem() instanceof  ProjectileWeaponItem projectileWeaponItem) {
            this.currentActionStaminaCost = 6;
            this.isPerformingAction = true;
        }
    }

    @Override
    public void performingActionServerSide(boolean isPerformingAction) {
        this.isPerformingAction = isPerformingAction;
    }

    @Override
    public void isAttackingServerSide(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    @Override
    public void setTotalActionStaminaCostServerSide(int totalActionStaminaCost) {
        this.totalActionStaminaCost = totalActionStaminaCost;
    }

    @Override
    public void setActionStaminaCostServerSide(int actionStaminaCost) {
        this.currentActionStaminaCost = actionStaminaCost;
    }
}