package net.cravencraft.epicparagliders.events;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.UpdatedClientPlayerMovement;
import net.cravencraft.epicparagliders.capabilities.UpdatedServerPlayerMovement;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tictim.paraglider.ModCfg;
import tictim.paraglider.capabilities.ClientPlayerMovement;
import tictim.paraglider.capabilities.PlayerMovement;
import tictim.paraglider.capabilities.ServerPlayerMovement;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = EpicParaglidersMod.MOD_ID)
public final class EpicParaglidersEventHandler {

    private EpicParaglidersEventHandler() {}

//    /**
//     * Once the server is launched, this method looks into the ModCfg.class from Paragliders and
//     * changes the 'paraglidingConsumesStamina' and 'runningConsumesStamina' attributes to false.
//     * This enables us to disable the default Paragliders stamina wheel from rendering, and allows
//     * us to render our own. Needed because the default doesn't allow us to modify it to include
//     * attacks as a form of stamina consumption.
//     *
//     * @param event The ServerStartEvent fires every time the server is started and ready to play.
//     */
//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    public static void ServerStartedEvent(ServerStartedEvent event) throws NoSuchFieldException, IllegalAccessException {
//        disableParagliderStaminaWheel("paraglidingConsumesStamina");
//        disableParagliderStaminaWheel("runningConsumesStamina");
//    }


//    /**
//     * Using reflection we modify the boolean fields within the ModCfg class in order to disable
//     * the Paragliders stamina wheel render system, which allows us to enable our own.
//     *
//     * @param fieldName Either 'paraglidingConsumesStamina' or 'runningConsumesStamina'
//     * @throws NoSuchFieldException
//     * @throws IllegalAccessException
//     */
//    private static void disableParagliderStaminaWheel(String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Field field = ModCfg.class.getDeclaredField(fieldName);
//        field.setAccessible(true);
//        ForgeConfigSpec.BooleanValue fieldValue = (ForgeConfigSpec.BooleanValue) field.get(null);
//        fieldValue.set(false);
//        field.set(null, fieldValue);
//    }

//    /**
//     * Instantiates the new UpdatedPlayerMovement classes. Both server side and client side.
//     * Through a few different conditionals the classes are set to ensure that if the
//     * Paragliders PlayerMovement classes are changed or null (death, logout, dimension change),
//     * then the new UpdatedPlayerMovement classes are also changed.
//     *
//     * @param event The PlayerTick event fire for every tick both client and server side.
//     */
//    @SubscribeEvent
//    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        PlayerMovement pm = PlayerMovement.of(event.player);
//        if (pm != null && event.phase==TickEvent.Phase.END) {
//            if (pm instanceof ServerPlayerMovement serverPlayerMovement) {
//                if (UpdatedServerPlayerMovement.instance == null) {
//                    EpicParaglidersMod.LOGGER.info("SETTING NEW SERVER MOVEMENT");
//                    new UpdatedServerPlayerMovement(serverPlayerMovement);
//                }
//                if (serverPlayerMovement != UpdatedServerPlayerMovement.instance.serverPlayerMovement) {
//                    EpicParaglidersMod.LOGGER.info("Does server pm = new pm? " + (serverPlayerMovement == UpdatedServerPlayerMovement.instance.serverPlayerMovement));
//                    new UpdatedServerPlayerMovement(serverPlayerMovement);
//                }
//
//                UpdatedServerPlayerMovement.instance.update();
//            }
//            else if (pm instanceof ClientPlayerMovement clientPlayerMovement) {
//
//                if (UpdatedClientPlayerMovement.instance == null) {
//                    EpicParaglidersMod.LOGGER.info("SETTING NEW CLIENT MOVEMENT");
//                    new UpdatedClientPlayerMovement(clientPlayerMovement);
//                }
//                 if (clientPlayerMovement != UpdatedClientPlayerMovement.instance.clientPlayerMovement) {
//                    EpicParaglidersMod.LOGGER.info("Does client pm = new pm? " + (clientPlayerMovement == UpdatedClientPlayerMovement.instance.clientPlayerMovement));
//                    new UpdatedClientPlayerMovement(clientPlayerMovement);
//                }
//
//                UpdatedClientPlayerMovement.instance.update();
//            }
//        }
//    }
}
