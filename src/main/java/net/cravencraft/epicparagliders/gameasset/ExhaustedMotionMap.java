package net.cravencraft.epicparagliders.gameasset;

import yesman.epicfight.api.animation.LivingMotions;

public enum ExhaustedMotionMap {
    IDLE(LivingMotions.IDLE, ExhaustionMotions.EXHAUSTED_IDLE),
    WALK(LivingMotions.WALK, ExhaustionMotions.EXHAUSTED_WALK);

   private final LivingMotions livingMotion;
   private final ExhaustionMotions exhaustionMotion;

    ExhaustedMotionMap(LivingMotions livingMotion, ExhaustionMotions exhaustionMotion) {
        this.livingMotion = livingMotion;
        this.exhaustionMotion = exhaustionMotion;
    }

//    public ExhaustedMotionMap getByLivingMotion(LivingMotions livingMotions) {}

    public LivingMotions getLivingMotions() {
        return this.livingMotion;
    }

    public ExhaustionMotions getExhaustionMotion() {
        return this.exhaustionMotion;
    }
}
