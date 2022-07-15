package phonebook;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Contact {
    private final Map<String, String> user;

    public Contact(String number) throws IllegalArgumentException {
        this(null, number, null, null);
    }

    public Contact(String name, String number) throws IllegalArgumentException {
        this(name, number, null, null);
    }

    public Contact(String name, String number, String email) throws IllegalArgumentException {
        this(name, number, email, null);
    }

    public Contact(String name, String number, String email, String address) throws IllegalArgumentException {
        user = new HashMap();

        char[] validPhoneNumberMembers = {'+', '-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

        user.put("name", name);

        if (!isSubset(validPhoneNumberMembers, number.toCharArray(), validPhoneNumberMembers.length, number.toCharArray().length)) {
            throw new IllegalArgumentException("Invalid number input! Numbers can contain only 0-9, '+' and '-'.");
        }

        if (number.length() < 8) {
            throw new IllegalArgumentException("Invalid number input! Number cannot be less than 8 digits/symbols ");
        }

        if (number.length() > 18) {
            throw new IllegalArgumentException("Invalid number input! Number can not be greater than 18 digits/symbols");
        }

        user.put("number", number);
        user.put("email", email);
        user.put("address", address);
    }

    private boolean isSubset(char[] arr1,
                             char[] arr2,
                             int m, int n) {
        int i = 0;
        int j = 0;
        for (i = 0; i < n; i++) {
            for (j = 0; j < m; j++) {
                if (arr2[i] == arr1[j]) {
                    break;
                }
            }

            if (j == m)
                return false;
        }
        return true;
    }

    public String getNumber() {
        return user.get("number");
    }

    public String getName() {
        return user.get("name");
    }

    public String getEmail() {
        return user.get("email");
    }

    public String getAddress() {
        return user.get("address");
    }

    public void editContact(String name, String number, String email, String address) {
        Optional<String> n = Optional.ofNullable(name);
        Optional<String> tel = Optional.ofNullable(number);
        Optional<String> e = Optional.ofNullable(email);
        Optional<String> a = Optional.ofNullable(address);

        String newName = n.orElseGet(this::getName);
        String newNumber = tel.orElseGet(this::getNumber);
        String newEmail = e.orElseGet(this::getEmail);
        String newAddress = a.orElseGet(this::getAddress);

        user.replace("name", newName);
        user.replace("number", newNumber);
        user.replace("email", newEmail);
        user.replace("address", newAddress);
    }
}
