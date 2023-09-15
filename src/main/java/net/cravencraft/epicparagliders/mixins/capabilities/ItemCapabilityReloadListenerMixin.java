package net.cravencraft.epicparagliders.mixins.capabilities;

import net.cravencraft.epicparagliders.EpicParaglidersAttributes;
import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.data.reloader.ItemCapabilityReloadListener;
import yesman.epicfight.main.EpicFightMod;

import java.util.Map;
import java.util.UUID;

@Mixin(ItemCapabilityReloadListener.class)
public abstract class ItemCapabilityReloadListenerMixin {
    private static final UUID STAMINA_COST_MODIFIER = UUID.fromString("45945b75-f098-4127-8002-ef7a73afec69");
    private static Map<Attribute, AttributeModifier> modifierMap;

    @Inject(method = "deserializeAttributes", at = @At("TAIL"), cancellable = true, remap = false)
    private static void addStaminaAttributeModifier(CompoundTag tag, CallbackInfoReturnable<Map<Attribute, AttributeModifier>> cir) {
        if (tag.contains("stamina_cost")) {
            EpicParaglidersMod.LOGGER.info("DATAPACK MODIFIED STAMINA COST: " + tag.getDouble("stamina_cost"));
//            EpicParaglidersMod.LOGGER.info("STAMINA ATTRIBUTE PRIOR TO MODIFICATION: " + EpicParaglidersAttributes.WEAPON_STAMINA_CONSUMPTION.get().getDefaultValue());
            modifierMap.put(EpicParaglidersAttributes.WEAPON_STAMINA_CONSUMPTION.get(), new AttributeModifier(STAMINA_COST_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", tag.getDouble("stamina_cost"), AttributeModifier.Operation.ADDITION));
        }
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "deserializeAttributes", at = @At(value = "STORE"), ordinal = 0, remap = false)
    private static Map<Attribute, AttributeModifier> addStamAttributeModifier(Map<Attribute, AttributeModifier> modMap) {
        modifierMap = modMap;
        return modMap;
    }

}
