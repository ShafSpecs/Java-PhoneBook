package phonebook;

import java.util.ArrayList;

public class User {
    private PhoneBook phoneBook;
    private String username;
    private String password;
    private boolean isLockedAccount;

    public User(String username) {
        this.username = username;
        this.password = null;
        this.isLockedAccount = false;
        phoneBook = new PhoneBook();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isLockedAccount = true;

        phoneBook = new PhoneBook();
    }

    public PhoneBook getPhoneBook() {
        return phoneBook;
    }

    public boolean lockedAccount(){
        return isLockedAccount;
    }

    public void changePassword(String oldPassword, String newPassword) throws IllegalAccessException {
        if (oldPassword == password) this.password = newPassword;
        else throw new IllegalAccessException("Wrong password input!");
    }

    public String getUserName() {
        return username;
    }

    public boolean checkPassword (String password) {
        if (this.password == password) {
            return true;
        }

        return false;
    }
}
