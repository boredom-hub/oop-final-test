import javax.swing.*;
import java.awt.*;

public class Register {
    static String savedUsername = "";
    static String savedPassword = "";

    JFrame frame;
    JTextField txtUsername;
    JPasswordField txtPassword;
    JPasswordField txtConfirmPass;
    JLabel lblError;

    public Register() {

        frame = new JFrame("Registration");
        frame.setSize(460, 590);
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

        card.add(left(UiKit.label("Create an account", Font.BOLD, 17, Theme.TEXT)));
        card.add(Box.createVerticalStrut(4));
        card.add(left(UiKit.label("Pick a username and a password.", Font.PLAIN, 12, Theme.MUTED)));
        card.add(Box.createVerticalStrut(16));

        card.add(left(UiKit.label("Username", Font.BOLD, 13, Theme.TEXT)));
        card.add(Box.createVerticalStrut(6));
        txtUsername = new UiKit.RoundedTextField(20);
        card.add(left(txtUsername));
        card.add(Box.createVerticalStrut(12));

        card.add(left(UiKit.label("Password", Font.BOLD, 13, Theme.TEXT)));
        card.add(Box.createVerticalStrut(6));
        txtPassword = new UiKit.RoundedPasswordField(20);
        card.add(left(txtPassword));
        card.add(Box.createVerticalStrut(12));

        card.add(left(UiKit.label("Confirm Password", Font.BOLD, 13, Theme.TEXT)));
        card.add(Box.createVerticalStrut(6));
        txtConfirmPass = new UiKit.RoundedPasswordField(20);
        card.add(left(txtConfirmPass));

        lblError = UiKit.label(" ", Font.PLAIN, 12, Theme.RED);
        lblError.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        card.add(left(lblError));
        card.add(Box.createVerticalStrut(12));

        UiKit.RoundedButton btnRegister = UiKit.primaryButton("Register", null);
        UiKit.RoundedButton btnBack = UiKit.outlineButton("Back");

        btnRegister.addActionListener(e -> doRegister());

        btnBack.addActionListener(e -> {
            frame.dispose();
            new MainFrame();
        });

        card.add(left(btnRegister));
        card.add(Box.createVerticalStrut(8));
        card.add(left(btnBack));
        stretch(btnRegister);
        stretch(btnBack);

        card.setPreferredSize(new Dimension(360, card.getPreferredSize().height));

        box.add(left(card));
        page.add(box);
        frame.add(page);

        frame.getRootPane().setDefaultButton(btnRegister);

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

    private void doRegister() {

        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirmPass.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            lblError.setText("Please complete all fields.");
            return;
        }

        if (!password.equals(confirm)){
            lblError.setText("Passwords do not match.");
            txtPassword.setText("");
            txtConfirmPass.setText("");
            txtPassword.requestFocus();
            return;
        }

        savedUsername = username;
        savedPassword = password;

        JOptionPane.showMessageDialog(frame, "Registration successful.");

        frame.dispose();
        new MainFrame();
    }
}
