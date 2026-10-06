import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

// shell shared by the 4 calculator frames: header with the steps, body from the subclass,
// Back / Next buttons at the bottom
//
// every step is its own window. going next / back closes this one and opens the other one
// and passes the same model along (same way MainFrame, Register and Login do it)
public abstract class StepFrame extends JFrame {

    protected final ProductPricingModel model;

    private final JPanel bodyHolder = new JPanel(new BorderLayout());
    private final UiKit.RoundedButton backButton = UiKit.outlineButton("Back");
    private final UiKit.RoundedButton nextButton = UiKit.primaryButton("Next", null);

    protected StepFrame(ProductPricingModel model, int step, int totalSteps, String stepTitle,
                        int width, int height) {
        super("Product Pricing Calculator - " + stepTitle);
        this.model = model;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Theme.BG);
        getContentPane().setLayout(new BorderLayout());
        bodyHolder.setBackground(Theme.BG);
        getContentPane().add(buildHeader(step, totalSteps, stepTitle), BorderLayout.NORTH);
        getContentPane().add(bodyHolder, BorderLayout.CENTER);
        getContentPane().add(buildFooter(), BorderLayout.SOUTH);

        backButton.addActionListener(e -> onBack());
        nextButton.addActionListener(e -> onNext());

        setSize(width, height);
        setMinimumSize(new Dimension(640, 460));
        // setResizable(false);
    }

    // Back button
    protected abstract void onBack();

    // Next button: check the input, save to the model, open the next frame
    protected abstract void onNext();

    protected final void setBody(JComponent body) {
        bodyHolder.removeAll();
        bodyHolder.add(body, BorderLayout.CENTER);
    }

    protected final void setBackLabel(String text) {
        backButton.setText(text);
    }

    protected final void setNextLabel(String text) {
        nextButton.setText(text);
    }

    // centers the window and shows it, call this last in the constructor
    protected final void showFrame() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

    protected final void showProblems(List<String> problems) {
        StringBuilder sb = new StringBuilder();
        for (String p : problems) {
            sb.append("- ").append(p).append('\n');
        }
        JOptionPane.showMessageDialog(this, sb.toString().trim(), "Please check your input",
                JOptionPane.WARNING_MESSAGE);
    }

    // ----- chrome -----
    private JComponent buildHeader(int step, int totalSteps, String stepTitle) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                BorderFactory.createEmptyBorder(14, 24, 14, 24)));

        JPanel text = UiKit.transparent(new java.awt.GridLayout(2, 1, 0, 2));
        text.add(UiKit.label("Product Pricing Calculator", Font.BOLD, 18, Theme.TEXT));
        text.add(UiKit.label("Step " + step + " of " + totalSteps + "  -  " + stepTitle,
                Font.PLAIN, 12, Theme.MUTED));
        header.add(text, BorderLayout.WEST);
        header.add(new StepDots(step, totalSteps), BorderLayout.EAST);
        return header;
    }

    private JComponent buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Theme.BG);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
                BorderFactory.createEmptyBorder(12, 24, 12, 24)));
        footer.add(backButton, BorderLayout.WEST);
        footer.add(nextButton, BorderLayout.EAST);
        return footer;
    }

    // the little progress bars at the top
    private static final class StepDots extends JComponent {
        private final int step;
        private final int total;

        StepDots(int step, int total) {
            this.step = step;
            this.total = total;
            setPreferredSize(new Dimension(total * 34, 30));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            UiKit.smooth(g2);
            int y = getHeight() / 2 - 3;
            for (int i = 1; i <= total; i++) {
                g2.setColor(i <= step ? Theme.GREEN : Theme.BORDER);
                g2.fillRoundRect((i - 1) * 34 + 4, y, 26, 6, 6, 6);
            }
            g2.dispose();
        }
    }
}
