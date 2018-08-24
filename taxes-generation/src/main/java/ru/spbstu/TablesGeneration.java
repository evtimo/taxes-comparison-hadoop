package ru.spbstu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class TablesGeneration {

    private static LinkedHashMap<String, String> queries;
    private String initPathName, comparePathName;
    private SQLReader sqlread;

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    private String URL = "jdbc:hive2://localhost:10000";

    private static final Logger log = LogManager.getLogger(TablesGeneration.class);

    private TablesGeneration() {
        String resourceName = "config.properties";

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlread = new SQLReader();

        comparePathName = props.getProperty("queriesCompareFilePath");
        log.info("Trying to read HQL-queries from path " + comparePathName);
        queries = sqlread.readQueries(comparePathName);  //Read COMPARE queries
        initPathName = props.getProperty("queriesCreateFilePath");
    }

    private void createTables(Connection con) {

        try {
            log.info("Generation of tables SELLER and CUSTOMER started");
            Process process = Runtime.getRuntime().exec("hive -f " + this.initPathName);
            process.waitFor();
            log.info("Tables SELLER and CUSTOMER created successfully");
        } catch (IOException | InterruptedException e) {
            log.fatal("Error in CREATE TABLE process, termination");
            onProgramExit();
            e.printStackTrace();
        }
    }

    private static void matchTables(Connection con) {
        //TODO: executing ALL queries from file, even without names
        try {
            log.info("Matching tables generation started");
            Statement stmt = con.createStatement();
            /*stmt.execute(queries.get("SELLER_ERRORS"));
            stmt.execute(queries.get("CUSTOMER_ERRORS"));
            stmt.execute(queries.get("SELLER_CORRECT"));
            stmt.execute(queries.get("CUSTOMER_CORRECT"));
            stmt.execute(queries.get("CORRECT_COMPLETELY"));
            stmt.execute(queries.get("CORRECT_TOTAL_DIFF"));
            stmt.execute(queries.get("SELLER_ERRORS_CUSTOMER_HAS_PAIR"));
            stmt.execute(queries.get("SELLER_ERRORS_SELLER_HAS_PAIR"));
            stmt.execute(queries.get("CUSTOMER_ERRORS_CUSTOMER_HAS_PAIR"));
            stmt.execute(queries.get("CUSTOMER_ERRORS_SELLER_HAS_PAIR"));
            stmt.execute(queries.get("SELLER_ERRORS_HAS_NO_PAIR"));
            stmt.execute(queries.get("CUSTOMER_ERRORS_HAS_NO_PAIR"));*/
            for (Map.Entry<String, String> entry : queries.entrySet()) {
                String tableName = entry.getKey();
                stmt.execute(queries.get(tableName));
                log.info(tableName + " table was created");
            }
        } catch (SQLException e) {
            log.info("Error during creating matching tables");
            e.printStackTrace();
        }
    }

    private void dropSourceTables(Connection con) {
        try {
            Statement stmt = con.createStatement();
            log.info("Dropping tables SELLER and CUSTOMER started");
            stmt.execute("DROP TABLE IF EXISTS SELLER");
            stmt.execute("DROP TABLE IF EXISTS CUSTOMER");
            log.info("Tables SELLER and CUSTOMER successfully dropped");
        } catch (SQLException e) {
            log.error("Error during dropping tables");
            e.printStackTrace();
        }

    }

    private void dropMatchingTables(Connection con) {

        try {
            Statement stmt = con.createStatement();
            for (Map.Entry<String, String> entry : queries.entrySet()) {
                String tableName = entry.getKey();
                stmt.execute("DROP TABLE IF EXISTS " + tableName);
                log.info(tableName + " was dropped");
            }
        } catch (SQLException e) {
            log.info("Error during dropping matching tables");
            e.printStackTrace();
        }
    }

    private Connection connect() {
        log.info("Trying to connect Hive by URL: " + URL);
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException e) {
            log.error("JDBC driver does not exist, check driver");
            e.printStackTrace();
            onProgramExit();
            return null;
        } catch (SQLException e) {
            log.error("SQL error during connection");
            e.printStackTrace();
            return null;
        }

    }

    private void onProgramExit() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread t : threadSet) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        //Use this code to run programm for the first time
        //Create all tables and insert your data from CVS or manually
        //TODO: method for loading csv ino SELLER and CUSTOMER tables

        TablesGeneration tg = new TablesGeneration();
        Connection con = tg.connect();
        log.info("Connected to Hive successfully!");
        if (args.length == 0) {
            log.error("Jar should be executed with arguments!");
        } else {
            switch (args[0]) {
                case "create":
                    tg.dropSourceTables(con);
                    tg.createTables(con);
                    break;
                case "match":
                    tg.dropMatchingTables(con);
                    switch (args.length > 1 ? args[1] : "all") {
                        //TODO: dictionary of exact queries
                        //case(
                        case "all":
                            tg.matchTables(con);
                            break;
                        default:
                            tg.matchTables(con);
                            break;
                    }
                    break;
                case "drop":
                    switch (args.length > 1 ? args[1] : "all") {
                        case "all":
                            tg.dropSourceTables(con);
                            tg.dropMatchingTables(con);
                            break;
                        case "source":
                            tg.dropSourceTables(con);
                            break;
                        case "matching":
                            tg.dropMatchingTables(con);
                            break;
                    }
                    break;
                default:
                    log.info("Incorrect arguments!");
            }
        }
    }

}
