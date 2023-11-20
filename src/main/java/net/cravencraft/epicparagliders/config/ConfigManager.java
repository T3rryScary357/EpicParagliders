package net.cravencraft.epicparagliders.config;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigManager {
    public static ClientConfig CLIENT_CONFIG;
    public static ServerConfig SERVER_CONFIG;

//    static {
//        final Pair<ServerConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
//    }

    public static void registerConfigs() {
        ForgeConfigSpec.Builder client = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder server = new ForgeConfigSpec.Builder();

        CLIENT_CONFIG = new ClientConfig(client);
        SERVER_CONFIG = new ServerConfig(server);
    }

    @Mod.EventBusSubscriber(modid = EpicParaglidersMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Events {

        @SubscribeEvent
        public static void modConfig(ModConfigEvent.Reloading event) {

            ModConfig config = event.getConfig();
            if (config.getType() != ModConfig.Type.SERVER) {
                return;
            }
            EpicParaglidersMod.LOGGER.info("INSIDE RELOADING CONFIG EVENT");
            EpicParaglidersMod.LOGGER.info("CONFIG TYPE: " + config.getType());
            EpicParaglidersMod.LOGGER.info("CONFIG NAME: " + config.getFileName());
            EpicParaglidersMod.LOGGER.info("CONFIG SPEC: " + config.getSpec());
            EpicParaglidersMod.LOGGER.info("CONFIG DATA: " + config.getConfigData());
        }
    }
}
