package com.cravencraft.epicparagliders.mixins.epicfight.skills.passive;

import com.cravencraft.epicparagliders.EpicParaglidersMod;
import com.cravencraft.epicparagliders.config.ConfigManager;
import com.cravencraft.epicparagliders.capabilities.StaminaOverride;
import com.cravencraft.epicparagliders.config.ServerConfig;
import com.cravencraft.epicparagliders.utils.MathUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tictim.paraglider.forge.capability.PlayerMovementProvider;
import tictim.paraglider.impl.movement.PlayerMovement;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPSkillExecutionFeedback;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.passive.HyperVitalitySkill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.SkillConsumeEvent;

@Mixin(HyperVitalitySkill.class)
public abstract class HyperVitalitySkillMixin extends PassiveSkill {

    public HyperVitalitySkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    /**
     * Modifies the Hyper Vitality skill by integrating with the Paragliders stamina system,
     * Innate weapon skills will consume the stamina of a basic attack with the given weapon
     * multiplied by the multiplier set in the server config for this skill.
     *
     * @param container
     * @param event
     * @param ci
     */
    @Inject(method = "lambda$onInitiate$0", at = @At("HEAD"), remap = false, cancellable = true)
    private void getPlayerPatch(SkillContainer container, SkillConsumeEvent event, CallbackInfo ci) {
        if (!container.getExecuter().getSkill(event.getSkill()).isDisabled() && event.getSkill().getCategory() == SkillCategories.WEAPON_INNATE) {
            PlayerPatch<?> playerpatch = event.getPlayerPatch();
            PlayerMovement playerMovement = PlayerMovementProvider.of(playerpatch.getOriginal());
            if (playerpatch.getSkill(SkillSlots.WEAPON_INNATE).getStack() < 1 && container.getStack() > 0 && !playerpatch.getOriginal().isCreative()) {
                float consumption = event.getSkill().getConsumption();
                if (!playerMovement.stamina().isDepleted()) {
                    event.setResourceType(Resource.NONE);
                    container.setMaxResource(consumption * 0.2F);
                    EpicParaglidersMod.LOGGER.info("HYPER VITATLITY BASE WEAPON DAMAGE: {}", MathUtils.getAttackStaminaCost(playerpatch.getOriginal()));
                    EpicParaglidersMod.LOGGER.info("HYPER VITALITY MULTIPLIER: {}", ConfigManager.SERVER_CONFIG.hyperVitalityMultiplier());
                    EpicParaglidersMod.LOGGER.info("INSIDE HYPER VITALITY SKILL. CONSUMPTION AMOUNT: {}", (float) (MathUtils.getAttackStaminaCost(playerpatch.getOriginal()) * ConfigManager.SERVER_CONFIG.hyperVitalityMultiplier()));
                    if (!container.getExecuter().isLogicalClient()) {
                        ((StaminaOverride) playerMovement.stamina()).performingAction(true);
//                        container.getExecuter().consumeStamina((float) (MathUtils.getAttackStaminaCost(playerpatch.getOriginal()) * ConfigManager.SERVER_CONFIG.hyperVitalityMultiplier()));
                        container.setMaxDuration(event.getSkill().getMaxDuration());
                        container.activate();
                        event.getPlayerPatch().setStamina((float) (MathUtils.getAttackStaminaCost(playerpatch.getOriginal()) * ConfigManager.SERVER_CONFIG.hyperVitalityMultiplier()));
                        EpicFightNetworkManager.sendToPlayer(SPSkillExecutionFeedback.executed(container.getSlotId()), (ServerPlayer)playerpatch.getOriginal());
                    }
                }
            }
        }
        ci.cancel();
    }
}