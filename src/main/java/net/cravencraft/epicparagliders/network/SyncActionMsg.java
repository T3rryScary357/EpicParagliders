package net.cravencraft.epicparagliders.network;

import net.cravencraft.epicparagliders.capabilities.UpdatedPlayerMovement;
import net.minecraft.network.FriendlyByteBuf;

public record SyncActionMsg (int actionStaminaCost, boolean setNewSkill) {
    public static SyncActionMsg read(FriendlyByteBuf buffer) {
        return new SyncActionMsg(buffer.readInt(), buffer.readBoolean());
    }

    public SyncActionMsg(UpdatedPlayerMovement playerMovement) {
        this(playerMovement.actionStaminaCost, playerMovement.setNewSkill);
    }

    public void copyTo(UpdatedPlayerMovement playerMovement){
        playerMovement.actionStaminaCost = actionStaminaCost;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(actionStaminaCost);
        buffer.writeBoolean(setNewSkill);
    }
}
