package gui;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private static GameFrame instance;
    private JPanel            mainContainer;
    private CardLayout        cardLayout;

    public static final String MAIN_MENU   = "MAIN_MENU";
    public static final String CAMPAIGN    = "CAMPAIGN";
    public static final String BATTLE      = "BATTLE";
    public static final String EXPLORATION = "EXPLORATION";
    public static final String SHOP        = "SHOP";
    public static final String INVENTORY   = "INVENTORY";

    // panels
    private MainMenuPanel    mainMenuPanel;
    private CampaignPanel    campaignPanel;
    private BattlePanel      battlePanel;
    private ExplorationPanel explorationPanel;
    private ShopPanel        shopPanel;
    private InventoryPanel   inventoryPanel;

    public GameFrame() {
        instance = this;
        setTitle("Hero's Chronicle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout     = new CardLayout();
        mainContainer  = new JPanel(cardLayout);
        mainContainer.setBackground(Color.BLACK);

        mainMenuPanel    = new MainMenuPanel(this);
        campaignPanel    = new CampaignPanel(this);
        battlePanel      = new BattlePanel(this);
        explorationPanel = new ExplorationPanel(this);
        shopPanel        = new ShopPanel(this);
        inventoryPanel   = new InventoryPanel(this);

        mainContainer.add(mainMenuPanel,    MAIN_MENU);
        mainContainer.add(campaignPanel,    CAMPAIGN);
        mainContainer.add(battlePanel,      BATTLE);
        mainContainer.add(explorationPanel, EXPLORATION);
        mainContainer.add(shopPanel,        SHOP);
        mainContainer.add(inventoryPanel,   INVENTORY);

        add(mainContainer);
        showPanel(MAIN_MENU);
        setVisible(true);
    }

    public void showPanel(String name) {
        switch (name) {
            case CAMPAIGN:    campaignPanel.refresh();    break;
            case EXPLORATION: explorationPanel.refresh(); break;
            case SHOP:        shopPanel.refresh();        break;
            case INVENTORY:   inventoryPanel.refresh();   break;
            case MAIN_MENU:   mainMenuPanel.refresh();    break;
        }
        cardLayout.show(mainContainer, name);
    }

    public void startBattle(String enemyConfig, String stageLabel, boolean isExplore, int epReward, int goldReward) {
        battlePanel.initBattle(enemyConfig, stageLabel, isExplore, epReward, goldReward);
        showPanel(BATTLE);
    }

    public static GameFrame getInstance() { return instance; }
}