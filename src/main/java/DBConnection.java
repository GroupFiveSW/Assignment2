
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Establishes connection to MySQL database at Heroku
 */
public class DBConnection {
    private static String host = "jdbc:mysql://us-cdbr-east-05.cleardb.net:3306/heroku_3584e8d8c21353f";
    private static String username = "b516f4dfafb243";
    private static String password = "2ed49261";
    private static Connection conn;

    /**
     * Function to create connection with database
     * @return Connection instance
     */
    public static Connection createDbConnection() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(host, username, password);
            System.out.println("Connected to db");
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
            e.printStackTrace();
        } finally {
            return conn;
        }
    }
}
