package net.cravencraft.epicparagliders.utils;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import tictim.paraglider.contents.item.ParagliderItem;

public class EpicParagliderUtils {

    public static boolean isParagliding(LivingEntity entity) {

        Item item = entity.getMainHandItem().getItem();
        if (item instanceof ParagliderItem paragliderItem) {
            return paragliderItem.isParagliding(item.getDefaultInstance());
        }
        else {
            return false;
        }
    }
}
