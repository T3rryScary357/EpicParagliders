package net.cravencraft.epicparagliders.skills;

import com.google.common.collect.Maps;
import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.UpdatedServerPlayerMovement;
import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.config.ConfigManager;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.*;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import java.util.Map;

public class ReRegisterSkills {

    public static final float ROLL_CONSUMPTION = 15.0F;
    public static final float STEP_CONSUMPTION = 12.0F;
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
                .setConsumption(ROLL_CONSUMPTION)
                .setAnimations(Animations.BIPED_ROLL_FORWARD, Animations.BIPED_ROLL_BACKWARD), serverPlayerMovement));
        STEP = registerSkill(new NewStepSkill(NewDodgeSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "step"))
                .setConsumption(STEP_CONSUMPTION)
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
        TECHNICIAN = registerSkill(new NewTechnicianSkill(PassiveSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "technician"))
                .setCategory(SkillCategories.PASSIVE).setConsumption(5.0F).setMaxStack(0)
                .setResource(Skill.Resource.NONE).setRequiredXp(5), serverPlayerMovement));
    }
// return (new Skill.Builder(resourceLocation)).setCategory(SkillCategories.PASSIVE).setConsumption(0.0F).setMaxStack(0).setResource(Skill.Resource.NONE).setRequiredXp(5);
    public static boolean reDefineSkill(SkillContainer skillContainer) {

        switch (skillContainer.getSkill().getRegistryName().getPath()) {
            case "roll":
                return setSkillIfAbsent(skillContainer, ROLL);
            case "step":
                return setSkillIfAbsent(skillContainer, STEP);
            case "guard":
                return setSkillIfAbsent(skillContainer, GUARD);
            case "active_guard":
                return setSkillIfAbsent(skillContainer, ACTIVE_GUARD);
            case "energizing_guard":
                return setSkillIfAbsent(skillContainer, ENERGIZING_GUARD);
            case "air_attack":
                return setSkillIfAbsent(skillContainer, AIR_ATTACK);
            case "knockdown_wakeup":
                return setSkillIfAbsent(skillContainer, KNOCKDOWN_WAKEUP);
            case "technician":
                return setSkillIfAbsent(skillContainer, TECHNICIAN);

            default:
                return false;
        }
    }

    //TODO: Make UpdatedServerPlayerMovement a field
    public static void reRegisterToPlayer(UpdatedServerPlayerMovement serverPlayerMovement) {
        ServerPlayerPatch serverPlayerPatch = serverPlayerMovement.serverPlayerPatch;
        EpicParaglidersMod.LOGGER.info("--------------- GETTING SKILLS --------------");
        EpicParaglidersMod.LOGGER.info("Keep skills? " + ConfigManager.KEEP_SKILLS.get());
        for (SkillContainer skillContainer : serverPlayerPatch.getSkillCapability().skillContainers) {
            if (skillContainer.getSkill() != null) {
                EpicParaglidersMod.LOGGER.info("Skill: " + skillContainer.getSkill().getRegistryName());
                EpicParaglidersMod.LOGGER.info("Is skill " + skillContainer.getSkill() + " replaced? " + reDefineSkill(skillContainer));
            }
        }
        EpicParaglidersMod.LOGGER.info("-----------------------------------------------");
    }

    /**
     * Will set the PlayerPatch SkillContainer skill if it is not the same skill as the static one
     * defined in this class. This is done to ensure that this class's skills are always being used.
     *
     * @param skillContainer Skill to be checked
     * @param thisSkill Skill that will replace the container skill if it's different
     * @return Returns whether the skill is replaced or not.
     */
    private static boolean setSkillIfAbsent(SkillContainer skillContainer, Skill thisSkill) {
        return (skillContainer.getSkill() == thisSkill) ? false : skillContainer.setSkill(thisSkill);
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
