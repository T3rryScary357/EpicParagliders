package net.cravencraft.epicparagliders.mixins.epicfight.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import tictim.paraglider.capabilities.ClientPlayerMovement;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.client.gui.BattleModeGui;

@Mixin(BattleModeGui.class)
public abstract class BattleModeGuiMixin extends GuiComponent {
    private boolean decreaseOpacity;
    private float modifiedOpacity;
    private float ratio;


    /**
     * Simply retrieves the blue color change ratio (based on the player's current stamina).
     * Uses this value to modify the GUI in the method below.
     *
     * @param ratio
     * @return
     */
    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "renderGui", at = @At("STORE"), name = "ratio", remap = false)
    private float getRatio(float ratio) {
        this.ratio = ratio;
        return ratio;
    }

    /**
     *  Will not render the Epic Fight stamina bar unless the server config is set to do so.
     *
     * @param maxStamina
     * @return
     */
    @ModifyVariable(method = "renderGui", at = @At("STORE"), ordinal = 1, remap = false)
    private float removeGui(float maxStamina) {
        if (!ConfigManager.CLIENT_CONFIG.useStaminaWheel()) {
            return maxStamina;
        }
        else {
            return 0.0F;
        }

    }

    /**
     * TODO: Some refmap issue when loading this method outside of the test environment.
     *       Need to figure out what the root cause is.
     * Modifies the Epic Fight stamina bar. It will now turn red and the opacity will decrease whenever
     * the player's stamina is depleted.
     *
     * @param red
     * @param blue
     * @param green
     * @param opacity
     */
    @Redirect(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 0),
            method = "renderGui")
    private void modifyStaminaBar(float red, float blue, float green, float opacity) {
        ClientPlayerMovement playerMovement = (ClientPlayerMovement) PlayerMovement.of(Minecraft.getInstance().player);

        if (playerMovement.isDepleted()) {
            if (decreaseOpacity) {
                modifiedOpacity -= 0.05F;
            }
            else {
                modifiedOpacity += 0.05f;
            }

            if (modifiedOpacity >= 1.0F) {
                decreaseOpacity = true;
            }
            else if(modifiedOpacity <= 0.0F) {
                decreaseOpacity = false;
            }

            RenderSystem.setShaderColor(red, 0.0F, 0.0F, modifiedOpacity);
        }
        else {
            modifiedOpacity = opacity;
            RenderSystem.setShaderColor(red, this.ratio, 0.25F, opacity);
        }
    }
}