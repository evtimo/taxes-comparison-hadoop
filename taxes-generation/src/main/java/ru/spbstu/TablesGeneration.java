package ru.spbstu;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

public class TablesGeneration {

    private static Map<String, String> queries;
    private String pathname;
    private SQLReader sqlread;

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    private String URL = "jdbc:hive2://localhost:10000";

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

        pathname = props.getProperty("queriesCompareFilePath");
        queries = sqlread.readQueries(pathname);  //Read COMPARE queries
        pathname = props.getProperty("queriesCreateFilePath");
        //this.queries = sqlread.readQueries(pathname);  //Read CREATE / DROP queries
    }

    private void createTables(Connection con) {

    private static void createTables(Connection con) {

        try {
            Process process = Runtime.getRuntime().exec("hive -f " + this.pathname);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compareTables(Connection con) {
        try {
            Statement stmt = con.createStatement();
            stmt.execute(queries.get("QUERY_COMPARE_SELLER_ERR"));
            stmt.execute(queries.get("QUERY_COMPARE_CUSTOMER_ERR"));
            stmt.execute(queries.get("QUERY_COMPARE_SELLER_CORR"));
            stmt.execute(queries.get("QUERY_COMPARE_CUSTOMER_CORR"));
            stmt.execute(queries.get("QUERY_COMPARE_CORRECT_COMPLETELY"));
            stmt.execute(queries.get("QUERY_COMPARE_CORRECT_TOTAL_DIFF"));
            stmt.execute(queries.get("QUERY_COMPARE_SELLER_ERROR_CUSTOMER_HAS_PAIR"));
            stmt.execute(queries.get("QUERY_COMPARE_SELLER_ERROR_SELLER_HAS_PAIR"));
            stmt.execute(queries.get("QUERY_COMPARE_CUSTOMER_ERROR_CUSTOMER_HAS_PAIR"));
            stmt.execute(queries.get("QUERY_COMPARE_CUSTOMER_ERROR_SELLER_HAS_PAIR"));
            stmt.execute(queries.get("QUERY_COMPARE_SELLER_ERROR_HAS_NO_PAIR)"));
            stmt.execute(queries.get("QUERY_COMPARE_CUSTOMER_ERROR_HAS_NO_PAIR"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dropAllTables(Connection con) {
        try {
            Statement stmt = con.createStatement();
            for (Map.Entry<String, String> entry : queries.entrySet()) {
                String tableName = entry.getKey();
                stmt.execute("DROP TABLE IF EXISTS " + tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection connect() {
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {

        //Use this code to run programm for the first time
        //Create all tables and insert your data from CVS or manually

        TablesGeneration tg = new TablesGeneration();
        Connection con = tg.connect();
        tg.dropAllTables(con);
        tg.createTables(con);
        tg.compareTables(con);
    }
}