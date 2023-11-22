package net.cravencraft.epicparagliders.utils;

import net.cravencraft.epicparagliders.capabilities.WeaponType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import yesman.epicfight.api.data.reloader.ItemCapabilityReloadListener;

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

    /**
     * Some math done here to determine how much stamina should be consumed from each weapon type.
     * Takes in a weapon's attack strength and tier, and the world's config setting to create a balanced stamina cost.
     *
     * @param player The given player attacking
     * @return The amount of stamina that should be drained from the attacking weapon
     */
    public static int getAttackStaminaCost(Player player) {
        //TODO: Double check duel wielding with this.
        //      Could easily add offhand support too by checking which
        //      hand is swinging the weapon.
        CompoundTag weaponTag = ItemCapabilityReloadListener.getWeaponDataStream()
                .filter(compoundTag -> compoundTag.getInt("id") == Item.getId(player.getMainHandItem().getItem()))
                .findFirst().get();

        int weaponStaminaCostOverride = weaponTag
                .getCompound("attributes")
                .getCompound("common")
                .getInt("stamina_cost");

        WeaponType weaponType = WeaponType.valueOf(weaponTag.get("type").getAsString().toUpperCase());

        double totalStaminaDrain = (weaponStaminaCostOverride > 0) ? weaponStaminaCostOverride : player.getAttributeValue(Attributes.ATTACK_DAMAGE);

        totalStaminaDrain *= weaponType.getStaminaMultiplier() * weaponType.getStaminaReduction(player);

        return (int) Math.round(totalStaminaDrain);
    }
}