package net.cravencraft.epicparagliders;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EpicParaglidersAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, EpicParaglidersMod.MOD_ID);

    /**
     * TODO: Attributes for stats
     */
    public static final RegistryObject<Attribute> DAGGER_STAMINA_REDUCTION = ATTRIBUTES.register("dagger_stamina_reduction", () -> new RangedAttribute("dagger_stamina_reduction", 1.0, 0.0, 10.0));

    /**
     * Attributes needed for datapack editing
     */
    public static final RegistryObject<Attribute> WEAPON_STAMINA_CONSUMPTION = ATTRIBUTES.register("weapon_stamina_consumption", () -> new RangedAttribute("weapon_stamina_consumption", 0.0, 0.0, 1000.0).setSyncable(true));
    public static final RegistryObject<Attribute> WEAPON_TYPE = ATTRIBUTES.register("weapon_type", () -> new RangedAttribute("weapon_type", 0.0, 0.0, 1000.0).setSyncable(true));

    public static void registerEventHandlers(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}