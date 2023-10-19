package net.cravencraft.epicparagliders.events;

import net.cravencraft.epicparagliders.EPModCfg;
import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tictim.paraglider.capabilities.PlayerMovement;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

@Mod.EventBusSubscriber(modid = EpicParaglidersMod.MOD_ID)
public final class EpicParaglidersEventHandler {

    private EpicParaglidersEventHandler() {
    }

    /**
     * Registers if the player blocks any attack from an entity. And uses math similar to the
     * GuardSkill class to determine how much stamina to drain.
     *
     * @param event
     */
    @SubscribeEvent
    public static void ShieldBlockEvent(ShieldBlockEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ServerPlayerPatch serverPlayerPatch = (ServerPlayerPatch) player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null);
            PlayerMovement playerMovement = PlayerMovement.of(player);

            if (playerMovement.isDepleted()) {
                serverPlayerPatch.playAnimationSynchronized(Animations.BIPED_COMMON_NEUTRALIZED, 0.0F);
                serverPlayerPatch.playSound(EpicFightSounds.NEUTRALIZE_MOBS, 3.0F, 0.0F, 0.1F);
            } else {
                int armorValue = player.getArmorValue();

                double blockMultiplier = EPModCfg.baseBlockStaminaMultiplier();

                float poise;
                float weight = serverPlayerPatch.getWeight();

                if (weight <= 40.0F) {
                    poise = 0.0F;
                } else {
                    float attenuation = Mth.clamp(player.level.getGameRules().getInt(EpicFightGamerules.WEIGHT_PENALTY), 0, 100) / 100.0F;
                    poise = (0.1F * (weight / 40.0F) * (Math.max(armorValue, 0) * 1.5F) * attenuation);
                }

                double guardConsumption = (blockMultiplier) + (event.getBlockedDamage());
                EpicParaglidersMod.LOGGER.info("PRIOR GUARD CONSUMPTION: " + guardConsumption);
                guardConsumption = (guardConsumption > poise) ? (guardConsumption - poise) : 0;
                EpicParaglidersMod.LOGGER.info("WEIGHT: " + weight);
                EpicParaglidersMod.LOGGER.info("POISE: " + poise);
                EpicParaglidersMod.LOGGER.info("ENEMY DAMAGE: " + event.getBlockedDamage());
                EpicParaglidersMod.LOGGER.info("AFTER GUARD CONSUMPTION: " + guardConsumption);
                ((PlayerMovementInterface) playerMovement).setActionStaminaCostServerSide((int) guardConsumption);
                ((PlayerMovementInterface) playerMovement).performingActionServerSide(true);
            }
        }
    }
}
