package net.cravencraft.epicparagliders.mixins.skills;

import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.api.utils.math.Formulars;
import yesman.epicfight.skill.DodgeSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(DodgeSkill.class)
public abstract class DodgeSkillMixin extends Skill {

    public DodgeSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "executeOnServer", at = @At("HEAD"), remap = false)
    private void getPlayerPatch(ServerPlayerPatch executer, FriendlyByteBuf args, CallbackInfo ci) {
        PlayerMovement playerMovement = PlayerMovement.of(executer.getOriginal());

        if (!playerMovement.isDepleted()){
            super.executeOnServer(executer, args);
            int rollConsumption = (int) (Formulars.getStaminarConsumePenalty(executer.getWeight(), 10, executer));
//            int totalRollConsumption = (int) MathUtils.calculateTriangularRoot((MathUtils.calculateTriangularNumber(rollConsumption)
//                                        + MathUtils.calculateTriangularNumber(((PlayerMovementInterface) playerMovement).getTotalActionStaminaCost())));
//            ((PlayerMovementInterface) playerMovement).setTotalActionStaminaCostServerSide(totalRollConsumption);
            ((PlayerMovementInterface) playerMovement).setActionStaminaCostServerSide(rollConsumption);
            ((PlayerMovementInterface) playerMovement).performingActionServerSide(true);
        }
        else {
            ci.cancel();
        }
    }
}