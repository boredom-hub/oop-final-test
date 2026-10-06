import javax.swing.*;
import java.awt.*;

public class MainFrame {

    JFrame frame;

    public MainFrame() {

        frame = new JFrame("Product Pricing Calculator");
        frame.setSize(460, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Theme.BG);

        // this panel keeps everything in the middle of the window
        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(Theme.BG);

        JPanel box = UiKit.transparent(null);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        // title above the card
        box.add(left(UiKit.label("Product Pricing Calculator", Font.BOLD, 22, Theme.TEXT)));
        box.add(Box.createVerticalStrut(4));
        box.add(left(UiKit.label("Work out your price, break-even point and profit.",
                Font.PLAIN, 12, Theme.MUTED)));
        box.add(Box.createVerticalStrut(20));

        // the card
        UiKit.RoundedPanel card = new UiKit.RoundedPanel(null, 16);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(22, 24, 24, 24));

        card.add(left(UiKit.label("Welcome", Font.BOLD, 17, Theme.TEXT)));
        card.add(Box.createVerticalStrut(4));
        card.add(left(UiKit.label("Choose an option to continue.", Font.PLAIN, 12, Theme.MUTED)));
        card.add(Box.createVerticalStrut(18));

        UiKit.RoundedButton btnLogin = UiKit.primaryButton("Login", null);
        UiKit.RoundedButton btnRegister = UiKit.outlineButton("Register");

        btnLogin.addActionListener(e -> {
            frame.dispose();
            new Login();
        });

        btnRegister.addActionListener(e -> {
            frame.dispose();
            new Register();
        });

        card.add(left(btnLogin));
        card.add(Box.createVerticalStrut(10));
        card.add(left(btnRegister));
        stretch(btnLogin);
        stretch(btnRegister);

        // fixed width for the card, the height is whatever it needs
        card.setPreferredSize(new Dimension(360, card.getPreferredSize().height));

        box.add(left(card));
        page.add(box);
        frame.add(page);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // BoxLayout wants everything lined up on the left
    private static JComponent left(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    // makes a button as wide as the card
    private static void stretch(JComponent c) {
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
