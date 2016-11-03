package course.gui;

import course.gui.JTextFieldLimit;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

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

    private Database db;
    private HashMap<String, String> accounts;
    private void setLogin(){
        jpLogin = new JPanel(new GridBagLayout());

        JLabel labelNick = new JLabel("Ваш никнейм:");
        JTextField textFieldNick = new JTextField();
        textFieldNick.setPreferredSize(new Dimension(220, 20));
        textFieldNick.setDocument(nickLimit);
        JLabel labelPass = new JLabel("Ваш пароль:");
        JTextField textFieldPass = new JTextField();
        textFieldPass.setPreferredSize(new Dimension(220, 20));
        textFieldPass.setDocument(passLimit);
        JButton buttonLogin = new JButton("Войти");
        JLabel labelLog = new JLabel();
        labelLog.setForeground(Color.red);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5,0,5,0);
        jpLogin.add(labelNick, c);
        c.gridy = 1;
        c.gridwidth = 2;
        jpLogin.add(textFieldNick,c);
        c.insets = new Insets(10,0,5,0);
        c.gridy = 2;
        c.gridwidth = 1;
        jpLogin.add(labelPass,c);
        c.insets = new Insets(5,0,5,0);
        c.gridy = 3;
        c.gridwidth = 2;
        jpLogin.add(textFieldPass,c);
        c.insets = new Insets(10,0,5,0);
        c.gridy = 4;
        c.gridwidth = 1;
        jpLogin.add(buttonLogin,c);
        c.gridy = 5;
        c.gridwidth = 3;
        c.weighty = 1;
        jpLogin.add(labelLog,c);

        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = textFieldNick.getText();
                String password = textFieldPass.getText();
                labelLog.setForeground(Color.red);
                labelLog.setText("");
                if (nickname.equals("") || password.equals("")) {
                    labelLog.setText("<html>Пароль и логин<br> не могут быть пусты!</html>");
                    return;
                }
                String actualPass = accounts.get(nickname);
                if (actualPass == null)
                    labelLog.setText("There is no such user in database");
                else if (!actualPass.equals(password))
                    labelLog.setText("Incorrect password");
                else {
                    labelLog.setForeground(Color.green);
                    labelLog.setText("Successfully logined!");
                    new Thread(new Runnable() {
                        @Override
                        public void run(){
                            try {
                                Thread.currentThread().sleep(1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            CardLayout cl = (CardLayout)(mainPanel.getLayout());
                            cl.show(mainPanel, SMTHANOTHER);
                        }
                    }).start();


                }

            }
        });

    }
    private void setSignup(){

        jpSignup = new JPanel(new GridBagLayout());

        JLabel labelNick = new JLabel("Ваш никнейм:");
        JTextField textFieldNick = new JTextField();
        textFieldNick.setPreferredSize(new Dimension(220, 20));
        textFieldNick.setDocument(nickLimit);
        JLabel labelPass = new JLabel("Ваш пароль:");
        JTextField textFieldPass = new JTextField();
        textFieldPass.setPreferredSize(new Dimension(220, 20));
        textFieldPass.setDocument(passLimit);
        JLabel labelRepeatPass = new JLabel("Повторите пароль:");
        JTextField textFieldRepeatPass = new JTextField();
        textFieldRepeatPass.setPreferredSize(new Dimension(220, 20));
        textFieldRepeatPass.setDocument(new JTextFieldLimit(20));
        JButton buttonLogin = new JButton("Зарегистрироваться");
        JLabel labelLog = new JLabel();
        labelLog.setForeground(Color.green);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5,0,5,0);
        jpSignup.add(labelNick, c);
        c.gridy = 1;
        c.gridwidth = 2;
        jpSignup.add(textFieldNick,c);
        c.insets = new Insets(10,0,5,0);
        c.gridy = 2;
        c.gridwidth = 1;
        jpSignup.add(labelPass,c);
        c.insets = new Insets(5,0,5,0);
        c.gridy = 3;
        c.gridwidth = 2;
        jpSignup.add(textFieldPass,c);
        c.insets = new Insets(10,0,5,0);
        c.gridy = 4;
        c.gridwidth = 1;
        jpSignup.add(labelRepeatPass,c);
        c.insets = new Insets(5,0,5,0);
        c.gridy = 5;
        c.gridwidth = 2;
        jpSignup.add(textFieldRepeatPass,c);
        c.insets = new Insets(10,0,10,0);
        c.gridy = 6;
        jpSignup.add(buttonLogin,c);
        c.gridy = 7;
        c.weighty = 1;
        c.insets = new Insets(0,0,10,0);
        jpSignup.add(labelLog,c);

        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = textFieldNick.getText();
                String password = textFieldPass.getText();
                labelLog.setText("");
                if (!nickname.equals("") && !password.equals("")) {
                    labelLog.setText("<html>Аккаунт успешно создан!</html>");
                    return;
                }
                //CardLayout cl = (CardLayout)(mainPanel.getLayout());
                //cl.show(mainPanel, SMTHANOTHER);
            }
        });

    }
    GuiMain() {
        db = new Database();
        db.createConnection();
        accounts = db.getAccounts();

        jfrm = new JFrame("И снова здравствуйте");
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfrm.setSize(290, 330);
        jfrm.setMinimumSize(new Dimension(290, 330));

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
        tabPanel.setPreferredSize(new Dimension(220, 280));
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
