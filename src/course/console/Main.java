package course.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.HashMap;

/**
 * Created by user on 30.10.2016.
 */

public class Main {
    static final String DB_URL = "jdbc:h2:./mainDb";
    static final String LOGIN = "sa";
    static final String PASSWORD = "";
    static HashMap<String, String> accounts;
    static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    static String nickname;

    public static void readAccounts() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Can't get driver: " + e.getMessage());
            accounts = null;
            return;
        }
        accounts = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM ACCOUNTS;");) {
            while (resultSet.next())
                accounts.put(resultSet.getString("nickname"), resultSet.getString("password"));
        } catch (SQLException e) {
            System.out.println("Can't get connection: " + e.getMessage());
            accounts = null;
        }
        return;
    }

    public static boolean login() {
        boolean isLogined = false;
        try {
            System.out.print("enter your nickname: ");
            nickname = in.readLine();
            String actualPass = accounts.get(nickname);
            System.out.print("enter your password: ");
            String password = in.readLine();
            if (actualPass == null)
                System.out.println("There is no such user in database");
            else if (!actualPass.equals(password))
                System.out.println("Incorrect password");
            else {
                System.out.println("Successfully logined!");
                isLogined = true;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return isLogined;
    }

    public static void signup() {
        boolean isUniqueNick = false;
        while (!isUniqueNick)
            try {
                System.out.print("enter your future nickname: ");
                nickname = in.readLine();
                if (!accounts.containsKey(nickname)) {
                    System.out.print("enter your password: ");
                    String password = in.readLine();
                    isUniqueNick = true;
                    accounts.put(nickname, password);
                    try (Connection connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
                         PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (nickname, password)" +
                                 "VALUES (?,?);");) {
                        statement.setString(1, nickname);
                        statement.setString(2, password);
                        int changedRows = statement.executeUpdate();
                        if (changedRows > 0) {
                            System.out.println("Successfully sign up!");
                        } else
                            System.out.println("Some troubles...");
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    System.out.println("this user already exists");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
    }

    public static void loginPart() {
        boolean isLogined = false;
        do {
            System.out.println("whatcha gonna do?");
            String action = null;
            try {
                action = in.readLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            switch (action) {
                case "login":
                    isLogined = login();
                    break;
                case "signup":
                    signup();
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("unknown operation");
                    break;
            }
        } while (!isLogined);
    }

    public static void createUserTable(){
        try (Connection connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + nickname + " (eng VARCHAR(255), rus VARCHAR(255));");) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addNewWord() {
        String engWord = null, rusWord = null;
        try {
            System.out.println("tap new english word, that will be added in your personal dictionary:");
            engWord = in.readLine();
            System.out.println("then it's translation:");
            rusWord = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try (Connection connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO " + nickname + " (eng, rus) VALUES (?,?);");) {
            statement.setString(1, engWord);
            statement.setString(2, rusWord);
            statement.executeUpdate();
            System.out.println("Successfuly added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void mainpart() {
        do {
            System.out.println("whatcha gonna do?");
            String action = null;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (action) {
                case "newWord":
                    addNewWord();
                    break;
                case "exit":
                    System.exit(0);
                    break;
            }
        }while(true);
    }



    public static void main(String[] args) {
        readAccounts();
        loginPart();
        createUserTable();
        mainpart();
    }
}
