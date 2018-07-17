import java.sql.*;


class TableRecord {

    private String seller_inn;

    public String getSeller_inn() {
        return seller_inn;
    }

    public String getSeller_kpp() {
        return seller_kpp;
    }

    public String getCustomer_inn() {
        return customer_inn;
    }

    public String getCustomer_kpp() {
        return customer_kpp;
    }

    double getTotal() {
        return total;
    }

    private String seller_kpp;
    private String customer_inn;
    private String customer_kpp;
    private double total;

    public TableRecord() {
        this.seller_inn = "";
        this.seller_kpp = "";
        this.customer_inn = "";
        this.customer_kpp = "";
        this.total = 0;
    }

    TableRecord(String rec) {
        String[] columns = rec.split(",");
        this.seller_inn = columns[0];
        this.seller_kpp = columns[1];
        this.customer_inn = columns[2];
        this.customer_kpp = columns[3];
        this.total = Double.parseDouble(columns[4]);
    }


    public void setSeller_inn(String seller_inn) {
        this.seller_inn = seller_inn;
    }

    public void setSeller_kpp(String seller_kpp) {
        this.seller_kpp = seller_kpp;
    }

    public void setCustomer_kpp(String customer_kpp) {
        this.customer_kpp = customer_kpp;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setCustomer_inn(String customer_inn) {
        this.customer_inn = customer_inn;
    }

    @Override
    public String toString() {
        return "TableRecord{" +
                "seller_inn='" + seller_inn + '\'' +
                ", seller_kpp='" + seller_kpp + '\'' +
                ", customer_inn='" + customer_inn + '\'' +
                ", customer_kpp='" + customer_kpp + '\'' +
                ", total=" + total +
                '}';
    }
}

public class GenerateTables {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    // Сonnection string
    public static String URL = "jdbc:hive2://localhost:10000";

    // Show all tables in the default database
    public static String QUERY = "CREATE TABLE IF NOT EXISTS employee ( eid int, " +
            "name String, salary String, destination String) COMMENT " +
            "'Employee details' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' STORED AS TEXTFILE";

    public static String QUERY_CREATE_SELLER = " CREATE TABLE IF NOT EXISTS seller ( seller_inn int, seller_kpp int, " +
            " customer_inn int,customer_kpp int, total_with_tax double, total_without_tax double) COMMENT " +
            "'Sellers transaction info' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' STORED AS TEXTFILE";

    public static String QUERY_CREATE_CUSTOMER = " CREATE TABLE IF NOT EXISTS customer (  customer_inn int,customer_kpp int, " +
            "seller_inn int,seller_kpp int,  total_with_tax double, total_without_tax double) COMMENT " +
            "'Sellers transaction info' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' STORED AS TEXTFILE";

    public static String QUERY_DROP_SELLER_ERROR = "DROP TABLE seller_errors";
    public static String QUERY_DROP_CUSTOMER_ERROR = "DROP TABLE customer_errors";
    public static String QUERY_DROP_SELLER_CORRECT = "DROP TABLE seller_correct";
    public static String QUERY_DROP_CUSTOMER_CORRECT = "DROP TABLE customer_correct";
    public static String QUERY_DROP_SELLER_CORRECT_TOTAL = "DROP TABLE seller_correct_total";
    public static String QUERY_DROP_CUSTOMER_CORRECT_TOTAL = "DROP TABLE customer_correct_total";
    public static String QUERY_DROP_SELLER_CORRECT_TOTAL_DIFF = "DROP TABLE seller_correct_total_diff";
    public static String QUERY_DROP_CUSTOMER_CORRECT_TOTAL_DIFF = "DROP TABLE customer_correct_total_diff";
    public static String QUERY_DROP_C = "DROP TABLE seller";
    public static String QUERY_DROP_S = "DROP TABLE customer";

    /*public static String QUERY_COMPARE = "CREATE TABLE IF NOT EXISTS errors_new AS SELECT s.seller_inn AS table_1_seller," +
            "s.customer_inn AS table_1_customer, c.seller_inn AS table_2_seller, c.customer_inn AS table_2_customer" +
            " FROM seller s LEFT JOIN customer c ON"+
            " s.customer_inn = c.customer_inn AND s.seller_inn = c.seller_inn AND" +
            " s.customer_kpp = c.customer_kpp AND s.seller_kpp = c.seller_kpp" +
            " WHERE c.customer_inn is null";
    */

