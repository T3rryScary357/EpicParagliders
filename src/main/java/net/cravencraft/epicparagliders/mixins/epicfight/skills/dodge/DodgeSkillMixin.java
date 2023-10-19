package net.cravencraft.epicparagliders.mixins.epicfight.skills.dodge;

import net.cravencraft.epicparagliders.EPModCfg;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.dodge.DodgeSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

@Mixin(DodgeSkill.class)
public abstract class DodgeSkillMixin extends Skill {

    public DodgeSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
        this.consumption = 0.3F;
    }

    @Inject(method = "executeOnServer", at = @At("HEAD"), remap = false)
    private void getPlayerPatch(ServerPlayerPatch executer, FriendlyByteBuf args, CallbackInfo ci) {
        PlayerMovement playerMovement = PlayerMovement.of(executer.getOriginal());
        int rollConsumption;
        float weight = executer.getWeight();


        if (!playerMovement.isDepleted()) {
            super.executeOnServer(executer, args);

            /*
             * If the weight is greater than 40 (the baseline), then factor in weight attenuation.
             * If the weight is less than 40, then just set it to 0 for the base formula.
             */
            if (weight > 40.0F) {
                float attenuation = Mth.clamp(executer.getOriginal().level.getGameRules().getInt(EpicFightGamerules.WEIGHT_PENALTY), 0, 100) / 100.0F;
                weight = (weight - 40) * attenuation;

            }
            else {
                weight = 0;
            }

            // Baseline stamina consumption. Roll = 18 | Dodge = 16
            rollConsumption = (int) (10 + (this.consumption * 2));

            // Divide the weight by 12 to create a smooth curve for stamina consumption that's balanced for all weights.
            rollConsumption = (int) ((rollConsumption + Math.ceil(weight / 12)) * EPModCfg.baseDodgeStaminaMultiplier());
            ((PlayerMovementInterface) playerMovement).setActionStaminaCostServerSide(rollConsumption);
            ((PlayerMovementInterface) playerMovement).performingActionServerSide(true);
        }
        else {
            ci.cancel();
        }
    }
}