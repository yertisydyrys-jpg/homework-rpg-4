package com.narxoz.rpg;

import com.narxoz.rpg.battle.RaidEngine;
import com.narxoz.rpg.battle.RaidResult;
import com.narxoz.rpg.bridge.AreaSkill;
import com.narxoz.rpg.bridge.FireEffect;
import com.narxoz.rpg.bridge.IceEffect;
import com.narxoz.rpg.bridge.PhysicalEffect;
import com.narxoz.rpg.bridge.ShadowEffect;
import com.narxoz.rpg.bridge.SingleTargetSkill;
import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.EnemyUnit;
import com.narxoz.rpg.composite.HeroUnit;
import com.narxoz.rpg.composite.PartyComposite;
import com.narxoz.rpg.composite.RaidGroup;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Homework 4 Demo: Bridge + Composite ===\n");

        HeroUnit warrior = new HeroUnit("Arthas", 140, 30);
        HeroUnit mage = new HeroUnit("Jaina", 90, 40);
        HeroUnit ranger = new HeroUnit("Sylvanas", 100, 35);
        HeroUnit priest = new HeroUnit("Anduin", 95, 18);

        EnemyUnit goblin = new EnemyUnit("Goblin", 70, 20);
        EnemyUnit orc = new EnemyUnit("Orc", 120, 25);
        EnemyUnit necromancer = new EnemyUnit("Necromancer", 85, 28);
        EnemyUnit imp = new EnemyUnit("Shadow Imp", 60, 16);

        PartyComposite vanguard = new PartyComposite("Vanguard Party");
        vanguard.add(warrior);
        vanguard.add(priest);

        PartyComposite casters = new PartyComposite("Caster Party");
        casters.add(mage);
        casters.add(ranger);

        RaidGroup heroes = new RaidGroup("Alliance Raid");
        heroes.add(vanguard);
        heroes.add(casters);

        PartyComposite frontline = new PartyComposite("Frontline Squad");
        frontline.add(goblin);
        frontline.add(orc);

        PartyComposite shadowCell = new PartyComposite("Shadow Cell");
        shadowCell.add(necromancer);
        shadowCell.add(imp);

        RaidGroup rearGuard = new RaidGroup("Rear Guard");
        rearGuard.add(shadowCell);

        RaidGroup enemies = new RaidGroup("Horde Raid");
        enemies.add(frontline);
        enemies.add(rearGuard);

        System.out.println("--- Team Structures ---");
        heroes.printTree("");
        enemies.printTree("");

        Skill slashFire = new SingleTargetSkill("Slash", 20, new FireEffect());
        Skill slashIce = new SingleTargetSkill("Slash", 20, new IceEffect());
        Skill barrageFire = new AreaSkill("Flame Barrage", 15, new FireEffect());
        Skill shadowNova = new AreaSkill("Shadow Nova", 18, new ShadowEffect());
        Skill strikePhysical = new SingleTargetSkill("Shield Strike", 22, new PhysicalEffect());

        System.out.println("\n--- Bridge Preview ---");
        System.out.println("Same skill, different effects:");
        System.out.println("- " + slashFire.getSkillName() + " -> " + slashFire.getEffectName() + " (damage=" + slashFire.previewDamage() + ")");
        System.out.println("- " + slashIce.getSkillName() + " -> " + slashIce.getEffectName() + " (damage=" + slashIce.previewDamage() + ")");
        System.out.println("Same effect, different skills:");
        System.out.println("- " + slashFire.getSkillName() + " -> " + slashFire.getEffectName() + " (damage=" + slashFire.previewDamage() + ")");
        System.out.println("- " + barrageFire.getSkillName() + " -> " + barrageFire.getEffectName() + " (damage=" + barrageFire.previewDamage() + ")");
        System.out.println("Extra combinations:");
        System.out.println("- " + shadowNova.getSkillName() + " -> " + shadowNova.getEffectName() + " (damage=" + shadowNova.previewDamage() + ")");
        System.out.println("- " + strikePhysical.getSkillName() + " -> " + strikePhysical.getEffectName() + " (damage=" + strikePhysical.previewDamage() + ")");

        RaidEngine engine = new RaidEngine().setRandomSeed(42L);
        RaidResult result = engine.runRaid(heroes, enemies, slashFire, shadowNova);

        System.out.println("\n--- Raid Result ---");
        System.out.println("Winner: " + result.getWinner());
        System.out.println("Rounds: " + result.getRounds());
        for (String line : result.getLog()) {
            System.out.println(line);
        }

        System.out.println("\n=== Demo Complete ===");
    }
}
