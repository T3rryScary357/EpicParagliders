package net.cravencraft.epicparagliders.mixins.skills;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.cravencraft.epicparagliders.utils.MathUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.api.utils.math.Formulars;
import yesman.epicfight.skill.ActiveGuardSkill;
import yesman.epicfight.skill.GuardSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;

@Mixin(ActiveGuardSkill.class)
public abstract class ActiveGuardSkillMixin extends Skill {

    private float penalty;
    private float impact;
    private PlayerPatch playerPatch;

    public ActiveGuardSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "guard", at = @At("HEAD"), remap = false)
    private void getPlayerPatch(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
//        EpicParaglidersMod.LOGGER.info("INSIDE ACTIVE GUARD");
        this.playerPatch = event.getPlayerPatch();
    }

//    @ModifyVariable(method = "guard", at = @At(value = "STORE"), ordinal = 0, remap = false)
//    private float impact(float impact) {
////        EpicParaglidersMod.LOGGER.info("INSIDE ACTIVE GUARD MIXIN ");
//        EpicParaglidersMod.LOGGER.info("IMPACT: " + impact);
//        this.impact = impact;
//        return impact;
//    }
//
//    @ModifyVariable(method = "guard", at = @At(value = "STORE"), ordinal = 2, remap = false)
//    private float penalty(float penalty) {
////        EpicParaglidersMod.LOGGER.info("INSIDE ACTIVE GUARD MIXIN ");
//        EpicParaglidersMod.LOGGER.info("PENALTY: " + penalty);
//        this.penalty = penalty;
//        return penalty;
//    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "guard", at = @At(value = "STORE"), ordinal = 3, remap = false)
    private float stamina(float stamina) {
        EpicParaglidersMod.LOGGER.info("INSIDE ACTIVE GUARD MIXIN " + stamina);
        PlayerMovement playerMovement = PlayerMovement.of(playerPatch.getOriginal());
        float poise = Formulars.getStaminarConsumePenalty(this.playerPatch.getWeight(), 1, this.playerPatch) * 0.1F;
//        int totalPenalty = (int) (penalty * 5);
//        int totalImpact = (int) (impact * 50);
//        EpicParaglidersMod.LOGGER.info("POISE: " + poise);
//        EpicParaglidersMod.LOGGER.info("TOTAL PENALTY: " + totalPenalty);
//        EpicParaglidersMod.LOGGER.info("TOTAL IMPACT: " + totalImpact);
//        EpicParaglidersMod.LOGGER.info("CONSUMPTION: " + getConsumption());
        //TODO: Gonna need to use the triangular numbers algorithm here too.
//        EpicParaglidersMod.LOGGER.info("CURRENT CONSUMPTION: " + ((PlayerMovementInterface) playerMovement).getTotalActionStaminaCost());
//        EpicParaglidersMod.LOGGER.info("INITIAL GUARD COST: " + ((getConsumption() + totalPenalty + totalImpact) * (1 - poise)));
//        int totalGuardConsumption = (int) MathUtils.calculateTriangularRoot((MathUtils.calculateTriangularNumber((int) ((getConsumption() + totalPenalty + totalImpact) * (1 - poise)))
//                + MathUtils.calculateTriangularNumber(((PlayerMovementInterface) playerMovement).getTotalActionStaminaCost())));

        float totalGuardConsumption = (15 - stamina);
        EpicParaglidersMod.LOGGER.info("GUARD CONSUMPTION AFTER TRIANGULAR NUMBERS: " + totalGuardConsumption);

        //TODO: Actually do the math. Just testing the output here.
        ((PlayerMovementInterface) playerMovement).setTotalActionStaminaCostServerSide((int) totalGuardConsumption + 10);

        if (playerMovement.isDepleted()) {
            EpicParaglidersMod.LOGGER.info("IS DEPLETED");
            return -0.1f; //TODO: That did it.
        }
        else {
            return 0.0f;
        }
//        return stamina;
    }
}
