package net.cravencraft.epicparagliders.network;

import net.cravencraft.epicparagliders.capabilities.UpdatedPlayerMovement;
import net.minecraft.network.FriendlyByteBuf;

public record SyncActionMsg (int totalActionStaminaCost, boolean setNewSkill) {
    public static SyncActionMsg read(FriendlyByteBuf buffer) {
        return new SyncActionMsg(buffer.readInt(), buffer.readBoolean());
    }

    public SyncActionMsg(UpdatedPlayerMovement playerMovement) {
        this(playerMovement.totalActionStaminaCost, playerMovement.setNewSkill);
    }

    public void copyTo(UpdatedPlayerMovement playerMovement){
        playerMovement.totalActionStaminaCost = totalActionStaminaCost;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(totalActionStaminaCost);
        buffer.writeBoolean(setNewSkill);
    }
}
