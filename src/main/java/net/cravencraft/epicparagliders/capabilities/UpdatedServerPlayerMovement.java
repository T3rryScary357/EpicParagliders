package net.cravencraft.epicparagliders.capabilities;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.network.ModNet;
import net.cravencraft.epicparagliders.network.SyncServerActionMsg;
import net.cravencraft.epicparagliders.skills.ReRegisterSkills;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.network.PacketDistributor;
import tictim.paraglider.ModCfg;
import tictim.paraglider.ParagliderMod;
import tictim.paraglider.capabilities.ServerPlayerMovement;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

public class UpdatedServerPlayerMovement extends UpdatedPlayerMovement {

    //TODO: Probably make a utility class for this and the client one.
    public static UpdatedServerPlayerMovement instance;
    public ServerPlayerPatch serverPlayerPatch;
    public ServerPlayerMovement serverPlayerMovement;

    //TODO: Need to make getters & setters for these, then make private.
    public boolean actionStaminaNeedsSync;

    public UpdatedServerPlayerMovement(ServerPlayerMovement serverPlayerMovement) {
        super(serverPlayerMovement);
        this.serverPlayerMovement = serverPlayerMovement;
        instance = this;
    }

    @Override
    public void update() {
        initServerPlayerPatch();
        if(!serverPlayerMovement.player.isCreative()&&serverPlayerMovement.isDepleted()){
            serverPlayerMovement.player.addEffect(new MobEffectInstance(MobEffect.byId(18))); // Adds weakness
        }
        syncActionStamina();
        addEffects();
        updateStamina();
    }

    //TODO: Separate this method into two methods and move the if statement into the update method.
    private void initServerPlayerPatch() {

        if (this.serverPlayerPatch == null && this.serverPlayerMovement.player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).isPresent()) {
            this.serverPlayerPatch = (ServerPlayerPatch) this.serverPlayerMovement.player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null);
            this.serverPlayerPatch.getOriginal().getAttribute(EpicFightAttributes.MAX_STAMINA.get()).setBaseValue(0.0D);
            ReRegisterSkills.setNewSkills(this);
            ReRegisterSkills.reRegisterToPlayer(this);
        }
        else if (this.setNewSkill) {
            ReRegisterSkills.reRegisterToPlayer(this);
        }
    }

    private void syncActionStamina() {
        if (actionStaminaNeedsSync && serverPlayerPatch != null) {
            if (serverPlayerMovement.player instanceof ServerPlayer serverPlayer) {
                SyncServerActionMsg msg = new SyncServerActionMsg(this);
                if(ModCfg.traceMovementPacket()) ParagliderMod.LOGGER.debug("Sending packet {} to player {}", msg, this.serverPlayerMovement.player);
                ModNet.NET.send(PacketDistributor.PLAYER.with(() -> serverPlayer), msg);
                actionStaminaNeedsSync = false;
            }
        }
    }
}