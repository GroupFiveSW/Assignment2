/**
 * Data class containing results of a maven test run.
 */
public class TestResult {
    private boolean success;
    private String log;

    /**
     * Creates a new data object
     * @param isSuccessful Whether the test run was successful or not
     * @param log The log of the test run
     */
    public TestResult(boolean isSuccessful, String log){
        this.success = isSuccessful;
        this.log = log;
    }

    /**
     * Gets whether the test run was successful or not
     * @return true if it was successful and false if not
     */
    public boolean isSuccessful() {
        return success;
    }

    /**
     * Gets the log of the test run
     * @return The test run log
     */
    public String getLog(){
        return log;
    }
}
