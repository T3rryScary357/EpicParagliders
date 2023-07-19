package net.cravencraft.epicparagliders.client;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.UpdatedModCfg;
import net.cravencraft.epicparagliders.capabilities.UpdatedClientPlayerMovement;
import tictim.paraglider.capabilities.PlayerMovement;
import tictim.paraglider.capabilities.PlayerState;
import tictim.paraglider.client.DisableStaminaRender;
import tictim.paraglider.client.StaminaWheelConstants;
import tictim.paraglider.client.StaminaWheelRenderer;
import tictim.paraglider.utils.Color;

import static tictim.paraglider.client.StaminaWheelConstants.*;

public class InGameStaminaWheelRenderer extends StaminaWheelRenderer implements DisableStaminaRender {
	private int prevStamina;
	private long fullTime;

	@Override protected void makeWheel(PlayerMovement pm) {
		UpdatedClientPlayerMovement clientPlayer = UpdatedClientPlayerMovement.instance;
		if (clientPlayer != null) {
			int stamina = clientPlayer.playerMovement.getStamina();
			int maxStamina = clientPlayer.playerMovement.getMaxStamina();

			if (stamina>=maxStamina) {
				long time = System.currentTimeMillis();
				long timeDiff;
				if (prevStamina!=stamina){
					prevStamina = stamina;
					fullTime = time;
					timeDiff = 0;
				}
				else timeDiff = time-fullTime;
				Color color = StaminaWheelConstants.getGlowAndFadeColor(timeDiff);
				if (color.alpha<=0) return;
				for (WheelLevel t : WheelLevel.values())
					addWheel(t, 0, t.getProportion(stamina), color);
			}
			else {
				prevStamina = stamina;
				Color color = DEPLETED_1.blend(DEPLETED_2, cycle(System.currentTimeMillis(), clientPlayer.playerMovement.isDepleted() ? DEPLETED_BLINK : BLINK));
				Color gainStam = Color.of(2, 2, 150).blend(Color.of(2, 150, 255), cycle(System.currentTimeMillis(), clientPlayer.playerMovement.isDepleted() ? DEPLETED_BLINK : BLINK));;
//				Color gainStam2 = Color.of(2, 150, 255);
				PlayerState state = clientPlayer.playerMovement.getState();
				// TODO: Maybe we can replace that entire giant if with this?? Look into it.
				int stateChange = (state.isConsume()) ? state.change() : -clientPlayer.totalActionStaminaCost;
				for (WheelLevel t : WheelLevel.values()) {
					addWheel(t, 0, t.getProportion(maxStamina), EMPTY);
					if (clientPlayer.playerMovement.isDepleted()) {
						addWheel(t, 0, t.getProportion(stamina), color);
					} else {
						addWheel(t, 0, t.getProportion(stamina), IDLE);
						if (((state.isConsume() && (state.isParagliding() ? UpdatedModCfg.paraglidingConsumesStamina() : UpdatedModCfg.runningConsumesStamina())))
								|| clientPlayer.totalActionStaminaCost > 0) {
							addWheel(t, t.getProportion(stamina + stateChange * 10), t.getProportion(stamina), color);
						}
						//TODO: If we render this only for the gain, then we could have both at the same time
						if (clientPlayer.totalActionStaminaCost < 0) {
							EpicParaglidersMod.LOGGER.info("Inside positive wheel gain " + (-clientPlayer.totalActionStaminaCost));
							addWheel(t,  t.getProportion(stamina), t.getProportion(stamina + (-clientPlayer.totalActionStaminaCost) * 10), gainStam);
						}
					}
				}
			}
		}
	}
}