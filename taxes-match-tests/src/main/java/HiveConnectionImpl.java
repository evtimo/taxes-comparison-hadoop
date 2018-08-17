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
class HiveConnectionImpl {

    @NonFinal
    static HiveConnectionImpl instance = null;

    @Getter
    @NonFinal
    Connection con;

    final static String driverName = "org.apache.hive.jdbc.HiveDriver";

    final static String URL = "jdbc:hive2://localhost:10000";

    @Synchronized
    static HiveConnectionImpl getInstance() {
            if (instance == null) {
                instance = new HiveConnectionImpl();
            }
            return instance;
    }

    private HiveConnectionImpl() {
        try {
           Class.forName(driverName);
           con = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
            onProgramExit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            onProgramExit();
	}
//      } 
//	finally {
//            if(con != null) try {con.close();} catch (SQLException e) {/* ignore*/}
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
