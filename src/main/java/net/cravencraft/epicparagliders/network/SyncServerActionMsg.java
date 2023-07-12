package net.cravencraft.epicparagliders.network;

import net.cravencraft.epicparagliders.capabilities.UpdatedPlayerMovement;
import net.minecraft.network.FriendlyByteBuf;

public record SyncServerActionMsg (int additionalStaminaCost) {
    public static SyncServerActionMsg read(FriendlyByteBuf buffer){
        return new SyncServerActionMsg(buffer.readInt());
    }

    public SyncServerActionMsg(UpdatedPlayerMovement playerMovement){
        this(playerMovement.skillStaminaCost);
    }

    public void copyTo(UpdatedPlayerMovement playerMovement){
        playerMovement.skillStaminaCost = additionalStaminaCost;
    }

    public void write(FriendlyByteBuf buffer){
        buffer.writeInt(additionalStaminaCost);
    }
}
