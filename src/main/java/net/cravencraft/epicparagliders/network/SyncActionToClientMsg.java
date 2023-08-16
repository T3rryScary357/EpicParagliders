package net.cravencraft.epicparagliders.network;

import net.minecraft.network.FriendlyByteBuf;

/**
 * A message that contains a record of the total amount of stamina to drain from
 * the player. Sent from server to client.
 *
 * @param totalActionStaminaCost
 */
public record SyncActionToClientMsg(int totalActionStaminaCost, boolean isPerformingAction) {
    public static SyncActionToClientMsg read(FriendlyByteBuf buffer){
        return new SyncActionToClientMsg(buffer.readInt(), buffer.readBoolean());
    }

    public void write(FriendlyByteBuf buffer){
        buffer.writeInt(totalActionStaminaCost);
        buffer.writeBoolean(isPerformingAction);
    }
}
