import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

class HiveConnection {

    private static HiveConnection instance = null;

    private Connection con;

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    private static String URL = "jdbc:hive2://localhost:10000";

    static Connection getInstance() {
        synchronized (HiveConnection.class) {
            if (instance == null) {
                instance = new HiveConnection();
            }
            return instance.con;
        }
    }

    private HiveConnection() {
        try {
           Class.forName(driverName);
           con = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(con != null) try {con.close();} catch (SQLException e) {/* ignore*/}
        }
    }

    private void onProgramExit() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread t : threadSet) {
            try {
                t.join();
            } catch (InterruptedException ex) {

            }
        }
    }
}
