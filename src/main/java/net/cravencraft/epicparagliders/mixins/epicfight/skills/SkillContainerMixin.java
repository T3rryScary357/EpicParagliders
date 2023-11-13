package net.cravencraft.epicparagliders.mixins.epicfight.skills;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.skill.ChargeableSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.SkillExecuteEvent;

@Mixin(SkillContainer.class)
public abstract class SkillContainerMixin {
    @Shadow public abstract boolean isActivated();

    @Shadow protected Skill containingSkill;

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
//            EpicParaglidersMod.LOGGER.info("SKILL: " + skill.getRegistryName());
//            EpicParaglidersMod.LOGGER.info("DEPLETED: " + playerMovement.isDepleted());
//            EpicParaglidersMod.LOGGER.info("ACTIVATED: " + (skill.getActivateType() == Skill.ActivateType.DURATION));
//            EpicParaglidersMod.LOGGER.info("TYPE == DURATION: " + this.isActivated());

            event.setResourcePredicate(!playerMovement.isDepleted() || (this.isActivated() && skill.getActivateType() == Skill.ActivateType.DURATION));
//            EpicParaglidersMod.LOGGER.info("IS CANCELED: " + event.isCanceled());
//            EpicParaglidersMod.LOGGER.info("IS EXECUTABLE: " + event.isExecutable());
            cir.setReturnValue(!event.isCanceled() && event.isExecutable());
        }
    }
}