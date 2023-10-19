package net.cravencraft.epicparagliders.mixins.epicfight.skills.passive;

import net.cravencraft.epicparagliders.EPModCfg;
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

    public StaminaPillagerSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "lambda$onInitiate$0", at = @At("HEAD"), remap = false)
    private void getPlayerPatch(DealtDamageEvent event, CallbackInfo ci) {
        if (!event.getTarget().isAlive()) {
            PlayerMovement playerMovement = PlayerMovement.of(event.getPlayerPatch().getOriginal());
            PlayerMovementInterface playerMovementInterface = ((PlayerMovementInterface) playerMovement);
            double staminaPillagerPercentModifier = EPModCfg.staminaPillagerPercentModifier() * 0.01;

            int trueTotalMissing = (int) (MathUtils.calculateTriangularNumber(playerMovementInterface.getTotalActionStaminaCost()) + (playerMovement.getMaxStamina() - playerMovement.getStamina()));

            playerMovementInterface.setActionStaminaCostServerSide(-(int) (MathUtils.calculateModifiedTriangularRoot(trueTotalMissing, staminaPillagerPercentModifier)));
            playerMovementInterface.performingActionServerSide(true);
        }
    }
}