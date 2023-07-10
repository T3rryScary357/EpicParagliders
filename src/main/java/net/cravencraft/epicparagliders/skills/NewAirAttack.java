package net.cravencraft.epicparagliders.skills;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import java.util.List;

public class NewAirAttack extends Skill {
	public static Builder<NewAirAttack> createBuilder() {
		return (new Builder<NewAirAttack>(new ResourceLocation(EpicFightMod.MODID, "air_attack"))).setCategory(SkillCategories.AIR_ATTACK).setConsumption(0.0F).setActivateType(ActivateType.ONE_SHOT).setResource(Resource.NONE);
	}
	
	public NewAirAttack(Builder<? extends Skill> builder) {
		super(builder);
	}
	
	@Override
	public boolean isExecutableState(PlayerPatch<?> executer) {
		EntityState playerState = executer.getEntityState();
		Player player = executer.getOriginal();
		return !(player.isPassenger() || player.isSpectator() || executer.isUnstable() || !playerState.canBasicAttack());
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		List<StaticAnimation> motions = executer.getHoldingItemCapability(InteractionHand.MAIN_HAND).getAutoAttckMotion(executer);
		StaticAnimation attackMotion = motions.get(motions.size() - 1);
		
		if (attackMotion != null) {
			super.executeOnServer(executer, args);
			executer.playAnimationSynchronized(attackMotion, 0);
		}
	}
}