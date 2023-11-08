package net.cravencraft.epicparagliders.mixins.epicfight.client;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import yesman.epicfight.client.gui.BattleModeGui;

@Mixin(BattleModeGui.class)
public abstract class BattleModeGuiMixin {

    /**
     * TODO: Can create a config here to determine if the player
     *      wants to use the Paragliders stamina wheel or the Epic Fight stamina bar.
     *
     * @param maxStamina
     * @return
     */
    @ModifyVariable(method = "renderGui", at = @At("STORE"), ordinal = 1, remap = false)
    private float removeStaminaBar(float maxStamina) {
//        EpicParaglidersMod.LOGGER.info("STAM BAR VALUE: " + maxStamina);
        return maxStamina;
    }
}
