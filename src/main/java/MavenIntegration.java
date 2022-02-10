import org.apache.maven.shared.invoker.*;

import java.io.*;
import java.util.Collections;

/**
 * Contains methods for handling interactions with maven
 */
public class MavenIntegration {

    private String pomFile;
    private String javaHome;
    private String mavenHome;

    /**
     * Creates a new maven instance linked to a pom.xml
     * @param pomFile pom.xml file of the maven project
     */
    public MavenIntegration(String pomFile){
        this.pomFile = pomFile;
        this.javaHome = getDefaultJavahome();
        this.mavenHome = getDefaultMavenhome();
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

    private static String getDefaultMavenhome() {
        String mavenHome = System.getProperty("maven.home");
        if (mavenHome == null || mavenHome.equals("")) {
            mavenHome = System.getenv("MAVENINTGR_MAVENHOME");
        }
        return mavenHome;
    }

    /**
     * Gets the java home directory to use
     * @return a JDK directory
     */
    public String getJavaHome(){
        return javaHome;
    }

    /**
     * Sets the java home directory to use
     * @param javaHome a JDK directory
     */
    public void setJavaHome(String javaHome){
        this.javaHome = javaHome;
    }

    /**
     * Gets the java home directory to use
     * @return a maven home directory
     */
    public String getMavenHome(){
        return mavenHome;
    }

    /**
     * Gets the java home directory to use
     * @param mavenHome a maven home directory
     */
    public void setMavenHome(String mavenHome){
        this.mavenHome = mavenHome;
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
        invoker.setMavenHome(new File(mavenHome));
        invoker.setOutputHandler(outputHandler);
        invoker.setErrorHandler(outputHandler);
        var result = invoker.execute(request);

        return new TestResult(
                result.getExitCode() == 0,
                outputHandler.toString()
        );
    }
}
