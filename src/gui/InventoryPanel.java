package gui;

import battle.GameState;
import items.Items;
import player.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InventoryPanel extends JPanel {
    private GameFrame frame;
    private JPanel charPanel;
    private JPanel statsPanel;

    public InventoryPanel(GameFrame frame) {
        this.frame = frame;
        setBackground(UI.BG_DARK);
        setLayout(new BorderLayout());
        buildHeader();
        buildContent();
    }

    private void buildHeader() {
        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setBackground(UI.BG_CARD);
        header.setBorder(new EmptyBorder(12, 18, 12, 18));

        JButton back = UI.goldButton("← Kembali");
        back.addActionListener(e -> frame.showPanel(GameFrame.MAIN_MENU));

        JLabel title = UI.label("Inventory", UI.GOLD, new Font("Serif", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);
    }

    private void buildContent() {
        JPanel content = new JPanel(new BorderLayout(12, 0));
        content.setBackground(UI.BG_DARK);
        content.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Left: characters
        charPanel = new JPanel();
        charPanel.setLayout(new BoxLayout(charPanel, BoxLayout.Y_AXIS));
        charPanel.setBackground(UI.BG_DARK);

        JScrollPane charScroll = new JScrollPane(charPanel);
        charScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UI.BORDER), "Party Characters",
                0, 0, UI.SMALL, UI.TEXT_DIM));
        charScroll.setBackground(UI.BG_DARK);
        charScroll.getViewport().setBackground(UI.BG_DARK);

        // Right: game stats
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(UI.BG_CARD);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UI.BORDER, 1),
                new EmptyBorder(14, 14, 14, 14)));
        statsPanel.setPreferredSize(new Dimension(220, 0));

        content.add(charScroll, BorderLayout.CENTER);
        content.add(statsPanel, BorderLayout.EAST);
        add(content, BorderLayout.CENTER);
    }

    public void refresh() {
        GameState gs = GameState.getInstance();

        // Characters
        charPanel.removeAll();
        for (Player p : gs.party) {
            charPanel.add(buildCharCard(p));
            charPanel.add(Box.createVerticalStrut(10));
        }
        charPanel.revalidate();
        charPanel.repaint();

        // Stats
        statsPanel.removeAll();
        statsPanel.add(UI.label("Statistik", UI.GOLD, new Font("Serif", Font.BOLD, 15)));
        statsPanel.add(Box.createVerticalStrut(12));
        addStat("Gold", String.valueOf(gs.gold));
        addStat("Exploration P.", String.valueOf(gs.explorationPoint));
        addStat("Stage Cleared", String.valueOf(gs.totalStageClear));
        addStat("Monster Slain", String.valueOf(gs.monsterSlain));
        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private void addStat(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        row.add(UI.label(label, UI.TEXT_DIM, UI.SMALL), BorderLayout.WEST);
        row.add(UI.label(value, UI.GOLD, UI.SMALL), BorderLayout.EAST);
        statsPanel.add(row);
        statsPanel.add(Box.createVerticalStrut(6));
    }

    private JPanel buildCharCard(Player p) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(UI.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UI.BORDER, 1),
                new EmptyBorder(12, 14, 12, 14)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        // Left: name + role + level
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(160, 0));

        Color roleColor = roleColor(p.getTipe());
        JLabel nameLabel = UI.label(p.getNama(), UI.GOLD_LIGHT, new Font("Serif", Font.BOLD, 14));
        JLabel roleLabel = UI.label(p.getTipe(), roleColor, UI.SMALL);
        JLabel levelLabel = UI.label(p.getLevelObj().toString(), UI.TEXT_DIM, UI.SMALL);

        left.add(nameLabel);
        left.add(roleLabel);
        left.add(Box.createVerticalStrut(4));
        left.add(levelLabel);

        // Items owned
        if (!p.getInventory().isEmpty()) {
            left.add(Box.createVerticalStrut(4));
            left.add(UI.label("Items:", UI.TEXT_DIM, UI.SMALL));
            for (Items it : p.getInventory()) {
                left.add(UI.label("  • " + it.getNama(), UI.TEXT_DIM, UI.SMALL));
            }
        }

        // Right: stats grid
        JPanel right = new JPanel(new GridLayout(4, 2, 4, 2));
        right.setOpaque(false);
        right.add(UI.label("HP", UI.TEXT_DIM, UI.SMALL));
        right.add(UI.label((int) p.getMaxHp() + "", UI.TEXT, UI.SMALL));
        right.add(UI.label("MP", UI.TEXT_DIM, UI.SMALL));
        right.add(UI.label((int) p.getMaxMp() + "", UI.TEXT, UI.SMALL));
        right.add(UI.label("ATK", UI.TEXT_DIM, UI.SMALL));
        right.add(UI.label((int) p.getAtkPower() + "", UI.TEXT, UI.SMALL));
        right.add(UI.label("DEF", UI.TEXT_DIM, UI.SMALL));
        right.add(UI.label((int) p.getDefPower() + "", UI.TEXT, UI.SMALL));

        card.add(left, BorderLayout.WEST);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    private Color roleColor(String tipe) {
        switch (tipe) {
            case "Assassin":
                return new Color(224, 128, 128);
            case "Healer":
                return new Color(128, 208, 128);
            case "Swordman":
                return UI.GOLD;
            case "Tank":
                return new Color(128, 160, 224);
            default:
                return UI.TEXT;
        }
    }
}