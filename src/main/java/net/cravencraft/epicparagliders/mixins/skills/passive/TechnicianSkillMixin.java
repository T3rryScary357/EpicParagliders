package net.cravencraft.epicparagliders.mixins.skills.passive;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.cravencraft.epicparagliders.utils.MathUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.skill.passive.TechnicianSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.DodgeSuccessEvent;

@Mixin(TechnicianSkill.class)
public abstract class TechnicianSkillMixin extends PassiveSkill {
    private static final float STAMINA_PERCENTAGE_RETURNED = 1.2F;
    private static PlayerPatch PLAYER_PATCH;

    public TechnicianSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "lambda$onInitiate$0", at = @At("HEAD"), remap = false)
    private static void getPlayerPatch(SkillContainer container, DodgeSuccessEvent event, CallbackInfo ci) {
        PLAYER_PATCH = container.getExecuter();
    }

    /*
     * TODO: WORKS!!!!
     *  - Clean up code and loggers here and in ServerPlayerMovement.
     *  - Use this method in the stamina pillager class.
     *  - Honestly, this technically should work for ANY skill that gives you back stamina.
     *  - Make the STAMINA_PERCENTAGE_RETURNED a config option.
     */

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "lambda$onInitiate$0", at = @At(value = "STORE"), ordinal = 0, remap = false)
    private static float setTechnicianStaminaCost(float consumption) {
        PlayerMovement playerMovement = PlayerMovement.of(PLAYER_PATCH.getOriginal());
        PlayerMovementInterface playerMovementInterface = ((PlayerMovementInterface) playerMovement);

//        float stamina = playerMovement.getStamina();
//        float missingStamina = playerMovement.getMaxStamina() - stamina;
        float currentActionStamina = playerMovementInterface.getTotalActionStaminaCost();
        EpicParaglidersMod.LOGGER.info("CURRENT ACTION STAMINA: " + currentActionStamina);
//        EpicParaglidersMod.LOGGER.info("MISSING STAMINA PERCENTAGE TO RETURN: " + MathUtils.calculateModifiedTriangularRoot(missingStamina, STAMINA_PERCENTAGE_RETURNED));
        int staminaReturned = (int) -(currentActionStamina * STAMINA_PERCENTAGE_RETURNED);
//        staminaReturned = (int) (currentActionStamina - staminaReturned);
//        staminaReturned = (staminaReturned > currentActionStamina) ? (-staminaReturned) : staminaReturned;

//        int staminaReduction = (int) (currentStaminaCost * ROLL_STAMINA_COST_REDUCTION);
        EpicParaglidersMod.LOGGER.info("POST ACTION STAMINA: " + staminaReturned);
        ((PlayerMovementInterface) playerMovement).setActionStaminaCostServerSide(staminaReturned);
        ((PlayerMovementInterface) playerMovement).performingActionServerSide(true);
        return 0.0f;
    }
}
