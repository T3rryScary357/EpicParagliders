package net.cravencraft.epicparagliders;

import com.google.common.collect.ImmutableList;
import net.cravencraft.epicparagliders.capabilities.UpdatedPlayerState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import tictim.paraglider.ModCfg;
import tictim.paraglider.ParagliderMod;
import tictim.paraglider.capabilities.PlayerState;
import tictim.paraglider.loot.ParagliderModifier;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tictim.paraglider.ParagliderMod.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Bus.MOD)
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
		paraglidingConsumesStamina = server.comment("Paragliding and ascending will consume stamina.").define("paraglidingConsumesStamina", true);
		runningConsumesStamina = server.comment("Actions other than paragliding or ascending will consume stamina.").define("runningAndSwimmingConsumesStamina", false);

		//TODO: Maybe add the updated state to include config options for your mod
//		server.push("consumptions");
//		for(PlayerState state : PlayerState.values()){
//			state.setConfig(server.defineInRange(state.id+"StaminaConsumption", state.defaultChange, Integer.MIN_VALUE, Integer.MAX_VALUE));
//		}
//		server.pop();
		server.pop();
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server.build());
	}
}
