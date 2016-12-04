package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Enumeration;
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
    private JTable tableVocab;
    private TableModelWords tableModelVocabulary;
    private JTable tableTraining;
    private CheckedWordsTableModel tableModelTraining;
    private JLabel labelNickValue;
    private class AuthorizationPanel extends JPanel {
        AuthorizationPanel() {
            super(new GridBagLayout());
            class TabbedPaneLogSign extends JTabbedPane{
                TabbedPaneLogSign() {
                    JTextFieldLimit limitNick = new JTextFieldLimit(30);
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
                            passwordField.setDocument(new JTextFieldLimit(30));
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
                                        tableVocab.setModel(tableModelVocabulary);
                                        tableModelTraining = new CheckedWordsTableModel(AMOUNT_OF_WORDS_FOR_TRAINING);
                                        tableTraining.setModel(tableModelTraining);
                                        labelNickValue.setText(nickname);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run(){
                                                try {
                                                    Thread.sleep(1000);
                                                    frame.setMinimumSize(new Dimension(765, 330));
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
                            textFieldPass.setDocument(new JTextFieldLimit(30));
                            JLabel labelRepeatPass = new JLabel("Повторите пароль:");
                            JTextField textFieldRepeatPass = new JTextField();
                            textFieldRepeatPass.setPreferredSize(new Dimension(220, 20));
                            textFieldRepeatPass.setDocument(new JTextFieldLimit(30));
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
        private JLabel labelAskedWord;
        private JRadioButton[] radioOptions = new JRadioButton[4];
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
                                    tableVocab = new JTable();
                                    tableVocab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                                    JScrollPane jsp = new JScrollPane(tableVocab);
                                    jsp.setPreferredSize(new Dimension(260, 160));
                                    tableTraining = new JTable(tableModelTraining);
                                    JScrollPane jsp2 = new JScrollPane(tableTraining);
                                    jsp2.setPreferredSize(new Dimension(260, 160));
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
                                            int selectedRow = tableVocab.getSelectedRow();
                                            if (selectedRow == -1)
                                                return;
                                            int nextSelect = (selectedRow < tableModelVocabulary.getRowCount() - 1 ? selectedRow + 1 : 0);
                                            tableVocab.setRowSelectionInterval(nextSelect, nextSelect);
                                            if (tableModelTraining.getRowCount() < 5
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
                                                                //it can't happen
                                                            }
                                                        }
                                                    }).start();
                                                    return;
                                                }
                                                tableModelTraining.addRow(tableModelVocabulary.getRow(selectedRow));
                                                buttonTrainingStart.setEnabled(tableModelTraining.getRowCount() == 5);
                                            }
                                        }
                                    });
                                    buttonLeft.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            int selectedRow = tableTraining.getSelectedRow();
                                            if (selectedRow != -1) {
                                                tableModelTraining.removeRow(tableModelTraining.getRow(selectedRow), selectedRow);
                                                if (tableModelTraining.getRowCount() != 0) {
                                                    int nextSelect = (selectedRow == tableModelTraining.getRowCount() ? selectedRow - 1 : selectedRow);
                                                    tableTraining.setRowSelectionInterval(nextSelect, nextSelect);
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
                                            String[] options = trainingLogic.getOptions();
                                            for (int i = 0; i < 4; ++i)
                                                radioOptions[i].setText(options[i]);
                                            CardLayout cl = (CardLayout)(AfterAuthPanel.this.getLayout());
                                            cl.show(AfterAuthPanel.this, "training");
                                        }
                                    });
                                }
                            }
                            class PanelNewTableAddition extends JPanel {
                                PanelNewTableAddition() {
                                    super(new GridBagLayout());
                                    JTable tableGeneralStandard = new JTable();
                                    tableGeneralStandard.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                                    TableModelWords tableModelGeneralStandard = new TableModelWords();
                                    tableModelGeneralStandard.setCells(db.getVocabulary("admin"));
                                    tableGeneralStandard.setModel(tableModelGeneralStandard);
                                    JScrollPane jsp = new JScrollPane(tableGeneralStandard);
                                    jsp.setPreferredSize(new Dimension(260, 160));
                                    JButton buttonAdd = new JButton("Добавить");
                                    JLabel labelLog = new JLabel("");
                                    labelLog.setPreferredSize(new Dimension(220, 20));
                                    labelLog.setHorizontalAlignment(SwingConstants.CENTER);
                                    labelLog.setVerticalAlignment(SwingConstants.CENTER);
                                    labelLog.setForeground(Color.red);
                                    GridBagConstraints c = new GridBagConstraints();
                                    c.gridx = 0;
                                    c.gridy = 0;
                                    c.gridheight = 5;
                                    c.insets = new Insets(0, 0, 0, 40);
                                    add(jsp, c);
                                    c.insets = new Insets(50, 0, 0, 0);
                                    c.gridx = 4;
                                    c.gridy = 1;
                                    c.gridheight = 1;
                                    add(buttonAdd, c);
                                    c.gridy = 2;
                                    c.insets = new Insets(5, 0, 0, 0);
                                    add(labelLog, c);
                                    buttonAdd.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            int selectedRow = tableGeneralStandard.getSelectedRow();
                                            if (selectedRow != -1) {
                                                String eng = (String)tableGeneralStandard.getValueAt(selectedRow, 0);
                                                String rus = (String)tableGeneralStandard.getValueAt(selectedRow, 1);
                                                if (entireSessionVocabulary.containsKey(eng)) {
                                                    labelLog.setText("Слово уже есть в словаре!");
                                                } else {
                                                    labelLog.setForeground(Color.green);
                                                    labelLog.setText("Добавлено");
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
                                                            //it can't happen
                                                        }
                                                        labelLog.setForeground(Color.red);
                                                        labelLog.setText("");
                                                    }
                                                }).start();
                                                int nextSelect = (selectedRow < tableModelGeneralStandard.getRowCount() - 1 ? selectedRow + 1 : 0);
                                                tableGeneralStandard.setRowSelectionInterval(nextSelect, nextSelect);
                                            }
                                        }
                                    });
                                }
                            }
                            class PanelNewOwnAddition extends JPanel {
                                PanelNewOwnAddition() {
                                    super(new GridBagLayout());
                                    JLabel labelEng = new JLabel("Слово на английском:");
                                    JTextField textFieldEng = new JTextField();
                                    textFieldEng.setPreferredSize(new Dimension(220, 20));
                                    textFieldEng.setDocument(new JTextFieldLimit(30));
                                    JLabel labelRus = new JLabel("Его перевод:");
                                    JTextField textFieldRus = new JTextField();
                                    textFieldRus.setPreferredSize(new Dimension(220, 20));
                                    textFieldRus.setDocument(new JTextFieldLimit(30));
                                    JButton buttonAdd = new JButton("Добавить");
                                    JLabel labelLog = new JLabel();
                                    labelLog.setPreferredSize(new Dimension(220, 20));
                                    labelLog.setForeground(Color.red);
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
                                    buttonAdd.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            String eng = textFieldEng.getText().trim().replaceAll("\\s+", " ");
                                            String rus = textFieldRus.getText().trim().replaceAll("\\s+", " ");
                                            if (eng.equals("") || rus.equals("")) {
                                                labelLog.setText("Поля не могут быть пустыми");
                                            } else if (entireSessionVocabulary.containsKey(eng)) {
                                                labelLog.setText("Слово уже есть в словаре!");
                                            } else {
                                                labelLog.setForeground(Color.green);
                                                labelLog.setText("Добавлено");
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
                                                    textFieldEng.setText("");
                                                    textFieldRus.setText("");
                                                    labelLog.setForeground(Color.red);
                                                    labelLog.setText("");
                                                }
                                            }).start();
                                        }
                                    });
                                }
                            }
                            class PanelWordRemoval extends JPanel {
                                PanelWordRemoval() {
                                    super(new GridBagLayout());
                                    JLabel labelEng = new JLabel("Слово на английском:");
                                    JTextField textFieldEng = new JTextField();
                                    textFieldEng.setPreferredSize(new Dimension(220, 20));
                                    textFieldEng.setDocument(new JTextFieldLimit(30));
                                    JButton buttonRemove = new JButton("Удалить");
                                    JLabel labelLog = new JLabel();
                                    labelLog.setPreferredSize(new Dimension(220, 20));
                                    labelLog.setForeground(Color.red);
                                    buttonRemove.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            String eng = textFieldEng.getText().trim().replaceAll("\\s+", " ");
                                            if (eng.equals("")) {
                                                labelLog.setText("Поле не может быть пустым");
                                            } else if (!entireSessionVocabulary.containsKey(eng)) {
                                                labelLog.setText("Такого слова нет в словаре!");
                                            } else {
                                                labelLog.setForeground(Color.green);
                                                labelLog.setText("Удалено");
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
                                                        //it can't happen
                                                    }
                                                    textFieldEng.setText("");
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
                                    passwordField.setDocument(new JTextFieldLimit(30));
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
                                                        //it can't happen
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
                            add(new PanelNewTableAddition(), "Добавить из таблицы");
                            add(new PanelNewOwnAddition(), "Добавить свое");
                            add(new PanelWordRemoval(), "Удалить слово");
                            add(new PanelAccountDeletion(), "Удалить аккаунт");
                            add(new PanelConfirmDeletion(), "Подтвердить удаление");
                        }
                    }
                    class FunctionToolbar extends JToolBar {
                        public FunctionToolbar(PanelMenuInner panelMenuInner) {
                            JButton buttonShowVocab = new JButton("Показать словарь");
                            JButton buttonShowNewTableWordAddition = new JButton("Добавить из таблицы");
                            JButton buttonShowNewOwnWordAddition = new JButton("Добавить свое");
                            JButton buttonShowWordRemoval = new JButton("Удалить слово");
                            JButton buttonShowAccountDeletion = new JButton("Удалить аккаунт");
                            add(buttonShowVocab);
                            add(buttonShowNewTableWordAddition);
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
                            buttonShowVocab.addActionListener(new ToolBarInteract());
                            buttonShowNewTableWordAddition.addActionListener(new ToolBarInteract());
                            buttonShowNewOwnWordAddition.addActionListener(new ToolBarInteract());
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
                private JLabel labelAskedWord;
                PanelTraining() {
                    super(new CardLayout());
                    class PanelTrainingPickWord extends JPanel {
                        PanelTrainingPickWord() {
                            super(new GridBagLayout());
                            AfterAuthPanel.this.labelAskedWord = new JLabel("Известное слово");
                            for (int i = 0; i < 4; ++i) {
                                radioOptions[i] = new JRadioButton();
                                radioOptions[i].setPreferredSize(new Dimension(140, 40));
                            }
                            ButtonGroup buttonGroup = new ButtonGroup();
                            for (int i = 0; i < 4; ++i)
                                buttonGroup.add(radioOptions[i]);
                            JButton buttonAnswer = new JButton("Ответить");
                            JLabel labelLog = new JLabel("");
                            labelLog.setPreferredSize(new Dimension(120, 20));
                            labelLog.setHorizontalAlignment(SwingConstants.CENTER);
                            labelLog.setVerticalAlignment(SwingConstants.CENTER);
                            labelLog.setForeground(Color.red);
                            GridBagConstraints c = new GridBagConstraints();
                            JPanel radioPanel = new JPanel(new GridBagLayout());
                            c.gridx = 0;
                            c.gridy = 0;
                            radioPanel.add(radioOptions[0], c);
                            c.gridx = 1;
                            c.gridy = 0;
                            radioPanel.add(radioOptions[1], c);
                            c.gridx = 0;
                            c.gridy = 1;
                            radioPanel.add(radioOptions[2], c);
                            c.gridx = 1;
                            c.gridy = 1;
                            radioPanel.add(radioOptions[3], c);
                            c.gridx = 0;
                            c.gridy = 0;
                            c.anchor = GridBagConstraints.CENTER;
                            c.insets = new Insets(5, 0, 5, 0);
                            add(AfterAuthPanel.this.labelAskedWord, c);
                            c.gridy = 1;
                            add(radioPanel, c);
                            c.gridy = 2;
                            add(buttonAnswer, c);
                            c.gridy = 3;
                            add(labelLog, c);
                            buttonAnswer.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    labelLog.setForeground(Color.red);
                                    Enumeration<AbstractButton> allRadioButtons = buttonGroup.getElements();
                                    while(allRadioButtons.hasMoreElements())
                                    {
                                        JRadioButton temp = (JRadioButton)allRadioButtons.nextElement();
                                        if(temp.isSelected())
                                            if (trainingLogic.isCorrectTranslation(temp.getActionCommand())){
                                                labelLog.setForeground(Color.green);
                                                labelLog.setText("Верно!");
                                            } else
                                                labelLog.setText("Не верно!");
                                        temp.setSelected(false);
                                    }
                                    buttonGroup.clearSelection();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run(){
                                            try {
                                                Thread.sleep(500);
                                                labelLog.setText("");
                                            } catch (InterruptedException e) {
                                                //it can't happen
                                            }
                                        }
                                    }).start();
                                    if (trainingLogic.isIntermediateStage()) {
                                        labelAskedWord.setText(trainingLogic.getCurrentAskedWord());
                                        CardLayout cl = (CardLayout)(PanelTraining.this.getLayout());
                                        cl.next(PanelTraining.this);
                                    } else {
                                        AfterAuthPanel.this.labelAskedWord.setText(trainingLogic.getCurrentAskedWord());
                                        String[] options = trainingLogic.getOptions();
                                        for (int i = 0; i < 4; ++i)
                                            radioOptions[i].setText(options[i]);
                                    }
                                }
                            });
                        }
                    }
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
                                                //it can't happen
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
                    add(new PanelTrainingPickWord(), "pickWord");
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
