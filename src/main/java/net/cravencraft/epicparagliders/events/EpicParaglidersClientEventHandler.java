package net.cravencraft.epicparagliders.events;

import com.mojang.blaze3d.platform.Window;
import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.UpdatedModCfg;
import net.cravencraft.epicparagliders.capabilities.UpdatedClientPlayerMovement;
import net.cravencraft.epicparagliders.client.InGameStaminaWheelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tictim.paraglider.ModCfg;
import tictim.paraglider.client.DisableStaminaRender;
import tictim.paraglider.client.StaminaWheelRenderer;

import static tictim.paraglider.client.StaminaWheelConstants.WHEEL_RADIUS;

@Mod.EventBusSubscriber(modid = EpicParaglidersMod.MOD_ID, value = Dist.CLIENT)
public final class EpicParaglidersClientEventHandler {

    private static final StaminaWheelRenderer STAMINA_WHEEL_RENDERER = new InGameStaminaWheelRenderer();

    private EpicParaglidersClientEventHandler() {}

    /**
     * This will Render the Paragliders stamina wheel based on the new 'UpdatedModCfg' properties.
     * Ensures that only the new system is used whenever the old system is disabled.
     *
     * @param event
     */
    @SubscribeEvent
    public static void afterGameOverlayRenderer(RenderGameOverlayEvent.Post event) {
        UpdatedClientPlayerMovement clientPlayerMovement = UpdatedClientPlayerMovement.instance;

        if (clientPlayerMovement != null) {
            if(Minecraft.getInstance().screen instanceof DisableStaminaRender ||
                event.getType()!=RenderGameOverlayEvent.ElementType.ALL ||
                !(UpdatedModCfg.paraglidingConsumesStamina()||UpdatedModCfg.runningConsumesStamina())) return;
            Window window = event.getWindow();

            int x = Mth.clamp((int)Math.round(ModCfg.staminaWheelX()*window.getGuiScaledWidth()), 1+WHEEL_RADIUS, window.getGuiScaledWidth()-2-WHEEL_RADIUS);
            int y = Mth.clamp((int)Math.round(ModCfg.staminaWheelY()*window.getGuiScaledHeight()), 1+WHEEL_RADIUS, window.getGuiScaledHeight()-2-WHEEL_RADIUS);

            STAMINA_WHEEL_RENDERER.renderStamina(event.getMatrixStack(), x, y, 25);
        }
    }
}
