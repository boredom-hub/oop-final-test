import javax.swing.*;
import java.awt.*;

public class Login {

    JFrame frame;
    JTextField txtUsername;
    JPasswordField txtPassword;
    JLabel lblError;

    public Login() {

        frame = new JFrame("Login");
        frame.setSize(460, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Theme.BG);

        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(Theme.BG);

        JPanel box = UiKit.transparent(null);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.add(left(UiKit.label("Product Pricing Calculator", Font.BOLD, 22, Theme.TEXT)));
        box.add(Box.createVerticalStrut(20));

        UiKit.RoundedPanel card = new UiKit.RoundedPanel(null, 16);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(22, 24, 24, 24));

        card.add(left(UiKit.label("Login", Font.BOLD, 17, Theme.TEXT)));
        card.add(Box.createVerticalStrut(4));
        card.add(left(UiKit.label("Enter your username and password.", Font.PLAIN, 12, Theme.MUTED)));
        card.add(Box.createVerticalStrut(16));

        // username
        card.add(left(UiKit.label("Username", Font.BOLD, 13, Theme.TEXT)));
        card.add(Box.createVerticalStrut(6));
        txtUsername = new UiKit.RoundedTextField(20);
        card.add(left(txtUsername));
        card.add(Box.createVerticalStrut(12));

        // password
        card.add(left(UiKit.label("Password", Font.BOLD, 13, Theme.TEXT)));
        card.add(Box.createVerticalStrut(6));
        txtPassword = new UiKit.RoundedPasswordField(20);
        card.add(left(txtPassword));

        // red message under the fields (a single space so the card doesnt jump when it shows up)
        lblError = UiKit.label(" ", Font.PLAIN, 12, Theme.RED);
        lblError.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        card.add(left(lblError));
        card.add(Box.createVerticalStrut(12));

        UiKit.RoundedButton btnLogin = UiKit.primaryButton("Login", null);
        UiKit.RoundedButton btnBack = UiKit.outlineButton("Back");

        btnLogin.addActionListener(e -> doLogin());

        btnBack.addActionListener(e -> {
            frame.dispose();
            new MainFrame();
        });

        card.add(left(btnLogin));
        card.add(Box.createVerticalStrut(8));
        card.add(left(btnBack));
        stretch(btnLogin);
        stretch(btnBack);

        card.setPreferredSize(new Dimension(360, card.getPreferredSize().height));

        box.add(left(card));
        page.add(box);
        frame.add(page);

        // enter key = login button
        frame.getRootPane().setDefaultButton(btnLogin);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JComponent left(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    private static void stretch(JComponent c) {
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
    }

    private void doLogin() {

        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (Register.savedUsername.isEmpty()) {
            lblError.setText("No account yet. Please register first.");
            return;
        }

        if (username.equals(Register.savedUsername)
                && password.equals(Register.savedPassword)) {

            JOptionPane.showMessageDialog(frame, "Login successful.");

            frame.dispose();
            new Calculator();

        } else {
            lblError.setText("Invalid username or password.");
        }
    }
}
