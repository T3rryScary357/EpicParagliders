package net.cravencraft.epicparagliders.utils;

import net.cravencraft.epicparagliders.EPModCfg;
import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.Style;
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
        double tierFactor = 0;
        double attackDamageFactor = 1; // Default hand damage is 1
        double configFactor = EPModCfg.baseMeleeStaminaConsumption();
        //TODO: Double check duel wielding with this.
        //      Could easily add offhand support too by checking which
        //      hand is swinging the weapon.
        Item weaponItem = player.getMainHandItem().getItem();

        if (weaponItem instanceof SwordItem swordItem) {
            if (swordItem instanceof GreatswordItem greatswordItem) {
                attackDamageFactor += 10;
                tierFactor = greatswordItem.getTier().getAttackDamageBonus();
                configFactor *= EPModCfg.greatSwordStaminaConsumption();
            }
            else if (swordItem instanceof LongswordItem longswordItem) {
                attackDamageFactor += longswordItem.getDamage();
                tierFactor = longswordItem.getTier().getAttackDamageBonus();
                configFactor *= EPModCfg.longSwordConsumption();
            }
            else if (swordItem instanceof TachiItem tachiItem) {
                attackDamageFactor += tachiItem.getDamage();
                tierFactor = tachiItem.getTier().getAttackDamageBonus();
                configFactor *= EPModCfg.tachiStaminaConsumption();
            }
            else if (swordItem instanceof KatanaItem katanaItem) {
                attackDamageFactor += katanaItem.getDamage();
                tierFactor = katanaItem.getTier().getAttackDamageBonus();
                configFactor *= EPModCfg.katanaStaminaConsumption();
            }
            else if (swordItem instanceof SpearItem spearItem) {
                attackDamageFactor += spearItem.getDamage();
                tierFactor = spearItem.getTier().getAttackDamageBonus();
                configFactor *= EPModCfg.spearStaminaConsumption();
            }
            else if (swordItem instanceof DaggerItem daggerItem) {
                attackDamageFactor += daggerItem.getDamage();
                tierFactor = daggerItem.getTier().getAttackDamageBonus();
                configFactor *= EPModCfg.daggerStaminaConsumption();
            }
            else if (swordItem instanceof KnuckleItem knuckleItem) {
                attackDamageFactor += knuckleItem.getDamage();
                tierFactor = knuckleItem.getTier().getAttackDamageBonus();
                configFactor *= EPModCfg.knuckleStaminaConsumption();
            }
            else {
                attackDamageFactor += swordItem.getDamage();
                tierFactor = swordItem.getTier().getAttackDamageBonus();
                configFactor *= EPModCfg.swordStaminaConsumption();
            }
        }
        else if (weaponItem instanceof AxeItem axeItem) {
            attackDamageFactor += axeItem.getAttackDamage();
            tierFactor = axeItem.getTier().getAttackDamageBonus();
            configFactor *= EPModCfg.axeStaminaConsumption();
        }

        double totalStaminaDrain = (attackDamageFactor * (tierFactor * 0.1 + 2)) * configFactor;
        return (int) Math.ceil(totalStaminaDrain);
    }
}
