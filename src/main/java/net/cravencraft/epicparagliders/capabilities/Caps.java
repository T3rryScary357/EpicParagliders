package net.cravencraft.epicparagliders.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

// TODO: Might not need.
public final class Caps {
	private Caps() {}

	public static final Capability<UpdatedPlayerMovement> updatedPlayerMovement = CapabilityManager.get(new CapabilityToken<>() {
	});
}
