package gui;

import battle.GameState;
import enemy.Enemy;
import player.*;
import entity.Entity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattlePanel extends JPanel {
    private GameFrame frame;
    private GameState gs;

    // Battle state
    private List<Enemy> enemies;
    private List<Player> party;
    private boolean      isExplore;
    private int          epReward;
    private int          goldReward;
    private String       stageLabel;
    private List<Entity> turnQueue = new ArrayList<>();
    private int          turnIdx = 0;
    private String       pendingAction = null;
    private Enemy        selectedTarget = null;
    private boolean healerSkillMode = false;

    // UI components
    private JLabel titleLabel;
    private JLabel turnLabel;
    private JPanel enemyZone;
    private JPanel partyZone;
    private JTextArea battleLog;
    private JButton btnAttack;
    private JButton btnSkill;
    private JButton btnUltimate;

    public BattlePanel(GameFrame frame) {
        this.frame = frame;
        this.gs = GameState.getInstance();
        setBackground(UI.BG_DARK);
        setLayout(new BorderLayout(0, 0));
        buildHeader();
        buildArena();
        buildActions();
    }

    // ── TURN QUEUE ───────────────────────────────────────────

    private void buildTurnQueue(){
        turnQueue.clear();
        turnQueue.addAll(getAliveParty());
        turnQueue.addAll(getAliveEnemies());

        turnQueue.sort((a, b) -> Integer.compare(b.getSpeed(), a.getSpeed()));

        turnIdx = 0;
    }

    private Entity getCurrentEntity() {
        while (turnIdx < turnQueue.size() && !turnQueue.get(turnIdx).isAlive()) {
            turnIdx++;
        }

        // Kalau antrian habis, buat antrian baru
        if (turnIdx >= turnQueue.size()) {
            buildTurnQueue();
        }

        return turnQueue.get(turnIdx);
    }
    // ── HEADER ───────────────────────────────────────────
    private void buildHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(UI.BG_CARD);
        header.setBorder(new EmptyBorder(10, 16, 10, 16));

        titleLabel = UI.label("Battle", UI.GOLD, new Font("Serif", Font.BOLD, 18));
        turnLabel = UI.label("Giliran Anda", UI.TEXT_DIM, UI.SMALL);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(turnLabel, BorderLayout.EAST);
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
        arena.add(logScroll, BorderLayout.CENTER);
        arena.add(partySection, BorderLayout.SOUTH);
        add(arena, BorderLayout.CENTER);
    }

    // ── ACTIONS ──────────────────────────────────────────
    private void buildActions() {
        JPanel actions = new JPanel(new GridLayout(1, 3, 10, 0));
        actions.setBackground(UI.BG_CARD);
        actions.setBorder(new EmptyBorder(10, 16, 10, 16));

        btnAttack = UI.actionButton("<html><center>⚔ Attack<br><font size='2'>Basic strike</font></center></html>",
                UI.RED_LIGHT, new Color(200, 64, 64, 120));
        btnSkill = UI.actionButton("<html><center>✦ Skill<br><font size='2'>Costs 20 MP</font></center></html>",
                UI.BLUE_LIGHT, new Color(64, 128, 224, 120));
        btnUltimate = UI.actionButton("<html><center>◈ Ultimate<br><font size='2'>Costs 50 MP</font></center></html>",
                UI.PURPLE, new Color(107, 79, 160, 120));

        btnAttack.addActionListener(e -> playerAction("attack"));
        btnSkill.addActionListener(e -> playerAction("skill"));
        btnUltimate.addActionListener(e -> playerAction("ultimate"));

        actions.add(btnAttack);
        actions.add(btnSkill);
        actions.add(btnUltimate);
        add(actions, BorderLayout.SOUTH);
    }

    // ── INIT BATTLE ──────────────────────────────────────
    public void initBattle(String enemyConfig, String label, boolean explore, int epRew, int goldRew) {

        this.stageLabel = label;
        this.isExplore = explore;
        this.epReward = epRew;
        this.goldReward = goldRew;
        this.pendingAction = null;
        this.healerSkillMode = false;

        gs.pulihkanParty();
        this.party = gs.party;
        this.enemies = gs.buatMusuh(enemyConfig);

        buildTurnQueue();

        titleLabel.setText(label);
        battleLog.setText("");
        log("⚔ Pertempuran dimulai!");
        log("Urutan giliran: " + getTurnOrderString());
        setActionsEnabled(true);
        renderAll();
        updateTurnLabel();

        processTurn();
    }
    private String getTurnOrderString() {
        StringBuilder sb = new StringBuilder();
        for (entity.Entity e : turnQueue) {
            sb.append(e.getNama()).append("(").append(e.getSpeed()).append(") → ");
        }
        return sb.toString();
    }
    private void processTurn() {
        if (checkBattleEnd()) return;

        entity.Entity current = getCurrentEntity();

        // Kalau giliran musuh → langsung eksekusi otomatis
        if (current instanceof Enemy) {
            updateTurnLabel();
            Timer timer = new Timer(700, e -> {
                enemyTurnSingle((Enemy) current);
                renderAll();
                turnIdx++;
                processTurn(); // lanjut ke giliran berikutnya
            });
            timer.setRepeats(false);
            timer.start();

            // Kalau giliran player → tunggu input
        } else if (current instanceof Player) {
            updateTurnLabel();
            setActionsEnabled(true);
            renderParty();
        }
    }



    // ── RENDER ───────────────────────────────────────────
    private void renderAll() {
        renderEnemies();
        renderParty();
    }

    private void renderEnemies() {
        enemyZone.removeAll();
        for (Enemy e : enemies) {
            if (e.isAlive())
                enemyZone.add(buildEnemyCard(e));
        }
        enemyZone.revalidate();
        enemyZone.repaint();
    }

    private void renderParty() {
        partyZone.removeAll();

        entity.Entity current = getCurrentEntity();
        for (int i = 0; i < party.size(); i++) {
            boolean isActive = party.get(i) == current; // cek apakah ini yang giliran sekarang
            partyZone.add(buildPlayerCard(party.get(i), isActive));
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

        JLabel hpLbl = UI.label("HP: " + (int) e.getHp() + "/" + (int) e.getMaxHp(), UI.TEXT_DIM, UI.SMALL);
        hpLbl.setAlignmentX(CENTER_ALIGNMENT);

        card.add(name);
        card.add(Box.createVerticalStrut(6));
        card.add(hpBar);
        card.add(Box.createVerticalStrut(2));
        card.add(hpLbl);

        if (pendingAction == null || healerSkillMode) {
            return card;
        }

        // Kasih border kuning sebagai tanda bisa diklik
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UI.GOLD, 2),
                new EmptyBorder(10, 12, 10, 12)));
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
        return card;
    }

    private JPanel buildPlayerCard(Player p, boolean isActive) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(isActive ? new Color(40, 34, 70) : UI.BG_PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isActive ? UI.GOLD : UI.BORDER, isActive ? 2 : 1),
                new EmptyBorder(8, 10, 8, 10)));

        JLabel name = UI.label(p.getNama(), isActive ? UI.GOLD_LIGHT : UI.TEXT, new Font("Serif", Font.BOLD, 12));
        name.setAlignmentX(CENTER_ALIGNMENT);
        JLabel role = UI.label(p.getTipe(), UI.TEXT_DIM, UI.SMALL);
        role.setAlignmentX(CENTER_ALIGNMENT);

        JProgressBar hpBar = UI.bar((int) p.getHp(), (int) p.getMaxHp(), UI.HP_COLOR);
        JProgressBar mpBar = UI.bar((int) p.getMp(), (int) p.getMaxMp(), UI.MP_COLOR);

        JLabel hpLbl = UI.label("HP " + (int) p.getHp(), UI.TEXT_DIM, new Font("SansSerif", Font.PLAIN, 10));
        JLabel mpLbl = UI.label("MP " + (int) p.getMp(), UI.TEXT_DIM, new Font("SansSerif", Font.PLAIN, 10));

        card.add(name);
        card.add(role);
        card.add(Box.createVerticalStrut(6));
        card.add(hpBar);
        card.add(hpLbl);
        card.add(Box.createVerticalStrut(3));
        card.add(mpBar);
        card.add(mpLbl);

        if (!p.isAlive()) {
            card.setBackground(new Color(60, 20, 20));
            name.setText(p.getNama() + " ✝");
            name.setForeground(new Color(120, 80, 80));
        }

        // Target selection untuk Healer skill
        boolean isHealerSkillMode = healerSkillMode;

        if (isHealerSkillMode && p.isAlive()) {
            Color normalHealTargetColor = new Color(40, 40, 80);
            Color hoverHealTargetColor = new Color(64, 72, 120);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UI.GOLD, 2),
                    new EmptyBorder(8, 10, 8, 10)));
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            card.setBackground(normalHealTargetColor);
            card.setToolTipText("Klik untuk heal " + p.getNama());

            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    executeAction(p);
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    card.setBackground(hoverHealTargetColor);
                    card.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(UI.GOLD_LIGHT, 2),
                            new EmptyBorder(8, 10, 8, 10)));
                    card.repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    card.setBackground(normalHealTargetColor);
                    card.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(UI.GOLD, 2),
                            new EmptyBorder(8, 10, 8, 10)));
                    card.repaint();
                }
            });
        }

        return card;
    }

    // ── PLAYER ACTION ────────────────────────────────────
    private void playerAction(String type) {
        List<Player> aliveParty = getAliveParty();
        List<Enemy> aliveEnemies = getAliveEnemies();
        if (aliveParty.isEmpty() || aliveEnemies.isEmpty())
            return;

        Player activeChar = (Player) getCurrentEntity();

        // Kalau Healer pakai ultimate → langsung eksekusi (party wide heal)
        if (activeChar instanceof Healer && type.equals("ultimate")) {
            pendingAction = type;
            healerSkillMode = false;
            executeAction(aliveEnemies.get(0));
            return;
        }

        // Kalau Healer pakai skill → pilih ally
        if (activeChar instanceof Healer && type.equals("skill")) {
            pendingAction = type;
            healerSkillMode = true;
            setActionsEnabled(false);
            turnLabel.setText("Pilih hero untuk disembuhkan!");
            renderAll();
            return;
        }

        // Jika musuh hanya tersisa 1, langsung eksekusi (untuk attack/skill non-healer)
        if (aliveEnemies.size() == 1) {
            pendingAction = type;
            healerSkillMode = false;
            executeAction(aliveEnemies.get(0));
            return;
        }

        // Kalau musuh lebih dari 1, simpan aksi dan tunggu player klik musuh
        pendingAction = type;
        healerSkillMode = false;
        setActionsEnabled(false);
        turnLabel.setText("Pilih target musuh!");
        renderEnemies(); // render ulang supaya kartu musuh berubah jadi bisa diklik
    }

    private void executeAction(entity.Entity target) {
        List<Player> aliveParty = getAliveParty();
        List<Enemy> aliveEnemies = getAliveEnemies();

        Player activeChar = (Player) getCurrentEntity();

        setActionsEnabled(false);
        String hasil;
        switch (pendingAction) {
            case "attack":
                hasil = activeChar.attack(target);
                break;
            case "skill":
                // Healer skill sekarang menggunakan target yang dipilih
                hasil = activeChar.skill(target);
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

        pendingAction = null;
        selectedTarget = null;
        healerSkillMode = false;

        log("▶ " + hasil);
        if (!target.isAlive())
            log("  ✦ " + target.getNama() + " dikalahkan!");

        renderAll();
        if (checkBattleEnd()) return;

        // Lanjut ke giliran berikutnya (turn-queue)
        turnIdx++;
        processTurn();
    }

    // ── ENEMY TURN ───────────────────────────────────────
    private void enemyTurnSingle(Enemy e) {
        List<Player> aliveParty = getAliveParty();
        if (aliveParty.isEmpty()) return;

        Player target = aliveParty.get(new Random().nextInt(aliveParty.size()));
        int roll = new Random().nextInt(3);
        String hasil;
        if (roll == 0)      hasil = e.attack(target);
        else if (roll == 1) hasil = e.skill(target);
        else                hasil = e.ultimate(target);

        log("  " + hasil);
        if (!target.isAlive()) log("  ✦ " + target.getNama() + " gugur!");
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
            gs.gold += goldReward;
            gs.explorationPoint += epReward;
            gs.currentStage++;
            gs.totalStageClear++;
            showResult("★ MENANG! ★",
                    "Stage selesai!\n+" + epReward + " Exploration Points\n+" + goldReward + " Gold",
                    true);
        } else {
            int ep = 15 + new Random().nextInt(20);
            int gld = 20 + new Random().nextInt(30);
            gs.explorationPoint += ep;
            gs.gold += gld;
            gs.monsterSlain += enemies.size();
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
                win ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);

        frame.showPanel(isExplore ? GameFrame.EXPLORATION : GameFrame.CAMPAIGN);
    }

    // ── HELPERS ──────────────────────────────────────────
    private void log(String msg) {
        battleLog.append(msg + "\n");
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }

    private void updateTurnLabel() {
        entity.Entity current = getCurrentEntity();
        if (current instanceof Player) {
            turnLabel.setText("Giliran: " + current.getNama() + " (" + current.getTipe() + ")");
        } else {
            turnLabel.setText("Giliran Musuh: " + current.getNama());
        }
    }

    private void setActionsEnabled(boolean en) {
        btnAttack.setEnabled(en);
        btnSkill.setEnabled(en);
        btnUltimate.setEnabled(en);
    }

    private List<Player> getAliveParty() {
        return party.stream().filter(Player::isAlive).collect(java.util.stream.Collectors.toList());
    }

//    private Player getActiveChar() {
//        List<Player> alive = getAliveParty();
//        if (alive.isEmpty())
//            return null;
//        return alive.get(activeCharIdx % alive.size());
//    }

    private List<Enemy> getAliveEnemies() {
        return enemies.stream().filter(Enemy::isAlive).collect(java.util.stream.Collectors.toList());
    }
}
