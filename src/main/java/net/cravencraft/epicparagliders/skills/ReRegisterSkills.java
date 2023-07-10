package net.cravencraft.epicparagliders.skills;

import com.google.common.collect.Maps;
import net.cravencraft.epicparagliders.capabilities.UpdatedServerPlayerMovement;
import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.*;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import java.util.Map;

public class ReRegisterSkills {
    private static Map<ResourceLocation, Skill> SKILLS = Maps.newHashMap();
    private static Map<ResourceLocation, Skill> LEARNABLE_SKILLS = Maps.newHashMap();

    /** Default skills **/
    public static Skill BASIC_ATTACK;
    public static Skill AIR_ATTACK;
    public static Skill KNOCKDOWN_WAKEUP;
    /** Dodging skills **/
    public static Skill ROLL;
    public static Skill STEP;
    /** Guard skills **/
    public static Skill GUARD;
    public static Skill ACTIVE_GUARD;
    public static Skill ENERGIZING_GUARD;
    /** Passive skills **/
    public static Skill BERSERKER;
    public static Skill STAMINA_PILLAGER;
    public static Skill SWORD_MASTER;
    public static Skill TECHNICIAN;
    /** Special attack skills**/
    public static Skill GUILLOTINE_AXE;
    public static Skill SWEEPING_EDGE;
    public static Skill DANCING_EDGE;
    public static Skill SLAUGHTER_STANCE;
    public static Skill HEARTPIERCER;
    public static Skill GIANT_WHIRLWIND;
    public static Skill FATAL_DRAW;
    public static Skill KATANA_PASSIVE;
    public static Skill LETHAL_SLICING;
    public static Skill RELENTLESS_COMBO;
    public static Skill LIECHTENAUER;
    public static Skill EVISCERATE;
    public static Skill BLADE_RUSH;

    public static void setNewSkills(UpdatedServerPlayerMovement serverPlayerMovement) {

        ROLL = registerSkill(new NewDodgeSkill(NewDodgeSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "roll"))
                .setConsumption(7.0F)
                .setAnimations(Animations.BIPED_ROLL_FORWARD, Animations.BIPED_ROLL_BACKWARD), serverPlayerMovement));
        STEP = registerSkill(new NewStepSkill(NewDodgeSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "step"))
                .setConsumption(5.0F)
                .setAnimations(Animations.BIPED_STEP_FORWARD, Animations.BIPED_STEP_BACKWARD, Animations.BIPED_STEP_LEFT, Animations.BIPED_STEP_RIGHT), serverPlayerMovement));
        GUARD = registerSkill(new NewGuardSkill(NewGuardSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "guard"))
                .setRequiredXp(0), serverPlayerMovement));
        ACTIVE_GUARD = registerSkill(new NewActiveGuardSkill(NewActiveGuardSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "active_guard")), serverPlayerMovement));
        ENERGIZING_GUARD = registerSkill(new EnergizingGuardSkill(EnergizingGuardSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "energizing_guard"))));
        AIR_ATTACK = registerSkill(new NewAirAttack(NewAirAttack.createBuilder()));
        KNOCKDOWN_WAKEUP = registerSkill(new KnockdownWakeupSkill(DodgeSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "knockdown_wakeup"))
                .setConsumption(0.0F)
                .setCategory(SkillCategories.KNOCKDOWN_WAKEUP)
                .setAnimations(Animations.BIPED_KNOCKDOWN_WAKEUP_LEFT, Animations.BIPED_KNOCKDOWN_WAKEUP_RIGHT)));
    }

    public static boolean reDefineSkill(SkillContainer skillContainer) {

        switch (skillContainer.getSkill().getRegistryName().getPath()) {
            case "roll":
                return skillContainer.setSkill(ROLL);
            case "step":
                return skillContainer.setSkill(STEP);
            case "guard":
                return skillContainer.setSkill(GUARD);
            case "active_guard":
                return skillContainer.setSkill(ACTIVE_GUARD);
            case "energizing_guard":
                return skillContainer.setSkill(ENERGIZING_GUARD);
            case "air_attack":
                return skillContainer.setSkill(AIR_ATTACK);
            case "knockdown_wakeup":
                return skillContainer.setSkill(KNOCKDOWN_WAKEUP);

            default:
                return false;
        }
    }

    //TODO: Make UpdatedServerPlayerMovement a field
    public static void reRegisterToPlayer(UpdatedServerPlayerMovement serverPlayerMovement) {
        ServerPlayerPatch serverPlayerPatch = serverPlayerMovement.serverPlayerPatch;
        for (SkillContainer skillContainer : serverPlayerPatch.getSkillCapability().skillContainers) {
            if (skillContainer.getSkill() != null) {
                reDefineSkill(skillContainer);
            }
        }
    }

    private static Skill registerSkill(Skill skill) {
        registerIfAbsent(SKILLS, skill);

        return skill;
    }

    private static void registerIfAbsent(Map<ResourceLocation, Skill> map, Skill skill) {
        if (map.containsKey(skill.getRegistryName())) {
            EpicFightMod.LOGGER.info("Duplicated skill name : " + skill.getRegistryName() + ". Registration was skipped.");
        } else {
            map.put(skill.getRegistryName(), skill);
        }
    }
}
