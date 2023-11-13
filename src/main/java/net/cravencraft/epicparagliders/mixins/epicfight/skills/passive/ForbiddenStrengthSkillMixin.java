package net.cravencraft.epicparagliders.mixins.epicfight.skills.passive;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.config.ConfigManager;
import net.cravencraft.epicparagliders.utils.MathUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.passive.ForbiddenStrengthSkill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.entity.eventlistener.SkillConsumeEvent;

@Mixin(ForbiddenStrengthSkill.class)
public abstract class ForbiddenStrengthSkillMixin extends PassiveSkill {

    public ForbiddenStrengthSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    /**
     *
     *
     * @param container
     * @param event
     * @param ci
     */
    @Inject(method = "lambda$onInitiate$0", at = @At("HEAD"), remap = false, cancellable = true)
    private static void modifyStaminaSources(SkillContainer container, SkillConsumeEvent event, CallbackInfo ci) {
        if (event.getResourceType() == Skill.Resource.STAMINA) {
            float staminaConsume = (float) MathUtils.calculateTriangularNumber((int) event.getAmount());

            if (!container.getExecuter().hasStamina(staminaConsume) && !container.getExecuter().getOriginal().isCreative()) {
                event.setResourceType(Skill.Resource.HEALTH);
                event.setAmount((float) (event.getAmount() * ConfigManager.SERVER_CONFIG.forbiddenStrengthMultiplier()));

                if (event.shouldConsume()) {
                    Player player = container.getExecuter().getOriginal();
                    player.level.playSound(null, player.getX(), player.getY(), player.getZ(), EpicFightSounds.FORBIDDEN_STRENGTH, player.getSoundSource(), 1.0F, 1.0F);
                    ((ServerLevel)player.level).sendParticles(ParticleTypes.DAMAGE_INDICATOR, player.getX(), player.getY(0.5D), player.getZ(), (int)staminaConsume, 0.1D, 0.0D, 0.1D, 0.2D);
                }
            }
        }
        ci.cancel();
    }

//    /**
//     *
//     *
//     * @param staminaConsume
//     * @return
//     */
//    @SuppressWarnings("InvalidInjectorMethodSignature")
//    @ModifyVariable(method = "lambda$onInitiate$0", at = @At(value = "STORE"), ordinal = 0, remap = false)
//    private static float modifiedStaminaConsumption(float staminaConsume) {
//        return (float) MathUtils.calculateTriangularNumber((int) staminaConsume);
//    }

//    @Redirect(at = @At(value = "INVOKE", target = "", ordinal = 0), method = "lambda$onInitiate$0")
}