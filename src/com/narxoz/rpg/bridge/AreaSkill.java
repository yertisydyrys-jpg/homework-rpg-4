package com.narxoz.rpg.bridge;

import com.narxoz.rpg.composite.CombatNode;

public class AreaSkill extends Skill {
    public AreaSkill(String skillName, int basePower, EffectImplementor effect) {
        super(skillName, basePower, effect);
    }

    @Override
    public void cast(CombatNode target) {
        if (target == null || !target.isAlive()) {
            return;
        }
        applyToAliveLeaves(target, resolvedDamage());
    }

    private void applyToAliveLeaves(CombatNode node, int damage) {
        if (node == null || !node.isAlive()) {
            return;
        }

        if (node.getChildren().isEmpty()) {
            node.takeDamage(damage);
            return;
        }

        for (CombatNode child : node.getChildren()) {
            applyToAliveLeaves(child, damage);
        }
    }
}
