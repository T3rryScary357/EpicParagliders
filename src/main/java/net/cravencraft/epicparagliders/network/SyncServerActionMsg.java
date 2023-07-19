package net.cravencraft.epicparagliders.network;

import net.cravencraft.epicparagliders.capabilities.UpdatedPlayerMovement;
import net.minecraft.network.FriendlyByteBuf;

public record SyncServerActionMsg (int additionalStaminaCost, int additionalStaminaGain) {
    public static SyncServerActionMsg read(FriendlyByteBuf buffer){
        return new SyncServerActionMsg(buffer.readInt(), buffer.readInt());
    }

    public SyncServerActionMsg(UpdatedPlayerMovement playerMovement){
        this(playerMovement.skillStaminaCost, playerMovement.skillStaminaGain);
    }

    public void copyTo(UpdatedPlayerMovement playerMovement){
        playerMovement.skillStaminaCost = additionalStaminaCost;
        playerMovement.skillStaminaGain = additionalStaminaGain;
    }

    public void write(FriendlyByteBuf buffer){
        buffer.writeInt(additionalStaminaCost);
        buffer.writeInt(additionalStaminaGain);
    }
}
