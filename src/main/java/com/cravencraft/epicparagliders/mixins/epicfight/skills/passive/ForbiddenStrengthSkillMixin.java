package com.cravencraft.epicparagliders.mixins.epicfight.skills.passive;

import com.cravencraft.epicparagliders.config.ConfigManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.forge.capability.PlayerMovementProvider;
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
     * Modifies the Forbidden Strength skill to use the Paragliders stamina system.
     * Multiplies the amount of remaining stamina by the multiplier set for the skill
     * in the server config, then uses the remainder as the amount of health for the
     * skill to consume. The consumes health only for skills and NOT for basic attacks.
     *
     * @param container
     * @param event
     * @param ci
     */
    @Inject(method = "lambda$onInitiate$0", at = @At("HEAD"), remap = false, cancellable = true)
    private void modifyStaminaSources(SkillContainer container, SkillConsumeEvent event, CallbackInfo ci) {
        if (event.getResourceType() == Skill.Resource.STAMINA) {
            if (PlayerMovementProvider.of(event.getPlayerPatch().getOriginal()).stamina().isDepleted() && !container.getExecuter().getOriginal().isCreative()) {
                float healthConsumed = (float) (event.getAmount() * ConfigManager.SERVER_CONFIG.forbiddenStrengthMultiplier());
                event.setResourceType(Skill.Resource.HEALTH);
                event.setAmount(healthConsumed);

                if (!container.getExecuter().isLogicalClient() && event.getResourceType().predicate.canExecute(event.getSkill(), container.getExecuter(), healthConsumed)) {
                    Player player = container.getExecuter().getOriginal();
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), EpicFightSounds.FORBIDDEN_STRENGTH.get(), player.getSoundSource(), 1.0F, 1.0F);
                    ((ServerLevel)player.level()).sendParticles(ParticleTypes.DAMAGE_INDICATOR, player.getX(), player.getY(0.5D), player.getZ(), (int)healthConsumed, 0.1D, 0.0D, 0.1D, 0.2D);
                }
            }
        }
        ci.cancel();
    }
}