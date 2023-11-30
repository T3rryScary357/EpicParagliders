package net.cravencraft.epicparagliders.mixins.epicfight.skills;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tictim.paraglider.forge.capability.PlayerMovementProvider;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(Skill.Resource.class)
public abstract class SkillMixin {

    @Redirect(at = @At(value = "INVOKE", target = "Lyesman/epicfight/world/capabilities/entitypatch/player/PlayerPatch;hasStamina(F)Z"), remap = false, method = "lambda$static$6")
    private static boolean testRedirect(PlayerPatch playerPatch, float amount) {
        if (PlayerMovementProvider.of(playerPatch.getOriginal()).stamina().isDepleted()) {
            return false;
        }
        else {
            return true;
        }
    }
}