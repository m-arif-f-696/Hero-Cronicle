package gui;

import battle.GameState;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private GameFrame frame;
    private JLabel    goldLabel;
    private JLabel    epLabel;

    public MainMenuPanel(GameFrame frame) {
        this.frame = frame;
        setBackground(UI.BG_DARK);
        setLayout(new GridBagLayout());
        build();
    }

    private void build() {
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // Title
        JLabel title = UI.label("⚔  Hero's Chronicle  ⚔", UI.GOLD_LIGHT, new Font("Serif", Font.BOLD, 32));
        title.setAlignmentX(CENTER_ALIGNMENT);
        JLabel sub   = UI.label("Chronicles of the Fallen Realm", UI.TEXT_DIM, new Font("Serif", Font.ITALIC, 14));
        sub.setAlignmentX(CENTER_ALIGNMENT);

        center.add(title);
        center.add(Box.createVerticalStrut(6));
        center.add(sub);
        center.add(Box.createVerticalStrut(30));

        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(UI.GOLD);
        sep.setMaximumSize(new Dimension(280, 1));
        center.add(sep);
        center.add(Box.createVerticalStrut(24));

        // Buttons
        String[] labels = {"⚔   Campaign", "🗺   Exploration", "🛒   Shop", "📦   Inventory"};
        String[] panels = {GameFrame.CAMPAIGN, GameFrame.EXPLORATION, GameFrame.SHOP, GameFrame.INVENTORY};
        for (int i = 0; i < labels.length; i++) {
            final String target = panels[i];
            JButton btn = UI.goldButton(labels[i]);
            btn.setAlignmentX(CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(260, 44));
            btn.setPreferredSize(new Dimension(260, 44));
            btn.addActionListener(e -> frame.showPanel(target));
            center.add(btn);
            center.add(Box.createVerticalStrut(10));
        }

        center.add(Box.createVerticalStrut(20));

        // Stats row
        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        statsRow.setOpaque(false);
        goldLabel = UI.label("Gold: 500", UI.GOLD, UI.SMALL);
        epLabel   = UI.label("EP: 0",    UI.EXP_COLOR, UI.SMALL);
        statsRow.add(goldLabel);
        statsRow.add(UI.label("|", UI.TEXT_DIM, UI.SMALL));
        statsRow.add(epLabel);
        center.add(statsRow);

        add(center);
    }

    public void refresh() {
        GameState gs = GameState.getInstance();
        goldLabel.setText("Gold: " + gs.gold);
        epLabel.setText("EP: " + gs.explorationPoint);
    }
}