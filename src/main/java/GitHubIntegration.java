import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HexFormat;

/**
 * Contains methods for handling integration towards GitHub.
 */
public class GitHubIntegration {

    private final String HMAC_SHA256 = "HmacSHA256";
    private final String WEBHOOK_SIGNATURE_PREFIX = "sha256=";

    /**
     * Validates a webhook request
     * @param body The body of the request
     * @param signatureHeader The contens of header X-Hub-Signature-256
     * @param secret The secret specified in the webhook
     * @return True if the signature is valid, false otherwise
     * @throws NoSuchAlgorithmException if the hardcoded algorithm is invalid
     * @throws InvalidKeyException if the hardcoded secret key spec is invalid
     * @throws IllegalArgumentException if the signature is incorrectly formatted
     */
    public boolean validateWebhook(String body, String signatureHeader, String secret)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalArgumentException
    {
        // Check that the signature formatting and encoding is valid
        if (!signatureHeader.startsWith(WEBHOOK_SIGNATURE_PREFIX)) {
            throw new IllegalArgumentException("The signature does not start with \""+WEBHOOK_SIGNATURE_PREFIX+"\"");
        }
        String signaturePart = signatureHeader.substring(WEBHOOK_SIGNATURE_PREFIX.length());;
        byte[] signature;
        try {
            signature = HexFormat.of().parseHex(signaturePart);
        } catch (Exception ex) {
            throw new IllegalArgumentException("The signature header is not hexadecimal", ex);
        }

        // Initialize Mac object
        Mac mac;
        try {
            mac = Mac.getInstance(HMAC_SHA256);
        } catch (NoSuchAlgorithmException ex){
            throw new NoSuchAlgorithmException("Could not validate webhook, the hardcoded algorithm is invalid", ex);
        }
        var secretKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA256);
        try {
            mac.init(secretKey);
        } catch (InvalidKeyException ex) {
            throw new InvalidKeyException("Could not validate webhook, Mac object could not accept secret key", ex);
        }

        // Generate and compare HMAC
        var expected = mac.doFinal(body.getBytes());
        return Arrays.equals(expected, signature);
    }
}
