package ui;

import exception.InvalidPasswordException;
import logic.PasswordScore;
import model.Password;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class AuthPanel extends JPanel {

    private static final int CARD_HEIGHT = 172;

    // colour palette
    static final Color C_BG             = Color.WHITE;
    static final Color C_PRIMARY        = new Color(0x2563EB);
    static final Color C_PRIMARY_DARK   = new Color(0x1E40AF);
    static final Color C_ACCENT_GREEN   = new Color(0x10B981);
    static final Color C_WARNING_YELLOW = new Color(0xF59E0B);
    static final Color C_ERROR_RED      = new Color(0xEF4444);
    static final Color C_BORDER_IDLE    = new Color(0xE5E7EB);
    static final Color C_BORDER_FOC     = new Color(0x2563EB);
    static final Color C_LABEL_IDLE     = new Color(0x888888);
    static final Color C_LABEL_UP       = new Color(0x374151);
    static final Color C_TEXT_PRIMARY   = new Color(0x1F2937);
    static final Color C_TEXT_SECONDARY = new Color(0x6B7280);
    static final Color C_FIELD_BG       = new Color(0xF9FAFB);
    static final Color C_HOVER_BG       = new Color(0xF0F1F3);

    // fonts
    static final Font FONT_BODY     = new Font("SF Pro Display", Font.PLAIN, 14);
    static final Font FONT_BOLD     = new Font("SF Pro Display", Font.BOLD,  14);
    static final Font FONT_TITLE    = new Font("SF Pro Display", Font.BOLD,  28);
    static final Font FONT_SUBTITLE = new Font("SF Pro Display", Font.BOLD,  16);
    static final Font FONT_SUB      = new Font("SF Pro Display", Font.PLAIN, 14);
    static final Font FONT_SMALL    = new Font("SF Pro Display", Font.PLAIN, 12);

    private final JFrame frame;

    private FloatLabelField siUsername, siPassword;
    private JLabel siError;

    private FloatLabelField suUsername, suPassword, suConfirm;
    private JLabel suError;

    private JButton tabSignIn, tabSignUp;
    private CardLayout cards;
    private JPanel formHolder;

    private JPanel strengthCardClip;
    private RoundedCardPanel strengthCardInner;
    private int clipHeight = 0;
    private Timer strengthTimer;
    private DotIcon strengthDot;
    private JLabel strengthText;
    private SegmentedBar segmentedBar;
    private RuleRow ruleLen, ruleUpper, ruleSpecial, ruleNumber;

    public AuthPanel(JFrame frame) {
        this.frame = frame;
        setBackground(C_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(40, 56, 44, 56));
        add(buildTabRow(), BorderLayout.NORTH);
        cards = new CardLayout();
        formHolder = new JPanel(cards);
        formHolder.setBackground(C_BG);
        formHolder.add(buildSignInForm(), "SIGNIN");
        formHolder.add(buildSignUpForm(), "SIGNUP");
        add(formHolder, BorderLayout.CENTER);
    }

    // ── tabs ──────────────────────────────────────────────────────────────────

    private JPanel buildTabRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(C_BG);
        row.setBorder(new MatteBorder(0, 0, 1, 0, C_BORDER_IDLE));
        tabSignIn = makeTabBtn("Sign in", true);
        tabSignUp = makeTabBtn("Create account", false);
        tabSignIn.addActionListener(e -> switchTab(true));
        tabSignUp.addActionListener(e -> switchTab(false));
        row.add(tabSignIn);
        row.add(tabSignUp);
        return row;
    }

    private JButton makeTabBtn(String text, boolean active) {
        JButton b = new JButton(text);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        styleTab(b, active);
        return b;
    }

    private void styleTab(JButton b, boolean active) {
        b.setFont(active ? FONT_SUBTITLE : FONT_SUB);
        b.setForeground(active ? C_TEXT_PRIMARY : C_TEXT_SECONDARY);
        b.setBorder(active
            ? BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 2, 0, C_PRIMARY),
                new EmptyBorder(12, 8, 12, 8))
            : new EmptyBorder(12, 8, 14, 8));
    }

    private void switchTab(boolean signIn) {
        cards.show(formHolder, signIn ? "SIGNIN" : "SIGNUP");
        styleTab(tabSignIn,  signIn);
        styleTab(tabSignUp, !signIn);
    }

    // ── sign-in form ──────────────────────────────────────────────────────────

    private JPanel buildSignInForm() {
        JPanel p = vbox();
        p.add(gap(28));
        p.add(title("Welcome back"));
        p.add(gap(8));
        p.add(sub("Enter your credentials to continue"));
        p.add(gap(36));

        siUsername = new FloatLabelField("Username", false);
        p.add(siUsername);
        p.add(gap(20));

        siPassword = new FloatLabelField("Password", true);
        p.add(siPassword);
        p.add(gap(10));

        siError = errorLabel();
        p.add(siError);
        p.add(gap(24));

        JButton btn = submitBtn("Sign in");
        btn.addActionListener(e -> doSignIn());
        p.add(btn);
        p.add(gap(22));
        p.add(footerLink("Don't have an account? ", "Create one", () -> switchTab(false)));
        p.add(Box.createVerticalGlue());
        return p;
    }

    // ── sign-up form ──────────────────────────────────────────────────────────

    private JPanel buildSignUpForm() {
        JPanel p = vbox();
        p.add(gap(28));
        p.add(title("Create account"));
        p.add(gap(8));
        p.add(sub("Set up a strong password for security"));
        p.add(gap(36));

        suUsername = new FloatLabelField("Username", false);
        p.add(suUsername);
        p.add(gap(20));

        suPassword = new FloatLabelField("Password", true);
        p.add(suPassword);
        p.add(gap(12));

        buildStrengthCard();
        p.add(strengthCardClip);
        p.add(gap(18));

        suConfirm = new FloatLabelField("Confirm password", true);
        p.add(suConfirm);
        p.add(gap(10));

        suError = errorLabel();
        p.add(suError);
        p.add(gap(24));

        JButton btn = submitBtn("Create account");
        btn.addActionListener(e -> doSignUp());
        p.add(btn);
        p.add(gap(22));
        p.add(footerLink("Already have an account? ", "Sign in", () -> switchTab(true)));
        p.add(Box.createVerticalGlue());

        suPassword.addTypingListener(() -> SwingUtilities.invokeLater(this::refreshStrength));
        return p;
    }

    // ── strength card ─────────────────────────────────────────────────────────

    private void buildStrengthCard() {
        strengthCardInner = new RoundedCardPanel();
        strengthCardInner.setLayout(new BoxLayout(strengthCardInner, BoxLayout.Y_AXIS));
        strengthCardInner.setBorder(new EmptyBorder(16, 16, 16, 16));
        strengthCardInner.setPreferredSize(new Dimension(10, CARD_HEIGHT));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        top.setOpaque(false);
        top.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        strengthDot = new DotIcon(C_ERROR_RED);
        top.add(strengthDot);
        strengthText = new JLabel("  Weak");
        strengthText.setFont(FONT_BOLD);
        strengthText.setForeground(C_ERROR_RED);
        top.add(strengthText);

        segmentedBar = new SegmentedBar();
        ruleLen     = new RuleRow("Be at least 8 characters long");
        ruleUpper   = new RuleRow("At least one uppercase letter (A-Z)");
        ruleSpecial = new RuleRow("At least one special character (!@#$%^&*)");
        ruleNumber  = new RuleRow("At least one number (0-9)");

        strengthCardInner.add(top);
        strengthCardInner.add(Box.createVerticalStrut(8));
        strengthCardInner.add(segmentedBar);
        strengthCardInner.add(Box.createVerticalStrut(10));
        strengthCardInner.add(ruleLen);
        strengthCardInner.add(Box.createVerticalStrut(5));
        strengthCardInner.add(ruleUpper);
        strengthCardInner.add(Box.createVerticalStrut(5));
        strengthCardInner.add(ruleSpecial);
        strengthCardInner.add(Box.createVerticalStrut(5));
        strengthCardInner.add(ruleNumber);

        // clip panel: height animates 0 → CARD_HEIGHT
        strengthCardClip = new JPanel(null) {
            public Dimension getPreferredSize() { return new Dimension(10, clipHeight); }
            public Dimension getMaximumSize()   { return new Dimension(Integer.MAX_VALUE, clipHeight); }
            public void doLayout()              { strengthCardInner.setBounds(0, 0, getWidth(), CARD_HEIGHT); }
        };
        strengthCardClip.setOpaque(false);
        strengthCardClip.setAlignmentX(LEFT_ALIGNMENT);
        strengthCardClip.setVisible(false);
        strengthCardClip.add(strengthCardInner);
    }

    private void refreshStrength() {
        String pwd = suPassword.getText();
        if (pwd.isEmpty()) { animateStrengthCard(false); return; }
        animateStrengthCard(true);

        boolean hasLen     = pwd.length() >= 8;
        boolean hasUpper   = pwd.matches(".*[A-Z].*");
        boolean hasSpecial = pwd.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?`~].*");
        boolean hasNumber  = pwd.matches(".*[0-9].*");

        ruleLen.setPassed(hasLen);
        ruleUpper.setPassed(hasUpper);
        ruleSpecial.setPassed(hasSpecial);
        ruleNumber.setPassed(hasNumber);

        int score;
        try {
            String user   = suUsername.getText().trim().isEmpty() ? "placeholder" : suUsername.getText().trim();
            String padded = pwd.length() < 8 ? pwd + "XXXXXXXX" : pwd;
            score = new PasswordScore(new Password(user, padded)).getScore();
        } catch (InvalidPasswordException ignored) { score = 0; }

        segmentedBar.set(score);
        Color lc; String lv;
        if      (score < 40)  { lc = C_ERROR_RED;      lv = "Weak"; }
        else if (score <= 70) { lc = C_WARNING_YELLOW; lv = "Medium"; }
        else                  { lc = C_ACCENT_GREEN;   lv = "Strong"; }
        strengthText.setText("  " + lv);
        strengthText.setForeground(lc);
        strengthDot.animateTo(lc);
    }

    private void animateStrengthCard(boolean open) {
        if (strengthTimer != null && strengthTimer.isRunning()) strengthTimer.stop();
        if (open && !strengthCardClip.isVisible()) { clipHeight = 0; strengthCardClip.setVisible(true); }
        strengthTimer = new Timer(8, e -> {
            clipHeight = open ? Math.min(clipHeight + 8, CARD_HEIGHT) : Math.max(clipHeight - 8, 0);
            strengthCardClip.revalidate();
            strengthCardClip.repaint();
            Container par = strengthCardClip.getParent();
            if (par != null) { par.revalidate(); par.repaint(); }
            if ((open && clipHeight == CARD_HEIGHT) || (!open && clipHeight == 0)) {
                ((Timer) e.getSource()).stop();
                if (!open) strengthCardClip.setVisible(false);
            }
        });
        strengthTimer.start();
    }

    // ── actions ───────────────────────────────────────────────────────────────

    private void doSignIn() {
        String username = siUsername.getText().trim();
        String password = siPassword.getText();
        if (username.isEmpty() || password.isEmpty()) { siError.setText("⚠  Please fill in all fields"); return; }
        try {
            int score = new PasswordScore(new Password(username, password)).getScore();
            showResult(username, password, score);
        } catch (InvalidPasswordException ex) { siError.setText("⚠  " + ex.getMessage()); }
    }

    private void doSignUp() {
        String username = suUsername.getText().trim();
        String password = suPassword.getText();
        String confirm  = suConfirm.getText();
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) { suError.setText("⚠  Please fill in all fields"); return; }
        if (!password.equals(confirm)) { suError.setText("⚠  Passwords do not match"); return; }
        try {
            int score = new PasswordScore(new Password(username, password)).getScore();
            showResult(username, password, score);
        } catch (InvalidPasswordException ex) { suError.setText("⚠  " + ex.getMessage()); }
    }

    private void showResult(String user, String password, int score) {
        frame.getContentPane().removeAll();
        frame.add(new ResultPanel(frame, user, password, score));
        frame.revalidate();
        frame.repaint();
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private JPanel vbox() {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(C_BG); return p;
    }

    private Component gap(int h) { return Box.createVerticalStrut(h); }

    private JLabel title(String t) {
        JLabel l = new JLabel(t); l.setFont(FONT_TITLE); l.setForeground(C_TEXT_PRIMARY);
        l.setAlignmentX(LEFT_ALIGNMENT); return l;
    }

    private JLabel sub(String t) {
        JLabel l = new JLabel(t); l.setFont(FONT_SUB); l.setForeground(C_TEXT_SECONDARY);
        l.setAlignmentX(LEFT_ALIGNMENT); return l;
    }

    private JLabel errorLabel() {
        JLabel l = new JLabel(" "); l.setFont(FONT_SMALL); l.setForeground(C_ERROR_RED);
        l.setAlignmentX(LEFT_ALIGNMENT); return l;
    }

    private JPanel footerLink(String plain, String action, Runnable r) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        row.setBackground(C_BG); row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        row.setAlignmentX(LEFT_ALIGNMENT);
        JLabel l = new JLabel(plain); l.setFont(FONT_SUB); l.setForeground(C_TEXT_SECONDARY);
        JButton b = new JButton(action);
        b.setFont(new Font("SF Pro Display", Font.BOLD, 13)); b.setForeground(C_PRIMARY);
        b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(0, 0, 0, 0));
        b.addActionListener(e -> r.run());
        row.add(l); row.add(b); return row;
    }

    // lerp helper used by inner classes
    static int lerp(int a, int b, int steps) {
        int d = (b - a) / Math.max(1, steps);
        return (Math.abs(b - a) < 3) ? b : a + d;
    }

    static Color lerpColor(Color a, Color b, float t) {
        float s = 1f - t;
        return new Color(
            (int)(a.getRed()   * s + b.getRed()   * t),
            (int)(a.getGreen() * s + b.getGreen() * t),
            (int)(a.getBlue()  * s + b.getBlue()  * t)
        );
    }

    // ── submit button with ripple ─────────────────────────────────────────────

    private JButton submitBtn(String text) {
        JButton b = new JButton(text) {
            private float ripple = 0f;
            private Timer rippleTimer;
            { addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (rippleTimer != null && rippleTimer.isRunning()) rippleTimer.stop();
                    ripple = 0.3f; repaint();
                    rippleTimer = new Timer(14, ev -> {
                        ripple = Math.max(0f, ripple - 0.025f); repaint();
                        if (ripple == 0f) ((Timer) ev.getSource()).stop();
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
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        b.setFont(FONT_BOLD); b.setBackground(C_PRIMARY); b.setForeground(Color.WHITE);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        b.setPreferredSize(new Dimension(0, 50));
        b.setAlignmentX(LEFT_ALIGNMENT);
        b.setFocusPainted(false); b.setBorderPainted(false); b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
            cur[0] = new Color(lerp(cur[0].getRed(), target.getRed(), 3),
                               lerp(cur[0].getGreen(), target.getGreen(), 3),
                               lerp(cur[0].getBlue(), target.getBlue(), 3));
            b.setBackground(cur[0]);
            if (cur[0].equals(target)) ((Timer) e.getSource()).stop();
        }); ref[0].start();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  FloatLabelField — animated floating label, hover bg, animated border
    //  Three completely independent timers so nothing fights anything else.
    // ══════════════════════════════════════════════════════════════════════════

    static class FloatLabelField extends JPanel {

        private final String    placeholder;
        private final boolean   isPassword;
        private final JTextField  textField;
        private final JPasswordField passField;
        private final JButton   eyeBtn;

        // label animation (0 = idle/centre, 1 = floated/top)
        private float labelP    = 0f;
        private Timer labelTimer;

        // background animation (0 = white, 1 = C_HOVER_BG)
        private float bgP       = 0f;
        private Timer bgTimer;

        // border colour animation
        private Color borderCol = C_BORDER_IDLE;
        private Timer borderTimer;

        // focus glow animation (0 = no glow, 1 = full glow)
        private float focusP = 0f;
        private Timer focusTimer;

        private boolean focused = false;
        private Runnable typingListener;

        FloatLabelField(String placeholder, boolean isPassword) {
            this.placeholder = placeholder;
            this.isPassword  = isPassword;
            setOpaque(false);
            setLayout(null);
            setPreferredSize(new Dimension(0, 72));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
            setAlignmentX(LEFT_ALIGNMENT);

            if (isPassword) {
                passField = new JPasswordField();
                textField = null;
                passField.setEchoChar('•');
                setupField(passField);
                add(passField);

                eyeBtn = buildEye();
                add(eyeBtn);
            } else {
                textField = new JTextField();
                passField = null;
                setupField(textField);
                add(textField);
                eyeBtn = null;
            }

            // hover on the wrapper panel itself
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { if (!focused) animBg(0.6f); }
                public void mouseExited (MouseEvent e) { if (!focused) animBg(0f); }
            });
        }

        private void setupField(JTextField f) {
            f.setBorder(null);
            f.setOpaque(false);
            f.setBackground(new Color(0, 0, 0, 0));
            f.setFont(FONT_BODY);
            f.setForeground(C_TEXT_PRIMARY);
            f.setCaretColor(C_PRIMARY);

            f.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    focused = true;
                    animLabel(1f);
                    animBg(1f);
                    animBorder(C_BORDER_FOC);
                    animFocus(1f);
                }
                public void focusLost(FocusEvent e) {
                    focused = false;
                    if (getText().isEmpty()) animLabel(0f);
                    animBg(0f);
                    animBorder(C_BORDER_IDLE);
                    animFocus(0f);
                }
            });

            f.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { if (typingListener != null) typingListener.run(); }
                public void removeUpdate(DocumentEvent e) {
                    if (!focused && getText().isEmpty()) animLabel(0f);
                    if (typingListener != null) typingListener.run();
                }
                public void changedUpdate(DocumentEvent e) {}
            });
        }

        void addTypingListener(Runnable r) { typingListener = r; }

        String getText() {
            return isPassword ? new String(passField.getPassword()) : textField.getText();
        }

        // ── three independent animation methods ──────────────────────────────

        private void animLabel(float target) {
            if (labelTimer != null && labelTimer.isRunning()) labelTimer.stop();
            labelTimer = new Timer(10, e -> {
                float d = target - labelP;
                if (Math.abs(d) < 0.015f) { labelP = target; repaint(); ((Timer)e.getSource()).stop(); return; }
                labelP += d * 0.22f;   // easeOut feel
                repaint();
            }); labelTimer.start();
        }

        private void animBg(float target) {
            if (bgTimer != null && bgTimer.isRunning()) bgTimer.stop();
            bgTimer = new Timer(12, e -> {
                float d = target - bgP;
                if (Math.abs(d) < 0.01f) { bgP = target; repaint(); ((Timer)e.getSource()).stop(); return; }
                bgP += d * 0.18f;
                repaint();
            }); bgTimer.start();
        }

        private void animBorder(Color target) {
            if (borderTimer != null && borderTimer.isRunning()) borderTimer.stop();
            borderTimer = new Timer(12, e -> {
                borderCol = new Color(
                    lerp(borderCol.getRed(),   target.getRed(),   4),
                    lerp(borderCol.getGreen(), target.getGreen(), 4),
                    lerp(borderCol.getBlue(),  target.getBlue(),  4));
                repaint();
                if (borderCol.equals(target)) ((Timer)e.getSource()).stop();
            }); borderTimer.start();
        }

        private void animFocus(float target) {
            if (focusTimer != null && focusTimer.isRunning()) focusTimer.stop();
            focusTimer = new Timer(12, e -> {
                float d = target - focusP;
                if (Math.abs(d) < 0.012f) { focusP = target; repaint(); ((Timer)e.getSource()).stop(); return; }
                focusP += d * 0.2f;
                repaint();
            }); focusTimer.start();
        }

        // ── layout ────────────────────────────────────────────────────────────
        // Field area is always y=16..72 (56px). Never changes regardless of label.

        private static final int FIELD_TOP = 16;
        private static final int FIELD_H   = 56;

        public void doLayout() {
            int w    = getWidth();
            int eyeW = isPassword ? 44 : 0;
            int fieldX = 16, fieldW = w - fieldX - eyeW - 6;

            if (isPassword) {
                passField.setBounds(fieldX, FIELD_TOP + 8, fieldW, FIELD_H - 16);
                eyeBtn.setBounds(w - eyeW - 6, 0, eyeW, getHeight());
            } else {
                textField.setBounds(fieldX, FIELD_TOP + 8, fieldW, FIELD_H - 16);
            }
        }

        // ── paint ─────────────────────────────────────────────────────────────
        // Background and border cover only the FIELD area (y=FIELD_TOP, h=FIELD_H).
        // The label floats in the top 16px gap — never causes the field to resize.

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // base bg is always #f9fafb; hover lerps to #f0f1f3
            Color bg = lerpColor(C_FIELD_BG, C_HOVER_BG, bgP);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, FIELD_TOP, getWidth(), FIELD_H, 10, 10));

            // focus glow: two soft rings just outside the field box
            if (focusP > 0) {
                int r = C_PRIMARY.getRed(), gv = C_PRIMARY.getGreen(), b2 = C_PRIMARY.getBlue();
                g2.setStroke(new BasicStroke(3f));
                g2.setColor(new Color(r, gv, b2, (int)(18 * focusP)));
                g2.draw(new RoundRectangle2D.Float(
                    -1.5f, FIELD_TOP - 1.5f, getWidth() + 3, FIELD_H + 3, 12, 12));
                g2.setStroke(new BasicStroke(5f));
                g2.setColor(new Color(r, gv, b2, (int)(8 * focusP)));
                g2.draw(new RoundRectangle2D.Float(
                    -3f, FIELD_TOP - 3f, getWidth() + 6, FIELD_H + 6, 15, 15));
            }

            // border
            float bw = 1.5f;
            g2.setStroke(new BasicStroke(bw));
            g2.setColor(borderCol);
            g2.draw(new RoundRectangle2D.Float(
                bw / 2, FIELD_TOP + bw / 2,
                getWidth() - bw, FIELD_H - bw, 10, 10));

            paintLabel(g2);
            g2.dispose();
        }

        private void paintLabel(Graphics2D g2) {
            float p  = labelP;
            float ep = easeOutCubic(p);

            // idle   → inside field centre:  y ≈ FIELD_TOP + FIELD_H * 0.60 = 49.6
            // floated → above field border:   y ≈ FIELD_TOP - 6             = 10
            float idleY  = FIELD_TOP + FIELD_H * 0.60f;
            float floatY = FIELD_TOP - 6f;
            float y      = idleY + (floatY - idleY) * ep;

            // size: 14 idle → 11 floated
            float sz = 14f + (11f - 14f) * p;

            // colour + opacity: idle 60% opaque grey, floated 100% dark
            Color base = lerpColor(C_LABEL_IDLE, C_LABEL_UP, p);
            float alpha = 0.60f + 0.40f * ep;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setFont(new Font("SF Pro Display", Font.PLAIN, (int) sz));
            g2.setColor(base);
            g2.drawString(placeholder, 16, (int) y);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        private static float easeOutCubic(float t) {
            return 1f - (float) Math.pow(1 - t, 3);
        }

        // ── eye icon (custom-drawn SVG-style) ─────────────────────────────────
        // Shows almond eye + iris when password is hidden; adds diagonal slash
        // when password is visible.

        private JButton buildEye() {
            JButton eye = new JButton() {
                private float hoverP = 0f;
                private Timer hoverTimer;
                {
                    addMouseListener(new MouseAdapter() {
                        public void mouseEntered(MouseEvent e) { animHover(1f); }
                        public void mouseExited (MouseEvent e) { animHover(0f); }
                    });
                }
                private void animHover(float t) {
                    if (hoverTimer != null && hoverTimer.isRunning()) hoverTimer.stop();
                    hoverTimer = new Timer(12, e -> {
                        float d = t - hoverP;
                        if (Math.abs(d) < 0.02f) { hoverP = t; repaint(); ((Timer) e.getSource()).stop(); return; }
                        hoverP += d * 0.25f; repaint();
                    }); hoverTimer.start();
                }
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int cx = getWidth() / 2;
                    int cy = FIELD_TOP + FIELD_H / 2;

                    // soft blue glow ring on hover
                    if (hoverP > 0) {
                        g2.setColor(new Color(37, 99, 235, (int)(30 * hoverP)));
                        g2.fill(new Ellipse2D.Float(cx - 14, cy - 14, 28, 28));
                    }

                    boolean hidden = passField != null && passField.echoCharIsSet();
                    Color iconColor = lerpColor(new Color(0xBBBEC4), new Color(0x2563EB), hoverP);
                    g2.setColor(iconColor);

                    float sw = 1.4f + 0.4f * hoverP;
                    g2.setStroke(new BasicStroke(sw, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                    float hw = 8f;                    // half-width of the almond
                    float eh = 4.5f + 1.2f * hoverP;  // lid height grows on hover

                    // almond outline as one closed path
                    Path2D.Float almond = new Path2D.Float();
                    almond.moveTo(cx - hw, cy);
                    almond.quadTo(cx, cy - eh, cx + hw, cy);
                    almond.quadTo(cx, cy + eh, cx - hw, cy);
                    almond.closePath();
                    g2.draw(almond);

                    if (hidden) {
                        // open eye: iris grows on hover
                        float ir = 2.5f + 0.8f * hoverP;
                        g2.fill(new Ellipse2D.Float(cx - ir, cy - ir, ir * 2, ir * 2));
                    } else {
                        // closed eye: clean diagonal strikethrough
                        g2.drawLine((int)(cx - hw + 2), (int)(cy + eh + 3), (int)(cx + hw - 2), (int)(cy - eh - 3));
                    }

                    g2.dispose();
                }
            };
            eye.setPreferredSize(new Dimension(44, 72));
            eye.setBorderPainted(false);
            eye.setContentAreaFilled(false);
            eye.setFocusPainted(false);
            eye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            eye.addActionListener(e -> {
                passField.setEchoChar(passField.echoCharIsSet() ? (char) 0 : '•');
                eye.repaint();
            });
            return eye;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Strength card sub-components
    // ══════════════════════════════════════════════════════════════════════════

    private static class RoundedCardPanel extends JPanel {
        RoundedCardPanel() { setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // layered shadow: 3 translucent rects offset downward
            g2.setColor(new Color(0, 0, 0, 6));
            g2.fillRoundRect(2, 5, getWidth()-4, getHeight()-2, 14, 14);
            g2.setColor(new Color(0, 0, 0, 8));
            g2.fillRoundRect(1, 3, getWidth()-2, getHeight()-1, 13, 13);
            g2.setColor(new Color(0, 0, 0, 6));
            g2.fillRoundRect(0, 1, getWidth(), getHeight(), 12, 12);
            // card fill
            g2.setColor(new Color(0xF8F9FA));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            // border
            g2.setColor(new Color(0xE5E7EB));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class DotIcon extends JPanel {
        private Color current;
        private Color target;
        private Timer timer;

        DotIcon(Color c) {
            current = c; target = c;
            setOpaque(false); setPreferredSize(new Dimension(14, 18));
        }

        void animateTo(Color c) {
            target = c;
            if (timer != null && timer.isRunning()) timer.stop();
            timer = new Timer(12, e -> {
                current = new Color(lerp(current.getRed(), target.getRed(), 4),
                                    lerp(current.getGreen(), target.getGreen(), 4),
                                    lerp(current.getBlue(), target.getBlue(), 4));
                repaint();
                if (current.equals(target)) ((Timer)e.getSource()).stop();
            }); timer.start();
        }

        // keep old API working
        void setColor(Color c) { animateTo(c); }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(current);
            g2.fill(new Ellipse2D.Float(1, 4, 11, 11));
            g2.dispose();
        }
    }

    class SegmentedBar extends JPanel {
        private int   litSegs = 0, targetSegs = 0;
        private Color segColor = C_ERROR_RED, targetColor = segColor;
        private Timer timer;

        SegmentedBar() {
            setOpaque(false);
            setPreferredSize(new Dimension(0, 8));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
            setAlignmentX(LEFT_ALIGNMENT);
        }

        void set(int score) {
            targetSegs  = score < 40 ? 1 : (score <= 70 ? 2 : 3);
            targetColor = score < 40 ? C_ERROR_RED : (score <= 70 ? C_WARNING_YELLOW : C_ACCENT_GREEN);
            if (timer != null && timer.isRunning()) timer.stop();
            timer = new Timer(16, e -> {
                segColor = new Color(lerp(segColor.getRed(), targetColor.getRed(), 5),
                                     lerp(segColor.getGreen(), targetColor.getGreen(), 5),
                                     lerp(segColor.getBlue(), targetColor.getBlue(), 5));
                if      (litSegs < targetSegs) litSegs++;
                else if (litSegs > targetSegs) litSegs--;
                repaint();
                boolean close = Math.abs(segColor.getRed()-targetColor.getRed())<4
                    && Math.abs(segColor.getGreen()-targetColor.getGreen())<4
                    && Math.abs(segColor.getBlue()-targetColor.getBlue())<4;
                if (litSegs == targetSegs && close) { segColor = targetColor; repaint(); ((Timer)e.getSource()).stop(); }
            }); timer.start();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int gap = 5, sw = (getWidth() - gap * 2) / 3;
            for (int i = 0; i < 3; i++) {
                g2.setColor(i < litSegs ? segColor : new Color(0xE5E7EB));
                g2.fill(new RoundRectangle2D.Float(i * (sw + gap), 0, sw, 8, 4, 4));
            }
            g2.dispose();
        }
    }

    class RuleRow extends JPanel {
        private final RuleIcon icon;
        private final JLabel   text;
        private Color textColor = new Color(0xBBBBBB);
        private Timer textTimer;

        RuleRow(String content) {
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
            setAlignmentX(LEFT_ALIGNMENT);
            icon = new RuleIcon(); add(icon);
            text = new JLabel("  " + content);
            text.setFont(FONT_SMALL); text.setForeground(textColor); add(text);
        }

        void setPassed(boolean pass) {
            icon.animate(pass);
            Color target = pass ? C_TEXT_PRIMARY : new Color(0xBBBBBB);
            if (textTimer != null && textTimer.isRunning()) textTimer.stop();
            textTimer = new Timer(12, e -> {
                textColor = new Color(lerp(textColor.getRed(), target.getRed(), 4),
                                      lerp(textColor.getGreen(), target.getGreen(), 4),
                                      lerp(textColor.getBlue(), target.getBlue(), 4));
                text.setForeground(textColor);
                if (textColor.equals(target)) ((Timer)e.getSource()).stop();
            }); textTimer.start();
        }
    }

    class RuleIcon extends JPanel {
        private float p = 0f;
        private Timer timer;
        RuleIcon() { setOpaque(false); setPreferredSize(new Dimension(14, 14)); }

        void animate(boolean pass) {
            if (timer != null && timer.isRunning()) timer.stop();
            float target = pass ? 1f : 0f;
            timer = new Timer(10, e -> {
                float d = target - p;
                if (Math.abs(d) < 0.03f) { p = target; repaint(); ((Timer)e.getSource()).stop(); return; }
                p += d * 0.18f; repaint();
            }); timer.start();
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color gray = new Color(0xD1D5DB);
            g2.setColor(lerpColor(gray, C_ACCENT_GREEN, p));
            g2.fill(new Ellipse2D.Float(1, 1, 12, 12));
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            if (p > 0.5f) { g2.drawLine(3, 7, 5, 10); g2.drawLine(5, 10, 10, 4); }
            else          { g2.drawLine(3, 3, 10, 10); g2.drawLine(10, 3, 3, 10); }
            g2.dispose();
        }
    }
}
