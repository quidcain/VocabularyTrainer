package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    private HashMap<String, String> entireSessionVocabularity;
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
                    labelLog.setText("Такого пользователя не существует");
                else if (!actualPass.equals(password))
                    labelLog.setText("Неправильный пароль");
                else {
                    buttonLogin.setEnabled(false);
                    labelLog.setForeground(Color.green);
                    labelLog.setText("Успех!");
                    entireSessionVocabularity = db.getVocabulatiry(nickname);
                    mainPanel.add(cardPanel2Init(nickname), SMTHANOTHER);
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
        JButton buttonSignup = new JButton("Зарегистрироваться");
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
        jpSignup.add(buttonSignup,c);
        c.gridy = 7;
        c.weighty = 1;
        c.insets = new Insets(0,0,10,0);
        jpSignup.add(labelLog,c);

        buttonSignup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = textFieldNick.getText();
                String password = textFieldPass.getText();
                String repeatedPassword = textFieldRepeatPass.getText();
                labelLog.setForeground(Color.red);
                labelLog.setText("");
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
                    db.createNewUser(nickname, password);
                    labelLog.setForeground(Color.green);
                    labelLog.setText("Успешно зарегестрирован!");
                }
            }
        });
        return jpSignup;
    }
    private JPanel authorizationPanelInit() {
        JPanel upperPanel = new JPanel(new GridBagLayout());
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
        upperPanel.add(tabPanel, c);
        return upperPanel;
    }
    private JPanel cardPanel2Init(String nickname) {
        JPanel upperPanel = new JPanel(new BorderLayout());

        JToolBar jtb = new JToolBar("Функции");
        JButton buttonNewOwnWord = new JButton("Добавить слово");
        JButton buttonTraining = new JButton("Показать словарь");

        JPanel innerPannel = new JPanel(new CardLayout());

        // it's here because it interacts with other panel
        WordsTableModel tableModel = new WordsTableModel(entireSessionVocabularity);

        //
        /*WordsTableModel tableTrainingModel = new WordsTableModel(entireSessionVocabularity);
        JTable tableToTraining = new JTable(tableTrainingModel);
        jsp = new JScrollPane(tableToTraining);
        jsp.setPreferredSize(new Dimension(240, 160));
        c.gridx = 1;
        c.gridy = 0;
        jpVocab.add(jsp, c);*/
        //

        JPanel jpVocab = new JPanel(new GridBagLayout()) {
            {
                JTable tableVocal = new JTable(tableModel);
                tableVocal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tableVocal.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (tableVocal.getSelectedRowCount() > 4)
                            tableVocal.getSelectionModel().removeIndexInterval(tableVocal.getSelectedRow(), tableVocal.getSelectedRow());
                        super.mouseReleased(e);
                    }
                });
                JScrollPane jsp = new JScrollPane(tableVocal);
                jsp.setPreferredSize(new Dimension(240, 160));
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                add(jsp, c);
            }
        };
        JPanel jpNewWord = new JPanel(new GridBagLayout()) {
            {
                JLabel labelEng = new JLabel("Слово на английском:");
                JTextField textFieldEng = new JTextField();
                textFieldEng.setPreferredSize(new Dimension(220, 20));
                textFieldEng.setDocument(new JTextFieldLimit(20));
                JLabel labelRus = new JLabel("Его перевод:");
                JTextField textFieldRus = new JTextField();
                textFieldRus.setPreferredSize(new Dimension(220, 20));
                textFieldRus.setDocument(new JTextFieldLimit(20));
                JButton buttonAdd = new JButton("Добавить");
                JLabel labelLog = new JLabel();
                labelLog.setForeground(Color.red);
                buttonAdd.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (textFieldEng.getText().equals("") || textFieldRus.getText().equals("")) {
                            labelLog.setText("Поля не могут быть пустыми");
                        } else if (entireSessionVocabularity.containsKey(textFieldEng.getText())) {
                            labelLog.setText("Слово уже есть в словаре!");
                        } else {
                            labelLog.setForeground(Color.green);
                            labelLog.setText("Добавлено");
                            entireSessionVocabularity.put(textFieldEng.getText(), textFieldRus.getText());
                            tableModel.addRow(new WordsPair(textFieldEng.getText(), textFieldRus.getText()));
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                labelLog.setForeground(Color.red);
                                labelLog.setText("");
                            }
                        }).start();
                    }
                });
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.anchor = GridBagConstraints.LINE_START;
                c.insets = new Insets(5, 0, 5, 0);
                add(labelEng, c);
                c.gridy = 1;
                c.gridwidth = 2;
                add(textFieldEng, c);
                c.insets = new Insets(10, 0, 5, 0);
                c.gridy = 2;
                c.gridwidth = 1;
                add(labelRus, c);
                c.insets = new Insets(5, 0, 5, 0);
                c.gridy = 3;
                c.gridwidth = 2;
                add(textFieldRus, c);
                c.insets = new Insets(10, 0, 5, 0);
                c.gridy = 4;
                c.gridwidth = 1;
                add(buttonAdd, c);
                c.gridy = 5;
                c.gridwidth = 3;
                c.weighty = 1;
                add(labelLog, c);
            }
        };
        innerPannel.add(jpVocab, buttonTraining.getActionCommand());
        innerPannel.add(jpNewWord, buttonNewOwnWord.getActionCommand());

        class ToolBarInteract implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout)(innerPannel.getLayout());
                cl.show(innerPannel, e.getActionCommand());
            }
        }
        buttonNewOwnWord.addActionListener(new ToolBarInteract());
        buttonTraining.addActionListener(new ToolBarInteract());
        jtb.add(buttonNewOwnWord);
        jtb.add(buttonTraining);

        upperPanel.add(jtb, BorderLayout.NORTH);
        upperPanel.add(innerPannel, BorderLayout.CENTER);
        return upperPanel;
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
        mainPanel.add(authorizationPanelInit(), AUTHORIZATION);

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
