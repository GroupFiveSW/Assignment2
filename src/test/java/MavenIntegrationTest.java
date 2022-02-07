import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MavenIntegrationTest {

    /**
     * Tests a valid project with valid test cases.
     */
    @Test
    void testTestTrue() {
        var mvn = new MavenIntegration("src/test/testprojects/valid/pom.xml");
        var result = assertDoesNotThrow(()->mvn.test(), "Test should not throw error");
        assertTrue(result.isSuccessful(), "Test should be successful");
    }

    /**
     * Tests a valid project with invalid test cases.
     */
    @Test
    void testTestFalse() {
        var mvn = new MavenIntegration("src/test/testprojects/testfail/pom.xml");
        var result = assertDoesNotThrow(()->mvn.test(), "Test should not throw error");
        assertFalse(result.isSuccessful(), "Test should not be successful");
        assertTrue(result.getLog().contains("Tests run: 1, Failures: 1"), "Test should fail 1 out of 1 tests");
    }
}