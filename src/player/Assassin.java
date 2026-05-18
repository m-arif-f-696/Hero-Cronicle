package player;

import entity.Entity;

public class Assassin extends Player {

    public Assassin(String nama) {
        super(nama, "Assassin", 180, 120, 35, 12, 90);
    }

    @Override
    public String attack(Entity target) {
        double dmg = hitungDamage(atkPower + 5);
        target.setHp(target.getHp() - dmg);
        return nama + " [Assassin] menyerang " + target.getNama() + " — Shadow Strike! (" + (int)dmg + " DMG)";
    }

    @Override
    public String skill(Entity target) {
        if (mp < 20) return nama + " tidak cukup MP untuk Skill!";
        mp -= 20;
        double dmg = hitungDamage(atkPower * 1.8);
        target.setHp(target.getHp() - dmg);
        return nama + " [Assassin] — Blade Rush! (" + (int)dmg + " DMG) [-20 MP]";
    }

    @Override
    public String ultimate(Entity target) {
        if (mp < 50) return nama + " tidak cukup MP untuk Ultimate!";
        mp -= 50;
        double dmg = hitungDamage(atkPower * 3.0);
        target.setHp(target.getHp() - dmg);
        return nama + " [Assassin] — ★ DEATH MARK! (" + (int)dmg + " DMG KRITIS!) [-50 MP]";
    }
}