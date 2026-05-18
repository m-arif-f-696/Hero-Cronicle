package player;

import entity.Entity;

public class Swordman extends Player {

    public Swordman(String nama) {
        super(nama, "Swordman", 240, 90, 28, 20, 65);
    }

    @Override
    public String attack(Entity target) {
        double dmg = hitungDamage(atkPower + 8);
        target.setHp(target.getHp() - dmg);
        return nama + " [Swordman] — Quick Slash! (" + (int)dmg + " DMG)";
    }

    @Override
    public String skill(Entity target) {
        if (mp < 20) return nama + " tidak cukup MP untuk Skill!";
        mp -= 20;
        double dmg = hitungDamage(atkPower * 2.0);
        target.setHp(target.getHp() - dmg);
        return nama + " [Swordman] — Blade Dance! (" + (int)dmg + " DMG) [-20 MP]";
    }

    @Override
    public String ultimate(Entity target) {
        if (mp < 50) return nama + " tidak cukup MP untuk Ultimate!";
        mp -= 50;
        double dmg = hitungDamage(atkPower * 3.5);
        target.setHp(target.getHp() - dmg);
        return nama + " [Swordman] — ★ TEMPEST SLASH! (" + (int)dmg + " DMG) [-50 MP]";
    }
}