package com.cravencraft.epicparagliders.mixins.epicfight.skills.dodge;

import com.cravencraft.epicparagliders.EpicParaglidersMod;
import com.cravencraft.epicparagliders.capabilities.StaminaOverride;
import com.cravencraft.epicparagliders.config.ConfigManager;
import com.cravencraft.epicparagliders.utils.MathUtils;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.forge.capability.PlayerMovementProvider;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.dodge.DodgeSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(DodgeSkill.class)
public abstract class DodgeSkillMixin extends Skill {

    public DodgeSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    /**
     * Modifies the base stamina consumed when performing a Step or Roll
     * dodge skill.
     *
     * @param executer
     * @param args
     * @param ci
     */
    @Inject(method = "executeOnServer", at = @At("HEAD"), remap = false, cancellable = true)
    private void getPlayerPatch(ServerPlayerPatch executer, FriendlyByteBuf args, CallbackInfo ci) {
        String skillName = this.registryName.getPath();
        if (skillName.equals("step")) {
            this.consumption = ConfigManager.SERVER_CONFIG.baseStepStaminaCost();
        }
        else if (skillName.equals("roll")) {
            this.consumption = ConfigManager.SERVER_CONFIG.baseRollStaminaCost();
        }

        StaminaOverride botwStamina = ((StaminaOverride) PlayerMovementProvider.of(executer.getOriginal()).stamina());
        botwStamina.setActionStaminaCost((int) this.consumption);
        ((StaminaOverride) PlayerMovementProvider.of(executer.getOriginal()).stamina()).performingAction(true);
    }
}