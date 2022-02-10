import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.sql.ResultSet;

public class BuildHistory {

    /**
     * Method to add build to database
     * @param result TestResult object
     * @param commit Commit id
     */
    public void addBuild (TestResult result, String commit) {
        try (Connection conn = DBConnection.createDbConnection()) {
            Date date = new Date();
            String query = "INSERT INTO builds ("
                    + " commit_id,"
                    + " build_date,"
                    + " build_log ) VALUES ("
                    + "?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, commit);
            st.setDate(2, new java.sql.Date(date.getTime()));
            st.setString(3, result.getLog());
            st.executeUpdate();
            st.close();
            System.out.println("Successfully added build log");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Method to get all builds from database
     * @return A list of Builds
     */
    public ArrayList<BuildHistoryResult> getAllBuilds() {
        try (Connection conn = DBConnection.createDbConnection()) {
            String query = "SELECT * FROM builds";
            Statement st = conn.createStatement();
            ResultSet results = st.executeQuery(query);
            ArrayList<BuildHistoryResult> builds = new ArrayList<BuildHistoryResult>();

            while (results.next()) {
                BuildHistoryResult bh = new BuildHistoryResult(
                        results.getDate("build_date"),
                        results.getString("build_log"),
                        results.getString("commit_id"),
                        results.getInt("id")
                );
                builds.add(bh);
            }
            return builds;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to get single build from database by ID
     * @param id Build ID
     * @return BuildHistoryResult Object
     */
    public BuildHistoryResult getBuildById(int id) {
        try (Connection conn = DBConnection.createDbConnection()) {
            String query = "SELECT * FROM builds WHERE id=?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, id);
            ResultSet results = st.executeQuery();
            while(results.next()) {
                BuildHistoryResult bh = new BuildHistoryResult(
                        results.getDate("build_date"),
                        results.getString("build_log"),
                        results.getString("commit_id"),
                        results.getInt("id")
                );
                return bh;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
