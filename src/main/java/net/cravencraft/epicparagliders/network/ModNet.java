package net.cravencraft.epicparagliders.network;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.cravencraft.epicparagliders.capabilities.StaminaOverride;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import tictim.paraglider.ParagliderMod;
import tictim.paraglider.forge.capability.PlayerMovementProvider;
import tictim.paraglider.impl.movement.ClientPlayerMovement;
import tictim.paraglider.impl.movement.PlayerMovement;
import tictim.paraglider.impl.stamina.BotWStamina;

import java.util.Optional;
import java.util.function.Supplier;

public class ModNet {
    private ModNet() {}

    public static final String NET_VERSION = "2.0";
    public static final SimpleChannel NET = NetworkRegistry.newSimpleChannel(new ResourceLocation(EpicParaglidersMod.MOD_ID, "master"), () -> NET_VERSION, NET_VERSION::equals, NET_VERSION::equals);

    public static void init() {

        NET.registerMessage(1, SyncActionToClientMsg.class,
                SyncActionToClientMsg::write, SyncActionToClientMsg::read,
                Client::handleActionToClientStaminaCost, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    private static final class Client {
        private Client(){}

        public static void handleActionToClientStaminaCost(SyncActionToClientMsg msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().setPacketHandled(true);
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            if (localPlayer == null) return;
//            PlayerMovement playerMovement = PlayerMovementProvider.of(localPlayer);
//            ClientPlayerMovement
//            StaminaOverride botwStaminaSystem = (StaminaOverride) PlayerMovementProvider.of(localPlayer).stamina();
            BotWStamina botWStamina = (BotWStamina) PlayerMovementProvider.of(localPlayer).stamina();
            if (botWStamina != null) {
//                msg.copyTo(clientPlayerMovement); //TODO: Look into later since you have multiple calls here.
//                ((PlayerMovementInterface) clientPlayerMovement).setTotalActionStaminaCostClientSide(msg.totalActionStaminaCost());
                ((StaminaOverride) botWStamina).setTotalActionStaminaCost(msg.totalActionStaminaCost());
            }
            else {
                ParagliderMod.LOGGER.error("Couldn't handle packet {}, capability not found", msg);
            }
        }
    }
}