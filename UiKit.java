import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// dark theme building blocks used by all the frames
// just plain Swing (Java2D painting + a few look and feel changes), no extra libraries
public final class UiKit {

    private UiKit() { }

    // ----- small helpers -----
    public static void smooth(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    public static JLabel label(String text, int style, float size, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.font(style, size));
        l.setForeground(color);
        return l;
    }

    // escapes text so it can go inside an HTML label (newlines become <br>)
    public static String esc(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>");
    }

    // label that wraps at the given width
    public static JLabel wrapped(String text, int widthPx, float size, Color color) {
        JLabel l = new JLabel("<html><body style='width:" + widthPx + "px'>" + esc(text) + "</body></html>");
        l.setFont(Theme.font(Font.PLAIN, size));
        l.setForeground(color);
        return l;
    }

    // thin vertical line, as tall as its row
    public static JComponent vLine() {
        JPanel p = new JPanel();
        p.setBackground(Theme.BORDER);
        p.setPreferredSize(new Dimension(1, 1));
        return p;
    }

    // thin horizontal line
    public static JComponent hLine() {
        JPanel p = new JPanel();
        p.setBackground(Theme.BORDER);
        p.setPreferredSize(new Dimension(1, 1));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return p;
    }

    // label with its (i) icon next to it
    public static JPanel labelRow(JLabel label, JComponent info) {
        JPanel row = transparent(new FlowLayout(FlowLayout.LEFT, 0, 0));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));
        row.add(label);
        row.add(info);
        return row;
    }

    public static JPanel transparent(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(false);
        return p;
    }

    public static JScrollPane scroll(JComponent view) {
        JScrollPane sp = new JScrollPane(view, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(null);
        sp.setBackground(Theme.BG);
        sp.getViewport().setBackground(Theme.BG);
        styleScrollBar(sp.getVerticalScrollBar());
        sp.getVerticalScrollBar().setUnitIncrement(20);
        return sp;
    }

    private static void styleScrollBar(JScrollBar bar) {
        bar.setUI(new DarkScrollBarUI());
        bar.setBackground(Theme.BG);
    }

    // ----- rounded panel ("card") -----
    public static class RoundedPanel extends JPanel {
        private final int arc;
        private Color fill = Theme.BG;
        private Color line = Theme.BORDER;
        private boolean dashed;

        public RoundedPanel(LayoutManager layout, int arc) {
            super(layout);
            this.arc = arc;
            setOpaque(false);
        }

        public RoundedPanel dashed(boolean value) {
            this.dashed = value;
            repaint();
            return this;
        }

        public RoundedPanel fill(Color c) {
            this.fill = c;
            repaint();
            return this;
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            smooth(g2);
            int w = getWidth() - 1;
            int h = getHeight() - 1;
            if (fill != null) {
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, w, h, arc, arc);
            }
            g2.setColor(line);
            g2.setStroke(dashed
                    ? new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{4f, 4f}, 0f)
                    : new BasicStroke(1f));
            g2.drawRoundRect(0, 0, w, h, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ----- buttons -----
    public static class RoundedButton extends JButton {
        private final Color fill;
        private final Color hoverFill;
        private final Color border;
        private final int arc;

        public RoundedButton(String text, Icon icon, Color foreground, Color fill, Color hoverFill,
                             Color border, int arc, int padV, int padH) {
            super(text, icon);
            this.fill = fill;
            this.hoverFill = hoverFill;
            this.border = border;
            this.arc = arc;
            setForeground(foreground);
            setFont(Theme.font(Font.PLAIN, 13));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setIconTextGap(6);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(padV, padH, padV, padH));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            smooth(g2);
            boolean hot = getModel().isRollover() || getModel().isPressed();
            Color f = hot ? hoverFill : fill;
            if (f != null) {
                g2.setColor(f);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            }
            if (border != null) {
                g2.setColor(border);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // the green call-to-action button (Add, Next...)
    public static RoundedButton primaryButton(String text, Icon icon) {
        RoundedButton b = new RoundedButton(text, icon, Theme.ON_GREEN, Theme.GREEN, Theme.GREEN_HI, null, 8, 7, 14);
        b.setFont(Theme.font(Font.BOLD, 13));
        return b;
    }

    // the outlined secondary button (Back...)
    public static RoundedButton outlineButton(String text) {
        return new RoundedButton(text, null, Theme.TEXT, null, Theme.SURFACE, Theme.BORDER, 8, 7, 16);
    }

    // a borderless icon-only button with a hover highlight
    public static RoundedButton iconButton(Icon icon, String tooltip) {
        RoundedButton b = new RoundedButton(null, icon, Theme.TEXT, null, Theme.SURFACE, null, 6, 7, 8);
        b.setToolTipText(tooltip);
        return b;
    }

    // a flat text button used inside a segmented input (the 50% / 75% / 100% buttons)
    public static RoundedButton segmentButton(String text) {
        RoundedButton b = new RoundedButton(text, null, Theme.TEXT, null, Theme.SURFACE, null, 0, 0, 12);
        b.setFont(Theme.font(Font.PLAIN, 12));
        return b;
    }

    // ----- text field -----
    public static class RoundedTextField extends JTextField {
        private boolean framed = true;

        public RoundedTextField(int columns) {
            super(columns);
            setOpaque(false);
            setForeground(Theme.TEXT);
            setCaretColor(Theme.TEXT);
            setSelectionColor(Theme.GREEN_HI);
            setSelectedTextColor(Color.WHITE);
            setFont(Theme.font(Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 12));
            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { repaint(); }
                public void focusLost(FocusEvent e)   { repaint(); }
            });
        }

        // set to false when the field sits inside a segment that draws the border itself
        public RoundedTextField framed(boolean value) {
            this.framed = value;
            return this;
        }

        protected void paintComponent(Graphics g) {
            if (framed) {
                Graphics2D g2 = (Graphics2D) g.create();
                smooth(g2);
                g2.setColor(Theme.BG);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(hasFocus() ? Theme.BORDER_HI : Theme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    // same look as RoundedTextField but for passwords (used on the login / register screens)
    public static class RoundedPasswordField extends JPasswordField {

        public RoundedPasswordField(int columns) {
            super(columns);
            setOpaque(false);
            setForeground(Theme.TEXT);
            setCaretColor(Theme.TEXT);
            setSelectionColor(Theme.GREEN_HI);
            setSelectedTextColor(Color.WHITE);
            setFont(Theme.font(Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 12));
            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { repaint(); }
                public void focusLost(FocusEvent e)   { repaint(); }
            });
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            smooth(g2);
            g2.setColor(Theme.BG);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            g2.setColor(hasFocus() ? Theme.BORDER_HI : Theme.BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // lets the user type only digits (and, optionally, one decimal point)
    public static void limitToNumber(JTextField field, final boolean allowDecimal) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            private boolean ok(String s) {
                return allowDecimal ? s.matches("\\d{0,9}(\\.\\d{0,4})?") : s.matches("\\d{0,9}");
            }

            public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr)
                    throws BadLocationException {
                String cur = fb.getDocument().getText(0, fb.getDocument().getLength());
                if (ok(cur.substring(0, offset) + str + cur.substring(offset))) {
                    super.insertString(fb, offset, str, attr);
                }
            }

            public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attr)
                    throws BadLocationException {
                String cur = fb.getDocument().getText(0, fb.getDocument().getLength());
                String insert = str == null ? "" : str;
                if (ok(cur.substring(0, offset) + insert + cur.substring(offset + length))) {
                    super.replace(fb, offset, length, str, attr);
                }
            }
        });
    }

    // a bordered box holding a text field with optional buttons/icons attached to its right edge
    public static RoundedPanel segment(RoundedTextField field, Component... trailing) {
        RoundedPanel box = new RoundedPanel(new BorderLayout(), 10);
        box.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        field.framed(false);
        box.add(field, BorderLayout.CENTER);
        if (trailing.length > 0) {
            JPanel east = transparent(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.VERTICAL;
            c.weighty = 1;
            c.gridy = 0;
            int x = 0;
            for (Component t : trailing) {
                c.gridx = x++;
                east.add(vLine(), c);
                c.gridx = x++;
                east.add(t, c);
            }
            box.add(east, BorderLayout.EAST);
        }
        return box;
    }

    // ----- (i) info popover -----
    // the little (i) icon. Clicking it opens a popover with a title and explanation.
    // In the text, a line that starts with "# " is shown in bold.
    public static class InfoButton extends JComponent {
        private String title;
        private String body;

        public InfoButton(String title, String body) {
            this.title = title;
            this.body = body;
            setPreferredSize(new Dimension(18, 18));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) { showPopover(); }
            });
        }

        public void setContent(String title, String body) {
            this.title = title;
            this.body = body;
        }

        private void showPopover() {
            StringBuilder html = new StringBuilder("<html><body style='width:280px'>");
            html.append("<b style='font-size:13px;color:#fafafa'>").append(esc(title)).append("</b><br>");
            for (String line : body.split("\n")) {
                if (line.startsWith("# ")) {
                    html.append("<div style='margin-top:6px;color:#fafafa'><b>").append(esc(line.substring(2)))
                            .append("</b></div>");
                } else {
                    html.append("<div style='color:#a1a1aa'>").append(esc(line)).append("</div>");
                }
            }
            html.append("</body></html>");
            JLabel content = new JLabel(html.toString());
            content.setFont(Theme.font(Font.PLAIN, 12));
            content.setForeground(Theme.MUTED);
            content.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

            JPopupMenu popup = new JPopupMenu();
            popup.setBackground(Theme.SURFACE);
            popup.setBorder(BorderFactory.createLineBorder(Theme.BORDER_HI));
            JPanel holder = new JPanel(new BorderLayout());
            holder.setBackground(Theme.SURFACE);
            holder.add(content);
            popup.add(holder);
            popup.show(this, 0, getHeight() + 4);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            smooth(g2);
            g2.setColor(Theme.MUTED);
            g2.setStroke(new BasicStroke(1.3f));
            int d = Math.min(getWidth(), getHeight()) - 4;
            int x = (getWidth() - d) / 2;
            int y = (getHeight() - d) / 2;
            g2.drawOval(x, y, d, d);
            int cx = x + d / 2;
            g2.drawLine(cx, y + d / 2 - 1, cx, y + d - 4);
            g2.fillOval(cx - 1, y + 3, 2, 2);
            g2.dispose();
        }
    }

    // ----- dark slider -----
    public static class DarkSliderUI extends BasicSliderUI {
        public DarkSliderUI(JSlider slider) {
            super(slider);
        }

        protected Dimension getThumbSize() {
            return new Dimension(18, 18);
        }

        public void paintTrack(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            smooth(g2);
            int h = 6;
            int y = trackRect.y + (trackRect.height - h) / 2;
            g2.setColor(Theme.SURFACE);
            g2.fillRoundRect(trackRect.x - 2, y, trackRect.width + 4, h, h, h);
            g2.dispose();
        }

        public void paintThumb(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            smooth(g2);
            g2.setColor(Theme.BG);
            g2.fillOval(thumbRect.x, thumbRect.y, thumbRect.width - 1, thumbRect.height - 1);
            g2.setColor(Theme.GREEN);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(thumbRect.x + 1, thumbRect.y + 1, thumbRect.width - 3, thumbRect.height - 3);
            g2.dispose();
        }

        public void paintFocus(Graphics g) { }
    }

    public static JSlider slider(int min, int max, int value) {
        JSlider s = new JSlider(min, max, value);
        s.setUI(new DarkSliderUI(s));
        s.setOpaque(false);
        s.setFocusable(true);
        s.setPreferredSize(new Dimension(200, 24));
        return s;
    }

    // ----- dark combo box -----
    public static class DarkComboBoxUI extends BasicComboBoxUI {
        protected JButton createArrowButton() {
            JButton b = new JButton(Icons.of(Icons.Kind.CHEVRON_DOWN, 14, Theme.MUTED));
            b.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 10));
            b.setContentAreaFilled(false);
            b.setFocusPainted(false);
            b.setOpaque(false);
            return b;
        }

        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) { }

        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox) {
                protected JScrollPane createScroller() {
                    JScrollPane sp = super.createScroller();
                    sp.setBorder(null);
                    sp.getViewport().setBackground(Theme.BG);
                    styleScrollBar(sp.getVerticalScrollBar());
                    return sp;
                }
            };
            popup.setBorder(BorderFactory.createLineBorder(Theme.BORDER_HI));
            return popup;
        }
    }

    // how the dropdown items look
    public static class DarkListRenderer extends javax.swing.DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean selected, boolean focus) {
            JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, selected, false);
            l.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));
            l.setFont(Theme.font(Font.PLAIN, 14));
            l.setForeground(Theme.TEXT);
            if (index < 0) {
                l.setOpaque(false);
            } else {
                l.setOpaque(true);
                l.setBackground(selected ? Theme.SURFACE : Theme.BG);
            }
            return l;
        }
    }

    // puts a combo box in a rounded box so it matches the text fields
    public static <T> RoundedPanel comboBox(JComboBox<T> combo, ListCellRenderer<? super T> renderer) {
        combo.setUI(new DarkComboBoxUI());
        combo.setBorder(BorderFactory.createEmptyBorder());
        combo.setOpaque(false);
        combo.setBackground(Theme.BG);
        combo.setForeground(Theme.TEXT);
        if (renderer == null) {
            combo.setRenderer(new DarkListRenderer());
        } else {
            combo.setRenderer(renderer);
        }
        combo.setMaximumRowCount(12);
        RoundedPanel box = new RoundedPanel(new BorderLayout(), 10);
        box.add(combo, BorderLayout.CENTER);
        return box;
    }

    // ----- dark scroll bar -----
    public static class DarkScrollBarUI extends BasicScrollBarUI {
        protected void configureScrollBarColors() {
            thumbColor = new Color(0x3F3F46);
            trackColor = Theme.BG;
        }

        private JButton zero() {
            JButton b = new JButton();
            Dimension d = new Dimension(0, 0);
            b.setPreferredSize(d);
            b.setMinimumSize(d);
            b.setMaximumSize(d);
            return b;
        }

        protected JButton createDecreaseButton(int orientation) { return zero(); }
        protected JButton createIncreaseButton(int orientation) { return zero(); }

        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            g.setColor(Theme.BG);
            g.fillRect(r.x, r.y, r.width, r.height);
        }

        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            if (r.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            smooth(g2);
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8);
            g2.dispose();
        }

        public Dimension getPreferredSize(JComponent c) {
            return new Dimension(12, 12);
        }
    }

    // grey block with an icon at the end of a segment (the box icon next to Production Quantity)
    // right corners are rounded to match the border
    public static JComponent endCap(final Icon icon) {
        return new JComponent() {
            {
                setPreferredSize(new Dimension(icon.getIconWidth() + 28, 10));
            }

            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                smooth(g2);
                g2.setClip(0, 0, getWidth(), getHeight());
                g2.setColor(Theme.SURFACE);
                g2.fillRoundRect(-20, 0, getWidth() + 20, getHeight(), 9, 9);
                icon.paintIcon(this, g2, (getWidth() - icon.getIconWidth()) / 2,
                        (getHeight() - icon.getIconHeight()) / 2);
                g2.dispose();
            }
        };
    }

    // panel for inside a JScrollPane, always as wide as the viewport so the content wraps
    public static class ScrollPage extends JPanel implements Scrollable {
        public ScrollPage(LayoutManager layout) {
            super(layout);
            setBackground(Theme.BG);
        }

        public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        public int getScrollableUnitIncrement(Rectangle r, int orientation, int direction) { return 20; }
        public int getScrollableBlockIncrement(Rectangle r, int orientation, int direction) {
            return Math.max(40, r.height - 40);
        }
        public boolean getScrollableTracksViewportWidth()  { return true; }
        public boolean getScrollableTracksViewportHeight() { return false; }
    }

    // vertical gap
    public static Component vgap(int px) {
        return Box.createVerticalStrut(px);
    }
}
