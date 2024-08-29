package com.cravencraft.epicparagliders.capabilities;

public interface StaminaOverride {


    /**
     * Interface for ServerBotWStaminaMixin and BotWStaminaMixin
     */
    void setTotalActionStaminaCostServerSide(int totalActionStaminaCost);
    void setActionStaminaCost(int attackStaminaCost);
    void attacking(boolean isAttacking);
    void performingAction(boolean isPerformingAction);
    boolean isPerformingAction();
    boolean isAttacking();
    int getTotalActionStaminaCost();
    void setTotalActionStaminaCost(int totalActionStaminaCost);
}