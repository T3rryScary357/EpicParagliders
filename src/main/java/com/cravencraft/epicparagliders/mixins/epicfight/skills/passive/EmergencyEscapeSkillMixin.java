package com.cravencraft.epicparagliders.mixins.epicfight.skills.passive;

import com.cravencraft.epicparagliders.EpicParaglidersMod;
import com.cravencraft.epicparagliders.capabilities.StaminaOverride;
import com.cravencraft.epicparagliders.utils.MathUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.forge.capability.PlayerMovementProvider;
import tictim.paraglider.impl.movement.PlayerMovement;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.passive.EmergencyEscapeSkill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.SkillConsumeEvent;
import yesman.epicfight.world.entity.eventlistener.SkillExecuteEvent;

@Mixin(EmergencyEscapeSkill.class)
public abstract class EmergencyEscapeSkillMixin extends PassiveSkill {

    public EmergencyEscapeSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lyesman/epicfight/world/entity/eventlistener/SkillExecuteEvent;setStateExecutable(Z)V"), remap = false, method = "lambda$onInitiate$0")
    private void cancelPlayerAttack(SkillExecuteEvent instance, boolean stateExecutable) {
        StaminaOverride botwStamina = ((StaminaOverride) PlayerMovementProvider.of(instance.getPlayerPatch().getOriginal()).stamina());

        if (instance.getPlayerPatch() instanceof ServerPlayerPatch) {
            if (botwStamina.isAttacking()) {
                botwStamina.attacking(false);
            }
        }

        instance.setStateExecutable(stateExecutable);
    }

    /**
     * Modifies the Emergency Escape skill by integrating with the Paragliders stamina system,
     * and checking if the actual stamina consumption will be more than the remaining stamina.
     * Pretty much exactly how it works originally, but now it uses the new system.
     *
     * @param container
     * @param event
     * @param ci
     */
    @Inject(method = "lambda$onInitiate$1", at = @At("HEAD"), remap = false)
    private void getPlayerPatch(SkillContainer container, SkillConsumeEvent event, CallbackInfo ci) {
        if (event.getSkill().getCategory() == SkillCategories.DODGE) {
            PlayerMovement playerMovement = PlayerMovementProvider.of(container.getExecuter().getOriginal());
            double actualConsumption =  MathUtils.calculateTriangularNumber((int) event.getSkill().getConsumption());

            if (!container.getExecuter().getOriginal().isCreative() && playerMovement.stamina().isDepleted() && container.getStack() > 0) {
                if (!container.getExecuter().isLogicalClient()) {
                    EpicParaglidersMod.LOGGER.info("PERFORMING EMERGENCY ESCAPE DODGE.");
                    ((StaminaOverride) playerMovement.stamina()).performingAction(true);
                    ((StaminaOverride) playerMovement.stamina()).setActionStaminaCost((int) actualConsumption);
                    this.setStackSynchronize((ServerPlayerPatch)container.getExecuter(), container.getStack() - 1);
                }

                event.setResourceType(Skill.Resource.NONE);
            }
        }
    }
}