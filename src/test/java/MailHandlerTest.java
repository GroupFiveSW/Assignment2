import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.subethamail.smtp.server.SMTPServer;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class MailHandlerTest {

    PipedInputStream inStream;
    SMTPServer server;
    TestResult testResultSuccessful = new TestResult(true, "test successful");
    TestResult testResultFail = new TestResult(false, "test failed");

    /**
     * Settings for sending an email to localhost
     */
    SMTPSettings settings = new SMTPSettings("localhost", "25000", "hej@hej.com");

    @BeforeEach
    void setUp() throws IOException{

        inStream = new PipedInputStream();
        PipedOutputStream outStream = new PipedOutputStream();
        inStream.connect(outStream);

        // Server that receives mail from tests.
        server = SMTPServer
                .port(25000)
                .messageHandler(
                        (context, from, to, data) -> {
                                try {
                                    outStream.write(data);
                                    outStream.close();
                                } catch (IOException e){
                                    throw new RuntimeException(e);
                                }})
                .build();


        server.start();
    }

    @AfterEach
    void tearDown(){
        server.stop();
    }

    /**
     * Sends an email with a successful test build.
     * Checks that the correct message is sent.
     * @throws IOException Error when reading mail.
     */
    @Test
    void mailSuccess() throws IOException{
        MailHandler mailer = new MailHandler(settings);
        mailer.mail(testResultSuccessful);
        var body = inStream.readAllBytes();
        String receivedMsg = new String(body);
        assertTrue(receivedMsg.contains("Tests successful!"), "Mail should contain 'Tests successful!'");
    }

    /**
     * Sends an email with a failed test build.
     * Checks that the correct message is sent.
     * @throws IOException Error when reading mail.
     */
    @Test
    void mailFail() throws IOException{
        MailHandler mailer = new MailHandler(settings);
        mailer.mail(testResultFail);
        var body = inStream.readAllBytes();
        String receivedMsg = new String(body);
        assertTrue(receivedMsg.contains("Tests failed."), "Mail should contain 'Tests failed'.");
    }
}
