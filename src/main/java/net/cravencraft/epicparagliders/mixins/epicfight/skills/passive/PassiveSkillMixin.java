package net.cravencraft.epicparagliders.mixins.epicfight.skills.passive;

import net.minecraft.nbt.ListTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.passive.PassiveSkill;

@Mixin(PassiveSkill.class)
public abstract class PassiveSkillMixin extends Skill {

    public PassiveSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    /**
     * Removes all attribute modifiers that add health or stamina. The Paragliders mod already
     * adds this, and this would make things more unbalanced.
     *
     * @param listTag
     * @return
     */
    @ModifyVariable(method = "setParams", at = @At("STORE"), ordinal = 0, remap = false)
    private ListTag removeStaminaAndHealthAttributes(ListTag listTag) {

        for (int i=0; i<listTag.size(); i++) {
            if (listTag.get(i).getAsString().contains("staminar") || listTag.get(i).getAsString().contains("max_health")) {
                listTag.remove(i);
            }
        }

        return listTag;
    }
}