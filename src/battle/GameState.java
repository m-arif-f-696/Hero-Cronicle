package battle;

import enemy.Enemy;
import items.Items;
import player.*;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private static GameState instance;

    public List<Player> party       = new ArrayList<>();
    public List<Items>  shopItems   = new ArrayList<>();
    public int  gold                = 500;
    public int  explorationPoint    = 0;
    public int  currentStage        = 1;
    public int  totalStageClear     = 0;
    public int  monsterSlain        = 0;
    public int  runs                = 0;

    private GameState() {
        party.add(new Assassin("Raven"));
        party.add(new Healer("Lyria"));
        party.add(new Swordman("Aldric"));
        party.add(new Tank("Gorrath"));

        shopItems.add(new Items("Elixir Kehidupan", "+80 HP untuk semua ally",  120,  0, 80));
        shopItems.add(new Items("Rune Kekuatan",    "+10 ATK permanen",         180, 10));
        shopItems.add(new Items("Vial Mana",        "+50 Max-HP semua ally",    100,  0, 50));
        shopItems.add(new Items("Baju Zirah",       "+30 HP permanen",          150,  0, 30));
        shopItems.add(new Items("Pedang Kuno",      "+15 ATK permanen",         300, 15));
        shopItems.add(new Items("Jimat Pelindung",  "+100 HP permanen",         250,  0,100));
    }

    public static GameState getInstance() {
        if (instance == null) instance = new GameState();
        return instance;
    }

    public void pulihkanParty() {
        for (Player p : party) p.pulihkan();
    }

    public List<Enemy> buatMusuh(String config) {
        List<Enemy> list = new ArrayList<>();
        int idx = 1;
        for (String part : config.split(",")) {
            switch (part.trim().toLowerCase()) {
                case "small": list.add(new enemy.SmallEnemy("Goblin #" + idx++)); break;
                case "large": list.add(new enemy.LargeEnemy("Ogre #"   + idx++)); break;
                case "boss":  list.add(new enemy.BossEnemy("Chaos Lord"));        break;
            }
        }
        return list;
    }
}
