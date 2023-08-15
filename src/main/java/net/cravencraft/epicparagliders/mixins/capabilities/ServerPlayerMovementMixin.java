package net.cravencraft.epicparagliders.mixins.capabilities;
import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.cravencraft.epicparagliders.network.ModNet;
import net.cravencraft.epicparagliders.network.SyncActionToClientMsg;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import tictim.paraglider.capabilities.ServerPlayerMovement;

@Mixin(ServerPlayerMovement.class)
public abstract class ServerPlayerMovementMixin extends PlayerMovement implements PlayerMovementInterface {
    @Shadow @Final private ServerPlayer serverPlayer;
    private int totalActionStaminaCost;

    public ServerPlayerMovementMixin(Player player) { super(player); }

    @Inject(method = "update", at = @At(value = "HEAD"),  remap=false)
    public void update(CallbackInfo ci) {
//        EpicParaglidersMod.LOGGER.info("INIT SERVER ACTION STAMINA COST: " + this.totalActionStaminaCost);
//        if (this.totalActionStaminaCost > 0) {
//            EpicParaglidersMod.LOGGER.info("Server Action stamina cost: " + this.totalActionStaminaCost + " | NEEDS SYNC: " + actionStaminaNeedsSync);
//        }

        //TODO: Is this necessary?
        if(!this.player.isCreative() && this.isDepleted()){
            this.player.addEffect(new MobEffectInstance(MobEffect.byId(18))); // Adds weakness
        }

//        EpicParaglidersMod.LOGGER.info("AFTER SERVER ACTION STAMINA COST: " + this.totalActionStaminaCost);
        if (this.totalActionStaminaCost > 0) {
            this.totalActionStaminaCost--;
            ModNet.NET.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncActionToClientMsg(this.totalActionStaminaCost));
        }
        this.setTotalActionStaminaCost(this.totalActionStaminaCost);
    }

    public void setTotalActionStaminaCostServerSide(int totalActionStaminaCost) {
        this.totalActionStaminaCost = totalActionStaminaCost;
    }
}