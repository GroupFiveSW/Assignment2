import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.*;

class GitHubIntegrationTest {
    String webhookBody;
    String webhookSecret;
    String webhookHMAC;

    @BeforeEach
    void setUp(){
        webhookBody = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
        webhookSecret = "pX%[?w2M:9zrx-mBp\"G8WV-";
        webhookHMAC = "sha256=399aff558b24178c98e6e07dfbecd1a6f34c8874d331ec3eae902d3711b6a5a5";
    }

    @Test
    void testValidateWebhookTrue() {
        var gh = new GitHubIntegration();
        var result = assertDoesNotThrow(() -> gh.validateWebhook(webhookBody, webhookHMAC, webhookSecret), "Validating the webhook should return true/false");
        assertTrue(result, "The webhook should be valid");
    }

    @Test
    void testValidateWebhookIncorrectSecret() {
        var gh = new GitHubIntegration();
        webhookSecret = "sX%[?w2M:9zrx-mBp\"G8WV-";
        var result = assertDoesNotThrow(() -> gh.validateWebhook(webhookBody, webhookHMAC, webhookSecret), "Validating the webhook should return true/false");
        assertFalse(result, "The webhook should not be valid");
    }

    @Test
    void testValidateWebhookBadSignatureFormat() {
        var gh = new GitHubIntegration();
        webhookHMAC = webhookHMAC.substring(2);
        assertThrows(IllegalArgumentException.class, () -> gh.validateWebhook(webhookBody, webhookHMAC, webhookSecret), "The signature format should be invalid");
    }

    @Test
    void testValidateWebhookBadSignatureEncoding() {
        var gh = new GitHubIntegration();
        webhookHMAC = "sha256=18af78f8f7c78b87eq";
        assertThrows(IllegalArgumentException.class, () -> gh.validateWebhook(webhookBody, webhookHMAC, webhookSecret), "The signature encoding should be invalid");
    }
}