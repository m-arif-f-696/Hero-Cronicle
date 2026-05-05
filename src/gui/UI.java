package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UI {
    // Palette
    public static final Color BG_DARK    = new Color(13,  10, 26);
    public static final Color BG_CARD    = new Color(26,  20, 48);
    public static final Color BG_PANEL   = new Color(36,  30, 61);
    public static final Color GOLD       = new Color(201,162, 39);
    public static final Color GOLD_LIGHT = new Color(240,192, 64);
    public static final Color TEXT       = new Color(232,223,200);
    public static final Color TEXT_DIM   = new Color(154,143,120);
    public static final Color HP_COLOR   = new Color(220, 80, 80);
    public static final Color MP_COLOR   = new Color( 64,128,224);
    public static final Color EXP_COLOR  = new Color( 64,192, 96);
    public static final Color RED_LIGHT  = new Color(224,128,128);
    public static final Color BLUE_LIGHT = new Color(128,176,224);
    public static final Color PURPLE     = new Color(155,127,204);
    public static final Color BORDER     = new Color(201,162,39,60);

    // Fonts
    public static final Font TITLE  = new Font("Serif",      Font.BOLD,  22);
    public static final Font HEADER = new Font("Serif",      Font.BOLD,  16);
    public static final Font BODY   = new Font("SansSerif",  Font.PLAIN, 13);
    public static final Font SMALL  = new Font("SansSerif",  Font.PLAIN, 11);
    public static final Font MONO   = new Font("Monospaced", Font.PLAIN, 12);

    public static JPanel darkCard() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        return p;
    }

    public static JLabel label(String text, Color color, Font font) {
        JLabel l = new JLabel(text);
        l.setForeground(color);
        l.setFont(font);
        return l;
    }

    public static JButton goldButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Serif", Font.BOLD, 13));
        b.setForeground(GOLD);
        b.setBackground(new Color(201,162,39,30));
        b.setBorder(BorderFactory.createLineBorder(GOLD, 1));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        return b;
    }

    public static JButton actionButton(String text, Color fg, Color border) {
        JButton b = new JButton(text);
        b.setFont(new Font("Serif", Font.BOLD, 12));
        b.setForeground(fg);
        b.setBackground(BG_PANEL);
        b.setBorder(BorderFactory.createLineBorder(border, 1));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        return b;
    }

    /** Simple HP/MP bar as a JProgressBar */
    public static JProgressBar bar(int val, int max, Color fill) {
        JProgressBar pb = new JProgressBar(0, max);
        pb.setValue(val);
        pb.setForeground(fill);
        pb.setBackground(new Color(255,255,255,20));
        pb.setBorderPainted(false);
        pb.setPreferredSize(new Dimension(100, 7));
        pb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 7));
        return pb;
    }
}