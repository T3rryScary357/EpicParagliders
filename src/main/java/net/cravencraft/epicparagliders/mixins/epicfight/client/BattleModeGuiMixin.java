package net.cravencraft.epicparagliders.mixins.epicfight.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.cravencraft.epicparagliders.EPModCfg;
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
    float ratio;

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
        if (!EPModCfg.useStaminaWheel()) {
            return maxStamina;
        }
        else {
            return 0.0F;
        }

    }

    /**
     * Modifies the Epic Fight stamina bar. It will now turn red and the opacity will decrease whenever
     * the player's stamina is depleted.
     *
     * @param red
     * @param blue
     * @param green
     * @param opacity
     */
    @Redirect(method = "renderGui", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 0), remap = false)
    private void modifyStaminaBar(float red, float blue, float green, float opacity) {
        ClientPlayerMovement playerMovement = (ClientPlayerMovement) PlayerMovement.of(Minecraft.getInstance().player);

        if (playerMovement.isDepleted()) {
            RenderSystem.setShaderColor(1.0F, 0.0F, 0.0F, 0.5F);
        }
        else {
            RenderSystem.setShaderColor(1.0F, this.ratio, 0.25F, 1.0F);
        }

    }
}