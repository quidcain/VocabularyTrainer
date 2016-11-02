package course.gui;

import course.gui.JTextFieldLimit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by user on 31.10.2016.
 */
public class GuiMain {
    private JFrame jfrm;
    private JPanel jpSignup, jpLogin, cardPanel1, cardPanel2, mainPanel;
    private JTabbedPane tabPanel;
    private String AUTHORIZATION = "Авторизация";
    private String SMTHANOTHER = "Что-то другое";
    private JTextFieldLimit nickLimit;
    private JTextFieldLimit passLimit;
    private void setLogin(){
        jpLogin = new JPanel();
        jpLogin.setLayout(new BoxLayout(jpLogin, BoxLayout.Y_AXIS));
        jpLogin.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel labelNick = new JLabel("Ваш никнейм:");
        labelNick.setMaximumSize(new Dimension(9999, 15));
        JTextField textFieldNick = new JTextField();
        textFieldNick.setDocument(nickLimit);
        textFieldNick.setMaximumSize(new Dimension(9999, 25));
        JLabel labelPass = new JLabel("Ваш пароль:");
        labelPass.setMaximumSize(new Dimension(9999, 15));
        JTextField textFieldPass = new JTextField();
        textFieldPass.setMaximumSize(new Dimension(9999, 25));
        textFieldPass.setDocument(passLimit);
        JButton buttonLogin = new JButton("Войти");
        JLabel labelLog = new JLabel();
        labelLog.setForeground(Color.red);

        jpLogin.add(labelNick);
        jpLogin.add(Box.createRigidArea(new Dimension(0, 5)));
        jpLogin.add(textFieldNick);
        jpLogin.add(Box.createRigidArea(new Dimension(0, 15)));
        jpLogin.add(labelPass);
        jpLogin.add(Box.createRigidArea(new Dimension(0, 5)));
        jpLogin.add(textFieldPass);
        jpLogin.add(Box.createRigidArea(new Dimension(0, 15)));
        jpLogin.add(buttonLogin);
        jpLogin.add(Box.createRigidArea(new Dimension(0, 15)));
        jpLogin.add(labelLog);
        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = textFieldNick.getText();
                String password = textFieldPass.getText();
                labelLog.setText("");
                if (nickname.equals("") || password.equals("")) {
                    labelLog.setText("Пароль и логин не могут быть пусты!");
                    return;
                }
                //CardLayout cl = (CardLayout)(mainPanel.getLayout());
                //cl.show(mainPanel, SMTHANOTHER);
            }
        });

    }
    private void setSignup(){
        jpSignup = new JPanel();
        jpSignup.setLayout(new BoxLayout(jpSignup, BoxLayout.Y_AXIS));
        jpSignup.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel labelNick = new JLabel("Ваш никнейм:");
        labelNick.setMaximumSize(new Dimension(9999, 15));
        JTextField textFieldNick = new JTextField();
        textFieldNick.setMaximumSize(new Dimension(9999, 25));
        textFieldNick.setDocument(nickLimit);
        JLabel labelPass = new JLabel("Ваш пароль:");
        labelPass.setMaximumSize(new Dimension(9999, 15));
        JTextField textFieldPass = new JTextField();
        textFieldPass.setMaximumSize(new Dimension(9999, 25));
        textFieldPass.setDocument(passLimit);
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
        jfrm.setSize(290, 280);
        jfrm.setMinimumSize(new Dimension(290, 280));

        mainPanel = new JPanel(new CardLayout());

        cardPanel1 = new JPanel(new GridBagLayout());
        tabPanel = new JTabbedPane();

        nickLimit = new JTextFieldLimit(20);
        passLimit = new JTextFieldLimit(20);
        setLogin();
        setSignup();

        cardPanel2 = new JPanel();

        tabPanel.addTab("Вход", jpLogin);
        tabPanel.addTab("Регистрация", jpSignup);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 40;
        cardPanel1.add(tabPanel, c);
        mainPanel.add(cardPanel1, AUTHORIZATION);
        mainPanel.add(cardPanel2, SMTHANOTHER);
        jfrm.add(mainPanel);
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
