package net.cravencraft.epicparagliders.capabilities;

public interface StaminaOverride {


    /**
     * TODO: Maybe test separating these into their own separate interfaces
     *       if this becomes too large?
     * ServerPlayerMovement
     */
    void setTotalActionStaminaCostServerSide(int totalActionStaminaCost);
    void setActionStaminaCost(int attackStaminaCost);
    void attacking(boolean isAttacking);
    void performingAction(boolean isPerformingAction);
    boolean isPerformingAction();
    boolean isAttacking();

    /**
     * PlayerMovement
     */
    int getTotalActionStaminaCost();
    void setTotalActionStaminaCost(int totalActionStaminaCost);
}