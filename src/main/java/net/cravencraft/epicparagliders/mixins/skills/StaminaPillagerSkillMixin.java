package net.cravencraft.epicparagliders.mixins.skills;

import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.cravencraft.epicparagliders.utils.MathUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.skill.*;
import yesman.epicfight.world.entity.eventlistener.DealtDamageEvent;

@Mixin(StaminaPillagerSkill.class)
public abstract class StaminaPillagerSkillMixin extends PassiveSkill {
    private static final double STAMINA_PERCENTAGE_RETURNED = 0.3F;

    public StaminaPillagerSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "lambda$onInitiate$0", at = @At("HEAD"), remap = false)
    private static void getPlayerPatch(DealtDamageEvent event, CallbackInfo ci) {
        if (!event.getTarget().isAlive()) {
            PlayerMovement playerMovement = PlayerMovement.of(event.getPlayerPatch().getOriginal());
            PlayerMovementInterface serverPlayerMovement = ((PlayerMovementInterface) playerMovement);

            float stamina = playerMovement.getStamina();
            float missingStamina = playerMovement.getMaxStamina() - stamina;
            float staminaPillaged = (int) MathUtils.calculateModifiedTriangularRoot(missingStamina, STAMINA_PERCENTAGE_RETURNED);
            float currentActionStamina = serverPlayerMovement.getTotalActionStaminaCost();
            int totalStaminaPillaged;
            if (staminaPillaged > currentActionStamina) {
                totalStaminaPillaged = -(int) MathUtils.calculateTriangularRoot((MathUtils.calculateTriangularNumber((int) staminaPillaged))
                        + MathUtils.calculateTriangularNumber(serverPlayerMovement.getTotalActionStaminaCost()));
            }
            else {
                totalStaminaPillaged =  (int) MathUtils.calculateTriangularRoot((MathUtils.calculateTriangularNumber((int) staminaPillaged))
                        + MathUtils.calculateTriangularNumber(serverPlayerMovement.getTotalActionStaminaCost()));
            }
            ((PlayerMovementInterface) playerMovement).setTotalActionStaminaCostServerSide(totalStaminaPillaged);
        }
    }
}