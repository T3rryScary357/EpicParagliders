package net.cravencraft.epicparagliders.utils;

import net.cravencraft.epicparagliders.EPModCfg;
import net.cravencraft.epicparagliders.EpicParaglidersAttributes;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import yesman.epicfight.world.item.*;

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
        double staminaOverride = player.getAttributeValue(EpicParaglidersAttributes.WEAPON_STAMINA_CONSUMPTION.get());
        double weaponTypeOverride = player.getAttributeValue(EpicParaglidersAttributes.WEAPON_TYPE.get());

        if (weaponItem instanceof AxeItem || weaponTypeOverride == 1.0) {
            configFactor *= EPModCfg.axeStaminaConsumption();
        }
        else if (weaponItem instanceof DaggerItem || weaponTypeOverride == 3.0) {
            configFactor *= EPModCfg.daggerStaminaConsumption();
        }
        else if (weaponItem instanceof KnuckleItem || weaponTypeOverride == 4.0) {
            configFactor *= EPModCfg.knuckleStaminaConsumption();
        }
        else if (weaponItem instanceof GreatswordItem || weaponTypeOverride == 5.0) {
            configFactor *= EPModCfg.greatSwordStaminaConsumption();
        }
        else if (weaponItem instanceof UchigatanaItem || weaponTypeOverride == 6.0) {
            configFactor *= EPModCfg.katanaStaminaConsumption();
        }
        else if (weaponItem instanceof LongswordItem || weaponTypeOverride == 7.0) {
            configFactor *= EPModCfg.longSwordConsumption();
        }
        else if (weaponItem instanceof SpearItem || weaponTypeOverride == 8.0) {
            configFactor *= EPModCfg.spearStaminaConsumption();
        }
        else if (weaponItem instanceof SwordItem || weaponTypeOverride == 9.0) {
            configFactor *= EPModCfg.swordStaminaConsumption();
        }
        else if (weaponItem instanceof TachiItem || weaponTypeOverride == 10.0) {
            configFactor *= EPModCfg.tachiStaminaConsumption();
        }

        double totalStaminaDrain;
        // Checks if there is a datapack config set for the given weapon. If so, then
        // uses the datapack info instead of attack damage. Still implements the weapon type config options.
        if (staminaOverride > 0) {
            totalStaminaDrain = staminaOverride * configFactor;
        }
        else {
            totalStaminaDrain = attackDamageFactor * configFactor;
        }

        return (int) Math.ceil(totalStaminaDrain);
    }
}