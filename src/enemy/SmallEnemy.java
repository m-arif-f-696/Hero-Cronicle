package enemy;

import entity.Entity;
import sound.Sound;

public class SmallEnemy extends Enemy {

    public SmallEnemy(String nama) {
        super(nama, "Small Enemy", 80, 12, 6, 20, 30, 75);
    }

    @Override
    public String attack(Entity target) {
        double dmg = hitungDamage(atkPower, target);
        target.setHp(target.getHp() - dmg);
        playSFX(Sound.ENEMY_ATTACK);
        return nama + " [Small] menyerang " + target.getNama() + "! (" + (int)dmg + " DMG)";
    }

    @Override
    public String skill(Entity target) {
        double dmg = hitungDamage(atkPower * 1.5, target);
        target.setHp(target.getHp() - dmg);
        playSFX(Sound.ENEMY_ATTACK);
        return nama + " [Small] — Swarm Attack! (" + (int)dmg + " DMG)";
    }

    @Override
    public String ultimate(Entity target) {
        double dmg = hitungDamage(atkPower * 2.0, target);
        target.setHp(target.getHp() - dmg);
        playSFX(Sound.ENEMY_ATTACK);
        return nama + " [Small] — Frenzy Bite! (" + (int)dmg + " DMG)";
    }
}