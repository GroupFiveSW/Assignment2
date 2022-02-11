import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Establishes a notification system to notify user via email about build results.
 */
public class MailHandler {
    SMTPSettings settings;

    Properties prop = System.getProperties();

    /**
     * Setup for SMTP service.
     * @param settings SMTP settings to be used when sending mail.
     */
    public MailHandler(SMTPSettings settings){
        this.settings = settings;
        prop.put("mail.smtp.host", settings.host());
        prop.put("mail.smtp.port", settings.port());
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
    }

    /**
     * Sends mail with the results of tests and compilation.
     * @param testResult Results from tests.
     */
    public void mail(TestResult testResult){
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(settings.username(), settings.password());
                    }
                });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(settings.recipient()));

            // Set Subject: header field
            message.setSubject("Tests " + (testResult.isSuccessful() ? "successful!": "failed."));

            // Now set the actual message
            message.setText(testResult.getLog());

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