    // #2
    public static String QUERY_COMPARE_SELL_ERR = "CREATE TABLE IF NOT EXISTS seller_errors AS SELECT " +
            "s.seller_inn AS seller_inn_err, s.seller_kpp AS seller_kpp_err,s.customer_inn AS customer_inn_err, s.customer_kpp AS customer_kpp_err, " +
            " s.total_with_tax AS total_with_tax_err, s.total_without_tax AS total_without_tax_err" +
            " FROM seller s LEFT JOIN customer c ON" +
            " s.customer_inn = c.customer_inn AND s.seller_inn = c.seller_inn AND" +
            " s.customer_kpp = c.customer_kpp AND s.seller_kpp = c.seller_kpp " +
            " WHERE c.customer_inn is NULL AND c.seller_inn is NULL AND c.customer_kpp is NULL AND c.seller_kpp is NULL";

    public static String QUERY_COMPARE_CUST_ERR = "CREATE TABLE IF NOT EXISTS customer_errors AS SELECT c.customer_inn AS customer_inn_err, c.customer_kpp AS customer_kpp_err," +
            "c.seller_inn AS seller_inn_err, c.seller_kpp AS seller_kpp_err,  " +
            " c.total_with_tax AS total_with_tax_err, c.total_without_tax AS total_without_tax_err" +
            " FROM customer c LEFT JOIN seller s ON" +
            " c.customer_inn = s.customer_inn AND c.seller_inn = s.seller_inn AND" +
            " s.customer_kpp = c.customer_kpp AND s.seller_kpp = c.seller_kpp" +
            " WHERE s.customer_inn is NULL AND s.seller_inn is NULL AND s.customer_kpp is NULL AND s.seller_kpp is NULL";

    // #3
    public static String QUERY_COMPARE_SELL_CORR = "CREATE TABLE IF NOT EXISTS seller_correct AS SELECT s.seller_inn AS seller_inn_corr," +
            "s.seller_kpp AS seller_kpp_corr, s.customer_inn AS customer_inn_corr, s.customer_kpp AS customer_kpp_corr,  " +
            " s.total_with_tax AS total_with_tax_corr, s.total_without_tax AS total_without_tax_corr" +
            " FROM seller s INNER JOIN customer c ON" +
            " s.customer_inn = c.customer_inn AND s.seller_inn = c.seller_inn AND" +
            " s.customer_kpp = c.customer_kpp AND s.seller_kpp = c.seller_kpp";

    public static String QUERY_COMPARE_CUST_CORR = "CREATE TABLE IF NOT EXISTS customer_correct AS SELECT c.customer_inn AS customer_inn_corr, c.customer_kpp AS customer_kpp_corr," +
            "c.seller_inn AS seller_inn_corr, c.seller_kpp AS seller_kpp_corr,  " +
            " c.total_with_tax AS total_with_tax_corr, c.total_without_tax AS total_without_tax_corr" +
            " FROM customer c INNER JOIN seller s ON" +
            " c.customer_inn = s.customer_inn AND c.seller_inn = s.seller_inn AND" +
            " s.customer_kpp = c.customer_kpp AND s.seller_kpp = c.seller_kpp";

    // #4
    public static String QUERY_COMPARE_SELL_CORR_TOTAL = "CREATE TABLE IF NOT EXISTS seller_correct_total AS SELECT s.seller_inn_corr AS seller_inn_corr_t," +
            " s.seller_kpp_corr AS seller_kpp_corr_t, s.customer_inn_corr AS customer_inn_corr_t, s.customer_kpp_corr AS customer_kpp_corr_t, " +
            " s.total_with_tax_corr AS total_with_tax_corr_t, s.total_without_tax_corr AS total_without_tax_corr_t" +
            " FROM seller_correct s INNER JOIN customer_correct c ON" +
            " s.total_with_tax_corr = c.total_with_tax_corr AND s.total_without_tax_corr = c.total_without_tax_corr";

    public static String QUERY_COMPARE_CUST_CORR_TOTAL = "CREATE TABLE IF NOT EXISTS customer_correct_total AS SELECT c.customer_inn_corr AS customer_inn_corr_t," +
            "c.customer_kpp_corr AS customer_kpp_corr_t, c.seller_inn_corr AS seller_inn_corr_t, c.seller_kpp_corr AS seller_kpp_corr_t,  " +
            " c.total_with_tax_corr AS total_with_tax_corr_t, c.total_without_tax_corr AS total_without_tax_corr_t" +
            " FROM customer_correct c INNER JOIN seller_correct s ON" +
            " s.total_with_tax_corr = c.total_with_tax_corr AND s.total_without_tax_corr = c.total_without_tax_corr";

    // #5
    public static String QUERY_COMPARE_SELL_CORR_TOTAL_DIFF = "CREATE TABLE IF NOT EXISTS seller_correct_total_diff AS SELECT s.seller_inn_corr AS seller_inn_corr_t," +
            "s.seller_kpp_corr AS seller_kpp_corr_t, s.customer_inn_corr AS customer_inn_corr_t, s.customer_kpp_corr AS customer_kpp_corr_t, " +
            " s.total_with_tax_corr AS total_with_tax_corr_t, s.total_without_tax_corr AS total_without_tax_corr_t" +
            " FROM seller_correct s LEFT JOIN customer_correct c ON" +
            " s.total_with_tax_corr = c.total_with_tax_corr AND s.total_without_tax_corr = c.total_without_tax_corr" +
            " WHERE c.total_with_tax_corr is NULL AND c.total_without_tax_corr IS NULL";

