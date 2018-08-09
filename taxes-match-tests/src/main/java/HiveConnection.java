import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

class HiveConnection {

    private static HiveConnection instance = null;

    private Connection con;

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    private static String URL = "jdbc:hive://localhost:10000";

    static Connection getInstance() {
        synchronized (HiveConnection.class) {
            if (instance == null) {
                instance = new HiveConnection();
            }
            return new HiveConnection().instance.con;
        }
    }

    private HiveConnection() {
        try {
            Class.forName(driverName);
            instance.con = DriverManager.getConnection(URL);
            // instance.con = new org.apache.hive.jdbc.HiveConnection(URL, new Properties());
        } catch (SQLException e) {
            e.printStackTrace();
            onProgramExit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            onProgramExit();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {/* ignore*/}
        }
    }

    private void onProgramExit() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread t : threadSet) {
            try {
                t.join();
            } catch (InterruptedException ex) {/* ignore*/}
        }
    }
}
