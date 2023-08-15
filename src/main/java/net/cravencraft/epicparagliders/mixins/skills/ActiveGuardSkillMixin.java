package net.cravencraft.epicparagliders.mixins.skills;

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
public abstract class ActiveGuardSkillMixin extends GuardSkill {

    private float penalty;
    private float impact;
    private PlayerPatch playerPatch;

    public ActiveGuardSkillMixin(Builder builder) {
        super(builder);
    }

    @Inject(method = "guard", at = @At("HEAD"), remap = false)
    private void getPlayerPatch(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        this.playerPatch = event.getPlayerPatch();
        this.impact = impact;
    }

    @ModifyVariable(method = "guard", at = @At(value = "STORE"), ordinal = 2, remap = false)
    private float penalty(float penalty) {
        this.penalty = penalty;
        return penalty;
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "guard", at = @At(value = "STORE"), ordinal = 3, remap = false)
    private float stamina(float stamina) {
        PlayerMovement playerMovement = PlayerMovement.of(playerPatch.getOriginal());

        float poise = Formulars.getStaminarConsumePenalty(this.playerPatch.getWeight(), 1, this.playerPatch) * 0.1F;
        float totalPenalty;
        float totalImpact;
        if (this.penalty > 0.1f) {
            totalPenalty = (penalty * 5);
            totalImpact = (impact * 12);
        }
        else {
            totalPenalty = (penalty * 5);
            totalImpact = (impact * 8);
        }

        float totalGuardConsumption = (int) MathUtils.calculateTriangularRoot((MathUtils.calculateTriangularNumber((int) ((getConsumption() + totalPenalty + totalImpact) * (1 - poise)))
                                    + MathUtils.calculateTriangularNumber(((PlayerMovementInterface) playerMovement).getTotalActionStaminaCost())));

        ((PlayerMovementInterface) playerMovement).setTotalActionStaminaCostServerSide((int) totalGuardConsumption);

        if (playerMovement.isDepleted()) {
            return -0.1f;
        }
        else {
            return 0.0f;
        }
    }
}