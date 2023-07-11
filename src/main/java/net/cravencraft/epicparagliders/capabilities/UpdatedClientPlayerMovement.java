package net.cravencraft.epicparagliders.capabilities;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.network.ModNet;
import net.cravencraft.epicparagliders.network.SyncActionMsg;
import net.minecraft.client.Minecraft;
import tictim.paraglider.capabilities.ClientPlayerMovement;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.gui.screen.SkillBookScreen;
import yesman.epicfight.world.item.SkillBookItem;

public class UpdatedClientPlayerMovement extends UpdatedPlayerMovement {

    public static UpdatedClientPlayerMovement instance;

    //TODO: Can we just have these in the parent class without issues?
    public ClientPlayerMovement clientPlayerMovement;
    private int totalStaminaCost;
    private int inactionTime;
    private int skillCheckDelay;

    public UpdatedClientPlayerMovement(ClientPlayerMovement clientPlayerMovement) {
        super(clientPlayerMovement);
        this.clientPlayerMovement = clientPlayerMovement;
        instance = this;
    }

    @Override
    public void update() {
//        EpicParaglidersMod.LOGGER.info("Client side stamina cost: " + this.actionStaminaCost);
        if (this.actionStaminaCost > 0) {
            this.totalStaminaCost += this.actionStaminaCost;
        }
        //TODO: Turn these two conditionals into a method. May want to use them in the client event class.
        //TODO: Ensure that the player patch is never null. Probably won't be based on how update() is called.
        if (ClientEngine.instance.getPlayerPatch().getEntityState().inaction()) {
            this.inactionTime++;
            EpicParaglidersMod.LOGGER.info("Inaction time: " + this.inactionTime);
            if (ClientEngine.instance.getPlayerPatch().getEntityState().attacking() && !this.isAttacking) {
//				this.player.getOffhandItem(); // TODO: add support for offhand weapons
                this.totalStaminaCost = 0;
                this.actionStaminaCost = (int)clientPlayerMovement.player.getCurrentItemAttackStrengthDelay();
                //TODO: Attack strength delay doesn't necessarily mean the total delay.
                //      Easy fix would be just to make exception for axes here.
                EpicParaglidersMod.LOGGER.info("Action stamina cost at root: " + (int)clientPlayerMovement.player.getCurrentItemAttackStrengthDelay());
                EpicParaglidersMod.LOGGER.info("Attack stamina cost: " + actionStaminaCost);
                this.isAttacking = true;
            }
            else if (!ClientEngine.instance.getPlayerPatch().getEntityState().attacking() && this.isAttacking){
                this.actionStaminaCost--;
                this.isAttacking = false;
            }
        }
        else if (this.actionStaminaCost > 0){
            this.actionStaminaCost--;
        }
        this.inactionTime = 0;

        updateStamina();
        addEffects();
        setNewSkill();
        ModNet.NET.sendToServer(new SyncActionMsg(this.actionStaminaCost, this.setNewSkill));
    }

    /**
     * Sets the new skill to be learned from a skill book. Method needed to ensure that
     * the new skill is transferred over to the new stamina system. a delay of 10 ticks
     * is set to ensure that the skill is set server side.
     */
    private void setNewSkill() {
        if (Minecraft.getInstance().screen instanceof SkillBookScreen) {
            EpicParaglidersMod.LOGGER.info("Current skill: " + SkillBookItem.getContainSkill(this.playerMovement.player.getMainHandItem()).getRegistryName());
//            SkillBookItem.getContainSkill(this.playerMovement.player.getMainHandItem());
            this.setNewSkill = true;
        }
        else if (this.setNewSkill && skillCheckDelay < 10) {
            EpicParaglidersMod.LOGGER.info("Skill check delay: " + this.skillCheckDelay);
            this.skillCheckDelay++;
        }
        else if (this.setNewSkill) {
            this.setNewSkill = false;
            this.skillCheckDelay = 0;
        }
    }
}
