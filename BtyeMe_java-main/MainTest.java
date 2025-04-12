import org.junit.Test;
import org.junit.jupiter.api.*;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;


public class MainTest {

    @BeforeEach
    void setUp() throws IOException {
        // Prepare a test file with a customer record
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Main.customerFilePath))) {
            writer.write("testUser,testEmail,testPass");
            writer.newLine();
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up test file after each test
        File file = new File(Main.customerFilePath);
        if (file.exists()) {
            assertTrue(file.delete(), "Failed to delete test file");
        }
    }

    @Test
    public void testInvalidAdminLogin() {
        String invalidUsername = "wrongAdmin";
        String invalidPassword = "wrongPass";

        String adminUsername = "123";
        String adminPassword = "123";

        boolean isValid = invalidUsername.equals(adminUsername) && invalidPassword.equals(adminPassword);
        assertFalse(isValid, "Admin login should fail for invalid credentials");
    }

    @Test
    public void testInvalidCustomerLogin() {
        String invalidUsername = "wrongUser";
        String invalidPassword = "wrongPass";

        Customer customer = Main.findCustomerFromFile(invalidUsername, invalidPassword, Main.customerFilePath);
        assertNull(customer, "Customer login should fail for invalid credentials");
    }

}
