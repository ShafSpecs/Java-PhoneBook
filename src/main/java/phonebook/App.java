package phonebook;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            Runner.kickStart();
        } catch (IOException err) {
            err.printStackTrace();
        } catch (NoSuchFieldException err) {
            err.printStackTrace();
        } catch (InterruptedException err) {
            err.printStackTrace();
        }
    }
}
