package phonebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContactTest {
    private Contact user;

    @BeforeEach
    public void initializeUser() throws IllegalArgumentException { user = new Contact("+2349027364555"); }

    @Test
    public void userCanBeCreatedWithNoInput(){
        assertNotNull(user);
    }

    @Test
    public void userCanBeCreatedWithJustNumber() throws IllegalArgumentException {
        user = new Contact("08017263778");

        assertEquals("08017263778", user.getNumber());
    }

    @Test
    public void userCanBeCreatedWithNameAndNumber() throws IllegalArgumentException {
        user = new Contact("Soliu Plumber", "09017261622");

        assertEquals("Soliu Plumber", user.getName());
        assertEquals("09017261622", user.getNumber());
    }

    @Test
    public void userCanBeCreatedWithNameAndNumberAndEmail() throws IllegalArgumentException {
        String name = "Michael Boyo";
        String number = "09013533522";
        String email = "dundee@gmail.com";

        user = new Contact(name, number, email);

        assertEquals(name, user.getName());
        assertEquals(number, user.getNumber());
        assertEquals(email, user.getEmail());
    }

    @Test
    public void userCanBeCreatedWithNameAndNumberAndEmailAndAddress() throws IllegalArgumentException {
        String name = "Idiot Olamide";
        String number = "+2349015625362";
        String email = "monkey@gmail.com";
        String address = "Outside the Galaxy, Nigeria";

        user = new Contact(name, number, email, address);

        assertEquals(name, user.getName());
        assertEquals(number, user.getNumber());
        assertEquals(email, user.getEmail());
        assertEquals(address, user.getAddress());
    }

    @Test
    public void userNumberCanContainOnlyNumbersAndSymbols() {
        String scamNumber = "+234-Prince-Harry";
        assertThrows(IllegalArgumentException.class, () -> user = new Contact(scamNumber));

        String realNumber = "+234-901-892-0232";
        user = new Contact(realNumber);

        assertEquals(realNumber, user.getNumber());
    }

    @Test
    public void numberCanHaveMinimumOfEightDigitsAndAMaxOfFifteenDigits() {
        String idiot = "+234-99";
        assertThrows(IllegalArgumentException.class, () -> user = new Contact(idiot));

        String tooLong = "+44-7336-384737-28244";
        assertThrows(IllegalArgumentException.class, () -> user = new Contact(tooLong));

        String perfect = "+2349017363454";
        user = new Contact(perfect);

        assertEquals(perfect, user.getNumber());
    }

    @Test
    public void contactCanBeEditedAfterCreation() {
        user.editContact("Sulayman", null, null, "Just on the edge of the Atlantic...");

        assertEquals("Sulayman", user.getName());
        assertNotNull(user.getAddress());
        assertNull(user.getEmail());
    }
}
