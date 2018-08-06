import org.apache.commons.csv.CSVRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class GenerateTables {

    public static Map<String, String> queries;
    private String pathname;
    private SQLReader sqlread;

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static String URL = "jdbc:hive2://localhost:10000";

    public static String QUERY_DROP_C = "DROP TABLE seller";
    public static String QUERY_DROP_S = "DROP TABLE customer";
    public static String QUERY_DROP_SELLER_ERROR = "DROP TABLE seller_errors";
    public static String QUERY_DROP_CUSTOMER_ERROR = "DROP TABLE customer_errors";
    public static String QUERY_DROP_SELLER_CORRECT = "DROP TABLE seller_correct";
    public static String QUERY_DROP_CUSTOMER_CORRECT = "DROP TABLE customer_correct";
    public static String QUERY_DROP_SELLER_CORRECT_COMPLETELY = "DROP TABLE correct_completely";
    public static String QUERY_DROP_CORRECT_TOTAL_DIFF = "DROP TABLE correct_total_diff";

    public static String QUERY_DROP_SELLER_ERROR_SELLER_HAS_PAIR = "DROP TABLE seller_errors_seller_has_pair";
    public static String QUERY_DROP_SELLER_ERROR_CUSTOMER_HAS_PAIR = "DROP TABLE seller_errors_customer_has_pair";
    public static String QUERY_DROP_CUSTOMER_ERROR_SELLER_HAS_PAIR = "DROP TABLE customer_errors_seller_has_pair";
    public static String QUERY_DROP_CUSTOMER_ERROR_CUSTOMER_HAS_PAIR = "DROP TABLE customer_errors_customer_has_pair";
    public static String QUERY_DROP_SELLER_ERROR_HAS_NO_PAIR = "DROP TABLE seller_errors_has_no_pair";
    public static String QUERY_DROP_CUSTOMER_ERROR_HAS_NO_PAIR = "DROP TABLE customer_errors_has_no_pair";

    public GenerateTables() {
        Properties prop = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
            prop.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.pathname = prop.getProperty("queriesCreateFilePath");
        this.sqlread = new SQLReader();
        this.queries = sqlread.readQueries(pathname);  //Read CREATE / DROP queries
        this.pathname = prop.getProperty("queriesCompareFilePath");
        this.queries = sqlread.readQueries(pathname);  //Read COMPARE queries

        for (Map.Entry<String, String> entry : queries.entrySet()) {
            //System.out.println(entry.getKey());
        }
    }

    //Load from csv file
    //static String csv_path ="'/home/student1/Documents/hadoop-tax-fl/data.csv'";
    //public static String QUERY_LOAD = "LOAD DATA LOCAL INPATH " + csv_path +  " OVERWRITE INTO TABLE employee";

    public static void CreateTables(Connection con) {

        try {
            Statement stmt = con.createStatement();
            stmt.execute(queries.get("QUERY_CREATE_SELLER"));
            stmt.execute(queries.get("QUERY_CREATE_CUSTOMER"));
            //How it should be in the future on server:
            //Process process = Runtime.getRuntime().exec("hive < production/queries.hql");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void DeleteTables(Connection con) {

        try {
            Statement stmt = con.createStatement();
            stmt.execute(QUERY_DROP_S);
            stmt.execute(QUERY_DROP_C);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void CompareTables(Connection con) {
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
            //Process process = Runtime.getRuntime().exec("hive < production/queries_match.hql");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void DropCompareTables(Connection con) {
        try {
            Statement stmt = con.createStatement();

            stmt.execute(QUERY_DROP_CORRECT_TOTAL_DIFF);
            stmt.execute(QUERY_DROP_SELLER_CORRECT_COMPLETELY);
            stmt.execute(QUERY_DROP_SELLER_CORRECT);
            stmt.execute(QUERY_DROP_CUSTOMER_CORRECT);
            stmt.execute(QUERY_DROP_SELLER_ERROR);
            stmt.execute(QUERY_DROP_CUSTOMER_ERROR);
            stmt.execute(QUERY_DROP_SELLER_ERROR_CUSTOMER_HAS_PAIR);
            stmt.execute(QUERY_DROP_CUSTOMER_ERROR_SELLER_HAS_PAIR);
            stmt.execute(QUERY_DROP_SELLER_ERROR_SELLER_HAS_PAIR);
            stmt.execute(QUERY_DROP_CUSTOMER_ERROR_CUSTOMER_HAS_PAIR);
            stmt.execute(QUERY_DROP_SELLER_ERROR_HAS_NO_PAIR);
            stmt.execute(QUERY_DROP_CUSTOMER_ERROR_HAS_NO_PAIR);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Connection connect() {
        try {
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(URL);
            return con;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

   /* public static List<TableRecord> getDataFromTable(Connection con, String TableName) throws SQLException {
        String tmp[] = TableName.split("_");
        RecordType rt = RecordType.valueOf(tmp[0]);
        Statement stmt = null;
        try {
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String rec;
        String QUERY_SHOW = "SELECT * FROM " + TableName;
        try {
            stmt.execute(QUERY_SHOW);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = stmt.getResultSet();

        List<TableRecord> table = new ArrayList<TableRecord>();

        while (rs.next()) {

            rec = rs.getString(1) + ","
                    + rs.getString(2) + ","
                    + rs.getString(3) + ","
                    + rs.getString(4) + ","
                    + rs.getString(5);

            //table.add(new TableRecord(rec, rt));
        }

        for (int i = 0; i < table.size(); i++) {
            System.out.println(table.get(i).toString());
        }

        return table;
    }*/

    public static void main(String[] args) throws SQLException {

        //Use this code to run programm for the first time
        //Create all tables and insert your data from CVS or manually

      /*GenerateTables gt = new GenerateTables();
        Connection con = connect();
        gt.DeleteTables(con);
        gt.CreateTables(con);

        InsertSellerIntoTable(con,"111,222,333,444,555 ");
        InsertSellerIntoTable(con,"112,223,334,445,555 ");
        InsertCustomerIntoTable(con,"333,444,111,222,555");
        InsertCustomerIntoTable(con,"334,445,112,223,556"); */

    }
}