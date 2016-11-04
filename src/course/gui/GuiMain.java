package course.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by user on 31.10.2016.
 */
public class GuiMain {
    private JFrame jfrm;
    private JPanel mainPanel;

    private String AUTHORIZATION = "Авторизация";
    private String SMTHANOTHER = "Что-то другое";

    private Database db;
    private HashMap<String, String> accounts;
    private JPanel loginInit(JTextFieldLimit nickLimit,JTextFieldLimit passLimit) {
        JPanel jpLogin = new JPanel(new GridBagLayout());

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
                    db.createUserTable(nickname);
                    new Thread(new Runnable() {
                        @Override
                        public void run(){
                            try {
                                Thread.sleep(1000);
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
        return jpLogin;

    }
    private JPanel signupInit(JTextFieldLimit nickLimit,JTextFieldLimit passLimit) {

        JPanel jpSignup = new JPanel(new GridBagLayout());

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
                String repeatedPassword = textFieldRepeatPass.getText();
                labelLog.setForeground(Color.red);
                labelLog.setText("");
                /*if (nickname.equals("") || password.equals("")) {
                    labelLog.setText("<html>Аккаунт успешно создан!</html>");
                    return;
                }*/
                if (nickname.equals("") || password.equals("")) {
                    labelLog.setText("<html>Пароль и логин не могут быть пусты!</html>");
                    return;
                }else if (!password.equals(repeatedPassword)) {
                    labelLog.setText("Пароли не совпадают!");
                    return;
                }else if(accounts.containsKey(nickname)){
                    labelLog.setText("Такой пользователь уже существует!");
                    return;
                }else {
                    accounts.put(nickname, password);
                    try (PreparedStatement statement = db.getConnection().prepareStatement("INSERT INTO accounts (nickname, password) VALUES (?,?);")) {
                        statement.setString(1, nickname);
                        statement.setString(2, password);
                        int changedRows = statement.executeUpdate();
                        if (changedRows > 0) {
                            labelLog.setForeground(Color.green);
                            labelLog.setText("Successfully sign up!");
                        } else
                            labelLog.setText("Some troubles...");
                    } catch (SQLException sqlEx) {
                        System.out.println(sqlEx.getMessage());
                    }
                }
            }
        });
        return jpSignup;
    }
    private JPanel cardPanel1Init() {
        JPanel cardPanel = new JPanel(new GridBagLayout());
        JTabbedPane tabPanel = new JTabbedPane();
        JTextFieldLimit nickLimit = new JTextFieldLimit(20);
        JTextFieldLimit passLimit = new JTextFieldLimit(20);

        tabPanel.addTab("Вход", loginInit(nickLimit, passLimit));
        tabPanel.addTab("Регистрация", signupInit(nickLimit, passLimit));
        tabPanel.setPreferredSize(new Dimension(220, 280));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 40;
        cardPanel.add(tabPanel, c);
        return cardPanel;
    }
    private JPanel cardPanel2Init() {
        JPanel cardPanel = new JPanel(new BorderLayout());
        JToolBar jtb = new JToolBar("Capabilities");
        JButton buttonNewOwnWord = new JButton("Добавить слово");
        jtb.add(buttonNewOwnWord);
        JButton buttonTraining = new JButton("Тренировка");
        jtb.add(buttonTraining);
        cardPanel.add(jtb, BorderLayout.NORTH);
        return cardPanel;
    }
    GuiMain() {
        try {
            db = new Database();
            db.createConnection();
        }catch(ClassNotFoundException|SQLException e) {
            System.exit(1);
        }
        accounts = db.getAccounts();

        jfrm = new JFrame("И снова здравствуйте");
        jfrm.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jfrm.setSize(290, 330);
        jfrm.setMinimumSize(new Dimension(290, 330));

        mainPanel = new JPanel(new CardLayout());
        mainPanel.add(cardPanel1Init(), AUTHORIZATION);
        mainPanel.add(cardPanel2Init(), SMTHANOTHER);
        jfrm.add(mainPanel);
        jfrm.setLocationRelativeTo(null);
        jfrm.setVisible(true);
        jfrm.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                db.closeConnection();
                System.exit(0);
            }
            public void windowActivated(WindowEvent event) {}
            public void windowClosed(WindowEvent event){}
            public void windowDeactivated(WindowEvent event){}
            public void windowDeiconified(WindowEvent event){}
            public void windowIconified(WindowEvent event) {}
            public void windowOpened(WindowEvent event) {}
        });
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
