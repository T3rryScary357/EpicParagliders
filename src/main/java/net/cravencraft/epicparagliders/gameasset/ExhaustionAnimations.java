package net.cravencraft.epicparagliders.gameasset;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.model.ClientModels;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.api.model.Model;
import yesman.epicfight.gameasset.Models;

public class ExhaustionAnimations {
    public static StaticAnimation EXHAUSTED_IDLE;
    public static StaticAnimation EXHAUSTED_WALK;
    public static StaticAnimation EXHAUSTED_IDLE_CROSSBOW;
    public static StaticAnimation EXHAUSTED_WALK_CROSSBOW;
    public static StaticAnimation EXHAUSTED_IDLE_GREATSWORD;
    public static StaticAnimation EXHAUSTED_WALK_GREATSWORD;
    public static StaticAnimation EXHAUSTED_IDLE_TACHI;
    public static StaticAnimation EXHAUSTED_WALK_TACHI;
    public static StaticAnimation EXHAUSTED_IDLE_SPEAR;
    public static StaticAnimation EXHAUSTED_WALK_SPEAR;
    public static StaticAnimation EXHAUSTED_IDLE_LIECHTENAUER;
    public static StaticAnimation EXHAUSTED_WALK_LIECHTENAUER;
    public static StaticAnimation EXHAUSTED_IDLE_SHEATH;
    public static StaticAnimation EXHAUSTED_WALK_SHEATH;
    public static StaticAnimation EXHAUSTED_IDLE_UNSHEATH;
    public static StaticAnimation EXHAUSTED_WALK_UNSHEATH;
    public static StaticAnimation EXHAUSTED_WALK_KATANA;

    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put(EpicParaglidersMod.MOD_ID, ExhaustionAnimations::build);
    }

    private static void build() {
        Models<?> models = FMLEnvironment.dist == Dist.CLIENT ? ClientModels.LOGICAL_CLIENT : Models.LOGICAL_SERVER;
        Model biped = models.biped;

        EXHAUSTED_IDLE = new StaticAnimation(true, "biped/living/exhausted_idle", biped);
        EXHAUSTED_WALK = new StaticAnimation(true, "biped/living/exhausted_walk", biped);
        EXHAUSTED_IDLE_CROSSBOW = new StaticAnimation(true, "biped/living/exhausted_idle_crossbow", biped);
        EXHAUSTED_WALK_CROSSBOW = new StaticAnimation(true, "biped/living/exhausted_walk_crossbow", biped);
        EXHAUSTED_IDLE_GREATSWORD = new StaticAnimation(true, "biped/living/exhausted_idle", biped);
        EXHAUSTED_WALK_GREATSWORD = new StaticAnimation(true, "biped/living/exhausted_walk_greatsword", biped);
        EXHAUSTED_IDLE_TACHI = new StaticAnimation(true, "biped/living/exhausted_idle_tachi", biped);
        EXHAUSTED_WALK_TACHI = new StaticAnimation(true, "biped/living/exhausted_walk_tachi", biped);
        EXHAUSTED_IDLE_SPEAR = new StaticAnimation(true, "biped/living/exhausted_idle_spear", biped);
        EXHAUSTED_WALK_SPEAR = new StaticAnimation(true, "biped/living/exhausted_walk", biped);
        EXHAUSTED_IDLE_LIECHTENAUER = new StaticAnimation(true, "biped/living/exhausted_idle_liechtenauer", biped);
        EXHAUSTED_WALK_LIECHTENAUER = new StaticAnimation(true, "biped/living/exhausted_walk_liechtenauer", biped);
        EXHAUSTED_IDLE_SHEATH = new StaticAnimation(true, "biped/living/exhausted_idle_sheath", biped);
        EXHAUSTED_WALK_SHEATH = new StaticAnimation(true, "biped/living/exhausted_walk_sheath", biped);
        EXHAUSTED_IDLE_UNSHEATH = new StaticAnimation(true, "biped/living/exhausted_idle_unsheath", biped);
        EXHAUSTED_WALK_UNSHEATH = new StaticAnimation(true, "biped/living/exhausted_walk_unsheath", biped);
        EXHAUSTED_WALK_KATANA = new StaticAnimation(true, "biped/living/exhausted_walk_katana", biped);
    }
}