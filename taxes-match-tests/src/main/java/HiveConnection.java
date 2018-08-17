import lombok.Getter;
import lombok.Synchronized;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
class HiveConnection {

    @NonFinal
    static HiveConnection instance = null;

    @Getter
    @NonFinal
    Connection con;

    final static String driverName = "org.apache.hive.jdbc.HiveDriver";

    final static String URL = "jdbc:hive2://localhost:10000";

    @Synchronized
    static HiveConnection getInstance() {
            if (instance == null) {
                instance = new HiveConnection();
            }
            return instance;
    }

    private HiveConnection() {
        try {
           Class.forName(driverName);
           con = DriverManager.getConnection(URL);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            onProgramExit();
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
