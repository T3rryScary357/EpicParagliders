package net.cravencraft.epicparagliders;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Collections;
import java.util.List;

import static net.cravencraft.epicparagliders.EpicParaglidersMod.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Bus.MOD)
public final class EPModCfg {
    private EPModCfg() {
    }

    /**
     * Melee Attacks
     */
    private static ForgeConfigSpec.DoubleValue BASE_MELEE_STAMINA_CONSUMPTION;
    private static ForgeConfigSpec.DoubleValue DAGGER_STAMINA_CONSUMPTION;
    private static ForgeConfigSpec.DoubleValue SWORD_STAMINA_CONSUMPTION;
    private static ForgeConfigSpec.DoubleValue LONG_SWORD_STAMINA_CONSUMPTION;
    private static ForgeConfigSpec.DoubleValue GREAT_SWORD_STAMINA_CONSUMPTION;
    private static ForgeConfigSpec.DoubleValue KATANA_STAMINA_CONSUMPTION;
    private static ForgeConfigSpec.DoubleValue TACHI_STAMINA_CONSUMPTION;
    private static ForgeConfigSpec.DoubleValue SPEAR_STAMINA_CONSUMPTION;
    private static ForgeConfigSpec.DoubleValue KNUCKLE_STAMINA_CONSUMPTION;
    private static ForgeConfigSpec.DoubleValue AXE_STAMINA_CONSUMPTION;

    /**
     * Ranged Attacks
     */
    private static ForgeConfigSpec.DoubleValue BASE_RANGED_STAMINA_CONSUMPTION;

    /**
     * Skills
     */
    private static ForgeConfigSpec.DoubleValue BASE_BLOCK_STAMINA_CONSUMPTION;
    private static ForgeConfigSpec.DoubleValue BASE_DODGE_STAMINA_CONSUMPTION;

    /**
     * Status Effects & Weapon Configs
     */
    private static ForgeConfigSpec.ConfigValue<List<? extends Integer>> DEPLETION_EFFECT_LIST;
    private static ForgeConfigSpec.ConfigValue<List<? extends Integer>> DEPLETION_EFFECT_STRENGTH_LIST;

    public static double baseMeleeStaminaConsumption() {
        return BASE_MELEE_STAMINA_CONSUMPTION.get();
    }

    public static double daggerStaminaConsumption() {
        return DAGGER_STAMINA_CONSUMPTION.get();
    }

    public static double swordStaminaConsumption() {
        return SWORD_STAMINA_CONSUMPTION.get();
    }

    public static double longSwordConsumption() {
        return LONG_SWORD_STAMINA_CONSUMPTION.get();
    }

    public static double greatSwordStaminaConsumption() {
        return GREAT_SWORD_STAMINA_CONSUMPTION.get();
    }

    public static double katanaStaminaConsumption() {
        return KATANA_STAMINA_CONSUMPTION.get();
    }

    public static double tachiStaminaConsumption() {
        return TACHI_STAMINA_CONSUMPTION.get();
    }

    public static double spearStaminaConsumption() {
        return SPEAR_STAMINA_CONSUMPTION.get();
    }

    public static double knuckleStaminaConsumption() {
        return KNUCKLE_STAMINA_CONSUMPTION.get();
    }

    public static double axeStaminaConsumption() {
        return AXE_STAMINA_CONSUMPTION.get();
    }

	public static double baseRangedStaminaConsumption() {
		return BASE_RANGED_STAMINA_CONSUMPTION.get();
	}

	public static double baseBlockStaminaConsumption() {
		return BASE_BLOCK_STAMINA_CONSUMPTION.get();
	}

	public static double baseDodgeStaminaConsumption() {
		return BASE_DODGE_STAMINA_CONSUMPTION.get();
	}

    public static List<Integer> depletionEffectList() { return (List<Integer>) DEPLETION_EFFECT_LIST.get(); }
    public static List<Integer> depletionEffectStrengthList() { return (List<Integer>) DEPLETION_EFFECT_STRENGTH_LIST.get(); }

