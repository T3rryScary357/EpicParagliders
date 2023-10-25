package net.cravencraft.epicparagliders.mixins.epicfight.skills.passive;

import net.cravencraft.epicparagliders.EPModCfg;
import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.cravencraft.epicparagliders.utils.MathUtils;
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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.api.utils.ParseUtil;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.skill.passive.StaminaPillagerSkill;
import yesman.epicfight.world.entity.eventlistener.DealtDamageEvent;

import java.util.Iterator;
import java.util.Map;

@Mixin(PassiveSkill.class)
public abstract class PassiveSkillMixin extends Skill {

    @Shadow @Final private Map<Attribute, AttributeModifier> passiveStats;

    public PassiveSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

//    @ModifyVariable(method = "setParams", at = @At("STORE"), ordinal = 0, remap = false)
//    private ListTag removeStaminaAndHealthAttributeModifier(ListTag value) {
//
//        for (Tag tag : value) {
//            CompoundTag compTag = (CompoundTag)tag;
//            String attribute = compTag.getString("attribute");
//
//            if (attribute.contains("staminar") || attribute.contains("max_health")) {
//                EpicParaglidersMod.LOGGER.info("PASSIVE SKILL COMP TAG: " + compTag);
//                EpicParaglidersMod.LOGGER.info("ATTRIBUTE: " + attribute);
//                value.remove(tag);
//                EpicParaglidersMod.LOGGER.info("HOPEFULLY TAG REMOVED WITHOUT ISSUES");
//            }
//        }
//        return value;
//    }

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

//    public PassiveSkillMixin(Builder<? extends Skill> builder) {
//        super(builder);
//    }

//    @Inject(method = "lambda$onInitiate$0", at = @At("HEAD"), remap = false)
//    private void getPlayerPatch(DealtDamageEvent event, CallbackInfo ci) {
//        if (!event.getTarget().isAlive()) {
//            PlayerMovement playerMovement = PlayerMovement.of(event.getPlayerPatch().getOriginal());
//            PlayerMovementInterface playerMovementInterface = ((PlayerMovementInterface) playerMovement);
//            double staminaPillagerPercentModifier = EPModCfg.staminaPillagerPercentModifier() * 0.01;
//
//            int trueTotalMissing = (int) (MathUtils.calculateTriangularNumber(playerMovementInterface.getTotalActionStaminaCost()) + (playerMovement.getMaxStamina() - playerMovement.getStamina()));
//
//            playerMovementInterface.setActionStaminaCostServerSide(-(int) (MathUtils.calculateModifiedTriangularRoot(trueTotalMissing, staminaPillagerPercentModifier)));
//            playerMovementInterface.performingActionServerSide(true);
//        }
//    }
}