package gui;

import battle.GameState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ExplorationPanel extends JPanel {
    private GameFrame frame;
    private JLabel    epLabel, monsterLabel, runsLabel, goldLabel;
    private JTextArea logArea;

    private static final String[][] ZONES = {
            {"Hutan Berbisik",    "small,small",  "Musuh: Goblin Lemah"},
            {"Dataran Berdarah",  "small,large",  "Musuh: Campuran"},
            {"Gua Abyssal",       "large,large",  "Musuh: Ogre Kuat"},
    };

    public ExplorationPanel(GameFrame frame) {
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

        JLabel title = UI.label("Exploration", UI.GOLD, new Font("Serif", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        goldLabel = UI.label("Gold: 0", UI.GOLD, UI.SMALL);
        header.add(back,      BorderLayout.WEST);
        header.add(title,     BorderLayout.CENTER);
        header.add(goldLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    private void buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(UI.BG_DARK);
        content.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Stats row
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 10, 0));
        statsRow.setOpaque(false);

        epLabel      = UI.label("0",  UI.GOLD_LIGHT, new Font("Serif", Font.BOLD, 26));
        monsterLabel = UI.label("0",  UI.GOLD_LIGHT, new Font("Serif", Font.BOLD, 26));
        runsLabel    = UI.label("0",  UI.GOLD_LIGHT, new Font("Serif", Font.BOLD, 26));

        statsRow.add(statCard("Exploration Points", epLabel));
        statsRow.add(statCard("Monster Dikalahkan", monsterLabel));
        statsRow.add(statCard("Total Ekspedisi",    runsLabel));

        // Zone buttons
        JPanel zonesPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        zonesPanel.setOpaque(false);
        for (String[] zone : ZONES) {
            zonesPanel.add(buildZoneCard(zone[0], zone[1], zone[2]));
        }

        // Log area
        logArea = new JTextArea(5, 40);
        logArea.setEditable(false);
        logArea.setBackground(UI.BG_CARD);
        logArea.setForeground(UI.TEXT_DIM);
        logArea.setFont(UI.MONO);
        logArea.setBorder(new EmptyBorder(8, 12, 8, 12));
        logArea.setText("Belum ada ekspedisi. Pilih zona untuk memulai!");

        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UI.BORDER), "Log Ekspedisi",
                0, 0, UI.SMALL, UI.TEXT_DIM));

        content.add(statsRow,  BorderLayout.NORTH);
        content.add(zonesPanel, BorderLayout.CENTER);
        content.add(logScroll,  BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
    }

    private JPanel statCard(String label, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UI.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UI.BORDER, 1), new EmptyBorder(12, 12, 12, 12)));
        valueLabel.setAlignmentX(CENTER_ALIGNMENT);
        JLabel lbl = UI.label(label, UI.TEXT_DIM, UI.SMALL);
        lbl.setAlignmentX(CENTER_ALIGNMENT);
        card.add(valueLabel);
        card.add(lbl);
        return card;
    }

    private JPanel buildZoneCard(String nama, String config, String desc) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UI.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UI.BORDER, 1), new EmptyBorder(16, 16, 16, 16)));

        JLabel nameLabel = UI.label(nama, UI.GOLD_LIGHT, new Font("Serif", Font.BOLD, 15));
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        JLabel descLabel = UI.label(desc, UI.TEXT_DIM, UI.SMALL);
        descLabel.setAlignmentX(CENTER_ALIGNMENT);

        JButton btn = UI.goldButton("Masuki Zona");
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.addActionListener(e -> {
            logArea.append("\nMemasuki " + nama + "...");
            frame.startBattle(config, "Exploration: " + nama, true, 0, 0);
        });

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(descLabel);
        card.add(Box.createVerticalStrut(14));
        card.add(btn);
        return card;
    }

    public void refresh() {
        GameState gs = GameState.getInstance();
        goldLabel.setText("Gold: " + gs.gold);
        epLabel.setText(String.valueOf(gs.explorationPoint));
        monsterLabel.setText(String.valueOf(gs.monsterSlain));
        runsLabel.setText(String.valueOf(gs.runs));
    }
}