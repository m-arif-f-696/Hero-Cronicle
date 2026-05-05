package enemy;

import entity.Entity;

public class BossEnemy extends Enemy {

    public BossEnemy(String nama) {
        super(nama, "Boss Enemy", 500, 35, 20, 150, 200);
    }

    @Override
    public String attack(Entity target) {
        double dmg = hitungDamage(atkPower, target);
        target.setHp(target.getHp() - dmg);
        return nama + " [BOSS] — Dark Claw! (" + (int)dmg + " DMG)";
    }

    @Override
    public String skill(Entity target) {
        double dmg = hitungDamage(atkPower * 2.0, target);
        target.setHp(target.getHp() - dmg);
        return nama + " [BOSS] — Soul Drain! (" + (int)dmg + " DMG)";
    }

    @Override
    public String ultimate(Entity target) {
        double dmg = hitungDamage(atkPower * 3.5, target);
        target.setHp(target.getHp() - dmg);
        return "⚠ " + nama + " [BOSS] — ★ CHAOS NOVA! (" + (int)dmg + " DMG ULTIMATE!)";
    }
}