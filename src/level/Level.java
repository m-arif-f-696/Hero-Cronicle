package level;

public class Level {
    private int level;
    private int exp;
    private double multiplier;

    public Level() {
        this.level      = 1;
        this.exp        = 0;
        this.multiplier = 1.0;
    }

    public void addExp(int tambahan) {
        this.exp += tambahan;
        while (this.exp >= getExpNeeded()) {
            this.exp -= getExpNeeded();
            upgradeLevel();
        }
    }

    public void upgradeLevel() {
        this.level++;
        this.multiplier += 0.1;
    }

    public int    getExpNeeded()  { return level * 50; }
    public double getMultiplier() { return multiplier; }
    public int    getLevel()      { return level; }
    public int    getExp()        { return exp; }

    @Override
    public String toString() {
        return "Lv." + level + "  (EXP: " + exp + "/" + getExpNeeded() + ")";
    }
}