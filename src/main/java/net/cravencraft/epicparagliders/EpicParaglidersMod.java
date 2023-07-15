package net.cravencraft.epicparagliders;

import net.cravencraft.epicparagliders.capabilities.UpdatedPlayerMovement;
import net.cravencraft.epicparagliders.events.FixParagliderRender;
import net.cravencraft.epicparagliders.network.ModNet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tictim.paraglider.contents.Contents;

@Mod(EpicParaglidersMod.MOD_ID)
@Mod.EventBusSubscriber(modid = EpicParaglidersMod.MOD_ID, bus = Bus.MOD)
public class EpicParaglidersMod
{
    public static final Logger LOGGER = LogManager.getLogger("EpicParagliders");
    public static final String MOD_ID = "epicparagliders";

    public EpicParaglidersMod()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Contents.registerEventHandlers(eventBus);
        UpdatedModCfg.init();
        ModNet.init();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new FixParagliderRender());
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        //TODO: Might be needed for the new PlayerMovement classes.
        event.register(UpdatedPlayerMovement.class);
    }
}
