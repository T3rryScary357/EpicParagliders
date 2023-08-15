package net.cravencraft.epicparagliders.mixins.skills;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.cravencraft.epicparagliders.utils.MathUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.api.utils.math.Formulars;
import yesman.epicfight.skill.GuardSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
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
        float poise = Formulars.getStaminarConsumePenalty(this.playerPatch.getWeight(), 1, this.playerPatch) * 0.1F;
        EpicParaglidersMod.LOGGER.info("PENALTY: " + this.penalty);
        int totalPenalty = (int) (penalty * 5);
        int totalImpact = (int) (impact * 10);
        int totalGuardConsumption = (int) MathUtils.calculateTriangularRoot((MathUtils.calculateTriangularNumber((int) ((getConsumption() + totalPenalty + totalImpact) * (1 - poise)))
                + MathUtils.calculateTriangularNumber(((PlayerMovementInterface) playerMovement).getTotalActionStaminaCost())));

        ((PlayerMovementInterface) playerMovement).setTotalActionStaminaCostServerSide(totalGuardConsumption);

        if (playerMovement.isDepleted()) {
            return -0.1f;
        }
        else {
            return 0.0f;
        }
    }
}
