package net.cravencraft.epicparagliders.mixins.epicfight.skills.passive;

import net.cravencraft.epicparagliders.EPModCfg;
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
    private static PlayerPatch PLAYER_PATCH;

    public TechnicianSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "lambda$onInitiate$0", at = @At("HEAD"), remap = false)
    private static void getPlayerPatch(SkillContainer container, DodgeSuccessEvent event, CallbackInfo ci) {
        PLAYER_PATCH = container.getExecuter();
    }

    /*
     * TODO:
     *  - Clean up code and loggers here and in ServerPlayerMovement.
     *  - Use this method in the stamina pillager class.
     *  - Make the STAMINA_PERCENTAGE_RETURNED a config option.
     */

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "lambda$onInitiate$0", at = @At(value = "STORE"), ordinal = 0, remap = false)
    private static float setTechnicianStaminaCost(float consumption) {
        PlayerMovement playerMovement = PlayerMovement.of(PLAYER_PATCH.getOriginal());
        PlayerMovementInterface playerMovementInterface = ((PlayerMovementInterface) playerMovement);

        int technicianConsumption = playerMovementInterface.getTotalActionStaminaCost();
        double technicianPercentModifier = EPModCfg.technicianPercentModifier() * 0.01;

        // If the player is successful with the parry, use one of these formulas depending on if parrying is set to drain stamina in the config.
        if (EPModCfg.technicianDrain()) {
            technicianConsumption *= (1 - technicianPercentModifier);
        }
        else {
            int trueTotalMissing = (int) (MathUtils.calculateTriangularNumber(playerMovementInterface.getTotalActionStaminaCost()) + (playerMovement.getMaxStamina() - playerMovement.getStamina()));
            technicianConsumption = -(int) (MathUtils.calculateModifiedTriangularRoot(trueTotalMissing, technicianPercentModifier));
        }

        playerMovementInterface.setActionStaminaCostServerSide(technicianConsumption);
        playerMovementInterface.performingActionServerSide(true);
        return 0.0f;
    }
}
