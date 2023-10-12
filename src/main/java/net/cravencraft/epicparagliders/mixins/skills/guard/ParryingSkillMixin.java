package net.cravencraft.epicparagliders.mixins.skills.guard;

import net.cravencraft.epicparagliders.EPModCfg;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.cravencraft.epicparagliders.utils.MathUtils;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.guard.ParryingSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

@Mixin(ParryingSkill.class)
public abstract class ParryingSkillMixin extends GuardSkill {

    private float penalty;
    private float impact;
    private PlayerPatch playerPatch;

    public ParryingSkillMixin(Builder builder) {
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

    /**
     * Modifies the 'stamina' variable in the 'guard' method of the ActiveGuardSkill class.
     * Takes in values retrieved from the above methods that simply store the penalty and impact
     * from the same method to factor into this method's new stamina consumption factor.
     *
     * Calculates the stamina cost based on player armor weight, impact, and block penalty.
     * If active guard is successful (penalty > 0.1), then less stamina is drained
     * (vs. negating all stamina being drained initially), else even more stamina is drained
     * from the block. Making this a high risk high reward system to choose of regular guard.
     *
     * @param blockType Either GUARD_BREAK or ADVANCED_GUARD
     * @return
     */
//    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "guard", at = @At(value = "STORE"), ordinal = 0, remap = false)
    private GuardSkill.BlockType blockType(GuardSkill.BlockType blockType) {
        PlayerMovement playerMovement = PlayerMovement.of(playerPatch.getOriginal());

        int guardConsumption;
        int armorValue = playerMovement.player.getArmorValue();

        double blockMultiplier = EPModCfg.baseBlockStaminaMultiplier();
        double parryPenaltyMultiplier = EPModCfg.parryPenaltyMultiplier();
        double parryPercentModifier = EPModCfg.parryPercentModifier() * 0.01;

        float poise;
        float weight = this.playerPatch.getWeight();
        float currentStamina = playerMovement.getStamina();
        float missingStamina = playerMovement.getMaxStamina() - currentStamina;

        if (weight > 40.0F) {
            float attenuation = Mth.clamp(this.playerPatch.getOriginal().level.getGameRules().getInt(EpicFightGamerules.WEIGHT_PENALTY), 0, 100) / 100.0F;
            poise = (0.1F * (weight / 40.0F) * (Math.max(armorValue, 0) * 1.5F) * attenuation);
        }
        else {
            poise = 0.0F;
        }

        if (this.penalty > 0.1f) {
            guardConsumption = (int) (( ((getConsumption() + (penalty * parryPenaltyMultiplier) + (impact * parryPenaltyMultiplier)) - poise)));
        }
        else {
            if (EPModCfg.parryDrain()) {
                guardConsumption = (int) ((getConsumption() + (penalty * blockMultiplier * parryPercentModifier) + (impact * blockMultiplier * parryPercentModifier)) - poise);
            }
            else {
                guardConsumption = (int) (MathUtils.calculateModifiedTriangularRoot(missingStamina, parryPercentModifier));
            }


        }

        ((PlayerMovementInterface) playerMovement).setActionStaminaCostServerSide(guardConsumption);
        ((PlayerMovementInterface) playerMovement).performingActionServerSide(true);

        if (playerMovement.isDepleted()) {
            return GuardSkill.BlockType.GUARD_BREAK;
        }
        else {
            return blockType;
        }
    }
}