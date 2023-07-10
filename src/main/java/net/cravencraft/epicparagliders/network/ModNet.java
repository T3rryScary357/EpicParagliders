package net.cravencraft.epicparagliders.network;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
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
import org.antlr.v4.codegen.model.Sync;
import tictim.paraglider.ModCfg;
import tictim.paraglider.ParagliderMod;
import tictim.paraglider.capabilities.PlayerMovement;

import java.util.Optional;
import java.util.function.Supplier;

public class ModNet {
    private ModNet() {}

    public static final String NET_VERSION = "1.0";
    public static final SimpleChannel NET = NetworkRegistry.newSimpleChannel(new ResourceLocation(EpicParaglidersMod.MOD_ID, "master"), () -> NET_VERSION, NET_VERSION::equals, NET_VERSION::equals);

    public static void init() {
        NET.registerMessage(0, SyncActionMsg.class,
                SyncActionMsg::write, SyncActionMsg::read,
                ModNet::handleActionStaminaCost, Optional.of(NetworkDirection.PLAY_TO_SERVER));

        NET.registerMessage(1, SyncServerActionMsg.class,
                SyncServerActionMsg::write, SyncServerActionMsg::read,
                Client::handleServerActionStaminaCost, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    private static void handleActionStaminaCost(SyncActionMsg msg, Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if(player==null){
                ParagliderMod.LOGGER.error("Cannot handle SyncMovementMsg: Wrong side");
                return;
            }

            UpdatedServerPlayerMovement.instance.actionStaminaCost = msg.actionStaminaCost();
        });
    }

    private static final class Client {

        private Client(){}
        public static void handleServerActionStaminaCost(SyncServerActionMsg msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().setPacketHandled(true);
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            if (localPlayer == null) return;
            UpdatedClientPlayerMovement updatedClientPlayerMovement = UpdatedClientPlayerMovement.instance;
            if (updatedClientPlayerMovement != null) {
                if (ModCfg.traceMovementPacket()) ParagliderMod.LOGGER.debug("Received {}", msg);
                msg.copyTo(updatedClientPlayerMovement);
            }
            else {
                ParagliderMod.LOGGER.error("Couldn't handle packet {}, capability not found", msg);
            }
        }
    }
}