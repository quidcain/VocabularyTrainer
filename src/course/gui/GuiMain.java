package course.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by user on 31.10.2016.
 */
public class GuiMain {
    private JFrame jfrm;
    private JPanel jpSignup, jpLogin, panelY, panelX;
    private JTabbedPane tabPanel;
    private void setLogin(){
        jpLogin = new JPanel();
        jpLogin.setLayout(new BoxLayout(jpLogin, BoxLayout.Y_AXIS));
        jpLogin.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        jpLogin.setMaximumSize(new Dimension(320, 9999));

        JLabel labelNick = new JLabel("Ваш никнейм:");
        labelNick.setMaximumSize(new Dimension(9999, 15));
        JFormattedTextField textFieldNick = new JFormattedTextField();
        textFieldNick.setMaximumSize(new Dimension(9999, 25));
        JLabel labelPass = new JLabel("Ваш пароль:");
        labelPass.setMaximumSize(new Dimension(9999, 15));
        JFormattedTextField textFieldPass = new JFormattedTextField();
        textFieldPass.setMaximumSize(new Dimension(9999, 25));
        JButton buttonLogin = new JButton("Войти");

        jpLogin.add(labelNick);
        jpLogin.add(Box.createRigidArea(new Dimension(0, 5)));
        jpLogin.add(textFieldNick);
        jpLogin.add(Box.createRigidArea(new Dimension(0, 15)));
        jpLogin.add(labelPass);
        jpLogin.add(Box.createRigidArea(new Dimension(0, 5)));
        jpLogin.add(textFieldPass);
        jpLogin.add(Box.createRigidArea(new Dimension(0, 15)));
        jpLogin.add(buttonLogin);
    }
    private void setSignup(){
        jpSignup = new JPanel();
        jpSignup.setLayout(new BoxLayout(jpSignup, BoxLayout.Y_AXIS));
        jpSignup.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        jpSignup.setMaximumSize(new Dimension(320, 9999));

        JLabel labelNick = new JLabel("Ваш никнейм:");
        labelNick.setMaximumSize(new Dimension(9999, 15));
        JFormattedTextField textFieldNick = new JFormattedTextField();
        textFieldNick.setMaximumSize(new Dimension(9999, 25));
        JLabel labelPass = new JLabel("Ваш пароль:");
        labelPass.setMaximumSize(new Dimension(9999, 15));
        JFormattedTextField textFieldPass = new JFormattedTextField();
        textFieldPass.setMaximumSize(new Dimension(9999, 25));
        JButton buttonSignup = new JButton("Зарегестрироваться");

        jpSignup.add(labelNick);
        jpSignup.add(Box.createRigidArea(new Dimension(0, 5)));
        jpSignup.add(textFieldNick);
        jpSignup.add(Box.createRigidArea(new Dimension(0, 15)));
        jpSignup.add(labelPass);
        jpSignup.add(Box.createRigidArea(new Dimension(0, 5)));
        jpSignup.add(textFieldPass);
        jpSignup.add(Box.createRigidArea(new Dimension(0, 15)));
        jpSignup.add(buttonSignup);
    }
    GuiMain() {
        jfrm = new JFrame("И снова здравствуйте");
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfrm.setSize(320, 260);
        jfrm.setMinimumSize(new Dimension(320, 260));
        jfrm.setLayout(new BorderLayout());

        panelX = new JPanel();
        panelX.setLayout(new BoxLayout(panelX, BoxLayout.X_AXIS));
        panelY = new JPanel();
        panelY.setMaximumSize(new Dimension(9999, 260));
        panelY.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        panelY.setLayout(new BoxLayout(panelY, BoxLayout.Y_AXIS));

        tabPanel = new JTabbedPane();
        tabPanel.setMaximumSize(new Dimension(320, 220));

        setLogin();
        setSignup();
        tabPanel.addTab("Вход", jpLogin);
        tabPanel.addTab("Регистрация", jpSignup);
        panelY.add(tabPanel);
        panelX.add(panelY);
        jfrm.add(panelX);
        jfrm.setLocationRelativeTo(null);
        jfrm.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                new GuiMain();
            }
        });
    }
}
