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
import java.util.concurrent.TimeUnit;

public class Runner {
    private static final Scanner input = new Scanner(System.in);
    private static final boolean dbDir = new File(System.getProperty("user.dir"), "src/main/java/phonebook/db").mkdir();
    private static final File db = new File(System.getProperty("user.dir"), "src/main/java/phonebook/db/db.json");
    private static ArrayList<User> users = new ArrayList<>();
    private static Session authSession = Session.LOGGED_OUT;
    private static String currentSession = null;

    public static void kickStart() throws IOException, NoSuchFieldException, InterruptedException {
        startDatabase();

        System.out.print("""
                Welcome to FakeRealPhonebook App!
                Type 'help' at any point to get a list of commands you can quickly execute
                                
                1. Create an account
                2. Login to your account
                                
                >>>  """);

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

    public static void startDatabase() {
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

    public static void createAccount() throws NoSuchFieldException, IOException, InterruptedException {
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

    public static void logIn() throws NoSuchFieldException, IOException, InterruptedException {
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

    public static void dashboard(User user) throws NoSuchFieldException, IOException, InterruptedException {
        System.out.printf("Welcome to %s's Dashboard! What do you want to do today?\n\n1. View all your contacts\n2. Add a contact\n3. Edit a contact\n4. Delete a contact\n5. Search for a contact\n6. Sign Out\n\n>>> ", user.getUserName());
        int userChoice = input.nextInt();

        switch (userChoice) {
            case 1 -> viewAllContacts(user);

            case 2 -> addContact(user);

            case 3 -> {
                System.out.println();
                System.out.print("Enter the contact number to edit: ");

                String number = input.nextLine();

                Contact foundNumber = user.getPhoneBook().searchByNumber(number);

                editContact(user, foundNumber);
            }

            case 4 -> {
                System.out.println();
                System.out.print("Enter the contact number to delete: ");

                String number = input.nextLine();

                Contact foundNumber = user.getPhoneBook().searchByNumber(number);

                deleteContact(user, foundNumber);
            }

            case 6 -> logOut(user);
        }
    }

    public static void viewAllContacts(User user) throws NoSuchFieldException, IOException, InterruptedException {
        ArrayList<Contact> phone = user.getPhoneBook().getPhoneBook();

        if(phone.size() == 0) {
            System.out.println();
            System.out.println("No contact found!");
            System.out.println();
            System.out.print("1. Add a contact\n2. Go back to main menu\n3. Sign Out\n>>> ");

            int userChoice = input.nextInt();

            switch (userChoice) {
                case 1 -> addContact(user);

                case 2 -> dashboard(user);

                case 3 -> logOut(user);
            }
        } else {
            for (int i = 0; i < phone.size(); i++) {
                System.out.println("==========");
//                System.out.println();
                System.out.printf("__%d__\nName: %s\nPhone Number: %s\nEmail Address: %s\nHome Address: %s\n", i + 1, phone.get(i).getName(), phone.get(i).getNumber(), phone.get(i).getEmail(), phone.get(i).getAddress());
                System.out.println();
                System.out.println("==========");
            }

            System.out.println();
            System.out.print("1. Add Contact\n2. Edit Contact\n3. Delete Contact\n4. Go Back to main menu\n5. Sign Out\n>>> ");

            int userChoice = input.nextInt();

            switch (userChoice) {
                case 1 -> addContact(user);

                case 2 -> {
                    System.out.println();

                    System.out.print("Which contact do you want to edit(Enter the index): ");
                    int choice = input.nextInt();

                    editContact(user, phone.get(choice - 1));
                }

                case 3 -> {
                    System.out.println();

                    System.out.print("Which contact do you want to delete(Enter the index): ");
                    int choice = input.nextInt();

                    deleteContact(user, phone.get(choice - 1));
                }

                case 4 -> dashboard(user);

                case 5 -> logOut(user);

                default -> {
                    System.out.println("Wrong input!");
                    viewAllContacts(user);
                }
            }
        }
    }

    public static void addContact(User user) throws NoSuchFieldException, IOException, InterruptedException {
        System.out.println();
        System.out.print("Enter the name (Not required. 's' to skip): ");
        String name = input.next();

        System.out.print("Enter the number (required): ");
        String num = input.next();

        System.out.print("Enter Email Address (Not required. 's' to skip): ");
        String email = input.next();

        System.out.print("Enter Home Address (Not required. 's' to skip): ");
        String address = input.next();

        user.getPhoneBook().createUser(name.equalsIgnoreCase("s") ? null : name, num, email.equalsIgnoreCase("s") ? null : email, address.equalsIgnoreCase("s") ? null : address);

        writeDatabase(users);

        System.out.println("User successfully created!\n");
        dashboard(user);
    }

    public static void editContact(User user, Contact con) throws NoSuchFieldException, IOException, InterruptedException {
        System.out.printf("Enter contacts' name (Press 's' to skip)(%s): ");
        String name = input.next();

        System.out.printf("Enter the number (Press 's' to skip)(%s): ");
        String num = input.next();

        System.out.printf("Enter Email Address (Press 's' to skip)(%s): ");
        String email = input.next();

        System.out.printf("Enter Home Address (Press 's' to skip)(%s): ");
        String address = input.next();

        con.editContact(name.equalsIgnoreCase("s") ? con.getName() : name, num.equalsIgnoreCase("s") ? con.getNumber() : num, email.equalsIgnoreCase("s") ? con.getEmail() : email, address.equalsIgnoreCase("s") ? con.getAddress() : address);

        writeDatabase(users);
        System.out.println("Contact updated successfully!");

        dashboard(user);
    }

    public static void deleteContact(User user, Contact con) throws NoSuchFieldException, IOException, InterruptedException {
        user.getPhoneBook().deleteUser(con);

        writeDatabase(users);

        dashboard(user);
    }

    public static void logOut(User user) throws InterruptedException, IOException, NoSuchFieldException {
        System.out.println();

        System.out.println("Logging Out...");
        TimeUnit.SECONDS.sleep(2);
        System.out.println("Successfully logged out!");

        currentSession = null;
        authSession = Session.LOGGED_OUT;

        kickStart();
    }
}
