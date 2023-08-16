package net.cravencraft.epicparagliders.network;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.cravencraft.epicparagliders.capabilities.UpdatedClientPlayerMovement;
import net.cravencraft.epicparagliders.capabilities.UpdatedServerPlayerMovement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import tictim.paraglider.ModCfg;
import tictim.paraglider.ParagliderMod;
import tictim.paraglider.capabilities.ClientPlayerMovement;
import tictim.paraglider.capabilities.PlayerMovement;

import java.util.Optional;
import java.util.function.Supplier;

public class ModNet {
    private ModNet() {}

    public static final String NET_VERSION = "1.0";
    public static final SimpleChannel NET = NetworkRegistry.newSimpleChannel(new ResourceLocation(EpicParaglidersMod.MOD_ID, "master"), () -> NET_VERSION, NET_VERSION::equals, NET_VERSION::equals);

    public static void init() {

        NET.registerMessage(0, SyncActionToServerMsg.class,
                SyncActionToServerMsg::write, SyncActionToServerMsg::read,
                ModNet::handleActionToServerStaminaCost, Optional.of(NetworkDirection.PLAY_TO_SERVER));

        NET.registerMessage(1, SyncActionToClientMsg.class,
                SyncActionToClientMsg::write, SyncActionToClientMsg::read,
                Client::handleActionToClientStaminaCost, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    private static void handleActionToServerStaminaCost(SyncActionToServerMsg msg, Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            PlayerMovement serverPlayerMovement = PlayerMovement.of(player);
            if(player==null){
                ParagliderMod.LOGGER.error("Cannot handle SyncMovementMsg: Wrong side");
                return;
            }

            ((PlayerMovementInterface) serverPlayerMovement).setTotalActionStaminaCostServerSide(msg.totalActionStaminaCost());
            ((PlayerMovementInterface) serverPlayerMovement).isAttackingServerSide(msg.isAttacking());
        });
    }

    private static final class Client {
        private Client(){}

        public static void handleActionToClientStaminaCost(SyncActionToClientMsg msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().setPacketHandled(true);
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            if (localPlayer == null) return;
            ClientPlayerMovement clientPlayerMovement = (ClientPlayerMovement) PlayerMovement.of(localPlayer);
            if (clientPlayerMovement != null) {
                if (ModCfg.traceMovementPacket()) ParagliderMod.LOGGER.debug("Received {}", msg);
//                msg.copyTo(clientPlayerMovement); //TODO: Look into later since you have multiple calls here.
                ((PlayerMovementInterface) clientPlayerMovement).setTotalActionStaminaCostClientSide(msg.totalActionStaminaCost());
                ((PlayerMovementInterface) clientPlayerMovement).performingActionClientSide(msg.isPerformingAction());
            }
            else {
                ParagliderMod.LOGGER.error("Couldn't handle packet {}, capability not found", msg);
            }
        }
    }
}