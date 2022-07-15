package phonebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PhoneBookTest {
    private PhoneBook phone;

    @BeforeEach
    public void initializePhone() {
        phone = new PhoneBook();
    }

    @Test
    public void phoneBookIsEmptyOnInitialization() {
        assertTrue(phone.isEmpty());
    }

    @Test
    public void phoneBookCanCreateANewContactEntry() throws NoSuchFieldException {
        phone.createUser("Soliu Plumber", "+2348097462763");

        assertEquals("Soliu Plumber", phone.searchByNumber("+2348097462763").getName());
    }

    @Test
    public void userCanSearchForAnEntryWithEntryNumber() throws NoSuchFieldException {
        phoneBookCanCreateANewContactEntry();
    }

    @Test
    public void phoneBookSizeIncreasesWhenANewEntryIsCreated() throws NoSuchFieldException {
        phoneBookCanCreateANewContactEntry();

        assertEquals(1, phone.size());
    }

    @Test
    public void canSearchPhoneBookEntryWithName() throws NoSuchFieldException {
        phone.createUser("Chicken Man", "09018277362");
        Contact user = phone.searchByName("Chicken Man");

        assertEquals("Chicken Man", user.getName());
    }

    @Test
    public void canRemoveEntryFromPhoneBook() throws NoSuchFieldException {
        canSearchPhoneBookEntryWithName();

        Contact uselessUser = phone.searchByName("Chicken Man");
        phone.deleteUser(uselessUser);

        assertEquals(0, phone.size());
        assertTrue(phone.isEmpty());
    }

    @Test
    public void phoneBookEntryCanBeEditedCauseWhyNot() throws NoSuchFieldException {
        canSearchPhoneBookEntryWithName();

        Contact user = phone.searchByName("Chicken Man");
        user.editContact(null, null, null, "Bariga Market");

        assertEquals("Bariga Market", user.getAddress());
        assertEquals("Chicken Man", user.getName());
    }
}
