import java.util.Date;

public class BuildHistoryResult {
    private Date date;
    private String log;
    private String commit;
    private Integer id;

    /**
     * Constructor for BuildHistoryResult
     * @param date Build date
     * @param log Build log
     * @param commit Commit id
     * @param id    Build id
     */
    public BuildHistoryResult(Date date, String log, String commit, Integer id) {
        this.date = date;
        this.log = log;
        this.commit = commit;
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
}
