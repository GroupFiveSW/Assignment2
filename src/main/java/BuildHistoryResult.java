import java.util.Date;

/**
 * Structure class for the resulting build-history list and individual items
 */

public class BuildHistoryResult {
    private Date date;
    private String log;
    private String commit;
    private String commitMessage;
    private Integer id;

    /**
     * Constructor for BuildHistoryResult
     * @param date Build date
     * @param log Build log
     * @param commit Commit id
     * @param id    Build id
     */
    public BuildHistoryResult(Date date, String log, String commit, String commitMessage, Integer id) {
        this.date = date;
        this.log = log;
        this.commit = commit;
        this.commitMessage = commitMessage;
        this.id = id;
    }

    /**
     * Gets the build date
     * @return Date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Gets the commit id
     * @return Commit id
     */
    public String getCommit() {
        return commit;
    }

    /**
     * Gets the build log
     * @return  Build log
     */
    public String getLog() {
        return log;
    }

    /**
     * Gets the commit id
     * @return Commit id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets commit message
     * @return Commit message
     */
    public String getCommitMessage() {
        return commitMessage;
    }
}
