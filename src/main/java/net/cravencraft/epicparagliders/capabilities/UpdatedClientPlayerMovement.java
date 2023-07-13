package net.cravencraft.epicparagliders.capabilities;

import net.cravencraft.epicparagliders.EpicParaglidersMod;
import net.cravencraft.epicparagliders.network.ModNet;
import net.cravencraft.epicparagliders.network.SyncActionMsg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import tictim.paraglider.capabilities.ClientPlayerMovement;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.gui.screen.SkillBookScreen;
import yesman.epicfight.client.gui.screen.SkillEditScreen;
import yesman.epicfight.world.item.*;

public class UpdatedClientPlayerMovement extends UpdatedPlayerMovement {

    public static UpdatedClientPlayerMovement instance;
    public ClientPlayerMovement clientPlayerMovement;
    private int skillCheckDelay;

    public UpdatedClientPlayerMovement(ClientPlayerMovement clientPlayerMovement) {
        super(clientPlayerMovement);
        this.clientPlayerMovement = clientPlayerMovement;
        instance = this;
    }

    /**
     * Updates the client side player by doing a few things. If the player is attacking, then the amount of
     * stamina to drain will be calculated. If there is an overlap between a skill being used (e.g., rolling)
     * and an attack (e.g., rolling immediately after an attack to cancel the animation), then that will be
     * accounted for in the total stamina cost so that there isn't any uneven amount of stamina drained.
     */
    @Override
    public void update() {
        if (this.isAttacking){
            this.isAttacking = false;
        }
        else if (ClientEngine.instance.getPlayerPatch().getEntityState().attacking() && !this.isAttacking) {
            this.attackStaminaCost = getAttackStaminaCost(clientPlayerMovement.player.getMainHandItem().getItem());
            this.isAttacking = true;
        }
        else if (this.skillStaminaCost > 0 || this.attackStaminaCost > 0) {
            if (this.attackStaminaCost > this.skillStaminaCost) {
                this.totalActionStaminaCost = this.attackStaminaCost;
                this.attackStaminaCost--;
            }
            else if (this.skillStaminaCost > this.attackStaminaCost) {
                this.totalActionStaminaCost = this.skillStaminaCost;
                this.skillStaminaCost--;
            }
            else {
                this.totalActionStaminaCost = this.attackStaminaCost + this.skillStaminaCost;
                this.skillStaminaCost--;
                this.attackStaminaCost--;
            }
        }
        else {
            this.totalActionStaminaCost = 0;
        }

        updateStamina();
        addEffects();
        setNewSkill();
        ModNet.NET.sendToServer(new SyncActionMsg(this.totalActionStaminaCost, this.setNewSkill));
    }

    /**
     * Sets the new skill to be learned from a skill book. Method needed to ensure that
     * the new skill is transferred over to the new stamina system. a delay of 10 ticks
     * is set to ensure that the skill is set server side.
     */
    private void setNewSkill() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof SkillBookScreen || screen instanceof SkillEditScreen) {
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

    /**
     * Some math done here to determine how much stamina should be consumed from each weapon type.
     * Takes in the attack delay of the weapon as well as its attack strength to create a balanced
     * stamina cost.
     *
     * @param item The weapon that is attacking
     * @return The amount of stamina that should be drained from the attacking weapon
     */
    private int getAttackStaminaCost(Item item) {
        int itemAttackStrDelay = (int) this.clientPlayerMovement.player.getCurrentItemAttackStrengthDelay();
        if (item instanceof SwordItem swordItem) {
            //TODO: may have to account for this as well. This doesn't quite work. Need to find a better way to
            //      figure out the bonus applied to weapons via datapacks.
            if (clientPlayerMovement.player.getMainHandItem().getTag().contains("damage_bonus")) {
                EpicParaglidersMod.LOGGER.info("Damage bonus of weapon: " + clientPlayerMovement.player.getMainHandItem().getTag().getDouble("damage_bonus"));
            }
            if (swordItem instanceof GreatswordItem greatswordItem) {
                EpicParaglidersMod.LOGGER.info("Greatsword item " + greatswordItem.getRegistryName());
                EpicParaglidersMod.LOGGER.info("Tier damage bonus: " + greatswordItem.getTier().getAttackDamageBonus());
                EpicParaglidersMod.LOGGER.info("Attack damage: " + (greatswordItem.getDamage() + 12));
                return (int) (itemAttackStrDelay + (swordItem.getDamage() + 12) / 2);
            }
            else {
                EpicParaglidersMod.LOGGER.info("Sword item: " + swordItem.getRegistryName());
                EpicParaglidersMod.LOGGER.info("Attack damage: " + swordItem.getDamage() + 1);
                return (int) (itemAttackStrDelay + (swordItem.getDamage() + 1));
            }
        }
        else if (item instanceof AxeItem axeItem) {
            EpicParaglidersMod.LOGGER.info("Axe item " + item.getRegistryName());
            EpicParaglidersMod.LOGGER.info("Attack damage: " + axeItem.getAttackDamage() + 1);
            return (int) ((itemAttackStrDelay / 2) + (axeItem.getAttackDamage() + 1));
        }
        else {
            return itemAttackStrDelay;
        }
    }
}