    public static String QUERY_COMPARE_CUST_CORR_TOTAL_DIFF = "CREATE TABLE IF NOT EXISTS customer_correct_total_diff AS SELECT c.customer_inn_corr AS customer_inn_corr_t," +
            "c.customer_kpp_corr AS customer_kpp_corr_t, c.seller_inn_corr AS seller_inn_corr_t, c.seller_kpp_corr AS seller_kpp_corr_t, " +
            " c.total_with_tax_corr AS total_with_tax_corr_t, c.total_without_tax_corr AS total_without_tax_corr_t" +
            " FROM customer_correct c LEFT JOIN seller_correct s ON" +
            " s.total_with_tax_corr = c.total_with_tax_corr AND s.total_without_tax_corr = c.total_without_tax_corr" +
            " WHERE s.total_with_tax_corr is NULL AND s.total_without_tax_corr IS NULL";


    //static String csv_path ="'/home/student1/Documents/hadoop-tax-fl/data.csv'";
    //public static String QUERY_LOAD = "LOAD DATA LOCAL INPATH " + csv_path +  " OVERWRITE INTO TABLE employee";

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Connection con = DriverManager.getConnection(URL);
        Statement stmt = con.createStatement();

        //stmt.execute(QUERY_DROP_S);
        //stmt.execute(QUERY_DROP_C);
        stmt.execute(QUERY_CREATE_SELLER);
        stmt.execute(QUERY_CREATE_CUSTOMER);

        PreparedStatement preparedStmt = con.prepareStatement("insert into table " + "default.seller VALUES(?,?,?,?,?,?)");
        preparedStmt.setInt(1, 111);
        preparedStmt.setInt(2, 222);
        preparedStmt.setInt(3, 333);
        preparedStmt.setInt(4, 444);
        preparedStmt.setInt(5, 558);
        preparedStmt.setInt(6, 668);
        //preparedStmt.executeUpdate();

        preparedStmt = con.prepareStatement("insert into table " + "default.customer VALUES(?,?,?,?,?,?)");
        preparedStmt.setInt(3, 111);
        preparedStmt.setInt(4, 222);
        preparedStmt.setInt(1, 333);
        preparedStmt.setInt(2, 444);
        preparedStmt.setInt(5, 558);
        preparedStmt.setInt(6, 668);
        //preparedStmt.executeUpdate();

        stmt.execute(QUERY_DROP_SELLER_CORRECT_TOTAL_DIFF);
        stmt.execute(QUERY_DROP_CUSTOMER_CORRECT_TOTAL_DIFF);
        stmt.execute(QUERY_DROP_SELLER_CORRECT_TOTAL);
        stmt.execute(QUERY_DROP_CUSTOMER_CORRECT_TOTAL);
        stmt.execute(QUERY_DROP_SELLER_CORRECT);
        stmt.execute(QUERY_DROP_CUSTOMER_CORRECT);
        stmt.execute(QUERY_DROP_SELLER_ERROR);
        stmt.execute(QUERY_DROP_CUSTOMER_ERROR);
        stmt.execute(QUERY_COMPARE_SELL_ERR);
        stmt.execute(QUERY_COMPARE_CUST_ERR);
        stmt.execute(QUERY_COMPARE_SELL_CORR);
        stmt.execute(QUERY_COMPARE_CUST_CORR);
        stmt.execute(QUERY_COMPARE_SELL_CORR_TOTAL);
        stmt.execute(QUERY_COMPARE_CUST_CORR_TOTAL);
        stmt.execute(QUERY_COMPARE_SELL_CORR_TOTAL_DIFF);
        stmt.execute(QUERY_COMPARE_CUST_CORR_TOTAL_DIFF);
        /*
        String table_name = "seller";
        String QUERY_SHOW = "select * from " + table_name;

        stmt.execute(QUERY_SHOW);

        StringBuilder result = new StringBuilder();
        ResultSet rs = stmt.getResultSet();
        TableRecord tr = new TableRecord();
        while (rs.next()) {
            tr.setCustomer_inn(rs.getString(1));
            tr.setCustomer_kpp(rs.getString(2));
            tr.setSeller_inn(rs.getString(3));
            tr.setSeller_kpp(rs.getString(4));
            tr.setTotal(Double.parseDouble(rs.getString(5)));

            System.out.println(tr.toString());

        }
        TableRecord tr1 = new TableRecord("1845411480,184504719,1216291511,121603240,1095764.52");
        System.out.println(tr1.toString());
        */

    }
}