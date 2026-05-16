package enemy;

import entity.Entity;

public class LargeEnemy extends Enemy {

    public LargeEnemy(String nama) {
        super(nama, "Large Enemy", 220, 22, 15, 50, 70, 50);
    }

    @Override
    public String attack(Entity target) {
        double dmg = hitungDamage(atkPower, target);
        target.setHp(target.getHp() - dmg);
        return nama + " [Large] — Brute Slam! (" + (int)dmg + " DMG)";
    }

    @Override
    public String skill(Entity target) {
        double dmg = hitungDamage(atkPower * 1.8, target);
        target.setHp(target.getHp() - dmg);
        return nama + " [Large] — Ground Smash! (" + (int)dmg + " DMG)";
    }

    @Override
    public String ultimate(Entity target) {
        double dmg = hitungDamage(atkPower * 2.5, target);
        target.setHp(target.getHp() - dmg);
        return nama + " [Large] — ★ EARTHQUAKE! (" + (int)dmg + " DMG BESAR!)";
    }
}