package gui;

import battle.GameState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CampaignPanel extends JPanel {
    private GameFrame frame;
    private JLabel    epLabel;
    private JLabel    goldLabel;
    private JPanel    stageGrid;

    private static final Object[][] STAGES = {
            // {id, nama, enemyConfig, epReward, goldReward, epReq}
            {1, "Hutan Kelam",       "small,small",       30,  80,   0},
            {2, "Rawa Terkutuk",     "small,large",       50,  120,  30},
            {3, "Kastil Reruntuhan", "large,large",       80,  180,  80},
            {4, "Puncak Bayangan",   "large,large,small", 120, 250, 160},
            {5, "Gerbang Infernal",  "boss",              200, 400, 280},
            {6, "Singgasana Chaos",  "boss,large",        350, 600, 480},
    };

    public CampaignPanel(GameFrame frame) {
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

        JLabel title = UI.label("Campaign", UI.GOLD, new Font("Serif", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        epLabel   = UI.label("EP: 0",    UI.EXP_COLOR, UI.SMALL);
        goldLabel = UI.label("Gold: 500", UI.GOLD,     UI.SMALL);
        right.add(epLabel); right.add(goldLabel);

        header.add(back,  BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        header.add(right, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    private void buildContent() {
        stageGrid = new JPanel(new GridLayout(2, 3, 12, 12));
        stageGrid.setBackground(UI.BG_DARK);
        stageGrid.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(new JScrollPane(stageGrid) {{
            setBackground(UI.BG_DARK);
            getViewport().setBackground(UI.BG_DARK);
            setBorder(null);
        }}, BorderLayout.CENTER);
    }

    public void refresh() {
        GameState gs = GameState.getInstance();
        epLabel.setText("EP: " + gs.explorationPoint);
        goldLabel.setText("Gold: " + gs.gold);

        stageGrid.removeAll();
        for (Object[] s : STAGES) {
            int    id      = (int) s[0];
            String nama    = (String) s[1];
            String config  = (String) s[2];
            int    epRew   = (int) s[3];
            int    goldRew = (int) s[4];
            int    epReq   = (int) s[5];
            boolean unlocked = gs.explorationPoint >= epReq;
            boolean cleared  = gs.currentStage > id;

            stageGrid.add(buildStageCard(id, nama, config, epRew, goldRew, epReq, unlocked, cleared));
        }
        stageGrid.revalidate();
        stageGrid.repaint();
    }

    private JPanel buildStageCard(int id, String nama, String config, int epRew, int goldRew,
                                  int epReq, boolean unlocked, boolean cleared) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UI.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cleared ? UI.EXP_COLOR : unlocked ? UI.BORDER : new Color(80,80,80,80), 1),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel numLabel = UI.label("Stage " + id, cleared ? UI.EXP_COLOR : unlocked ? UI.GOLD_LIGHT : UI.TEXT_DIM,
                new Font("Serif", Font.BOLD, 18));
        numLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel nameLabel = UI.label(nama, UI.TEXT_DIM, UI.SMALL);
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel statusLabel = UI.label(
                cleared ? "✓ Selesai" : unlocked ? "Tersedia" : "🔒 Butuh " + epReq + " EP",
                cleared ? UI.EXP_COLOR : unlocked ? UI.TEXT : UI.TEXT_DIM, UI.SMALL);
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel rewardLabel = UI.label("+" + epRew + " EP  |  " + goldRew + " Gold", UI.GOLD, UI.SMALL);
        rewardLabel.setAlignmentX(CENTER_ALIGNMENT);

        card.add(numLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(statusLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(rewardLabel);

        if (unlocked && !cleared) {
            card.add(Box.createVerticalStrut(10));
            JButton mulaiBtn = UI.goldButton("Mulai");
            mulaiBtn.setAlignmentX(CENTER_ALIGNMENT);
            mulaiBtn.addActionListener(e ->
                    frame.startBattle(config, "Stage " + id + " — " + nama, false, epRew, goldRew));
            card.add(mulaiBtn);
        }

        return card;
    }
}