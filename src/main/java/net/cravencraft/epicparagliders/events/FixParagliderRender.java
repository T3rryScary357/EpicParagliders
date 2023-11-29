package net.cravencraft.epicparagliders.events;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.utils.EpicParagliderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tictim.paraglider.contents.item.ParagliderItem;
import tictim.paraglider.forge.capability.PlayerMovementProvider;
import tictim.paraglider.impl.movement.PlayerMovement;

/**
 * author: Thundertheidiot
 *
 * Contains client events that will override Epic Fight's render engine regardless of whether
 * the 'filterAnimations' config option is set or not. Necessary to allow the Paragliders glide
 * animation to play properly.
 *
 * Big thanks to Thunder for taking the code used in the DawnCraft-Tweaks mod, and integrating it
 * with this mod.
 */
@Mod.EventBusSubscriber(modid = EpicParaglidersMod.MOD_ID, value = Dist.CLIENT)
public class FixParagliderRender {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void renderLivingEventStart(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        if (EpicParagliderUtils.isParagliding(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void renderLivingEventEnd(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        if (EpicParagliderUtils.isParagliding(event.getEntity())) {
            event.setCanceled(false);
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void renderHandEventStart(RenderHandEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (EpicParagliderUtils.isParagliding(mc.player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void renderHandEventEnd(RenderHandEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (EpicParagliderUtils.isParagliding(mc.player)) {
            event.setCanceled(false);
        }
    }
}