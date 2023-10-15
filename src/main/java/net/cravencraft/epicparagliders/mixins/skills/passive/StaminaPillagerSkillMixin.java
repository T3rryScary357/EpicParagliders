package net.cravencraft.epicparagliders.mixins.skills.passive;

import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.cravencraft.epicparagliders.utils.MathUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.skill.passive.StaminaPillagerSkill;
import yesman.epicfight.world.entity.eventlistener.DealtDamageEvent;

@Mixin(StaminaPillagerSkill.class)
public abstract class StaminaPillagerSkillMixin extends PassiveSkill {
    private static final double STAMINA_PERCENTAGE_RETURNED = 0.3F;

    public StaminaPillagerSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

//    @Inject(method = "lambda$onInitiate$0", at = @At("HEAD"), remap = false)
//    private static void getPlayerPatch(DealtDamageEvent event, CallbackInfo ci) {
//        if (!event.getTarget().isAlive()) {
//            PlayerMovement playerMovement = PlayerMovement.of(event.getPlayerPatch().getOriginal());
//            PlayerMovementInterface playerMovementInterface = ((PlayerMovementInterface) playerMovement);
//
//            playerMovementInterface.setActionStaminaCostServerSide((int) -(playerMovementInterface.getTotalActionStaminaCost() * STAMINA_PERCENTAGE_RETURNED));
//            playerMovementInterface.performingActionServerSide(true);
//
////            float stamina = playerMovement.getStamina();
////            float missingStamina = playerMovement.getMaxStamina() - stamina;
////            float currentActionStamina = playerMovementInterface.getTotalActionStaminaCost();
//            int staminaPillaged = (int) MathUtils.calculateModifiedTriangularRoot(playerMovement.getMaxStamina() - playerMovement.getStamina(), STAMINA_PERCENTAGE_RETURNED);
//
////            staminaPillaged = (staminaPillaged > currentActionStamina) ? (-staminaPillaged) : staminaPillaged;
//
//            ((PlayerMovementInterface) playerMovement).setActionStaminaCostServerSide(staminaPillaged);
//            ((PlayerMovementInterface) playerMovement).performingActionServerSide(true);
//        }
//    }
}