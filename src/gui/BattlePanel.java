package gui;

import battle.GameState;
import enemy.Enemy;
import player.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class BattlePanel extends JPanel {
    private GameFrame   frame;
    private GameState   gs;

    // Battle state
    private List<Enemy>  enemies;
    private List<Player> party;
    private boolean      isExplore;
    private int          epReward;
    private int          goldReward;
    private String       stageLabel;
    private int          activeCharIdx = 0;
    private String       pendingAction = null;
    private Enemy        selectedTarget = null;

    // UI components
    private JLabel    titleLabel;
    private JLabel    turnLabel;
    private JPanel    enemyZone;
    private JPanel    partyZone;
    private JTextArea battleLog;
    private JButton   btnAttack;
    private JButton   btnSkill;
    private JButton   btnUltimate;

    public BattlePanel(GameFrame frame) {
        this.frame = frame;
        this.gs    = GameState.getInstance();
        setBackground(UI.BG_DARK);
        setLayout(new BorderLayout(0, 0));
        buildHeader();
        buildArena();
        buildActions();
    }

    // ── HEADER ───────────────────────────────────────────
    private void buildHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(UI.BG_CARD);
        header.setBorder(new EmptyBorder(10, 16, 10, 16));

        titleLabel = UI.label("Battle", UI.GOLD, new Font("Serif", Font.BOLD, 18));
        turnLabel  = UI.label("Giliran Anda", UI.TEXT_DIM, UI.SMALL);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(turnLabel,  BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    // ── ARENA ────────────────────────────────────────────
    private void buildArena() {
        JPanel arena = new JPanel(new BorderLayout(0, 8));
        arena.setBackground(UI.BG_DARK);
        arena.setBorder(new EmptyBorder(12, 12, 0, 12));

        // Enemy zone
        JPanel enemySection = new JPanel(new BorderLayout());
        enemySection.setOpaque(false);
        enemySection.add(UI.label("MUSUH", UI.TEXT_DIM, new Font("SansSerif", Font.BOLD, 10)), BorderLayout.NORTH);
        enemyZone = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        enemyZone.setBackground(UI.BG_CARD);
        enemyZone.setBorder(BorderFactory.createLineBorder(UI.BORDER, 1));
        enemySection.add(enemyZone, BorderLayout.CENTER);

        // Log
        battleLog = new JTextArea(5, 40);
        battleLog.setEditable(false);
        battleLog.setBackground(UI.BG_CARD);
        battleLog.setForeground(UI.TEXT_DIM);
        battleLog.setFont(UI.MONO);
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        battleLog.setBorder(new EmptyBorder(6, 10, 6, 10));
        JScrollPane logScroll = new JScrollPane(battleLog);
        logScroll.setBorder(BorderFactory.createLineBorder(UI.BORDER, 1));
        logScroll.setPreferredSize(new Dimension(0, 110));

        // Party zone
        JPanel partySection = new JPanel(new BorderLayout());
        partySection.setOpaque(false);
        partySection.add(UI.label("PARTY ANDA", UI.TEXT_DIM, new Font("SansSerif", Font.BOLD, 10)), BorderLayout.NORTH);
        partyZone = new JPanel(new GridLayout(1, 4, 8, 0));
        partyZone.setBackground(UI.BG_CARD);
        partyZone.setBorder(BorderFactory.createLineBorder(UI.BORDER, 1));
        partySection.add(partyZone, BorderLayout.CENTER);

        arena.add(enemySection, BorderLayout.NORTH);
        arena.add(logScroll,    BorderLayout.CENTER);
        arena.add(partySection, BorderLayout.SOUTH);
        add(arena, BorderLayout.CENTER);
    }

    // ── ACTIONS ──────────────────────────────────────────
    private void buildActions() {
        JPanel actions = new JPanel(new GridLayout(1, 3, 10, 0));
        actions.setBackground(UI.BG_CARD);
        actions.setBorder(new EmptyBorder(10, 16, 10, 16));

        btnAttack   = UI.actionButton("<html><center>⚔ Attack<br><font size='2'>Basic strike</font></center></html>",
                UI.RED_LIGHT, new Color(200,64,64,120));
        btnSkill    = UI.actionButton("<html><center>✦ Skill<br><font size='2'>Costs 20 MP</font></center></html>",
                UI.BLUE_LIGHT, new Color(64,128,224,120));
        btnUltimate = UI.actionButton("<html><center>◈ Ultimate<br><font size='2'>Costs 50 MP</font></center></html>",
                UI.PURPLE, new Color(107,79,160,120));

        btnAttack.addActionListener(e   -> playerAction("attack"));
        btnSkill.addActionListener(e    -> playerAction("skill"));
        btnUltimate.addActionListener(e -> playerAction("ultimate"));

        actions.add(btnAttack);
        actions.add(btnSkill);
        actions.add(btnUltimate);
        add(actions, BorderLayout.SOUTH);
    }

    // ── INIT BATTLE ──────────────────────────────────────
    public void initBattle(String enemyConfig, String label, boolean explore, int epRew, int goldRew) {
        this.stageLabel  = label;
        this.isExplore   = explore;
        this.epReward    = epRew;
        this.goldReward  = goldRew;
        this.activeCharIdx = 0;

        gs.pulihkanParty();
        this.party   = gs.party;
        this.enemies = gs.buatMusuh(enemyConfig);

        titleLabel.setText(label);
        battleLog.setText("");
        log("⚔ Pertempuran dimulai!");
        setActionsEnabled(true);
        renderAll();
        updateTurnLabel();
    }

    // ── RENDER ───────────────────────────────────────────
    private void renderAll() {
        renderEnemies();
        renderParty();
    }

    private void renderEnemies() {
        enemyZone.removeAll();
        for (Enemy e : enemies) {
            if (e.isAlive()) enemyZone.add(buildEnemyCard(e));
        }
        enemyZone.revalidate();
        enemyZone.repaint();
    }

    private void renderParty() {
        partyZone.removeAll();
        for (int i = 0; i < party.size(); i++) {
            partyZone.add(buildPlayerCard(party.get(i), i == activeCharIdx));
        }
        partyZone.revalidate();
        partyZone.repaint();
    }

    private JPanel buildEnemyCard(Enemy e) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UI.BG_PANEL);
        card.setBorder(new EmptyBorder(10, 12, 10, 12));

        JLabel name = UI.label(e.getNama(), UI.RED_LIGHT, new Font("Serif", Font.BOLD, 12));
        name.setAlignmentX(CENTER_ALIGNMENT);

        JProgressBar hpBar = UI.bar((int) e.getHp(), (int) e.getMaxHp(), UI.HP_COLOR);

        JLabel hpLbl = UI.label("HP: " + (int)e.getHp() + "/" + (int)e.getMaxHp(), UI.TEXT_DIM, UI.SMALL);
        hpLbl.setAlignmentX(CENTER_ALIGNMENT);

        card.add(name);
        card.add(Box.createVerticalStrut(6));
        card.add(hpBar);
        card.add(Box.createVerticalStrut(2));
        card.add(hpLbl);

        // Kalau sedang menunggu pilih target, ubah tampilan dan tambah klik
        if (pendingAction != null) {

            // Kasih border kuning sebagai tanda bisa diklik
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UI.GOLD, 2),
                    new EmptyBorder(10, 12, 10, 12)
            ));
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            card.setBackground(new Color(50, 40, 20)); // sedikit highlight

            // Tambah MouseListener supaya bisa diklik
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (pendingAction != null) {
                        executeAction(e); // jalankan aksi ke musuh ini
                    }
                }

                // Efek hover — saat mouse masuk kartu
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    card.setBackground(new Color(70, 55, 20));
                    card.repaint();
                }

                // Efek hover — saat mouse keluar kartu
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    card.setBackground(new Color(50, 40, 20));
                    card.repaint();
                }
            });
        }
        return card;
    }

    private JPanel buildPlayerCard(Player p, boolean isActive) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(isActive ? new Color(40, 34, 70) : UI.BG_PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isActive ? UI.GOLD : UI.BORDER, isActive ? 2 : 1),
                new EmptyBorder(8, 10, 8, 10)
        ));

        JLabel name = UI.label(p.getNama(), isActive ? UI.GOLD_LIGHT : UI.TEXT, new Font("Serif", Font.BOLD, 12));
        name.setAlignmentX(CENTER_ALIGNMENT);
        JLabel role = UI.label(p.getTipe(), UI.TEXT_DIM, UI.SMALL);
        role.setAlignmentX(CENTER_ALIGNMENT);

        JProgressBar hpBar = UI.bar((int) p.getHp(), (int) p.getMaxHp(), UI.HP_COLOR);
        JProgressBar mpBar = UI.bar((int) p.getMp(), (int) p.getMaxMp(), UI.MP_COLOR);

        JLabel hpLbl = UI.label("HP " + (int)p.getHp(), UI.TEXT_DIM, new Font("SansSerif", Font.PLAIN, 10));
        JLabel mpLbl = UI.label("MP " + (int)p.getMp(), UI.TEXT_DIM, new Font("SansSerif", Font.PLAIN, 10));

        card.add(name);
        card.add(role);
        card.add(Box.createVerticalStrut(6));
        card.add(hpBar); card.add(hpLbl);
        card.add(Box.createVerticalStrut(3));
        card.add(mpBar); card.add(mpLbl);

        if (!p.isAlive()) {
            card.setBackground(new Color(60,20,20));
            name.setText(p.getNama() + " ✝");
            name.setForeground(new Color(120,80,80));
        }
        return card;
    }

    // ── PLAYER ACTION ────────────────────────────────────
    private void playerAction(String type) {
        List<Player> aliveParty   = getAliveParty();
        List<Enemy>  aliveEnemies = getAliveEnemies();
        if (aliveParty.isEmpty() || aliveEnemies.isEmpty()) return;

        Player activeChar = aliveParty.get(activeCharIdx % aliveParty.size());

        // Kalau Healer pakai skill/ultimate → langsung eksekusi, tidak perlu pilih musuh
        if (activeChar instanceof Healer && (type.equals("skill") || type.equals("ultimate"))) {
            pendingAction = type;
            executeAction(aliveEnemies.get(0)); // target tidak dipakai untuk Healer skill/ultimate
            return;
        }



//        JIka musuh hanya tersisa 1, langsung eksekusi
        if(aliveEnemies.size() == 1) {
            pendingAction = type;
            executeAction(aliveEnemies.get(0));
            return;
        }

        // Kalau musuh lebih dari 1, simpan aksi dan tunggu player klik musuh
        pendingAction = type;
        setActionsEnabled(false);
        turnLabel.setText("Pilih target musuh!");
        renderEnemies(); // render ulang supaya kartu musuh berubah jadi bisa diklik

    }

    private void executeAction(Enemy target) {
        List<Player> aliveParty   = getAliveParty();
        List<Enemy>  aliveEnemies = getAliveEnemies();

        Player activeChar = aliveParty.get(activeCharIdx % aliveParty.size());


        setActionsEnabled(false);
        String hasil;
        switch (pendingAction) {
            case "attack":
                hasil = activeChar.attack(target);
                break;
            case "skill":
                if (activeChar instanceof Healer) {
                    // Healer skill: sembuhkan ally dengan HP terendah
                    Player lowestHp = aliveParty.stream()
                            .min((a,b) -> Double.compare(a.getHp()/a.getMaxHp(), b.getHp()/b.getMaxHp()))
                            .orElse(activeChar);
                    hasil = activeChar.skill(lowestHp);
                } else {
                    hasil = activeChar.skill(target);
                }
                break;
            case "ultimate":
                if (activeChar instanceof Healer) {
                    hasil = ((Healer) activeChar).ultimateParty(aliveParty);
                } else if (activeChar instanceof Tank) {
                    hasil = ((Tank) activeChar).ultimateSemuaMusuh(aliveEnemies);
                } else {
                    hasil = activeChar.ultimate(target);
                }
                break;
            default:
                hasil = activeChar.attack(target);
        }

        pendingAction  = null;
        selectedTarget = null;

        log("▶ " + hasil);
        if (!target.isAlive()) log("  ✦ " + target.getNama() + " dikalahkan!");

        renderAll();
        if (checkBattleEnd()) return;

        // Advance character turn
        activeCharIdx = (activeCharIdx + 1) % aliveParty.size();
        updateTurnLabel();

        // Enemy turn after small delay when all chars have acted
        Timer timer = new Timer(700, e2 -> {
            enemyTurn();
            renderAll();
            if (!checkBattleEnd()) {
                activeCharIdx = (activeCharIdx + 1 ) % getAliveParty().size();
                updateTurnLabel();
                setActionsEnabled(true);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // ── ENEMY TURN ───────────────────────────────────────
    private void enemyTurn() {
        List<Player> aliveParty   = getAliveParty();
        List<Enemy>  aliveEnemies = getAliveEnemies();
        if (aliveParty.isEmpty() || aliveEnemies.isEmpty()) return;

        log("--- Giliran Musuh ---");
        for (Enemy e : aliveEnemies) {
            if (aliveParty.isEmpty()) break;
            Player target = aliveParty.get(new Random().nextInt(aliveParty.size()));
            int roll = new Random().nextInt(3);
            String hasil;
            if (roll == 0) hasil = e.attack(target);
            else if (roll == 1) hasil = e.skill(target);
            else hasil = e.ultimate(target);
            log("  " + hasil);
            if (!target.isAlive()) log("  ✦ " + target.getNama() + " gugur!");
        }
    }

    // ── CHECK END ────────────────────────────────────────
    private boolean checkBattleEnd() {
        if (getAliveEnemies().isEmpty()) {
            onVictory();
            return true;
        }
        if (getAliveParty().isEmpty()) {
            onDefeat();
            return true;
        }
        return false;
    }

    private void onVictory() {
        if (!isExplore) {
            gs.gold             += goldReward;
            gs.explorationPoint += epReward;
            gs.currentStage++;
            gs.totalStageClear++;
            showResult("★ MENANG! ★",
                    "Stage selesai!\n+" + epReward + " Exploration Points\n+" + goldReward + " Gold",
                    true);
        } else {
            int ep  = 15 + new Random().nextInt(20);
            int gld = 20 + new Random().nextInt(30);
            gs.explorationPoint += ep;
            gs.gold             += gld;
            gs.monsterSlain     += enemies.size();
            gs.runs++;
            showResult("★ MENANG! ★", "+" + ep + " EP\n+" + gld + " Gold", true);
        }
    }

    private void onDefeat() {
        showResult("✝ KALAH ✝", "Party Anda telah dikalahkan.\nCoba lagi!", false);
    }

    private void showResult(String title, String body, boolean win) {
        UIManager.put("OptionPane.background", UI.BG_CARD);
        UIManager.put("Panel.background", UI.BG_CARD);
        UIManager.put("OptionPane.messageForeground", win ? UI.GOLD_LIGHT : UI.RED_LIGHT);

        int opt = JOptionPane.showConfirmDialog(
                frame,
                body + "\n\nKembali ke menu sebelumnya?",
                title,
                JOptionPane.DEFAULT_OPTION,
                win ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE
        );

        frame.showPanel(isExplore ? GameFrame.EXPLORATION : GameFrame.CAMPAIGN);
    }

    // ── HELPERS ──────────────────────────────────────────
    private void log(String msg) {
        battleLog.append(msg + "\n");
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }

    private void updateTurnLabel() {
        List<Player> alive = getAliveParty();
        if (!alive.isEmpty()) {
            Player cur = alive.get(activeCharIdx % alive.size());
            turnLabel.setText("Giliran: " + cur.getNama() + " (" + cur.getTipe() + ")");
        }
    }

    private void setActionsEnabled(boolean en) {
        btnAttack.setEnabled(en);
        btnSkill.setEnabled(en);
        btnUltimate.setEnabled(en);
    }

    private List<Player> getAliveParty()   {
        return party.stream().filter(Player::isAlive).collect(java.util.stream.Collectors.toList());
    }
    private List<Enemy> getAliveEnemies()  {
        return enemies.stream().filter(Enemy::isAlive).collect(java.util.stream.Collectors.toList());
    }
}