    public static void init() {
        Builder server = new Builder();
        server.push("Stamina");
        BASE_MELEE_STAMINA_CONSUMPTION = server.comment("How much more/less stamina is consumed from all melee attacks.")
                .defineInRange("baseMeleeStaminaConsumption", 1.0, 0.0, 10.0);
        DAGGER_STAMINA_CONSUMPTION = server.comment("How much more/less stamina is consumed from daggers (stacks with baseMeleeStaminaConsumption).")
                .defineInRange("daggerStaminaConsumption", 1.0, 0.0, 10.0);
        SWORD_STAMINA_CONSUMPTION = server.comment("How much more/less stamina is consumed from swords (stacks with baseMeleeStaminaConsumption)..")
                .defineInRange("swordStaminaConsumption", 1.0, 0.0, 10.0);
        LONG_SWORD_STAMINA_CONSUMPTION = server.comment("How much more/less stamina is consumed from longswords (stacks with baseMeleeStaminaConsumption).")
                .defineInRange("longSwordConsumption", 1.0, 0.0, 10.0);
        GREAT_SWORD_STAMINA_CONSUMPTION = server.comment("How much more/less stamina is consumed from greatswords (stacks with baseMeleeStaminaConsumption).")
                .defineInRange("greatSwordStaminaConsumption", 1.0, 0.0, 10.0);
        KATANA_STAMINA_CONSUMPTION = server.comment("How much more/less stamina is consumed from katanas (stacks with baseMeleeStaminaConsumption).")
                .defineInRange("katanaStaminaConsumption", 1.0, 0.0, 10.0);
        TACHI_STAMINA_CONSUMPTION = server.comment("How much more/less stamina is consumed from tachis (stacks with baseMeleeStaminaConsumption).")
                .defineInRange("tachiStaminaConsumption", 1.0, 0.0, 10.0);
        SPEAR_STAMINA_CONSUMPTION = server.comment("How much more/less stamina is consumed from spears (stacks with baseMeleeStaminaConsumption).")
                .defineInRange("spearStaminaConsumption", 1.0, 0.0, 10.0);
        KNUCKLE_STAMINA_CONSUMPTION = server.comment("How much more/less stamina is consumed from knuckles (stacks with baseMeleeStaminaConsumption).")
                .defineInRange("knuckleStaminaConsumption", 1.0, 0.0, 10.0);
        AXE_STAMINA_CONSUMPTION = server.comment("How much more/less stamina is consumed from axes (stacks with baseMeleeStaminaConsumption).")
                .defineInRange("axeStaminaConsumption", 1.0, 0.0, 10.0);
		BASE_RANGED_STAMINA_CONSUMPTION = server.comment("How much more/less stamina is consumed from ranged attacks with bows or crossbows.")
				.defineInRange("baseRangedStaminaConsumption", 1.0, 0.0, 10.0);
		BASE_BLOCK_STAMINA_CONSUMPTION = server.comment("How much more/less stamina is consumed from using block skills.")
				.defineInRange("baseBlockStaminaConsumption", 1.0, 0.0, 10.0);
		BASE_DODGE_STAMINA_CONSUMPTION = server.comment("How much more/less stamina is consumed from dodge skills.")
				.defineInRange("baseDodgeStaminaConsumption", 1.0, 0.0, 10.0);
        DEPLETION_EFFECT_LIST = server
                .comment("The effect ID that will be applied when a player runs out of stamina (default is Mining Fatigue and Weakness respectively).\n" +
                         "Refer to https://minecraft.fandom.com/wiki/Effect#Effect_list for a list of the effects and their corresponding IDs")
                .defineListAllowEmpty(Collections.singletonList("effects"), () -> ImmutableList.of(4, 18), o -> true);
        DEPLETION_EFFECT_STRENGTH_LIST = server
                .comment("The strength applied to the depletion effect above (The default is 2 and 7. A value such as 4 would apply Weakness IV).\n" +
                         "If no value is set here, and an extra effect is added above, then the effect strength will default to 1.")
                .defineListAllowEmpty(Collections.singletonList("effects_strength"), () -> ImmutableList.of(5, 1), o -> true);

        server.pop();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server.build());
    }
}
