package net.cravencraft.epicparagliders.events;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.UpdatedClientPlayerMovement;
import net.cravencraft.epicparagliders.capabilities.UpdatedServerPlayerMovement;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tictim.paraglider.capabilities.ClientPlayerMovement;
import tictim.paraglider.capabilities.PlayerMovement;
import tictim.paraglider.capabilities.ServerPlayerMovement;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

@Mod.EventBusSubscriber(modid = EpicParaglidersMod.MOD_ID)
public final class EpicParaglidersEventHandler {

    private EpicParaglidersEventHandler() {}

    /**
     * Instantiates the new UpdatedPlayerMovement classes. Both server side and client side.
     * Through a few different conditionals the classes are set to ensure that if the
     * Paragliders PlayerMovement classes are changed or null (death, logout, dimension change),
     * then the new UpdatedPlayerMovement classes are also changed.
     *
     * @param event
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        PlayerMovement pm = PlayerMovement.of(event.player);
        if (pm != null && event.phase==TickEvent.Phase.END) {
            if (pm instanceof ServerPlayerMovement serverPlayerMovement) {
                if (UpdatedServerPlayerMovement.instance == null) {
                    EpicParaglidersMod.LOGGER.info("SETTING NEW SERVER MOVEMENT");
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
                    EpicParaglidersMod.LOGGER.info("SETTING NEW CLIENT MOVEMENT");
                    new UpdatedClientPlayerMovement(clientPlayerMovement);
                    if (ClientEngine.instance.getPlayerPatch() != null) {
                        ClientEngine.instance.getPlayerPatch().getOriginal().getAttribute(EpicFightAttributes.MAX_STAMINA.get()).setBaseValue(0.0D);
                    }
                }
                 if (clientPlayerMovement != UpdatedClientPlayerMovement.instance.clientPlayerMovement) {
                    EpicParaglidersMod.LOGGER.info("Does client pm = new pm? " + (clientPlayerMovement == UpdatedClientPlayerMovement.instance.clientPlayerMovement));
                    new UpdatedClientPlayerMovement(clientPlayerMovement);
                     if (ClientEngine.instance.getPlayerPatch() != null) {
                         ClientEngine.instance.getPlayerPatch().getOriginal().getAttribute(EpicFightAttributes.MAX_STAMINA.get()).setBaseValue(0.0D);
                     }
                }

                UpdatedClientPlayerMovement.instance.update();
            }
        }
    }
}
