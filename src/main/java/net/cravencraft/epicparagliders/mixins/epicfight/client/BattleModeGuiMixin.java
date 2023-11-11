package net.cravencraft.epicparagliders.mixins.epicfight.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.ModCfg;
import tictim.paraglider.capabilities.ClientPlayerMovement;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.api.utils.math.Vec2i;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.gui.BattleModeGui;
import yesman.epicfight.client.gui.EntityIndicator;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.config.ConfigurationIngame;

@Mixin(BattleModeGui.class)
public abstract class BattleModeGuiMixin extends GuiComponent {
    @Shadow(remap = false) private int sliding;
    @Shadow(remap = false) private boolean slidingToggle;
    @Shadow(remap = false) @Final private ConfigurationIngame config;

    private int maxPossibleStamina = ModCfg.maxStamina();
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
     * Displays ONLY the stamina bar even if the player is not in Battle Mode (only the config is set to display
     * via the InGameStaminaWheelRendererMixin class). Is needed because the player can potentially drain
     * stamina from actions like running and swimming now.
     *
     * @param poseStack
     * @param playerPatch
     * @param partialTicks
     * @param ci
     */
    @Inject(at = @At("HEAD"), method = "renderGui", remap = false, cancellable = true)
    private void testMethod(PoseStack poseStack, LocalPlayerPatch playerPatch, float partialTicks, CallbackInfo ci) {
        if (!playerPatch.isBattleMode()) {
            if (!playerPatch.getOriginal().isAlive() || playerPatch.getOriginal().getVehicle() != null) {
                return;
            }

            if (this.sliding > 28) {
                return;
            } else if (this.sliding > 0) {
                if (this.slidingToggle) {
                    this.sliding -= 2;
                } else {
                    this.sliding += 2;
                }
            }

            Window sr = Minecraft.getInstance().getWindow();
            int width = sr.getGuiScaledWidth();
            int height = sr.getGuiScaledHeight();

            boolean depthTestEnabled = GL11.glGetBoolean(GL11.GL_DEPTH_TEST);
            boolean blendEnabled = GL11.glGetBoolean(GL11.GL_BLEND);

            if (depthTestEnabled) {
                RenderSystem.disableDepthTest();
            }

            if (!blendEnabled) {
                RenderSystem.enableBlend();
            }

            poseStack.pushPose();
            poseStack.setIdentity();

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, EntityIndicator.BATTLE_ICON);

            float maxStamina = playerPatch.getMaxStamina();
            float stamina = playerPatch.getStamina();

            if (maxStamina > 0.0F && stamina < maxStamina) {
                Vec2i pos = this.config.getStaminaPosition(width, height);
                float prevStamina = playerPatch.getPrevStamina();
                float ratio = (prevStamina + (stamina - prevStamina) * partialTicks) / maxStamina;

                poseStack.pushPose();
                poseStack.translate(0, this.sliding, 0);
                this.setModifiedOpacity(1.0F, ratio, 0.25F, 1.0F);
                GuiComponent.blit(poseStack, pos.x, pos.y, this.getDisplayStamina(118), 4, 2, 38, 237, 9, 255, 255);
                GuiComponent.blit(poseStack, pos.x, pos.y, this.getDisplayStamina((int) (118*ratio)), 4, 2, 47, (int)(237*ratio), 9, 255, 255);
                poseStack.popPose();
            }
            ci.cancel();
        }
    }

    /**
     * Adjusts the stamina bar's length to be relative to the max stamina a user has vs. the max
     * stamina possible to achieve.
     */
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiComponent;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIFFIIII)V", ordinal = 0), method = "renderGui")
    private void modifyStaminaBarSize(PoseStack p_93161_, int p_93162_, int p_93163_, int p_93164_, int p_93165_, float p_93166_, float p_93167_, int p_93168_, int p_93169_, int p_93170_, int p_93171_) {
        GuiComponent.blit(p_93161_, p_93162_, p_93163_, this.getDisplayStamina(p_93164_), p_93165_, p_93166_, p_93167_, p_93168_, p_93169_, p_93170_, p_93171_);
    }

    /**
     * Adjusts the stamina bar's dynamic length to be relative to the max stamina a user has vs. the max
     * stamina possible to achieve.
     */
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiComponent;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIFFIIII)V", ordinal = 1), method = "renderGui")
    private void modifyDynamicStaminaBar(PoseStack p_93161_, int p_93162_, int p_93163_, int p_93164_, int p_93165_, float p_93166_, float p_93167_, int p_93168_, int p_93169_, int p_93170_, int p_93171_) {
        GuiComponent.blit(p_93161_, p_93162_, p_93163_, this.getDisplayStamina(p_93164_), p_93165_, p_93166_, p_93167_, p_93168_, p_93169_, p_93170_, p_93171_);
    }

    /**
     * TODO: THIS WORKS! I'd like to create one extra config for an Elden Ring-like stamina system.
     *       Stamina only drains while running/swimming if you are in battle mode. Makes traversal
     *       less tedious, but still keeps combat intense. Sure, it can be abused by just simply turning
     *       battle mode off and running away a bit, but I can mitigate that with a counter that takes
     *       at least 3-5 seconds to stop consuming stamina after switching. Should discourage abusing that.
     *
     *
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
        this.setModifiedOpacity(red, this.ratio, green, opacity);
    }

    private int getDisplayStamina(int originalStamina) {
        return (int) (originalStamina / (this.maxPossibleStamina / ClientEngine.getInstance().getPlayerPatch().getMaxStamina()));
    }

    /**
     * Helper method to display the stamina bar as blinking red if the player is out of stamina.
     *
     * @param red
     * @param blue
     * @param green
     * @param opacity
     */
    private void setModifiedOpacity(float red, float blue, float green, float opacity) {
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
            RenderSystem.setShaderColor(red, blue, 0.25F, opacity);
        }
    }
}