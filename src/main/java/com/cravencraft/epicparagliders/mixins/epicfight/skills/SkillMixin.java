package com.cravencraft.epicparagliders.mixins.epicfight.skills;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import yesman.epicfight.skill.Skill;

import java.util.ArrayList;

@Mixin(Skill.class)
public abstract class SkillMixin {

    @Shadow protected float consumption;

    /**
     * Removes all attribute modifiers that add health or stamina. The Paragliders mod already
     * adds this, and this would make things more unbalanced.
     *
     * @param listTag
     * @return
     */
    @ModifyVariable(method = "setParams", at = @At("STORE"), ordinal = 0, remap = false)
    private ListTag removeStaminaAndHealthAttributes(ListTag listTag) {
        ArrayList<Tag> attributesToRemove = new ArrayList<>();
        for (int i=0; i<listTag.size(); i++) {
            if (listTag.get(i).getAsString().contains("staminar") || listTag.get(i).getAsString().contains("max_health")) {
                attributesToRemove.add(listTag.get(i));
            }
        }

        for (Tag attribute : attributesToRemove) {
            listTag.remove(attribute);
        }

        return listTag;
    }
}