package net.cravencraft.epicparagliders.capabilities;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import tictim.paraglider.capabilities.ServerPlayerMovement;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class UpdatedServerPlayerMovement extends UpdatedPlayerMovement {

    //TODO: Probably make a utility class for this and the client one.
    public static UpdatedServerPlayerMovement instance;
    public ServerPlayerPatch serverPlayerPatch;
    private ServerPlayerMovement serverPlayerMovement;

    public UpdatedServerPlayerMovement(ServerPlayerMovement serverPlayerMovement) {
        super(serverPlayerMovement);
        this.serverPlayerMovement = serverPlayerMovement;
        instance = this;
    }

    @Override
    public void update() {
        EpicParaglidersMod.LOGGER.info("Server state change & recovery delay: " + serverPlayerMovement.getState() + " | " + serverPlayerMovement.getRecoveryDelay());
        initServerPlayerPatch();
        if(!serverPlayerMovement.player.isCreative()&&serverPlayerMovement.isDepleted()){
            serverPlayerMovement.player.addEffect(new MobEffectInstance(MobEffect.byId(18))); // Adds weakness
        }
        addEffects();
        updateStamina();
    }
    private void initServerPlayerPatch() {
        if (this.serverPlayerPatch == null && this.serverPlayerMovement.player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).isPresent()) {
            this.serverPlayerPatch = (ServerPlayerPatch) this.serverPlayerMovement.player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null);
//            ReRegisterSkills.reRegisterToPlayer(this);

        }
//        else if (this.serverPlayerPatch != null) {
////            ReRegisterSkills.reRegisterToPlayer(this);
//        }
    }
}