package net.cravencraft.epicparagliders.mixins.epicfight.capabilities;

import com.google.gson.Gson;
import net.cravencraft.epicparagliders.EpicParaglidersAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.data.reloader.ItemCapabilityReloadListener;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.Map;
import java.util.UUID;

@Mixin(ItemCapabilityReloadListener.class)
public abstract class ItemCapabilityReloadListenerMixin extends SimpleJsonResourceReloadListener {
    private static final UUID WEAPON_TYPE_MODIFIER = UUID.fromString("3b7aadbb-8a96-46b9-be4a-35b7d1626a8a");
    private static final UUID STAMINA_COST_MODIFIER = UUID.fromString("45945b75-f098-4127-8002-ef7a73afec69");
    private static Map<Attribute, AttributeModifier> modifierMap;
    private static AttributeModifier attributeModifier;

    public ItemCapabilityReloadListenerMixin(Gson p_10768_, String p_10769_) {
        super(p_10768_, p_10769_);
    }

    @Inject(method = "deserializeWeapon", at = @At("HEAD"), cancellable = true, remap = false)
    private static void addWeaponTypeAttributeModifier(Item item, CompoundTag tag, CapabilityItem.Builder defaultCapability, CallbackInfoReturnable<CapabilityItem> cir) {
        if (tag.contains("type")) {
            switch (tag.getString("type")) {
                case "axe":
                    attributeModifier = new AttributeModifier(WEAPON_TYPE_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", 1.0, AttributeModifier.Operation.ADDITION);
                    break;
                case "bow":
                case "crossbow":
                    attributeModifier = new AttributeModifier(WEAPON_TYPE_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", 2.0, AttributeModifier.Operation.ADDITION);
                    break;
                case "dagger":
                    attributeModifier = new AttributeModifier(WEAPON_TYPE_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", 3.0, AttributeModifier.Operation.ADDITION);
                    break;
                case "fist":
                    attributeModifier = new AttributeModifier(WEAPON_TYPE_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", 4.0, AttributeModifier.Operation.ADDITION);
                    break;
                case "greatsword":
                    attributeModifier = new AttributeModifier(WEAPON_TYPE_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", 5.0, AttributeModifier.Operation.ADDITION);
                    break;
                case "katana":
                    attributeModifier = new AttributeModifier(WEAPON_TYPE_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", 6.0, AttributeModifier.Operation.ADDITION);
                    break;
                case "longsword":
                    attributeModifier = new AttributeModifier(WEAPON_TYPE_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", 7.0, AttributeModifier.Operation.ADDITION);
                    break;
                case "spear":
                case "trident":
                    attributeModifier = new AttributeModifier(WEAPON_TYPE_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", 8.0, AttributeModifier.Operation.ADDITION);
                    break;
                case "sword":
                    attributeModifier = new AttributeModifier(WEAPON_TYPE_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", 9.0, AttributeModifier.Operation.ADDITION);
                    break;
                case "tachi":
                    attributeModifier = new AttributeModifier(WEAPON_TYPE_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", 10.0, AttributeModifier.Operation.ADDITION);
                    break;
                default:
                    attributeModifier = new AttributeModifier(WEAPON_TYPE_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", 0.0, AttributeModifier.Operation.ADDITION);
            }
//            modifierMap.put(EpicParaglidersAttributes.WEAPON_TYPE.get(), attributeModifier);
        }
    }

    @Inject(method = "deserializeAttributes", at = @At("TAIL"), cancellable = true, remap = false)
    private static void addStaminaAttributeModifier(CompoundTag tag, CallbackInfoReturnable<Map<Attribute, AttributeModifier>> cir) {
        if (tag.contains("stamina_cost")) {
            modifierMap.put(EpicParaglidersAttributes.WEAPON_STAMINA_CONSUMPTION.get(), new AttributeModifier(STAMINA_COST_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", tag.getDouble("stamina_cost"), AttributeModifier.Operation.ADDITION));
        }
        //TODO: Test this with a modified weapon that doesn't have a type.
        modifierMap.put(EpicParaglidersAttributes.WEAPON_TYPE.get(), attributeModifier);
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "deserializeAttributes", at = @At(value = "STORE"), ordinal = 0, remap = false)
    private static Map<Attribute, AttributeModifier> addStamAttributeModifier(Map<Attribute, AttributeModifier> modMap) {
        modifierMap = modMap;
        return modMap;
    }

}
