import java.sql.*;

public class GenerateTables {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    // Сonnection string
    public static String URL = "jdbc:hive2://localhost:10000";

    // Show all tables in the default database
    public static String QUERY = "CREATE TABLE IF NOT EXISTS employee ( eid int, " +
            "name String, salary String, destination String) COMMENT " +
            "'Employee details' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' STORED AS TEXTFILE";

    public static String QUERY_CREATE_SELLER = " CREATE TABLE IF NOT EXISTS seller ( customer_inn int, seller_inn int, " +
            "customer_kpp int, seller_kpp int, total_with_tax double, total_without_tx double) COMMENT " +
            "'Sellers transaction info' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' STORED AS TEXTFILE";

    public static String QUERY_CREATE_CUSTOMER= " CREATE TABLE IF NOT EXISTS customer ( seller_inn int, customer_inn int, " +
            "seller_kpp int, customer_kpp int, total_with_tax double, total_without_tx double) COMMENT " +
            "'Sellers transaction info' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' STORED AS TEXTFILE";

    public static String QUERY_DROP = "DROP TABLE errors";
    public static String QUERY_COMPARE = "CREATE TABLE IF NOT EXISTS errors AS SELECT s.seller_inn AS 'table_1_seller'," +
            "s.customer_inn AS 'table_1_customer', c.seller_inn AS 'table_2_seller', c.customer_inn AS 'table_2_customer'" +
            " AS seller_errors FROM seller AS s LEFT JOIN customer AS c ON"+
            " s.customer_inn = c.customer_inn AND s.seller_inn = c.seller_inn AND" +
            " s.customer_kpp = c.customer_kpp AND s.seller_kpp = c.seller_kpp" ;

            //" WHERE customer.customer_inn is null";

    //static String csv_path ="'/home/student1/Documents/hadoop-tax-fl/data.csv'";
    //public static String QUERY_LOAD = "LOAD DATA LOCAL INPATH " + csv_path +  " OVERWRITE INTO TABLE employee";

    public static String QUERY_SHOW = "select * from seller";

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName (driverName);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Connection con = DriverManager.getConnection (URL);
        Statement stmt = con.createStatement();


        //stmt.execute(QUERY_CREATE_SELLER);
        //stmt.execute(QUERY_CREATE_CUSTOMER);

        PreparedStatement preparedStmt = con.prepareStatement("insert into table " + "default.seller VALUES(?,?,?,?,?,?)");
        preparedStmt.setInt(1, 1);
        preparedStmt.setInt(2, 222);
        preparedStmt.setInt(3, 333);
        preparedStmt.setInt(4, 444);
        preparedStmt.setInt(5, 555);
        preparedStmt.setInt(6, 666);
        preparedStmt.executeUpdate();

        preparedStmt = con.prepareStatement("insert into table " + "default.customer VALUES(?,?,?,?,?,?)");
        preparedStmt.setInt(1, 1);
        preparedStmt.setInt(2, 222);
        preparedStmt.setInt(3, 333);
        preparedStmt.setInt(4, 444);
        preparedStmt.setInt(5, 555);
        preparedStmt.setInt(6, 666);
        preparedStmt.executeUpdate();

        stmt.execute(QUERY_DROP);
        stmt.execute(QUERY_COMPARE);
    }
}