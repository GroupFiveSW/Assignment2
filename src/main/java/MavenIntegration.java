import org.apache.maven.shared.invoker.*;

import java.io.*;
import java.util.Collections;

/**
 * Contains methods for handling interactions with maven
 */
public class MavenIntegration {

    private String pomFile;
    private String javaHome;

    /**
     * Creates a new maven instance linked to a pom.xml
     * @param pomFile
     */
    public MavenIntegration(String pomFile) {
        this(pomFile, getDefaultJavahome());
    }

    /**
     * Creates a new maven instance linked to a pom.xml
     * @param pomFile pom.xml file of the maven project
     * @param javaHome An installation directory of a JDK
     */
    public MavenIntegration(String pomFile, String javaHome){
        this.pomFile = pomFile;
        this.javaHome = javaHome;
    }

    private static String getDefaultJavahome() {
        String javaHome = System.getProperty("mavenintgr.java_home");
        if (javaHome == null || javaHome.equals("")) {
            javaHome = System.getenv("MAVENINTGR_JAVAHOME");
        }
        if (javaHome == null || javaHome.equals("")) {
            javaHome = System.getenv("JAVA_HOME");
        }
        return javaHome;
    }

    /**
     * Runs all tests on the maven project
     * @return The result of the tests indicating if they
     * were successful.
     * @throws MavenInvocationException if maven failed to run
     */
    public TestResult test() throws MavenInvocationException {
        var request = new DefaultInvocationRequest();
        var file = new File(pomFile);
        request.setPomFile(file);
        request.setJavaHome(new File(javaHome));
        request.setGoals(Collections.singletonList("test"));
        request.setInputStream(InputStream.nullInputStream());

        var outputHandler = new StringBuilderOutputHandler();

        var invoker = new DefaultInvoker();
        invoker.setOutputHandler(outputHandler);
        invoker.setErrorHandler(outputHandler);
        var result = invoker.execute(request);

        return new TestResult(
                result.getExitCode() == 0,
                outputHandler.toString()
        );
    }
}
