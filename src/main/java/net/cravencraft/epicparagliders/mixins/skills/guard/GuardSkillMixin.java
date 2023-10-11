package net.cravencraft.epicparagliders.mixins.skills.guard;

import net.cravencraft.epicparagliders.EPModCfg;
import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;

@Mixin(GuardSkill.class)
public abstract class GuardSkillMixin extends Skill {

    private float penalty;
    private float impact;
    private PlayerPatch playerPatch;

    public GuardSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "guard", at = @At("HEAD"), remap = false)
    private void getPlayerPatch(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        this.playerPatch = event.getPlayerPatch();
        this.impact = impact;
    }

    @ModifyVariable(method = "guard", at = @At(value = "STORE"), ordinal = 2, remap = false)
    private float getPenalty(float penalty) {
        this.penalty = penalty;
        return penalty;
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "guard", at = @At(value = "STORE"), ordinal = 3, remap = false)
    private float stamina(float stamina) {
        PlayerMovement playerMovement = PlayerMovement.of(playerPatch.getOriginal());
        EpicParaglidersMod.LOGGER.info("PENALTY (isn't that the multiplier?): " + penalty);
//        float poise = Formulars.getStaminarConsumePenalty(this.playerPatch.getWeight(), 1, this.playerPatch) * 0.1F;
        float poise = 0.0f;
        int totalPenalty = (int) (penalty * 5);
        int totalImpact = (int) (impact * 10);

        int guardConsumption = (int) ((getConsumption() + totalPenalty + totalImpact) * (1 - poise) * EPModCfg.baseBlockStaminaConsumption());

        ((PlayerMovementInterface) playerMovement).setActionStaminaCostServerSide(guardConsumption);
        ((PlayerMovementInterface) playerMovement).performingActionServerSide(true);

        if (playerMovement.isDepleted()) {
            return -0.1f;
        }
        else {
            return 0.0f;
        }
    }
}
