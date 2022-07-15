package phonebook;

import java.util.ArrayList;

public class PhoneBook {
    private int size;
    private ArrayList<Contact> phoneBook;

    public PhoneBook(){
        size = 0;
        phoneBook = new ArrayList<>();
    }

    public boolean isEmpty(){
        if (size == 0){
            return true;
        }

        return false;
    }

    public void createUser(String number) {
        createUser(null, number, null, null);
    }

    public void createUser(String name, String number) {
        createUser(name, number, null, null);
    }

    public void createUser(String name, String number, String email) {
        createUser(name, number, email, null);
    }

    public void createUser(String name, String number, String email, String address) {
        Contact newUser = new Contact(name, number, email, address);

        phoneBook.add(newUser);
        size++;
    }

    public Contact searchByNumber(String number) throws NoSuchFieldException {
        for (Contact user : phoneBook) {
            if(user.getNumber() == number) return user;
        }

        throw new NoSuchFieldException("User not found");
    }

    public int size() {
        return size;
    }

    public Contact searchByName(String name) throws NoSuchFieldException {
        for (Contact user : phoneBook) {
            if(user.getName() == name) return user;
        }

        throw new NoSuchFieldException("User not found");
    }

    public void deleteUser(Contact uselessUser) {
        phoneBook.remove(uselessUser);
        size--;
    }

    public ArrayList<Contact> getPhoneBook() {
        return phoneBook;
    }
}
