package net.cravencraft.epicparagliders.mixins.epicfight.skills;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.SkillExecuteEvent;

@Mixin(SkillContainer.class)
public abstract class SkillContainerMixin {
    @Shadow(remap = false) public abstract boolean isActivated();

    /**
     * Modifies the canExecute method to return false if the player is trying to use a skill that requires
     * stamina and the player's current stamina is depleted.
     *
     * @param executer
     * @param event
     * @param cir
     */
    @Inject(method = "canExecute", at = @At("TAIL"), remap = false, cancellable = true)
    private void modifyExecution(PlayerPatch<?> executer, SkillExecuteEvent event, CallbackInfoReturnable<Boolean> cir) {
        Skill skill = event.getSkillContainer().getSkill();
        PlayerMovement playerMovement = PlayerMovement.of(executer.getOriginal());

        if (skill != null && skill.getResourceType().equals(Skill.Resource.STAMINA)) {
            event.setResourcePredicate(!playerMovement.isDepleted() || (this.isActivated() && skill.getActivateType() == Skill.ActivateType.DURATION));
            cir.setReturnValue(!event.isCanceled() && event.isExecutable());
        }
    }
}