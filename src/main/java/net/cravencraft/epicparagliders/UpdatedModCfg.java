package net.cravencraft.epicparagliders;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;

import static net.cravencraft.epicparagliders.EpicParaglidersMod.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Bus.MOD)
public final class UpdatedModCfg {
	private UpdatedModCfg(){}
	private static BooleanValue paraglidingConsumesStamina;
	private static BooleanValue runningConsumesStamina;

	public static boolean paraglidingConsumesStamina() {
		return paraglidingConsumesStamina.get();}
	public static boolean runningConsumesStamina(){
		return runningConsumesStamina.get();
	}

	public static void init(){
		ForgeConfigSpec.Builder server = new ForgeConfigSpec.Builder();
		server.push("stamina");
//		paraglidingConsumesStamina = server.comment("Paragliding and ascending will consume stamina.").define("paraglidingConsumesStamina", true);
//		runningConsumesStamina = server.comment("Actions other than paragliding or ascending will consume stamina.").define("runningAndSwimmingConsumesStamina", false);

		server.pop();
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server.build());
	}
}
