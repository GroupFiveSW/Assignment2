import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidTest {
    @Test
    void testReturnTrue() {
        assertFalse(Main.returnTrue(), "Should return false");
    }
}