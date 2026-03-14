package com.narxoz.rpg.bridge;

import com.narxoz.rpg.composite.CombatNode;

public abstract class Skill {
    private final String skillName;
    private final int basePower;
    private final EffectImplementor effect;

    protected Skill(String skillName, int basePower, EffectImplementor effect) {
        if (skillName == null || skillName.isBlank()) {
            throw new IllegalArgumentException("Skill name must not be blank");
        }
        if (basePower < 0) {
            throw new IllegalArgumentException("Base power must not be negative");
        }
        if (effect == null) {
            throw new IllegalArgumentException("Effect implementor must not be null");
        }
        this.skillName = skillName;
        this.basePower = basePower;
        this.effect = effect;
    }

    public String getSkillName() {
        return skillName;
    }

    public int getBasePower() {
        return basePower;
    }

    public String getEffectName() {
        return effect.getEffectName();
    }

    public int previewDamage() {
        return resolvedDamage();
    }

    protected int resolvedDamage() {
        return effect.computeDamage(basePower);
    }

    public abstract void cast(CombatNode target);
}
