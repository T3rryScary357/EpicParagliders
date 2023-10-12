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
    private static ForgeConfigSpec.DoubleValue BASE_MELEE_STAMINA_MULTIPLIER;
    private static ForgeConfigSpec.DoubleValue DAGGER_STAMINA_MULTIPLIER;
    private static ForgeConfigSpec.DoubleValue SWORD_STAMINA_MULTIPLIER;
    private static ForgeConfigSpec.DoubleValue LONG_SWORD_STAMINA_MULTIPLIER;
    private static ForgeConfigSpec.DoubleValue GREAT_SWORD_STAMINA_MULTIPLIER;
    private static ForgeConfigSpec.DoubleValue UCHIGATANA_STAMINA_MULTIPLIER;
    private static ForgeConfigSpec.DoubleValue TACHI_STAMINA_MULTIPLIER;
    private static ForgeConfigSpec.DoubleValue SPEAR_STAMINA_MULTIPLIER;
    private static ForgeConfigSpec.DoubleValue KNUCKLE_STAMINA_MULTIPLIER;
    private static ForgeConfigSpec.DoubleValue AXE_STAMINA_MULTIPLIER;

    /**
     * Ranged Attacks
     */
    private static ForgeConfigSpec.DoubleValue BASE_RANGED_STAMINA_MULTIPLIER;

    /**
     * Skills
     */
    private static ForgeConfigSpec.DoubleValue BASE_BLOCK_STAMINA_MULTIPLIER;
    private static ForgeConfigSpec.DoubleValue BASE_DODGE_STAMINA_MULTIPLIER;
    private static ForgeConfigSpec.DoubleValue PARRY_PENALTY_MULTIPLIER;
    private static ForgeConfigSpec.IntValue PARRY_PERCENT_MODIFIER;
    private static ForgeConfigSpec.BooleanValue PARRY_DRAIN;

    /**
     * Status Effects & Weapon Configs
     */
    private static ForgeConfigSpec.ConfigValue<List<? extends Integer>> DEPLETION_EFFECT_LIST;
    private static ForgeConfigSpec.ConfigValue<List<? extends Integer>> DEPLETION_EFFECT_STRENGTH_LIST;

    public static double baseMeleeStaminaMultiplier() {
        return BASE_MELEE_STAMINA_MULTIPLIER.get();
    }

    public static double daggerStaminaMultiplier() {
        return DAGGER_STAMINA_MULTIPLIER.get();
    }

    public static double swordStaminaMultiplier() {
        return SWORD_STAMINA_MULTIPLIER.get();
    }

    public static double longSwordMultiplier() {
        return LONG_SWORD_STAMINA_MULTIPLIER.get();
    }

    public static double greatSwordStaminaMultiplier() {
        return GREAT_SWORD_STAMINA_MULTIPLIER.get();
    }

    public static double uchigatanaStaminaMultiplier() {
        return UCHIGATANA_STAMINA_MULTIPLIER.get();
    }

    public static double tachiStaminaMultiplier() {
        return TACHI_STAMINA_MULTIPLIER.get();
    }

    public static double spearStaminaMultiplier() {
        return SPEAR_STAMINA_MULTIPLIER.get();
    }

    public static double knuckleStaminaMultiplier() {
        return KNUCKLE_STAMINA_MULTIPLIER.get();
    }

    public static double axeStaminaMultiplier() {
        return AXE_STAMINA_MULTIPLIER.get();
    }

	public static double baseRangedStaminaMultiplier() {
		return BASE_RANGED_STAMINA_MULTIPLIER.get();
	}

	public static double baseBlockStaminaMultiplier() {
		return BASE_BLOCK_STAMINA_MULTIPLIER.get();
	}

	public static double baseDodgeStaminaMultiplier() {
		return BASE_DODGE_STAMINA_MULTIPLIER.get();
	}

    public static double parryPenaltyMultiplier() {
        return PARRY_PENALTY_MULTIPLIER.get();
    }
    public static int parryPercentModifier() { return PARRY_PERCENT_MODIFIER.get(); }
    public static boolean parryDrain() { return PARRY_DRAIN.get(); }

    public static List<Integer> depletionEffectList() { return (List<Integer>) DEPLETION_EFFECT_LIST.get(); }
    public static List<Integer> depletionEffectStrengthList() { return (List<Integer>) DEPLETION_EFFECT_STRENGTH_LIST.get(); }

    public static void init() {
        Builder server = new Builder();
        server.push("stamina");
        BASE_RANGED_STAMINA_MULTIPLIER = server.defineInRange("weapons.base_ranged_stamina_multiplier", 1.0, 0.0, 10.0);
        BASE_MELEE_STAMINA_MULTIPLIER = server.comment("Base multiplier for all melee based attacks.").defineInRange("weapons.base_melee_stamina_multiplier", 1.0, 0.0, 10.0);
        DAGGER_STAMINA_MULTIPLIER = server.defineInRange("weapons.dagger_stamina_multiplier", 2.5, 0.0, 10.0);
        SWORD_STAMINA_MULTIPLIER = server.defineInRange("weapons.sword_stamina_multiplier", 2.5, 0.0, 10.0);
        LONG_SWORD_STAMINA_MULTIPLIER = server.defineInRange("weapons.longsword_stamina_multiplier", 2.5, 0.0, 10.0);
        GREAT_SWORD_STAMINA_MULTIPLIER = server.defineInRange("weapons.greatsword_stamina_multiplier", 1.5, 0.0, 10.0);
        UCHIGATANA_STAMINA_MULTIPLIER = server.defineInRange("weapons.uchigatana_stamina_multiplier", 2.0, 0.0, 10.0);
        TACHI_STAMINA_MULTIPLIER = server.defineInRange("weapons.tachi_stamina_multiplier", 2.0, 0.0, 10.0);
        SPEAR_STAMINA_MULTIPLIER = server.defineInRange("weapons.spear_stamina_multiplier", 2.0, 0.0, 10.0);
        KNUCKLE_STAMINA_MULTIPLIER = server.defineInRange("weapons.knuckle_stamina_multiplier", 2.0, 0.0, 10.0);
        AXE_STAMINA_MULTIPLIER = server.defineInRange("weapons.axe_stamina_multiplier", 2.5, 0.0, 10.0);

        BASE_BLOCK_STAMINA_MULTIPLIER = server.defineInRange("skills.block.block_stamina_multiplier", 7.5, 0.0, 10.0);
        BASE_DODGE_STAMINA_MULTIPLIER = server.defineInRange("skills.dodge.dodge_stamina_multiplier", 1.0, 0.0, 10.0);

        PARRY_PENALTY_MULTIPLIER = server.comment("How much stamina will be drained on a failed parry.\n" +
                                                  "Make the same as block_stamina_multiplier for no penalty.")
                .defineInRange("skills.block.block_stamina_multiplier", 9.0, 0.0, 10.0);

        PARRY_DRAIN = server.comment("If true, stamina drain will be reduced based on the percentage defined below.\n" +
                                     "If false, missing stamina will be replenished based on the percentage defined below.")
                .define("parry.drain",false);

        PARRY_PERCENT_MODIFIER = server.comment("How much stamina reduction will be applied on a successful parry,\n" +
                                                " or how much missing stamina will be replenished on a successful parry.")
                .defineInRange("skills.parry.parry_percent_modifier", 30, 0, 100);

        server.pop();

        server.push("depletion_effects");
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