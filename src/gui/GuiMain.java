package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 31.10.2016.
 */
public class GuiMain {
    private JFrame frame;
    private JPanel mainPanel;
    private final int AMOUNT_OF_WORDS_FOR_TRAINING = 5;
    private String nickname;
    private Database db;
    private HashMap<String, String> accounts;
    private HashMap<String, String> entireSessionVocabulary;
    private JTable vocabTable;
    private TableModelWords tableModelVocabulary;
    private JTable trainingTable;
    private CheckedWordsTableModel tableModelTraining;
    private JLabel labelNickValue;
    private class AuthorizationPanel extends JPanel {
        AuthorizationPanel() {
            super(new GridBagLayout());
            class TabbedPaneLogSign extends JTabbedPane{
                TabbedPaneLogSign() {
                    JTextFieldLimit limitNick = new JTextFieldLimit(20);
                    class PanelLogin extends JPanel {
                        PanelLogin(JTextFieldLimit nickLimit){
                            super(new GridBagLayout());
                            JLabel labelNick = new JLabel("Ваш никнейм:");
                            JTextField textFieldNick = new JTextField();
                            textFieldNick.setPreferredSize(new Dimension(220, 20));
                            textFieldNick.setDocument(nickLimit);
                            JLabel labelPass = new JLabel("Ваш пароль:");
                            JPasswordField passwordField = new JPasswordField();
                            passwordField.setPreferredSize(new Dimension(220, 20));
                            passwordField.setDocument(new JTextFieldLimit(20));
                            JButton buttonLogin = new JButton("Войти");
                            JLabel labelLog = new JLabel();
                            labelLog.setHorizontalAlignment(SwingConstants.CENTER);
                            labelLog.setVerticalAlignment(SwingConstants.CENTER);
                            labelLog.setPreferredSize(new Dimension(220, 40));
                            labelLog.setForeground(Color.red);

                            GridBagConstraints c = new GridBagConstraints();
                            c.gridx = 0;
                            c.gridy = 0;
                            c.anchor = GridBagConstraints.LINE_START;
                            c.insets = new Insets(5,0,5,0);
                            add(labelNick, c);
                            c.gridy = 1;
                            c.gridwidth = 2;
                            add(textFieldNick, c);
                            c.insets = new Insets(10,0,5,0);
                            c.gridy = 2;
                            c.gridwidth = 1;
                            add(labelPass, c);
                            c.insets = new Insets(5,0,5,0);
                            c.gridy = 3;
                            c.gridwidth = 2;
                            add(passwordField, c);
                            c.insets = new Insets(10,0,5,0);
                            c.gridy = 4;
                            c.gridwidth = 1;
                            add(buttonLogin, c);
                            c.gridy = 5;
                            c.gridwidth = 3;
                            c.weighty = 1;
                            c.anchor = GridBagConstraints.CENTER;
                            add(labelLog, c);

                            buttonLogin.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    nickname = textFieldNick.getText();
                                    String password = new String(passwordField.getPassword());
                                    labelLog.setForeground(Color.red);
                                    labelLog.setText("");
                                    if (nickname.equals("") || password.equals("")) {
                                        labelLog.setText("<html>Пароль и логин<br> не могут быть пусты!</html>");
                                        return;
                                    }
                                    String actualPass = accounts.get(nickname);
                                    if (actualPass == null)
                                        labelLog.setText("<html>Такого пользователя<br>не существует</html>");
                                    else if (!actualPass.equals(password))
                                        labelLog.setText("Неправильный пароль");
                                    else {
                                        buttonLogin.setEnabled(false);
                                        labelLog.setForeground(Color.green);
                                        labelLog.setText("Успех!");
                                        entireSessionVocabulary = db.getVocabulary(nickname);
                                        tableModelVocabulary = new TableModelWords();
                                        tableModelVocabulary.setCells(entireSessionVocabulary);
                                        vocabTable.setModel(tableModelVocabulary);
                                        tableModelTraining = new CheckedWordsTableModel(AMOUNT_OF_WORDS_FOR_TRAINING);
                                        trainingTable.setModel(tableModelTraining);
                                        labelNickValue.setText(nickname);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run(){
                                                try {
                                                    Thread.sleep(1000);
                                                    frame.setMinimumSize(new Dimension(700, 330));
                                                    passwordField.setText("");
                                                    labelLog.setText("");
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                CardLayout cl = (CardLayout)(mainPanel.getLayout());
                                                cl.show(mainPanel, "Smth another");
                                                buttonLogin.setEnabled(true);
                                            }
                                        }).start();
                                    }
                                }
                            });
                        }
                    }
                    class PanelSignUp extends JPanel {
                        PanelSignUp(JTextFieldLimit nickLimit) {
                            super(new GridBagLayout());
                            JLabel labelNick = new JLabel("Ваш никнейм:");
                            JTextField textFieldNick = new JTextField();
                            textFieldNick.setPreferredSize(new Dimension(220, 20));
                            textFieldNick.setDocument(nickLimit);
                            JLabel labelPass = new JLabel("Ваш пароль:");
                            JTextField textFieldPass = new JTextField();
                            textFieldPass.setPreferredSize(new Dimension(220, 20));
                            textFieldPass.setDocument(new JTextFieldLimit(20));
                            JLabel labelRepeatPass = new JLabel("Повторите пароль:");
                            JTextField textFieldRepeatPass = new JTextField();
                            textFieldRepeatPass.setPreferredSize(new Dimension(220, 20));
                            textFieldRepeatPass.setDocument(new JTextFieldLimit(20));
                            JButton buttonSignUp = new JButton("Зарегистрироваться");
                            JLabel labelLog = new JLabel();
                            labelLog.setPreferredSize(new Dimension(220, 40));
                            labelLog.setForeground(Color.green);
                            GridBagConstraints c = new GridBagConstraints();
                            c.gridx = 0;
                            c.gridy = 0;
                            c.anchor = GridBagConstraints.LINE_START;
                            c.insets = new Insets(5,0,5,0);
                            add(labelNick, c);
                            c.gridy = 1;
                            c.gridwidth = 2;
                            add(textFieldNick,c);
                            c.insets = new Insets(10,0,5,0);
                            c.gridy = 2;
                            c.gridwidth = 1;
                            add(labelPass,c);
                            c.insets = new Insets(5,0,5,0);
                            c.gridy = 3;
                            c.gridwidth = 2;
                            add(textFieldPass,c);
                            c.insets = new Insets(10,0,5,0);
                            c.gridy = 4;
                            c.gridwidth = 1;
                            add(labelRepeatPass,c);
                            c.insets = new Insets(5,0,5,0);
                            c.gridy = 5;
                            c.gridwidth = 2;
                            add(textFieldRepeatPass,c);
                            c.insets = new Insets(10,0,10,0);
                            c.gridy = 6;
                            add(buttonSignUp,c);
                            c.gridy = 7;
                            c.insets = new Insets(0,0,10,0);
                            add(labelLog,c);
                            buttonSignUp.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String nickname = textFieldNick.getText();
                                    String password = textFieldPass.getText();
                                    String repeatedPassword = textFieldRepeatPass.getText();
                                    labelLog.setForeground(Color.red);
                                    labelLog.setText("");
                                    if (nickname.equals("") || password.equals("")) {
                                        labelLog.setText("Заполните все поля!");
                                        return;
                                    }else if (!password.equals(repeatedPassword)) {
                                        labelLog.setText("Пароли не совпадают!");
                                        return;
                                    }else if(accounts.containsKey(nickname)){
                                        labelLog.setText("<html>Такой пользователь уже<br> существует!</html>");
                                        return;
                                    }else {
                                        accounts.put(nickname, password);
                                        db.createNewUser(nickname, password);
                                        labelLog.setForeground(Color.green);
                                        labelLog.setText("Успешно зарегестрирован!");
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run(){
                                                try {
                                                    Thread.sleep(2000);
                                                    labelLog.setText("");
                                                    textFieldPass.setText("");
                                                    textFieldRepeatPass.setText("");
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                    }
                                }
                            });
                        }
                    }
                    addTab("Вход", new PanelLogin(limitNick));
                    addTab("Регистрация", new PanelSignUp(limitNick));
                }
            }
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.ipadx = 40;
            add(new TabbedPaneLogSign(), c);
        }
    }
    private class AfterAuthPanel extends JPanel {
        private TrainingLogic trainingLogic;
        private JLabel labelAskedWord; //here because it's need to load word while starting training
        AfterAuthPanel() {
            super(new CardLayout());
            class PanelMenu extends JPanel {
                private boolean keepVocabulary = false;
                JLabel labelDecision;
                PanelMenu(){
                    super(new BorderLayout());
                    class PanelMenuInner extends JPanel {
                        PanelMenuInner() {
                            super(new CardLayout());
                            class PanelVocab extends JPanel {
                                PanelVocab() {
                                    super(new GridBagLayout());
                                    vocabTable = new JTable();
                                    vocabTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                                    JScrollPane jsp = new JScrollPane(vocabTable);
                                    jsp.setPreferredSize(new Dimension(240, 160));
                                    trainingTable = new JTable(tableModelTraining);
                                    JScrollPane jsp2 = new JScrollPane(trainingTable);
                                    jsp2.setPreferredSize(new Dimension(240, 160));
                                    GridBagConstraints c = new GridBagConstraints();
                                    JButton buttonRight = new JButton("-->");
                                    JButton buttonLeft = new JButton("<--");
                                    JButton buttonTrainingStart = new JButton("Тренировка");
                                    JLabel labelLog = new JLabel("");
                                    labelLog.setPreferredSize(new Dimension(400,40));
                                    labelLog.setHorizontalAlignment(SwingConstants.CENTER);
                                    labelLog.setVerticalAlignment(SwingConstants.CENTER);
                                    labelLog.setForeground(Color.red);
                                    buttonTrainingStart.setEnabled(false);
                                    c.gridx = 0;
                                    c.gridy = 0;
                                    c.gridheight = 3;
                                    add(jsp, c);
                                    c.gridx = 1;
                                    c.gridy = 0;
                                    c.insets = new Insets(40,10,0,10);
                                    c.gridheight = 1;
                                    add(buttonRight, c);
                                    c.gridx = 1;
                                    c.gridy = 1;
                                    c.insets = new Insets(0,10,0,10);
                                    add(buttonLeft, c);
                                    c.gridx = 2;
                                    c.gridy = 0;
                                    c.gridheight = 3;
                                    c.insets = new Insets(0,0,0,0);
                                    add(jsp2, c);
                                    c.gridx = 0;
                                    c.gridy = 3;
                                    c.gridwidth = 3;
                                    c.gridwidth = 5;
                                    c.insets = new Insets(5,0,0,0);
                                    c.gridheight = 1;
                                    add(labelLog, c);
                                    c.gridx = 0;
                                    c.gridy = 4;
                                    c.insets = new Insets(5,0,0,0);
                                    c.gridheight = 1;
                                    add(buttonTrainingStart, c);
                                    buttonRight.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            int selectedRow = vocabTable.getSelectedRow();
                                            if (selectedRow != -1 && tableModelTraining.getRowCount() < 5
                                                    && !tableModelTraining.containsKey(tableModelVocabulary.getRow(selectedRow))) {
                                                if (tableModelTraining.containsValue(tableModelVocabulary.getRow(selectedRow))) {
                                                    labelLog.setText("Нельзя тренировать слова с одинаковым переводом");
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run(){
                                                            try {
                                                                Thread.sleep(2000);
                                                                labelLog.setText("");
                                                            } catch (InterruptedException e) {
                                                            }
                                                        }
                                                    }).start();
                                                    return;
                                                }
                                                tableModelTraining.addRow(tableModelVocabulary.getRow(selectedRow));
                                                int nextSelect = (selectedRow < tableModelVocabulary.getRowCount() - 1 ? selectedRow + 1 : 0);
                                                vocabTable.setRowSelectionInterval(nextSelect, nextSelect);
                                                buttonTrainingStart.setEnabled(tableModelTraining.getRowCount() == 5);
                                            }
                                        }
                                    });
                                    buttonLeft.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            int selectedRow = trainingTable.getSelectedRow();
                                            if (selectedRow != -1) {
                                                tableModelTraining.removeRow(tableModelTraining.getRow(selectedRow), selectedRow);
                                                if (tableModelTraining.getRowCount() != 0) {
                                                    int nextSelect = (selectedRow == tableModelTraining.getRowCount() ? selectedRow - 1 : selectedRow);
                                                    trainingTable.setRowSelectionInterval(nextSelect, nextSelect);
                                                }
                                                buttonTrainingStart.setEnabled(tableModelTraining.getRowCount() == 5);
                                            }
                                        }
                                    });
                                    buttonTrainingStart.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            trainingLogic = new TrainingLogic(tableModelTraining.getCells());
                                            labelAskedWord.setText(trainingLogic.getCurrentAskedWord());
                                            CardLayout cl = (CardLayout)(AfterAuthPanel.this.getLayout());
                                            cl.show(AfterAuthPanel.this, "training");
                                        }
                                    });
                                }
                            }
                            class PanelNewWordAddition extends JPanel {
                                PanelNewWordAddition() {
                                    super(new GridBagLayout());
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
                                    labelLog.setPreferredSize(new Dimension(220, 20));
                                    labelLog.setForeground(Color.red);
                                    buttonAdd.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            if (textFieldEng.getText().equals("") || textFieldRus.getText().equals("")) {
                                                labelLog.setText("Поля не могут быть пустыми");
                                            } else if (entireSessionVocabulary.containsKey(textFieldEng.getText())) {
                                                labelLog.setText("Слово уже есть в словаре!");
                                            } else {
                                                labelLog.setForeground(Color.green);
                                                labelLog.setText("Добавлено");
                                                String eng = textFieldEng.getText();
                                                String rus = textFieldRus.getText();
                                                entireSessionVocabulary.put(eng, rus);
                                                tableModelVocabulary.addRow(new WordsPair(eng, rus));
                                                db.addWord(nickname, eng, rus);
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
                                    add(labelLog, c);
                                }
                            }
                            class PanelWordRemoval extends JPanel {
                                PanelWordRemoval() {
                                    super(new GridBagLayout());
                                    JLabel labelEng = new JLabel("Слово на английском:");
                                    JTextField textFieldEng = new JTextField();
                                    textFieldEng.setPreferredSize(new Dimension(220, 20));
                                    textFieldEng.setDocument(new JTextFieldLimit(20));
                                    JButton buttonRemove = new JButton("Удалить");
                                    JLabel labelLog = new JLabel();
                                    labelLog.setPreferredSize(new Dimension(220, 20));
                                    labelLog.setForeground(Color.red);
                                    buttonRemove.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            if (textFieldEng.getText().equals("")) {
                                                labelLog.setText("Поле не может быть пустым");
                                            } else if (!entireSessionVocabulary.containsKey(textFieldEng.getText())) {
                                                labelLog.setText("Такого слова нет в словаре!");
                                            } else {
                                                labelLog.setForeground(Color.green);
                                                labelLog.setText("Удалено");
                                                String eng = textFieldEng.getText();
                                                entireSessionVocabulary.remove(textFieldEng.getText());
                                                for (int i = 0; i < tableModelVocabulary.getRowCount(); ++i)
                                                    if (tableModelVocabulary.getRow(i).eng.equals(textFieldEng.getText())) {
                                                        tableModelVocabulary.removeRow(i);
                                                        db.removeWord(nickname, textFieldEng.getText());
                                                    }
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
                                    add(buttonRemove, c);
                                    c.gridy = 3;
                                    c.gridwidth = 3;
                                    add(labelLog, c);
                                }
                            }
                            class PanelAccountDeletion extends JPanel {
                                PanelAccountDeletion(){
                                    super(new GridBagLayout());
                                    JLabel labelNick = new JLabel("Ваш никнейм:");
                                    labelNickValue = new JLabel("");
                                    labelNickValue.setPreferredSize(new Dimension(220, 20));
                                    JLabel labelPass = new JLabel("Ваш пароль:");
                                    JPasswordField passwordField = new JPasswordField();
                                    passwordField.setPreferredSize(new Dimension(220, 20));
                                    passwordField.setDocument(new JTextFieldLimit(20));
                                    JRadioButton radioButtonKeepVocabulary = new JRadioButton("Оставить словарь?");
                                    JButton buttonDelete = new JButton("Удалить");
                                    JLabel labelLog = new JLabel();
                                    labelLog.setHorizontalAlignment(SwingConstants.CENTER);
                                    labelLog.setVerticalAlignment(SwingConstants.CENTER);
                                    labelLog.setPreferredSize(new Dimension(220, 40));
                                    labelLog.setForeground(Color.red);
                                    GridBagConstraints c = new GridBagConstraints();
                                    c.gridx = 0;
                                    c.gridy = 0;
                                    c.anchor = GridBagConstraints.LINE_START;
                                    c.insets = new Insets(5,0,5,0);
                                    add(labelNick, c);
                                    c.gridy = 1;
                                    c.gridwidth = 2;
                                    add(labelNickValue, c);
                                    c.insets = new Insets(10,0,5,0);
                                    c.gridy = 2;
                                    c.gridwidth = 1;
                                    add(labelPass, c);
                                    c.insets = new Insets(5,0,5,0);
                                    c.gridy = 3;
                                    c.gridwidth = 2;
                                    add(passwordField, c);
                                    c.insets = new Insets(5,0,5,0);
                                    c.gridy = 4;
                                    c.gridwidth = 1;
                                    add(radioButtonKeepVocabulary, c);
                                    c.insets = new Insets(10,0,5,0);
                                    c.gridy = 5;
                                    c.gridwidth = 1;
                                    add(buttonDelete, c);
                                    c.gridy = 6;
                                    c.gridwidth = 3;
                                    c.anchor = GridBagConstraints.CENTER;
                                    add(labelLog, c);
                                    buttonDelete.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            String password = new String(passwordField.getPassword());
                                            labelLog.setForeground(Color.red);
                                            labelLog.setText("");
                                            if (nickname.equals("") || password.equals("")) {
                                                labelLog.setText("<html>Пароль и логин<br> не могут быть пусты!</html>");
                                                return;
                                            }
                                            String actualPass = accounts.get(nickname);
                                            if (actualPass == null)
                                                labelLog.setText("<html>Такого пользователя<br>не существует</html>");
                                            else if (!actualPass.equals(password))
                                                labelLog.setText("Неправильный пароль");
                                            else {
                                                if(radioButtonKeepVocabulary.isSelected()) {
                                                    keepVocabulary = true;
                                                    labelDecision.setText("<html>Вы уверены, что хотите удалить ваш аккаунт?<br>Словарь удален не будет</html>");
                                                }
                                                else {
                                                    keepVocabulary = false;
                                                    labelDecision.setText("<html>Вы уверены, что хотите удалить ваш аккаунт<br>и словарь?</html>");
                                                }
                                                CardLayout cl = (CardLayout)(PanelMenuInner.this.getLayout());
                                                cl.show(PanelMenuInner.this, "Подтвердить удаление");
                                                passwordField.setText("");
                                                radioButtonKeepVocabulary.setSelected(false);
                                            }
                                        }
                                    });
                                }
                            }
                            class PanelConfirmDeletion extends JPanel {
                                PanelConfirmDeletion(){
                                    super(new GridBagLayout());
                                    labelDecision = new JLabel("");
                                    labelDecision.setPreferredSize(new Dimension(200, 60));
                                    JButton buttonYes = new JButton("Да");
                                    JButton buttonNo = new JButton("Нет");
                                    JLabel labelLog = new JLabel();
                                    labelLog.setHorizontalAlignment(SwingConstants.CENTER);
                                    labelLog.setVerticalAlignment(SwingConstants.CENTER);
                                    labelLog.setPreferredSize(new Dimension(220, 40));
                                    labelLog.setForeground(Color.red);
                                    GridBagConstraints c = new GridBagConstraints();
                                    c.gridx = 0;
                                    c.gridy = 0;
                                    c.gridwidth = 3;
                                    c.anchor = GridBagConstraints.LINE_START;
                                    c.insets = new Insets(0,0,10,0);
                                    add(labelDecision, c);
                                    c.gridx = 0;
                                    c.gridy = 1;
                                    c.gridwidth = 1;
                                    c.insets = new Insets(0,0,0,10);
                                    add(buttonYes, c);
                                    c.gridx = 2;
                                    c.insets = new Insets(0,10,0,0);
                                    add(buttonNo, c);
                                    buttonYes.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            labelLog.setForeground(Color.green);
                                            labelLog.setText("Успех!");
                                            buttonYes.setEnabled(false);
                                            buttonNo.setEnabled(false);
                                            db.removeAccount(nickname);
                                            accounts.remove(nickname);
                                            if (!keepVocabulary)
                                                db.deleteUserTable(nickname);
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run(){
                                                    try {
                                                        Thread.sleep(1000);
                                                        CardLayout cl = (CardLayout)(mainPanel.getLayout());
                                                        cl.show(mainPanel, "Authorization");
                                                        cl = (CardLayout)(PanelMenuInner.this.getLayout());
                                                        cl.show(PanelMenuInner.this, "Показать словарь");
                                                        buttonYes.setEnabled(true);
                                                        buttonNo.setEnabled(true);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).start();
                                        }
                                    });
                                    buttonNo.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent actionEvent) {
                                            CardLayout cl = (CardLayout)(PanelMenuInner.this.getLayout());
                                            cl.show(PanelMenuInner.this, "Удалить аккаунт");
                                        }
                                    });
                                }
                            }
                            add(new PanelVocab(), "Показать словарь");
                            add(new PanelNewWordAddition(), "Добавить слово");
                            add(new PanelWordRemoval(), "Удалить слово");
                            add(new PanelAccountDeletion(), "Удалить аккаунт");
                            add(new PanelConfirmDeletion(), "Подтвердить удаление");
                        }
                    }
                    class FunctionToolbar extends JToolBar {
                        public FunctionToolbar(PanelMenuInner panelMenuInner) {
                            JButton buttonShowNewOwnWordAddition = new JButton("Добавить слово");
                            JButton buttonShowVocab = new JButton("Показать словарь");
                            JButton buttonShowWordRemoval = new JButton("Удалить слово");
                            JButton buttonShowAccountDeletion = new JButton("Удалить аккаунт");
                            add(buttonShowVocab);
                            add(buttonShowNewOwnWordAddition);
                            add(buttonShowWordRemoval);
                            add(buttonShowAccountDeletion);
                            class ToolBarInteract implements ActionListener {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    CardLayout cl = (CardLayout) (panelMenuInner.getLayout());
                                    cl.show(panelMenuInner, e.getActionCommand());
                                }
                            }
                            buttonShowNewOwnWordAddition.addActionListener(new ToolBarInteract());
                            buttonShowVocab.addActionListener(new ToolBarInteract());
                            buttonShowWordRemoval.addActionListener(new ToolBarInteract());
                            buttonShowAccountDeletion.addActionListener(new ToolBarInteract());
                        }
                    }
                    PanelMenuInner panelMenuInner = new PanelMenuInner();
                    add(new FunctionToolbar(panelMenuInner), BorderLayout.NORTH);
                    add(panelMenuInner, BorderLayout.CENTER);
                }
            }
            class PanelTraining extends JPanel {
                private JLabel labelResult;
                PanelTraining() {
                    super(new CardLayout());
                    class PanelTrainingWriteWord extends JPanel {
                        protected JButton buttonAnswer;
                        protected JLabel labelLog;
                        protected JTextField textFieldTranslation;
                        PanelTrainingWriteWord(){
                            super(new GridBagLayout());
                            labelAskedWord = new JLabel("Известное слово");
                            textFieldTranslation = new JTextField();
                            textFieldTranslation.setPreferredSize(new Dimension(220, 20));
                            textFieldTranslation.setDocument(new JTextFieldLimit(40));
                            buttonAnswer = new JButton("Ответить");
                            labelLog = new JLabel("");
                            labelLog.setPreferredSize(new Dimension(120, 20));
                            labelLog.setHorizontalAlignment(SwingConstants.CENTER);
                            labelLog.setVerticalAlignment(SwingConstants.CENTER);
                            labelLog.setForeground(Color.red);
                            GridBagConstraints c = new GridBagConstraints();
                            c.gridx = 0;
                            c.gridy = 0;
                            c.anchor = GridBagConstraints.CENTER;
                            c.insets = new Insets(5, 0, 5, 0);
                            add(labelAskedWord, c);
                            c.gridx = 0;
                            c.gridy = 1;
                            add(textFieldTranslation, c);
                            c.gridx = 0;
                            c.gridy = 2;
                            add(buttonAnswer, c);
                            c.gridx = 0;
                            c.gridy = 3;
                            add(labelLog, c);
                            buttonAnswer.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    labelLog.setForeground(Color.red);
                                    if (trainingLogic.isCorrectTranslation(textFieldTranslation.getText())){
                                        labelLog.setForeground(Color.green);
                                        labelLog.setText("Верно!");
                                    } else
                                        labelLog.setText("Не верно!");
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run(){
                                            try {
                                                Thread.sleep(500);
                                                labelLog.setText("");
                                            } catch (InterruptedException e) {
                                            }
                                        }
                                    }).start();
                                    textFieldTranslation.setText("");
                                    if (trainingLogic.isIntermediateStage()) {
                                        textFieldTranslation.setText("");
                                        String stringResult = String.format("<html><center>Ваш результат:%d/%d</center><br>VERDICT</html>", trainingLogic.getCorrectAnswers(), trainingLogic.getTotalQuestions());
                                        double ratio = (double)trainingLogic.getCorrectAnswers() / trainingLogic.getTotalQuestions();
                                        if (ratio == 1)
                                            stringResult = stringResult.replaceFirst("VERDICT", "Вы идеально выучили слова!");
                                        else if (ratio >= 0.8)
                                            stringResult = stringResult.replaceFirst("VERDICT", "Хороший резуьтат!");
                                        else if (ratio >= 0.6)
                                            stringResult = stringResult.replaceFirst("VERDICT", "Вы знаете больше половины, так держать");
                                        else if (ratio >= 0.4)
                                            stringResult = stringResult.replaceFirst("VERDICT", "Что-то есть, но лучше повторите");
                                        else if (ratio >= 0.2)
                                            stringResult = stringResult.replaceFirst("VERDICT", "Правильные ответы были, но очень мало. Повторите материал");
                                        else
                                            stringResult = stringResult.replaceFirst("VERDICT", "Результат ужасен, получше изучите слова и возвращайтесь");
                                        labelResult.setText(stringResult);
                                        CardLayout cl = (CardLayout)(PanelTraining.this.getLayout());
                                        cl.next(PanelTraining.this);
                                    } else
                                        labelAskedWord.setText(trainingLogic.getCurrentAskedWord());
                                }
                            });
                        }
                    }
                    class PanelTrainingResult extends JPanel {
                        PanelTrainingResult(){
                            super(new GridBagLayout());
                            labelResult = new JLabel();
                            JButton buttonBackToMenu = new JButton("Назад в меню");
                            GridBagConstraints c = new GridBagConstraints();
                            c.gridx = 0;
                            c.gridy = 0;
                            c.anchor = GridBagConstraints.CENTER;
                            c.insets = new Insets(5, 0, 5, 0);
                            add(labelResult, c);
                            c.gridy = 1;
                            add(buttonBackToMenu, c);
                            buttonBackToMenu.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent actionEvent) {
                                    CardLayout cl = (CardLayout)(PanelTraining.this.getLayout());
                                    cl.next(PanelTraining.this);
                                    cl = (CardLayout)(AfterAuthPanel.this.getLayout());
                                    cl.show(AfterAuthPanel.this, "menu");
                                }
                            });
                        }
                    }
                    add(new PanelTrainingWriteWord(), "writeWord");
                    add(new PanelTrainingResult(), "result");
                }
            }
            add(new PanelMenu(),"menu");
            add(new PanelTraining(), "training");
        }
    }
    GuiMain() {
        try {
            db = new Database();
            db.createConnection();
        }catch(ClassNotFoundException|SQLException e) {
            System.exit(1);
        }
        accounts = db.getAccounts();
        mainPanel = new JPanel(new CardLayout());
        mainPanel.add(new AuthorizationPanel(), "Authorization");
        mainPanel.add(new AfterAuthPanel(), "Smth another");
        frame = new JFrame("VocabularyTrainer");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(290, 330);
        frame.setMinimumSize(new Dimension(290, 330));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.add(mainPanel);
        frame.addWindowListener(new WindowListener() {
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
