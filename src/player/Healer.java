package player;

import entity.Entity;
import sound.Sound;

import java.util.List;

public class Healer extends Player {

    public Healer(String nama) {
        super(nama, "Healer", 200, 160, 12, 16, 45);
    }

    @Override
    public String attack(Entity target) {
        double dmg = hitungDamage(atkPower);
        target.setHp(target.getHp() - dmg);
        playSFX(Sound.PLAYER_ATTACK);
        return nama + " [Healer] — Holy Light! (" + (int)dmg + " DMG)";
    }

    @Override
    public String skill(Entity target) {
        if (mp < 20) return nama + " tidak cukup MP untuk Skill!";
        mp -= 20;
        if (target instanceof Player) {
            Player ally = (Player) target;
            double heal = hitungDamage(50);
            ally.setHp(Math.min(ally.getMaxHp(), ally.getHp() + heal));
            playSFX(Sound.HEALER_SKILL);
            return nama + " [Healer] — Holy Mend ke " + ally.getNama() + "! (+" + (int)heal + " HP) [-20 MP]";
        }
        return nama + " tidak bisa menyembuhkan musuh!";
    }

    @Override
    public String ultimate(Entity target) {
        if (mp < 50) return nama + " tidak cukup MP untuk Ultimate!";
        mp -= 50;
        playSFX(Sound.HEALER_SKILL);
        return nama + " [Healer] — Divine Shield aktif! [-50 MP]";
    }

    public String ultimateParty(List<Player> party) {
        if (mp < 50) return nama + " tidak cukup MP untuk Ultimate!";
        mp -= 50;
        double heal = hitungDamage(80);
        StringBuilder sb = new StringBuilder(nama + " [Healer] — ★ DIVINE SHIELD! Party dipulihkan:\n");
        for (Player ally : party) {
            if (ally.isAlive()) {
                ally.setHp(Math.min(ally.getMaxHp(), ally.getHp() + heal));
                sb.append("  → ").append(ally.getNama()).append(" +").append((int)heal).append(" HP\n");
            }
        }
        playSFX(Sound.HEALER_SKILL);
        return sb.toString().trim();
    }
}