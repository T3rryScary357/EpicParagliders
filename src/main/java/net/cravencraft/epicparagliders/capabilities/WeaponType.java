package net.cravencraft.epicparagliders.capabilities;

import net.cravencraft.epicparagliders.EpicParaglidersAttributes;
import net.cravencraft.epicparagliders.config.ConfigManager;
import net.cravencraft.epicparagliders.config.ServerConfig;
import net.minecraft.world.entity.player.Player;

public enum WeaponType {

    AXE,
    DAGGER,
    SWORD,
    LONGSWORD,
    GREATSWORD,
    SPEAR,
    KNUCKLE,
    TACHI,
    UCHIGATANA;

    public double getStaminaMultiplier() {
        ServerConfig serverConfig = ConfigManager.SERVER_CONFIG;

        switch (this) {
            case AXE:
                return serverConfig.axeStaminaMultiplier();
            case DAGGER:
                return serverConfig.daggerStaminaMultiplier();
            case SWORD:
                return serverConfig.swordStaminaMultiplier();
            case LONGSWORD:
                return serverConfig.longSwordMultiplier();
            case GREATSWORD:
                return serverConfig.greatSwordStaminaMultiplier();
            case SPEAR:
                return serverConfig.spearStaminaMultiplier();
            case KNUCKLE:
                return serverConfig.knuckleStaminaMultiplier();
            case TACHI:
                return serverConfig.tachiStaminaMultiplier();
            case UCHIGATANA:
                return serverConfig.uchigatanaStaminaMultiplier();
            default:
                return serverConfig.defaultMeleeStaminaMultiplier();
        }
    }

    public double getStaminaReduction(Player player) {

        switch (this) {
            case AXE:
                return player.getAttributeValue(EpicParaglidersAttributes.AXE_STAMINA_REDUCTION.get());
            case DAGGER:
                return player.getAttributeValue(EpicParaglidersAttributes.DAGGER_STAMINA_REDUCTION.get());
            case SWORD:
                return player.getAttributeValue(EpicParaglidersAttributes.SWORD_STAMINA_REDUCTION.get());
            case LONGSWORD:
                return player.getAttributeValue(EpicParaglidersAttributes.LONGSWORD_STAMINA_REDUCTION.get());
            case GREATSWORD:
                return player.getAttributeValue(EpicParaglidersAttributes.GREATSWORD_STAMINA_REDUCTION.get());
            case SPEAR:
                return player.getAttributeValue(EpicParaglidersAttributes.SPEAR_STAMINA_REDUCTION.get());
            case KNUCKLE:
                return player.getAttributeValue(EpicParaglidersAttributes.KNUCKLE_STAMINA_REDUCTION.get());
            case TACHI:
                return player.getAttributeValue(EpicParaglidersAttributes.TACHI_STAMINA_REDUCTION.get());
            case UCHIGATANA:
                return player.getAttributeValue(EpicParaglidersAttributes.UCHIGATANA_STAMINA_REDUCTION.get());
            default:
                return player.getAttributeValue(EpicParaglidersAttributes.DEFAULT_STAMINA_REDUCTION.get());
        }
    }
}