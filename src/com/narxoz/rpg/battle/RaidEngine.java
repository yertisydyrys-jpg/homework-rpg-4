package com.narxoz.rpg.battle;

import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.CombatNode;

import java.util.Random;

public class RaidEngine {
    private static final int MAX_ROUNDS = 50;

    private Random random = new Random(1L);

    public RaidEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public RaidResult runRaid(CombatNode teamA, CombatNode teamB, Skill teamASkill, Skill teamBSkill) {
        validateInputs(teamA, teamB, teamASkill, teamBSkill);

        RaidResult result = new RaidResult();

        if (!teamA.isAlive() && !teamB.isAlive()) {
            result.setWinner("Draw");
            result.setRounds(0);
            result.addLine("Both teams are already defeated.");
            return result;
        }
        if (!teamA.isAlive()) {
            result.setWinner(teamB.getName());
            result.setRounds(0);
            result.addLine(teamA.getName() + " is already defeated.");
            return result;
        }
        if (!teamB.isAlive()) {
            result.setWinner(teamA.getName());
            result.setRounds(0);
            result.addLine(teamB.getName() + " is already defeated.");
            return result;
        }

        result.addLine("Raid started: " + teamA.getName() + " vs " + teamB.getName());
        result.addLine(teamA.getName() + " uses " + describeSkill(teamASkill) + "; " + teamB.getName() + " uses " + describeSkill(teamBSkill));

        int rounds = 0;
        while (teamA.isAlive() && teamB.isAlive() && rounds < MAX_ROUNDS) {
            rounds++;
            result.addLine("");
            result.addLine("Round " + rounds);

            executeTurn(teamA, teamB, teamASkill, result);
            if (!teamB.isAlive()) {
                break;
            }

            executeTurn(teamB, teamA, teamBSkill, result);
        }

        result.setRounds(rounds);
        if (teamA.isAlive() && !teamB.isAlive()) {
            result.setWinner(teamA.getName());
        } else if (teamB.isAlive() && !teamA.isAlive()) {
            result.setWinner(teamB.getName());
        } else {
            result.setWinner("Draw");
            result.addLine("Maximum rounds reached.");
        }

        result.addLine("");
        result.addLine("Final HP -> " + teamA.getName() + ": " + teamA.getHealth() + ", " + teamB.getName() + ": " + teamB.getHealth());
        return result;
    }

    private void executeTurn(CombatNode attacker, CombatNode defender, Skill skill, RaidResult result) {
        if (!attacker.isAlive()) {
            result.addLine(attacker.getName() + " cannot act because it is defeated.");
            return;
        }
        if (!defender.isAlive()) {
            result.addLine(defender.getName() + " is already defeated.");
            return;
        }

        int beforeHealth = defender.getHealth();
        int attackPressure = attacker.getAttackPower();
        int aliveLeaves = countAliveLeaves(defender);
        boolean luckyStrike = random.nextInt(100) < 15;

        skill.cast(defender);
        if (luckyStrike && defender.isAlive()) {
            int bonusDamage = Math.max(1, attackPressure / 10);
            defender.takeDamage(bonusDamage);
        }

        int dealtDamage = Math.max(0, beforeHealth - defender.getHealth());
        StringBuilder line = new StringBuilder();
        line.append(attacker.getName())
                .append(" casts ")
                .append(skill.getSkillName())
                .append(" [")
                .append(skill.getEffectName())
                .append("] on ")
                .append(defender.getName())
                .append(" (targets=")
                .append(aliveLeaves)
                .append(", pressure=")
                .append(attackPressure)
                .append(") -> ")
                .append(dealtDamage)
                .append(" damage");

        if (luckyStrike) {
            line.append(" with lucky strike");
        }
        result.addLine(line.toString());
        result.addLine("Remaining HP of " + defender.getName() + ": " + defender.getHealth());
    }

    private int countAliveLeaves(CombatNode node) {
        if (node == null || !node.isAlive()) {
            return 0;
        }
        if (node.getChildren().isEmpty()) {
            return 1;
        }

        int count = 0;
        for (CombatNode child : node.getChildren()) {
            count += countAliveLeaves(child);
        }
        return count;
    }

    private String describeSkill(Skill skill) {
        return skill.getSkillName() + " [" + skill.getEffectName() + ", dmg=" + skill.previewDamage() + "]";
    }

    private void validateInputs(CombatNode teamA, CombatNode teamB, Skill teamASkill, Skill teamBSkill) {
        if (teamA == null || teamB == null) {
            throw new IllegalArgumentException("Teams must not be null");
        }
        if (teamASkill == null || teamBSkill == null) {
            throw new IllegalArgumentException("Skills must not be null");
        }
    }
}
