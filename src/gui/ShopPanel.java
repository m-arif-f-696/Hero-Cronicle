package gui;

import battle.GameState;
import items.Items;
import player.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ShopPanel extends JPanel {
    private GameFrame frame;
    private JLabel    goldLabel;
    private JPanel    itemsGrid;

    public ShopPanel(GameFrame frame) {
        this.frame = frame;
        setBackground(UI.BG_DARK);
        setLayout(new BorderLayout());
        buildHeader();
        buildContent();
    }

    private void buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UI.BG_CARD);
        header.setBorder(new EmptyBorder(12, 18, 12, 18));

        JButton back = UI.goldButton("← Kembali");
        back.addActionListener(e -> frame.showPanel(GameFrame.MAIN_MENU));

        JLabel title = UI.label("Shop", UI.GOLD, new Font("Serif", Font.BOLD, 20));
        goldLabel    = UI.label("Gold: 0", UI.GOLD, UI.SMALL);

        header.add(back,      BorderLayout.WEST);
        header.add(title,     BorderLayout.CENTER);
        header.add(goldLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    private void buildContent() {
        itemsGrid = new JPanel(new GridLayout(2, 3, 12, 12));
        itemsGrid.setBackground(UI.BG_DARK);
        itemsGrid.setBorder(new EmptyBorder(16, 16, 16, 16));

        JScrollPane scroll = new JScrollPane(itemsGrid);
        scroll.setBackground(UI.BG_DARK);
        scroll.getViewport().setBackground(UI.BG_DARK);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    public void refresh() {
        GameState gs = GameState.getInstance();
        goldLabel.setText("🪙 Gold: " + gs.gold);

        itemsGrid.removeAll();
        for (Items item : gs.shopItems) {
            itemsGrid.add(buildItemCard(item, gs));
        }
        itemsGrid.revalidate();
        itemsGrid.repaint();
    }

    private JPanel buildItemCard(Items item, GameState gs) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UI.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UI.BORDER, 1),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel nameLabel = UI.label(item.getNama(), UI.GOLD, new Font("Serif", Font.BOLD, 13));
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel descLabel = UI.label("<html><center>" + item.getDeskripsi() + "</center></html>", UI.TEXT_DIM, UI.SMALL);
        descLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel priceLabel = UI.label("🪙 " + (int)item.getPrice() + " Gold", UI.GOLD_LIGHT, UI.BODY);
        priceLabel.setAlignmentX(CENTER_ALIGNMENT);

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(descLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(10));

        boolean cukupGold = gs.gold >= item.getPrice();
        JButton buyBtn = UI.goldButton("Beli");
        buyBtn.setAlignmentX(CENTER_ALIGNMENT);
        buyBtn.setEnabled(cukupGold);
        buyBtn.addActionListener(e -> handleBuy(item, gs));
        card.add(buyBtn);

        return card;
    }

    private void handleBuy(Items item, GameState gs) {
        if (gs.gold < item.getPrice()) {
            JOptionPane.showMessageDialog(frame, "Gold tidak cukup!", "Shop", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Pilih karakter
        String[] charNames = gs.party.stream()
                .map(p -> p.getNama() + " (" + p.getTipe() + ")")
                .toArray(String[]::new);

        String chosen = (String) JOptionPane.showInputDialog(
                frame,
                "Berikan " + item.getNama() + " kepada siapa?",
                "Pilih Karakter",
                JOptionPane.PLAIN_MESSAGE,
                null,
                charNames,
                charNames[0]
        );

        if (chosen == null) return;

        int idx = 0;
        for (int i = 0; i < charNames.length; i++) {
            if (charNames[i].equals(chosen)) { idx = i; break; }
        }

        gs.gold -= (int) item.getPrice();
        Player target = gs.party.get(idx);
        target.tambahItem(new Items(item.getNama(), item.getDeskripsi(), item.getPrice(), item.getDamage(), item.getHealth()));

        JOptionPane.showMessageDialog(frame,
                item.getNama() + " diberikan ke " + target.getNama() + "!\nSisa Gold: " + gs.gold,
                "Berhasil!", JOptionPane.INFORMATION_MESSAGE);

        refresh();
    }
}