package player;

import entity.Entity;
import items.Items;
import level.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class Player extends Entity {
    protected double mp;
    protected double maxMp;
    protected double atkPower;
    protected double defPower;
    protected Level  levelObj;
    protected List<Items> inventory;

    public Player(String nama, String tipe, double hp, double mp, double atkPower, double defPower, int speed) {
        super(nama, tipe, hp, speed);
        this.mp        = mp;
        this.maxMp     = mp;
        this.atkPower  = atkPower;
        this.defPower  = defPower;
        this.levelObj  = new Level();
        this.inventory = new ArrayList<>();
    }

    protected double hitungDamage(double base) {
        return base * levelObj.getMultiplier();
    }

    public void tambahItem(Items item) {
        inventory.add(item);
        this.atkPower += item.getDamage();
        this.maxHp    += item.getHealth();
        this.hp       += item.getHealth();
    }

    public void addExp(int exp)    { levelObj.addExp(exp); }

    public void pulihkan() {
        this.hp = maxHp;
        this.mp = maxMp;
    }

    public double    getMp()        { return mp; }
    public double    getMaxMp()     { return maxMp; }
    public double    getAtkPower()  { return atkPower; }
    public double    getDefPower()  { return defPower; }
    public Level     getLevelObj()  { return levelObj; }
    public List<Items> getInventory() { return inventory; }

    @Override public abstract String attack(Entity target);
    @Override public abstract String skill(Entity target);
    @Override public abstract String ultimate(Entity target);
}