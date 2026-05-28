package items;

public class Items {
    private String nama;
    private String deskripsi;
    private double price;
    private double damage;
    private double health;

    public Items(String nama, String deskripsi, double price, double damage) {
        this(nama, deskripsi, price, damage, 0);
    }

    public Items(String nama, String deskripsi, double price, double damage, double health) {
        this.nama      = nama;
        this.deskripsi = deskripsi;
        this.price     = price;
        this.damage    = damage;
        this.health    = health;
    }

    public void addDamage(double d) { this.damage += d; }
    public void addHealth()         { /* placeholder sesuai UML */ }

    public String getNama()      { return nama; }
    public String getDeskripsi() { return deskripsi; }
    public double getPrice()     { return price; }
    public double getDamage()    { return damage; }
    public double getHealth()    { return health; }

    @Override
    public String toString() {
        return nama + "  |  +" + (int)damage + " ATK  +" + (int)health + " HP  |  " + (int)price + " Gold  —  " + deskripsi;
    }
}
