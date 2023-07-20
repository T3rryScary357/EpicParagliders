package net.cravencraft.epicparagliders.events;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.UpdatedClientPlayerMovement;
import net.cravencraft.epicparagliders.capabilities.UpdatedServerPlayerMovement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tictim.paraglider.capabilities.ClientPlayerMovement;
import tictim.paraglider.capabilities.PlayerMovement;
import tictim.paraglider.capabilities.ServerPlayerMovement;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = EpicParaglidersMod.MOD_ID)
public final class EpicParaglidersEventHandler {

    private EpicParaglidersEventHandler() {}

    /**
     * Looks up the paraglider-server.toml file in the serverconfig file, and changes the paraglidingConsumesStamina and
     * runningAndSwimmingConsumesStamina attributes to false so that the epicparagliders-server.toml can properly
     * override it. This should remove the need for players to manually configure the files themselves. Works for
     * singleplayer and multiplayer worlds.
     *
     * TODO: Test in CurseForge. Brand new world.
     *
     * @param event The ServerStartEvent fires every time the server is started and ready to play.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void testGetServerPath(ServerStartedEvent event) throws IOException {
        MinecraftServer server = event.getServer();
        WorldData worldData = server.getWorldData();

        if (worldData != null) {
            String worldName = worldData.getLevelName();
            File paragliderConfig;

            if (server.isSingleplayer()) {
                EpicParaglidersMod.LOGGER.info("IS SINGLEPLAYER");
                paragliderConfig = new File(server.getServerDirectory() + "/saves/" + worldName + "/serverconfig/paraglider-server.toml");
            }
            else {
                paragliderConfig = new File(server.getServerDirectory() + "/" + worldName + "/serverconfig/paraglider-server.toml");
            }

            if (paragliderConfig.isFile()) {
                EpicParaglidersMod.LOGGER.info("IS FILE");
                modifyParagliderServerConfig(paragliderConfig);
            }
        }
    }

    /**
     * Separate method that performs all the reading and modifying of the paraglider-server.toml file.
     * Will look up the two attributes for paragliding and running to consume stamina and set them to false
     * for EpicParagliders to take over.
     *
     * @param paragliderConfig the paraglider-server.toml file to be edited
     * @throws IOException
     */
    private static void modifyParagliderServerConfig(File paragliderConfig) throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(paragliderConfig.toPath(), StandardCharsets.UTF_8));

        for (int i = 0; i < fileContent.size(); i++) {
            if (fileContent.get(i).contains("paraglidingConsumesStamina = true") || fileContent.get(i).contains("runningAndSwimmingConsumesStamina = true")) {
                EpicParaglidersMod.LOGGER.info(fileContent.get(i) + " || BEING CHANGED");
                String overrideSetting = fileContent.get(i).replace("true", "false");
                fileContent.set(i, overrideSetting);
            }
        }

        Files.write(paragliderConfig.toPath(), fileContent, StandardCharsets.UTF_8);
    }

    /**
     * Instantiates the new UpdatedPlayerMovement classes. Both server side and client side.
     * Through a few different conditionals the classes are set to ensure that if the
     * Paragliders PlayerMovement classes are changed or null (death, logout, dimension change),
     * then the new UpdatedPlayerMovement classes are also changed.
     *
     * @param event The PlayerTick event fire for every tick both client and server side.
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
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
