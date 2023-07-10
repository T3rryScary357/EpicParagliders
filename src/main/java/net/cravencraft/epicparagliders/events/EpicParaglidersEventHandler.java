package net.cravencraft.epicparagliders.events;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.UpdatedClientPlayerMovement;
import net.cravencraft.epicparagliders.capabilities.UpdatedServerPlayerMovement;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tictim.paraglider.capabilities.ClientPlayerMovement;
import tictim.paraglider.capabilities.PlayerMovement;
import tictim.paraglider.capabilities.ServerPlayerMovement;

@Mod.EventBusSubscriber(modid = EpicParaglidersMod.MOD_ID)
public final class EpicParaglidersEventHandler {

    private EpicParaglidersEventHandler() {}

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        PlayerMovement pm = PlayerMovement.of(event.player);



        if (pm != null && event.phase==TickEvent.Phase.END) {
            if (pm instanceof ServerPlayerMovement serverPlayerMovement) {
                if (UpdatedServerPlayerMovement.instance == null) {
                    EpicParaglidersMod.LOGGER.info("SETTING NEW CLIENT MOVEMENT");
                    new UpdatedServerPlayerMovement(serverPlayerMovement);
                }
                if (serverPlayerMovement != UpdatedServerPlayerMovement.instance.serverPlayerMovement) {
                    EpicParaglidersMod.LOGGER.info("Does server pm = new pm? " + (serverPlayerMovement == UpdatedServerPlayerMovement.instance.serverPlayerMovement));
                    new UpdatedServerPlayerMovement(serverPlayerMovement);
                }

                UpdatedServerPlayerMovement.instance.update();
            }
            else if (pm instanceof ClientPlayerMovement clientPlayerMovement) {

                if (UpdatedClientPlayerMovement.instance == null) {
                    EpicParaglidersMod.LOGGER.info("SETTING NEW SERVER MOVEMENT");
                    new UpdatedClientPlayerMovement(clientPlayerMovement);
                }
                 if (clientPlayerMovement != UpdatedClientPlayerMovement.instance.clientPlayerMovement) {
                    EpicParaglidersMod.LOGGER.info("Does client pm = new pm? " + (clientPlayerMovement == UpdatedClientPlayerMovement.instance.clientPlayerMovement));
                    new UpdatedClientPlayerMovement(clientPlayerMovement);
                }
                UpdatedClientPlayerMovement.instance.update();
            }
        }
    }
}
