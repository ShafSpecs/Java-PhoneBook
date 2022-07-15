package phonebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;

    @BeforeEach
    public void initializeUser() {
        user = new User("Random");
    }

    @Test
    public void userCanBeInitializedPerfectly() {
        assertNotNull(user);
    }

    @Test
    public void userHasJustOnePhoneBook() {
        user = new User("SearX");

        PhoneBook contactCollection = user.getPhoneBook();

        assertTrue(contactCollection.isEmpty());

        contactCollection.createUser("+1-902-4943-37");

        assertFalse(contactCollection.isEmpty());
    }

    @Test
    public void userCanAddContactToTheirPhoneBook() {
        userHasJustOnePhoneBook();

        assertEquals(1, user.getPhoneBook().size());
    }

    @Test
    public void userCanEditContactInTheirPhoneBook() throws NoSuchFieldException {
        userHasJustOnePhoneBook();

        PhoneBook collection = user.getPhoneBook();
        Contact contact = collection.searchByNumber("+1-902-4943-37");

        contact.editContact("Solomon", null, null, null);
        assertEquals("Solomon", user.getPhoneBook().searchByNumber("+1-902-4943-37").getName());
    }

    @Test
    public void userNeedsAUserNameToCreateAPhoneBook(){
        user = new User("Dorcas");

        assertNotNull(user);
    }

    @Test
    public void userCanGetAProtectedPhoneBookWithPassword() {
        user = new User("Morili", "password12345");

        assertTrue(user.lockedAccount());
    }

    @Test
    public void userCanChangePasswordOfLockedAccount() throws IllegalAccessException {
        user = new User("Chi", "jump");

        user.changePassword("jump", "slide");

        assertNotNull(user);
    }

    @Test
    public void ErrorOccursWhenWrongPasswordIsUsedToResetPassword() {
        user = new User("Monkey", "banana");

        assertThrows(IllegalAccessException.class,() -> user.changePassword("money", "orangutan"));
    }

    @Test
    public void userCanGetTheirUserNames() {
        user = new User("Mellow");

        assertEquals("Mellow", user.getUserName());
    }
}
