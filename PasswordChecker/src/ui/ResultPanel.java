package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class ResultPanel extends JPanel {

    private static final Color C_PRIMARY        = AuthPanel.C_PRIMARY;
    private static final Color C_PRIMARY_DARK   = AuthPanel.C_PRIMARY_DARK;
    private static final Color C_ACCENT_GREEN   = AuthPanel.C_ACCENT_GREEN;
    private static final Color C_WARNING_YELLOW = AuthPanel.C_WARNING_YELLOW;
    private static final Color C_ERROR_RED      = AuthPanel.C_ERROR_RED;
    private static final Color C_TEXT_PRIMARY   = AuthPanel.C_TEXT_PRIMARY;
    private static final Color C_TEXT_SECONDARY = AuthPanel.C_TEXT_SECONDARY;
    private static final Color C_BG_LIGHT       = new Color(0xF9FAFB);

    private static final Font FONT_BOLD     = AuthPanel.FONT_BOLD;
    private static final Font FONT_TITLE    = AuthPanel.FONT_TITLE;
    private static final Font FONT_BODY     = AuthPanel.FONT_BODY;
    private static final Font FONT_SUB      = AuthPanel.FONT_SUB;
    private static final Font FONT_SMALL    = AuthPanel.FONT_SMALL;

    // animated state
    private float circleScale   = 0f;
    private float checkProgress = 0f;
    private float barProgress   = 0f;
    private float contentAlpha  = 0f;
    private int   displayedScore = 0;
    private Timer entryTimer;

    private final int    score;
    private final Color  levelColor;
    private final String levelText;

    // real password checks
    private final boolean hasLen, hasUpper, hasSpecial, hasNumber;

    public ResultPanel(JFrame frame, String username, String password, int score) {
        this.score = score;

        hasLen     = password.length() >= 8;
        hasUpper   = password.matches(".*[A-Z].*");
        hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?`~].*");
        hasNumber  = password.matches(".*[0-9].*");

        if      (score < 40)  { levelColor = C_ERROR_RED;      levelText = "Weak"; }
        else if (score <= 70) { levelColor = C_WARNING_YELLOW; levelText = "Medium"; }
        else                  { levelColor = C_ACCENT_GREEN;   levelText = "Strong"; }

        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(40, 56, 40, 56));
        add(buildContent(frame, username), BorderLayout.CENTER);
        SwingUtilities.invokeLater(this::startEntrance);
    }

    // keep old 3-arg constructor so nothing else breaks
    public ResultPanel(JFrame frame, String username, int score) {
        this(frame, username, "", score);
    }

    private JPanel buildContent(JFrame frame, String username) {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        // ── animated circle (now 130px) ───────────────────────────────────────
        JPanel circleSlot = new JPanel() {
            public Dimension getPreferredSize()  { return new Dimension(150, 150); }
            public Dimension getMaximumSize()    { return new Dimension(Integer.MAX_VALUE, 150); }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int target = 130;
                int sz = (int)(target * Math.max(0f, circleScale));
                int x  = (getWidth()  - sz) / 2;
                int y  = (getHeight() - sz) / 2;
                if (sz > 2) {
                    // soft shadow
                    g2.setColor(new Color(levelColor.getRed(), levelColor.getGreen(), levelColor.getBlue(),
                                          (int)(50 * Math.min(1f, circleScale))));
                    g2.fill(new Ellipse2D.Float(x + 4, y + 8, sz, sz));
                    // circle
                    g2.setColor(levelColor);
                    g2.fill(new Ellipse2D.Float(x, y, sz, sz));
                    // checkmark — coords scale with sz
                    if (checkProgress > 0) {
                        g2.setColor(Color.WHITE);
                        g2.setStroke(new BasicStroke(sz * 0.05f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        float p1x = x + sz*0.26f, p1y = y + sz*0.50f;
                        float m1x = x + sz*0.42f, m1y = y + sz*0.66f;
                        float p2x = x + sz*0.74f, p2y = y + sz*0.30f;
                        if (checkProgress <= 0.5f) {
                            float t = checkProgress / 0.5f;
                            g2.drawLine((int)p1x, (int)p1y, (int)(p1x+(m1x-p1x)*t), (int)(p1y+(m1y-p1y)*t));
                        } else {
                            float t = (checkProgress - 0.5f) / 0.5f;
                            g2.drawLine((int)p1x, (int)p1y, (int)m1x, (int)m1y);
                            g2.drawLine((int)m1x, (int)m1y, (int)(m1x+(p2x-m1x)*t), (int)(m1y+(p2y-m1y)*t));
                        }
                    }
                }
                g2.dispose();
            }
            public boolean isOpaque() { return false; }
        };
        circleSlot.setOpaque(false);
        circleSlot.setAlignmentX(CENTER_ALIGNMENT);

        JLabel title = label("Password checked!", FONT_TITLE, C_TEXT_PRIMARY);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = label("Checked for: " + username, FONT_BODY, C_TEXT_SECONDARY);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        JPanel card = buildScoreCard();
        card.setAlignmentX(CENTER_ALIGNMENT);

        JButton btn = buildBtn("Check another password", () -> {
            frame.getContentPane().removeAll();
            frame.add(new AuthPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        btn.setAlignmentX(CENTER_ALIGNMENT);

        p.add(Box.createVerticalGlue());
        p.add(circleSlot);
        p.add(gap(28));
        p.add(title);
        p.add(gap(10));
        p.add(sub);
        p.add(gap(40));
        p.add(card);
        p.add(gap(36));
        p.add(btn);
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JPanel buildScoreCard() {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, contentAlpha)));

                // layered drop shadow for depth
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fill(new RoundRectangle2D.Float(2, 6, getWidth()-4, getHeight()-2, 18, 18));
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fill(new RoundRectangle2D.Float(1, 3, getWidth()-2, getHeight()-1, 17, 17));
                g2.setColor(new Color(0, 0, 0, 6));
                g2.fill(new RoundRectangle2D.Float(0, 1, getWidth(), getHeight(), 16, 16));

                // card background
                g2.setColor(C_BG_LIGHT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(new Color(0xE5E7EB));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 16, 16));

                super.paintComponent(g);
                g2.dispose();
            }
            public boolean isOpaque() { return false; }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(28, 28, 28, 28));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));

        // header row — label on left, BIG score number on right
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel scoreLabel = label("Password Strength", FONT_BOLD, C_TEXT_PRIMARY);

        Font scoreNumFont = new Font("SF Pro Display", Font.BOLD, 24);
        JLabel numLabel = new JLabel(displayedScore + " / 100") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, contentAlpha)));
                super.paintComponent(g); g2.dispose();
            }
        };
        numLabel.setFont(scoreNumFont);
        numLabel.setForeground(levelColor);
        new Timer(16, e -> numLabel.setText(displayedScore + " / 100")).start();

        header.add(scoreLabel, BorderLayout.WEST);
        header.add(numLabel,   BorderLayout.EAST);

        // animated fill bar
        JPanel bar = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, contentAlpha)));
                g2.setColor(new Color(0xE5E7EB));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                int fill = (int)(getWidth() * barProgress);
                if (fill > 0) {
                    GradientPaint gp = new GradientPaint(0, 0, levelColor.brighter(), fill, 0, levelColor);
                    g2.setPaint(gp);
                    g2.fill(new RoundRectangle2D.Float(0, 0, fill, getHeight(), 6, 6));
                }
                g2.dispose();
            }
            public boolean isOpaque() { return false; }
        };
        bar.setPreferredSize(new Dimension(0, 12));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 12));
        bar.setAlignmentX(LEFT_ALIGNMENT);

        // level badge (pill)
        JPanel badgeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        badgeRow.setOpaque(false);
        badgeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        badgeRow.setAlignmentX(LEFT_ALIGNMENT);

        JPanel badge = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, contentAlpha)));
                g2.setColor(new Color(levelColor.getRed(), levelColor.getGreen(), levelColor.getBlue(), 28));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
                g2.setColor(levelColor);
                g2.setStroke(new BasicStroke(1.4f));
                g2.draw(new RoundRectangle2D.Float(0.7f, 0.7f, getWidth()-1.4f, getHeight()-1.4f, getHeight(), getHeight()));
                g2.setFont(new Font("SF Pro Display", Font.BOLD, 13));
                g2.setColor(levelColor);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(levelText, (getWidth()-fm.stringWidth(levelText))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
            public boolean isOpaque() { return false; }
        };
        badge.setPreferredSize(new Dimension(86, 30));
        badgeRow.add(badge);

        // breakdown rows
        JPanel breakdown = new JPanel();
        breakdown.setLayout(new BoxLayout(breakdown, BoxLayout.Y_AXIS));
        breakdown.setOpaque(false);
        breakdown.setAlignmentX(LEFT_ALIGNMENT);

        String[] labels  = { "At least 8 characters", "Uppercase letter (A-Z)", "Special character (!@#...)", "Number (0-9)" };
        boolean[] passed = { hasLen, hasUpper, hasSpecial, hasNumber };

        breakdown.add(gap(8));
        for (int i = 0; i < labels.length; i++) {
            breakdown.add(makeRow(labels[i], passed[i]));
            if (i < labels.length - 1) breakdown.add(gap(12));
        }

        card.add(header);
        card.add(gap(18));
        card.add(bar);
        card.add(gap(20));
        card.add(badgeRow);
        card.add(breakdown);
        return card;
    }

    private JPanel makeRow(String text, boolean pass) {
        JPanel row = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, contentAlpha)));
                super.paintComponent(g); g2.dispose();
            }
        };
        row.setOpaque(false);
        row.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        row.setAlignmentX(LEFT_ALIGNMENT);

        JPanel dot = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(pass ? C_ACCENT_GREEN : new Color(0xD1D5DB));
                g2.fill(new Ellipse2D.Float(0, 4, 12, 12));
                if (pass) {
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(3, 9, 5, 12); g2.drawLine(5, 12, 9, 6);
                }
                g2.dispose();
            }
            public boolean isOpaque() { return false; }
        };
        dot.setPreferredSize(new Dimension(13, 18));

        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        lbl.setForeground(pass ? C_TEXT_PRIMARY : new Color(0xAAAAAA));

        row.add(dot); row.add(lbl);
        return row;
    }

    // ── entrance animation sequence ───────────────────────────────────────────

    private void startEntrance() {
        entryTimer = new Timer(12, null);
        final long  start     = System.currentTimeMillis();
        final float targetBar = score / 100f;

        entryTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - start;

            // 0–380ms  circle pops in with bounce
            circleScale = elapsed < 380
                ? easeOutBack(elapsed / 380f)
                : 1f;

            // 280–560ms  checkmark draws itself
            if (elapsed >= 280)
                checkProgress = Math.min(easeOutCubic((elapsed - 280f) / 280f), 1f);

            // 420–680ms  card + text fade in
            if (elapsed >= 420)
                contentAlpha = Math.min(easeOutCubic((elapsed - 420f) / 260f), 1f);

            // 560–960ms  bar fills & score counts up
            if (elapsed >= 560) {
                float t = Math.min(easeOutCubic((elapsed - 560f) / 400f), 1f);
                barProgress    = t * targetBar;
                displayedScore = (int)(t * score);
            }

            repaint();

            if (elapsed >= 960) {
                circleScale = 1f; checkProgress = 1f;
                contentAlpha = 1f; barProgress = targetBar; displayedScore = score;
                repaint();
                ((Timer) e.getSource()).stop();
            }
        });
        entryTimer.start();
    }

    private static float easeOutBack(float t) {
        float c1 = 1.70158f, c3 = c1 + 1;
        return 1 + c3 * (float)Math.pow(t-1, 3) + c1 * (float)Math.pow(t-1, 2);
    }

    private static float easeOutCubic(float t) {
        return 1f - (float)Math.pow(1-t, 3);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text); l.setFont(font); l.setForeground(color); return l;
    }

    private Component gap(int h) { return Box.createVerticalStrut(h); }

    private JButton buildBtn(String text, Runnable action) {
        JButton b = new JButton(text) {
            private float ripple = 0f;
            private Timer rippleTimer;
            { addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (rippleTimer != null && rippleTimer.isRunning()) rippleTimer.stop();
                    ripple = 0.3f; repaint();
                    rippleTimer = new Timer(14, ev -> {
                        ripple = Math.max(0f, ripple - 0.025f); repaint();
                        if (ripple == 0f) ((Timer)ev.getSource()).stop();
                    }); rippleTimer.start();
                }
            }); }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                if (ripple > 0) {
                    g2.setColor(new Color(1f, 1f, 1f, ripple));
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                }
                g2.setFont(getFont()); g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        b.setText(text); b.setFont(FONT_BOLD); b.setBackground(C_PRIMARY); b.setForeground(Color.WHITE);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        b.setPreferredSize(new Dimension(0, 54));
        b.setFocusPainted(false); b.setBorderPainted(false); b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> action.run());

        Color[] cur = {C_PRIMARY}; Timer[] ht = {null};
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { animBtn(b, cur, ht, C_PRIMARY_DARK); }
            public void mouseExited (MouseEvent e) { animBtn(b, cur, ht, C_PRIMARY); }
        });
        return b;
    }

    private void animBtn(JButton b, Color[] cur, Timer[] ref, Color target) {
        if (ref[0] != null && ref[0].isRunning()) ref[0].stop();
        ref[0] = new Timer(10, e -> {
            cur[0] = new Color(AuthPanel.lerp(cur[0].getRed(), target.getRed(), 3),
                               AuthPanel.lerp(cur[0].getGreen(), target.getGreen(), 3),
                               AuthPanel.lerp(cur[0].getBlue(), target.getBlue(), 3));
            b.setBackground(cur[0]);
            if (cur[0].equals(target)) ((Timer)e.getSource()).stop();
        }); ref[0].start();
    }
}
