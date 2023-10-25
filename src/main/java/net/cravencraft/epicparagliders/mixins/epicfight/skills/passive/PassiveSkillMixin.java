package net.cravencraft.epicparagliders.mixins.epicfight.skills.passive;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.utils.ParseUtil;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.passive.PassiveSkill;

import java.util.Iterator;
import java.util.Map;

@Mixin(PassiveSkill.class)
public abstract class PassiveSkillMixin extends Skill {

    @Shadow @Final private Map<Attribute, AttributeModifier> passiveStats;

    public PassiveSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "setParams", at = @At("HEAD"), remap = false, cancellable = true)
    private void removeAttributes(CompoundTag parameters, CallbackInfo ci) {
        this.passiveStats.clear();
        if (parameters.contains("attribute_modifiers")) {
            ListTag attributeList = parameters.getList("attribute_modifiers", 10);
            Iterator var3 = attributeList.iterator();

            while(var3.hasNext()) {
                Tag tag = (Tag)var3.next();
                CompoundTag comp = (CompoundTag)tag;
                String attribute = comp.getString("attribute");
                Attribute attr = (Attribute) ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attribute));
                AttributeModifier modifier = ParseUtil.toAttributeModifier(comp);
                if (!attribute.contains("staminar") && !attribute.contains("max_health")) {
                    EpicParaglidersMod.LOGGER.info("NOT STAMINA OR HEALTH RELATED");
                    this.passiveStats.put(attr, modifier);
                }
                else {
                    EpicParaglidersMod.LOGGER.info("IS STAMINA/HEALTH RELATED: " + comp);
                }
            }
        }
        ci.cancel();
    }
}