package entity;

public abstract class Entity {
    protected String nama;
    protected String tipe;
    protected double hp;
    protected double maxHp;
    protected double speed;

    public Entity(String nama, String tipe, double hp, double speed) {
        this.nama  = nama;
        this.tipe  = tipe;
        this.hp    = hp;
        this.maxHp = hp;
        this.speed = speed;
    }

    public String getNama()          { return nama; }
    public String getTipe()          { return tipe; }
    public double getHp()            { return hp; }
    public double getMaxHp()         { return maxHp; }
    public double getSpeed()         { return speed; }
    public void   setHp(double hp)   { this.hp = Math.max(0, hp); }
    public boolean isAlive()         { return hp > 0; }

    public abstract String attack(Entity target);
    public abstract String skill(Entity target);
    public abstract String ultimate(Entity target);
}