package net.cravencraft.epicparagliders.utils;

import com.google.common.collect.Multimap;
import net.cravencraft.epicparagliders.EPModCfg;
import net.cravencraft.epicparagliders.EpicParaglidersAttributes;
import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.item.*;

import java.util.Iterator;

public class MathUtils {

    public static double calculateTriangularNumber(int baseNumber) {
        int absBaseNumber = Math.abs(baseNumber);
        double triangularNumber = (absBaseNumber * (absBaseNumber + 1)) / 2;
        return (baseNumber > 0) ? triangularNumber : -triangularNumber;
    }

    public static double calculateTriangularRoot(double triangularNumber) {
        double triangularRoot = ((Math.sqrt(Math.abs(triangularNumber) * 8 - 1) - 1) / 2);
        return (triangularNumber > 0) ? triangularRoot : -triangularRoot;
    }

    public static double calculateModifiedTriangularRoot(double triangularNumber, double percentageOfTriangularNumber) {
        double triangularRoot = ((Math.sqrt(Math.abs(triangularNumber) * percentageOfTriangularNumber * 8 - 1) - 1) / 2);
        return (triangularNumber > 0) ? triangularRoot : -triangularRoot;
    }

    //TODO: Create one more method here that takes in two numbers and combines them.
    //      Since this will be a very common piece of math used in this project.

    /**
     * Some math done here to determine how much stamina should be consumed from each weapon type.
     * Takes in a weapon's attack strengt and tier, and the world's config setting to create a balanced stamina cost.
     *
     * @param player The given player attacking
     * @return The amount of stamina that should be drained from the attacking weapon
     */
    public static int getAttackStaminaCost(Player player) {
        double configFactor = EPModCfg.baseMeleeStaminaConsumption();
        //TODO: Double check duel wielding with this.
        //      Could easily add offhand support too by checking which
        //      hand is swinging the weapon.
        Item weaponItem = player.getMainHandItem().getItem();
        double attackDamageFactor = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
//        player.getAttribute(Attributes.ATTACK_DAMAGE).
        double staminaOverride = player.getAttributeValue(EpicParaglidersAttributes.WEAPON_STAMINA_CONSUMPTION.get());
        double weaponTypeOverride = player.getAttributeValue(EpicParaglidersAttributes.WEAPON_TYPE.get());
        //TODO: IT'S ALIVE. IT'S ALIVE. OK, Need to test this in multiplayer and ensure that these attribute values don't override
        //      another player's used item when they are different.
        EpicParaglidersMod.LOGGER.info("STAMINA ATTRIBUTE: " + player.getAttributeValue(EpicParaglidersAttributes.WEAPON_STAMINA_CONSUMPTION.get()));
        EpicParaglidersMod.LOGGER.info("OVERRIDDEN STAMINA COST: " + player.getAttribute(EpicParaglidersAttributes.WEAPON_STAMINA_CONSUMPTION.get()).getValue());

        //TODO: Remove all loggers before finishing
        if (weaponItem instanceof AxeItem || weaponTypeOverride == 1.0) {
            EpicParaglidersMod.LOGGER.info("AxeItem CONSUMPTION CONFIG: " + EPModCfg.axeStaminaConsumption());
            configFactor *= EPModCfg.axeStaminaConsumption();
        }
        else if (weaponItem instanceof DaggerItem || weaponTypeOverride == 3.0) {
            EpicParaglidersMod.LOGGER.info("DaggerItem CONSUMPTION CONFIG: " + EPModCfg.daggerStaminaConsumption());
            configFactor *= EPModCfg.daggerStaminaConsumption();
        }
        else if (weaponItem instanceof KnuckleItem || weaponTypeOverride == 4.0) {
            EpicParaglidersMod.LOGGER.info("KnuckleItem CONSUMPTION CONFIG: " + EPModCfg.knuckleStaminaConsumption());
            configFactor *= EPModCfg.knuckleStaminaConsumption();
        }
        else if (weaponItem instanceof GreatswordItem || weaponTypeOverride == 5.0) {
            EpicParaglidersMod.LOGGER.info("GreatswordItem CONSUMPTION CONFIG: " + EPModCfg.greatSwordStaminaConsumption());
            configFactor *= EPModCfg.greatSwordStaminaConsumption();
        }
        else if (weaponItem instanceof KatanaItem || weaponTypeOverride == 6.0) {
            EpicParaglidersMod.LOGGER.info("KatanaItem CONSUMPTION CONFIG: " + EPModCfg.katanaStaminaConsumption());
            configFactor *= EPModCfg.katanaStaminaConsumption();
        }
        else if (weaponItem instanceof LongswordItem || weaponTypeOverride == 7.0) {
            EpicParaglidersMod.LOGGER.info("LongswordItem CONSUMPTION CONFIG: " + EPModCfg.longSwordConsumption());
            configFactor *= EPModCfg.longSwordConsumption();
        }
        else if (weaponItem instanceof SpearItem || weaponTypeOverride == 8.0) {
            EpicParaglidersMod.LOGGER.info("SpearItem CONSUMPTION CONFIG: " + EPModCfg.spearStaminaConsumption());
            configFactor *= EPModCfg.spearStaminaConsumption();
        }
        else if (weaponItem instanceof SwordItem || weaponTypeOverride == 9.0) {
            EpicParaglidersMod.LOGGER.info("SwordItem CONSUMPTION CONFIG: " + EPModCfg.swordStaminaConsumption());
            configFactor *= EPModCfg.swordStaminaConsumption();
        }
        else if (weaponItem instanceof TachiItem || weaponTypeOverride == 10.0) {
            EpicParaglidersMod.LOGGER.info("TachiItem CONSUMPTION CONFIG: " + EPModCfg.tachiStaminaConsumption());
            configFactor *= EPModCfg.tachiStaminaConsumption();
        }

        //TODO: I still need to add one more attribute that gets the type of the weapon (i.e., "greatsword" or "sword").
        //      This will allow me to add an extra conditional to all the above statements so these edited weapons can
        //      be fully compatible with the stamina drain system.
        double totalStaminaDrain;
        if (staminaOverride > 0) {
            totalStaminaDrain = staminaOverride * configFactor;
        }
        else {
            totalStaminaDrain = attackDamageFactor * configFactor;
        }

        EpicParaglidersMod.LOGGER.info("ATTACK DAMAGE VALUE: " + attackDamageFactor);
        EpicParaglidersMod.LOGGER.info("COMBINED CONFIG FACTOR: " + configFactor);
//        double totalStaminaDrain = attackDamageFactor * configFactor;
        EpicParaglidersMod.LOGGER.info("Current stamina drain: " + totalStaminaDrain);
        return (int) Math.ceil(totalStaminaDrain);
    }
}
