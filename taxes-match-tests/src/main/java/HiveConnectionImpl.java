import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Log4j
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
        log.info("Trying to get connection");
        try {
           Class.forName(driverName);
           con = DriverManager.getConnection(URL);
           log.info("Connection created successfully");
        } catch (SQLException | ClassNotFoundException e) {
            log.fatal("Can't create connection, check JDBC driver");
            onProgramExit();
        }
    }

    @SneakyThrows
    private void onProgramExit() {
        log.info("Terminating...");
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread t : threadSet) {
                t.join();
        }
    }
}
