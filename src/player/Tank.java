package player;

import enemy.Enemy;
import entity.Entity;
import java.util.List;

public class Tank extends Player {

    public Tank(String nama) {
        super(nama, "Tank", 380, 70, 18, 35);
    }

    @Override
    public String attack(Entity target) {
        double dmg = hitungDamage(atkPower);
        target.setHp(target.getHp() - dmg);
        return nama + " [Tank] — Shield Bash! (" + (int)dmg + " DMG)";
    }

    @Override
    public String skill(Entity target) {
        if (mp < 20) return nama + " tidak cukup MP untuk Skill!";
        mp -= 20;
        defPower += 15;
        return nama + " [Tank] — Iron Fortress! DEF +" + 15 + " (Total: " + (int)defPower + ") [-20 MP]";
    }

    @Override
    public String ultimate(Entity target) {
        if (mp < 50) return nama + " tidak cukup MP untuk Ultimate!";
        mp -= 50;
        double dmg = hitungDamage(atkPower * 2.0);
        target.setHp(target.getHp() - dmg);
        return nama + " [Tank] — ★ EARTHBREAKER! (" + (int)dmg + " DMG) [-50 MP]";
    }

    public String ultimateSemuaMusuh(List<Enemy> enemies) {
        if (mp < 50) return nama + " tidak cukup MP untuk Ultimate!";
        mp -= 50;
        double dmg = hitungDamage(atkPower * 1.5);
        StringBuilder sb = new StringBuilder(nama + " [Tank] — ★ EARTHBREAKER ke semua musuh!\n");
        for (Enemy e : enemies) {
            if (e.isAlive()) {
                e.setHp(e.getHp() - dmg);
                sb.append("  → ").append(e.getNama()).append(" terkena ").append((int)dmg).append(" DMG\n");
            }
        }
        return sb.toString().trim();
    }
}