package net.cravencraft.epicparagliders.network;

import net.cravencraft.epicparagliders.capabilities.UpdatedPlayerMovement;
import net.minecraft.network.FriendlyByteBuf;
import tictim.paraglider.capabilities.PlayerMovement;

public record SyncServerActionMsg (int actionStaminaCost) {
    public static SyncServerActionMsg read(FriendlyByteBuf buffer){
        return new SyncServerActionMsg(buffer.readInt());
    }

    public SyncServerActionMsg(UpdatedPlayerMovement playerMovement){
        this(playerMovement.actionStaminaCost);
    }

    public void copyTo(UpdatedPlayerMovement playerMovement){
        playerMovement.actionStaminaCost = actionStaminaCost;
    }

    public void write(FriendlyByteBuf buffer){
        buffer.writeInt(actionStaminaCost);
    }
}
