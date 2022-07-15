package phonebook;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Runner {
    private static final Scanner input = new Scanner(System.in);
    private static final boolean dbDir = new File(System.getProperty("user.dir"), "src/main/java/phonebook/db").mkdir();
    private static final File db = new File(System.getProperty("user.dir"), "src/main/java/phonebook/db/db.json");
    private static ArrayList<User> users = new ArrayList<>();
    private static Session authSession = Session.LOGGED_OUT;
    private static String currentSession = null;

    public static void kickStart() throws IOException {
        startDatabase();

        System.out.print("""
                Welcome to FakeRealPhonebook App!
                Type 'help' at any point to get a list of commands you can quickly execute
                                
                1. Create an account
                2. Login to your account
                                
                >>> """);

        int menuChoice = input.nextInt();

        System.out.println();

        switch (menuChoice) {
            case 1 -> createAccount();

            case 2 -> logIn();

            default -> {
                System.out.println("Wrong Input! Try again.\n");
                kickStart();
            }
        }
    }

    // todo: Re-navigate user if logged in.

    public static void startDatabase() throws IOException {
        try {
            if (db.createNewFile()) {
                try (FileWriter file = new FileWriter(db)) {
                    Gson gson = new GsonBuilder().create();
                    JsonArray customArr = gson.toJsonTree(users).getAsJsonArray();
                    file.write(String.valueOf(customArr));
                    file.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Gson gson = new Gson();

                    Reader reader = Files.newBufferedReader(Path.of(db.getAbsolutePath()));
                    users = gson.fromJson(reader, new TypeToken<ArrayList<User>>() {
                    }.getType());

                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @Nullable
    public static ArrayList<User> readDatabase() {
        try {
            Gson gson = new Gson();

            Reader reader = Files.newBufferedReader(Path.of(db.getAbsolutePath()));
            users = gson.fromJson(reader, new TypeToken<ArrayList<User>>() {
            }.getType());

            reader.close();

            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void writeDatabase(ArrayList<User> editedDb) {
        try (FileWriter file = new FileWriter(db)) {
            Gson gson = new GsonBuilder().create();
            JsonArray customArr = gson.toJsonTree(editedDb).getAsJsonArray();
            file.write(String.valueOf(customArr));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //todo: Add handler for duplicates (account)

    public static void createAccount() {
        System.out.print("Enter your username: ");
        String name = input.next();

        System.out.print("Do you want a locked account? ([Y]es/[N]o): ");
        char choice = input.next().toCharArray()[0];

        System.out.println();

        if (choice == 'Y') {
            System.out.print("Enter your password: ");
            String password = input.next();

            User newUser = new User(name, password);
            users.add(newUser);

            currentSession = name;
            authSession = Session.LOGGED_IN;

            writeDatabase(users);

            dashboard(newUser);
        } else if (choice == 'N') {
            User newUser = new User(name);
            users.add(newUser);

            currentSession = name;
            authSession = Session.LOGGED_IN;

            writeDatabase(users);

            dashboard(newUser);
        } else {
            System.out.println("Wrong input! Try again.\n");
            createAccount();
        }
    }

    public static void logIn() {
        System.out.print("Enter your username: ");
        String name = input.next();

        ArrayList<User> users = readDatabase();

        for (User user : users) {
            if (Objects.equals(user.getUserName(), name)) {
                if (user.lockedAccount()) {
                    System.out.print("Enter your password: ");
                    String pass = input.next();

                    if (user.checkPassword(pass)) {
                        authSession = Session.LOGGED_IN;
                        currentSession = name;

                        System.out.println("Logged In");

                        dashboard(user);
                        return;
                    }

                    System.out.println("Wrong Credentials! Try again\n");
                    logIn();
                }

                authSession = Session.LOGGED_IN;
                currentSession = name;
                System.out.println("Logged in successfully\n");

                dashboard(user);
                return;
            }
        }

        System.out.println("Wrong Credentials! Try again\n");
        logIn();
    }

    public static void dashboard(User user) {
        System.out.printf("Welcome to %s's Dashboard! What do you want to do today?\n\n1. View all your contacts\n2. Add a contact\n3. Edit a contact\n4. Delete a contact\n5. Search for a contact\n6. Sign Out", user.getUserName());
    }
}
