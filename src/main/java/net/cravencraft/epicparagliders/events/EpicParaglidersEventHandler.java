package net.cravencraft.epicparagliders.events;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.capabilities.PlayerMovementInterface;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tictim.paraglider.capabilities.PlayerMovement;

@Mod.EventBusSubscriber(modid = EpicParaglidersMod.MOD_ID)
public final class EpicParaglidersEventHandler {

    private EpicParaglidersEventHandler() {}

    /**
     * TODO: Flesh this out in a different release. EFM changes something about how
     *       blocking is done. Makes it possible to keep holding guard even when your
     *       stamina is gone, and doesn't block all of attacks. Shields should block
     *       all attacks, but cost stamina based on impact and such (Less than with swords).
     * Registers if the player blocks any attack from an entity.
     *
     * @param event
     */
//    @SubscribeEvent
//    public static void ShieldBlockEvent(ShieldBlockEvent event) {
//        if (event.getEntity() instanceof ServerPlayer player) {
//            EpicParaglidersMod.LOGGER.info("BLOCKING ATTACKING");
//            PlayerMovement playerMovement = PlayerMovement.of(player);
//            ((PlayerMovementInterface) playerMovement).setActionStaminaCostServerSide(25);
//            ((PlayerMovementInterface) playerMovement).performingActionServerSide(true);
//        }
//    }
}
