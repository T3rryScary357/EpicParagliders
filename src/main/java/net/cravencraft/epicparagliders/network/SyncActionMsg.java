package net.cravencraft.epicparagliders.network;

import net.cravencraft.epicparagliders.capabilities.UpdatedPlayerMovement;
import net.minecraft.network.FriendlyByteBuf;

public record SyncActionMsg (int totalActionStaminaCost, int attackStaminaCost, int skillStaminaCost, int skillStaminaGain, boolean setNewSkill) {
    public static SyncActionMsg read(FriendlyByteBuf buffer) {
        return new SyncActionMsg(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readBoolean());
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(totalActionStaminaCost);
        buffer.writeInt(attackStaminaCost);
        buffer.writeInt(skillStaminaCost);
        buffer.writeInt(skillStaminaGain);
        buffer.writeBoolean(setNewSkill);
    }
}
