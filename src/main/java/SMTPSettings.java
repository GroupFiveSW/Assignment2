/**
 * Settings for SMTP services used when sending email.
 */
public record SMTPSettings(String username, String password, String host, String port, String recipient) {
    /**
     * Optional constructor to be used when using default user to send mail.
     * @param host Host for SMTP server
     * @param port Port on which to host
     * @param recipient Recipient of mail.
     */
    public SMTPSettings(String host, String port, String recipient){
        this("testman543216789", "Testing12", host, port, recipient);

    }
}
