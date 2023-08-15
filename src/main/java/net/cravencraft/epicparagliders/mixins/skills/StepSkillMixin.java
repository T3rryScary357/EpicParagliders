//package net.cravencraft.epicparagliders.mixins.skills;
//
//import net.cravencraft.epicparagliders.EpicParaglidersMod;
//import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
//import net.cravencraft.epicparagliders.utils.MathUtils;
//import net.minecraft.network.FriendlyByteBuf;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import tictim.paraglider.capabilities.PlayerMovement;
//import yesman.epicfight.api.utils.math.Formulars;
//import yesman.epicfight.skill.DodgeSkill;
//import yesman.epicfight.skill.Skill;
//import yesman.epicfight.skill.StepSkill;
//import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
//
//@Mixin(StepSkill.class)
//public abstract class StepSkillMixin extends Skill {
//
//    public StepSkillMixin(Builder<? extends Skill> builder) {
//        super(builder);
//    }
//
//    @Inject(method = "executeOnServer", at = @At("HEAD"), remap = false)
//    private void getPlayerPatch(ServerPlayerPatch executer, FriendlyByteBuf args, CallbackInfo ci) {
//        PlayerMovement playerMovement = PlayerMovement.of(executer.getOriginal());
//
//        if (!playerMovement.isDepleted()){
//            super.executeOnServer(executer, args);
//            int rollConsumption = (int) (Formulars.getStaminarConsumePenalty(executer.getWeight(), 10, executer));
//            EpicParaglidersMod.LOGGER.info("ROLL CONSUMPTION: " + rollConsumption);
//            EpicParaglidersMod.LOGGER.info("ACTION STAMINA PRIOR: " + ((PlayerMovementInterface) playerMovement).getTotalActionStaminaCost());
//            int totalRollConsumption = (int) MathUtils.calculateTriangularRoot((MathUtils.calculateTriangularNumber(rollConsumption)
//                                        + MathUtils.calculateTriangularNumber(((PlayerMovementInterface) playerMovement).getTotalActionStaminaCost())));
//            EpicParaglidersMod.LOGGER.info("ROLL CONSUMPTION AFTER TRIANGULAR NUMBERS: " + totalRollConsumption);
//            ((PlayerMovementInterface) playerMovement).setTotalActionStaminaCostServerSide(totalRollConsumption);
//        }
//        else {
//            ci.cancel();
//        }
//    }
//}