package net.cravencraft.epicparagliders.events;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tictim.paraglider.item.ParagliderItem;

@Mod.EventBusSubscriber(modid = EpicParaglidersMod.MOD_ID, value = Dist.CLIENT)
public class FixParagliderRender {
    // DawnCraft-Tweaks
    private static boolean isParagliding(LivingEntity entity) {
        ItemStack item = entity.getMainHandItem();

        if (item.isEmpty()) {
            return false;
        }

        return ParagliderItem.isItemParagliding(item);
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void renderLivingEventStart(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        if (isParagliding(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void renderLivingEventEnd(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        if (isParagliding(event.getEntity())) {
            event.setCanceled(false);
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void renderHandEventStart(RenderHandEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (isParagliding(mc.player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void renderHandEventEnd(RenderHandEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (isParagliding(mc.player)) {
            event.setCanceled(false);
        }
    }
}