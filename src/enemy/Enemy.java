package enemy;

import entity.Entity;
import player.Player;

public abstract class Enemy extends Entity {
    protected double atkPower;
    protected double defPower;
    protected int    expReward;
    protected int    goldReward;

    public Enemy(String nama, String tipe, double hp, double atkPower, double defPower, int expReward, int goldReward) {
        super(nama, tipe, hp);
        this.atkPower   = atkPower;
        this.defPower   = defPower;
        this.expReward  = expReward;
        this.goldReward = goldReward;
    }

    protected double hitungDamage(double base, Entity target) {
        double def = (target instanceof Player) ? ((Player) target).getDefPower() : 0;
        return Math.max(5, base - def * 0.3);
    }

    public int    getExpReward()  { return expReward; }
    public int    getGoldReward() { return goldReward; }
    public double getAtkPower()   { return atkPower; }
    public double getDefPower()   { return defPower; }

    @Override public abstract String attack(Entity target);
    @Override public abstract String skill(Entity target);
    @Override public abstract String ultimate(Entity target);
}