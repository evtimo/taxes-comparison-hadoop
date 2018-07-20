import java.sql.*;
import java.util.ArrayList;
import java.util.List;


class TableRecord {

    private String seller_inn;
    private String seller_kpp;
    private String customer_inn;
    private String customer_kpp;
    private double total;
    RecordType recordType;

    TableRecord(String rec, RecordType rt) {
    this.recordType = rt;
    String[] columns = rec.split(",");
        switch (recordType) {
            case seller:
                this.setSeller_inn(columns[0]);
                this.setSeller_kpp(columns[1]);
                this.setCustomer_inn(columns[2]);
                this.setCustomer_kpp(columns[3]);
                this.setTotal(Double.parseDouble(columns[4]));
                break;
            case customer:

                this.setCustomer_inn(columns[0]);
                this.setCustomer_kpp(columns[1]);
                this.setSeller_inn(columns[2]);
                this.setSeller_kpp(columns[3]);
                this.setTotal(Double.parseDouble(columns[4]));
                break;
        }
    }

    @Override
    public String toString() {

        switch (recordType) {
            case seller:
                return getSeller_inn() + "," + getSeller_kpp() + "," + getCustomer_inn() + "," + getCustomer_kpp() + "," + getTotal() + ',' + getTotal();

            case customer:
                return getCustomer_inn() + "," + getCustomer_kpp() + "," + getSeller_inn() + "," + getSeller_kpp() + "," + getTotal() + ',' + getTotal();
        }
        return "";
    }

    public void insertIntoTable(Connection con) {

        PreparedStatement preparedStmt = null;
        try {

            switch (recordType) {
                case seller:
                    preparedStmt = con.prepareStatement("insert into table default.seller VALUES(?,?,?,?,?,?)");
                    preparedStmt.setString(1, getSeller_inn());
                    preparedStmt.setString(2, getSeller_kpp());
                    preparedStmt.setString(3, getCustomer_inn());
                    preparedStmt.setString(4, getCustomer_kpp());
                    break;
                case customer:
                    preparedStmt = con.prepareStatement("insert into table default.customer VALUES(?,?,?,?,?,?)");
                    preparedStmt.setString(1, getCustomer_inn());
                    preparedStmt.setString(2, getCustomer_kpp());
                    preparedStmt.setString(3, getSeller_inn());
                    preparedStmt.setString(4, getSeller_kpp());
                    break;
            }

            preparedStmt.setDouble(5, getTotal());
            preparedStmt.setDouble(6, getTotal());
            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public double getTotal() {
        return total;
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
}

public class GenerateTables {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static String URL = "jdbc:hive2://localhost:10000";

    public static String QUERY_CREATE_SELLER = "CREATE TABLE IF NOT EXISTS seller ( seller_inn varchar(10), seller_kpp varchar(9), " +
            "customer_inn varchar(10),customer_kpp varchar(9), total_with_tax double, total_without_tax double) COMMENT " +
            "'Sellers transaction info' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\\t' LINES TERMINATED BY  '\n' STORED AS TEXTFILE";

    public static String QUERY_CREATE_CUSTOMER = "CREATE TABLE IF NOT EXISTS customer (  customer_inn varchar(10), customer_kpp varchar(9), " +
            "seller_inn varchar(10),seller_kpp varchar(9),  total_with_tax double, total_without_tax double) COMMENT " +
            "'Sellers transaction info' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\\t' LINES TERMINATED BY  '\n' STORED AS TEXTFILE";

    // #2
    public static String QUERY_COMPARE_SELLER_ERR = "CREATE TABLE IF NOT EXISTS seller_errors AS SELECT " +
            "s.seller_inn AS seller_inn_err, s.seller_kpp AS seller_kpp_err,s.customer_inn AS customer_inn_err, s.customer_kpp AS customer_kpp_err, " +
            "s.total_with_tax AS total_with_tax_err, s.total_without_tax AS total_without_tax_err " +
            "FROM seller s LEFT JOIN customer c ON " +
            "s.customer_inn = c.customer_inn AND s.seller_inn = c.seller_inn AND " +
            "s.customer_kpp = c.customer_kpp AND s.seller_kpp = c.seller_kpp " +
            "WHERE c.customer_inn is NULL AND c.seller_inn is NULL AND c.customer_kpp is NULL AND c.seller_kpp is NULL";

    public static String QUERY_COMPARE_CUSTOMER_ERR = "CREATE TABLE IF NOT EXISTS customer_errors AS SELECT c.customer_inn AS customer_inn_err, c.customer_kpp AS customer_kpp_err," +
            "c.seller_inn AS seller_inn_err, c.seller_kpp AS seller_kpp_err,  " +
            "c.total_with_tax AS total_with_tax_err, c.total_without_tax AS total_without_tax_err " +
            "FROM customer c LEFT JOIN seller s ON " +
            "C.customer_inn = s.customer_inn AND c.seller_inn = s.seller_inn AND " +
            "s.customer_kpp = c.customer_kpp AND s.seller_kpp = c.seller_kpp " +
            "WHERE s.customer_inn is NULL AND s.seller_inn is NULL AND s.customer_kpp is NULL AND s.seller_kpp is NULL";

    // #3
    public static String QUERY_COMPARE_SELLER_CORR = "CREATE TABLE IF NOT EXISTS seller_correct AS SELECT s.seller_inn AS seller_inn_corr, " +
            "s.seller_kpp AS seller_kpp_corr, s.customer_inn AS customer_inn_corr, s.customer_kpp AS customer_kpp_corr,  " +
            "s.total_with_tax AS total_with_tax_corr, s.total_without_tax AS total_without_tax_corr " +
            "FROM seller s INNER JOIN customer c ON " +
            "s.customer_inn = c.customer_inn AND s.seller_inn = c.seller_inn AND " +
            "s.customer_kpp = c.customer_kpp AND s.seller_kpp = c.seller_kpp ";

    public static String QUERY_COMPARE_CUSTOMER_CORR = "CREATE TABLE IF NOT EXISTS customer_correct AS SELECT c.customer_inn AS customer_inn_corr, c.customer_kpp AS customer_kpp_corr, " +
            "c.seller_inn AS seller_inn_corr, c.seller_kpp AS seller_kpp_corr,  " +
            "c.total_with_tax AS total_with_tax_corr, c.total_without_tax AS total_without_tax_corr " +
            "FROM customer c INNER JOIN seller s ON " +
            "c.customer_inn = s.customer_inn AND c.seller_inn = s.seller_inn AND " +
            "s.customer_kpp = c.customer_kpp AND s.seller_kpp = c.seller_kpp";

    // #4
    public static String QUERY_COMPARE_CORRECT_COMPLETELY = "CREATE TABLE IF NOT EXISTS correct_completely AS SELECT s.seller_inn_corr AS seller_inn_corr_t, " +
            "s.seller_kpp_corr AS seller_kpp_corr_t, s.customer_inn_corr AS customer_inn_corr_t, s.customer_kpp_corr AS customer_kpp_corr_t, " +
            "s.total_with_tax_corr AS total_with_tax_corr_t, s.total_without_tax_corr AS total_without_tax_corr_t " +
            "FROM seller_correct s INNER JOIN customer_correct c ON " +
            "s.customer_inn_corr = c.customer_inn_corr AND s.seller_inn_corr = c.seller_inn_corr AND " +
            "s.customer_kpp_corr = c.customer_kpp_corr AND s.seller_kpp_corr = c.seller_kpp_corr AND " +
            "s.total_with_tax_corr = c.total_with_tax_corr AND s.total_without_tax_corr = c.total_without_tax_corr" ;

    // #5
    public static String QUERY_COMPARE_CORRECT_TOTAL_DIFF = "CREATE TABLE IF NOT EXISTS correct_total_diff AS SELECT s.seller_inn_corr AS seller_inn_corr_t, " +
            "s.seller_kpp_corr AS seller_kpp_corr_t, s.customer_inn_corr AS customer_inn_corr_t, s.customer_kpp_corr AS customer_kpp_corr_t, " +
            "s.total_with_tax_corr AS total_with_tax_corr_t, s.total_without_tax_corr AS total_without_tax_corr_t " +
            "FROM seller_correct s LEFT JOIN customer_correct c ON " +
            "s.total_with_tax_corr = c.total_with_tax_corr AND s.total_without_tax_corr = c.total_without_tax_corr AND " +
            "s.customer_inn_corr = c.customer_inn_corr AND s.seller_inn_corr = c.seller_inn_corr AND " +
            "s.customer_kpp_corr = c.customer_kpp_corr AND s.seller_kpp_corr = c.seller_kpp_corr " +
            "WHERE c.total_with_tax_corr is NULL AND c.total_without_tax_corr IS NULL ";

    // #6
    public static String QUERY_COMPARE_SELLER_ERROR_SELLER_HAS_PAIR = "CREATE TABLE IF NOT EXISTS seller_errors_seller_has_pair AS SELECT " +
            "s.seller_inn_err AS seller_inn_err, s.seller_kpp_err AS seller_kpp_err,s.customer_inn_err AS customer_inn_err, s.customer_kpp_err AS customer_kpp_err, " +
            "s.total_with_tax_err AS total_with_tax_err, s.total_without_tax_err AS total_without_tax_err " +
            "FROM seller_errors s INNER JOIN customer_errors c ON " +
            "s.seller_inn_err = c.seller_inn_err AND s.seller_kpp_err = c.seller_kpp_err ";

    public static String QUERY_COMPARE_CUSTOMER_ERROR_SELLER_HAS_PAIR = "CREATE TABLE IF NOT EXISTS customer_errors_seller_has_pair AS SELECT " +
            "c.customer_inn_err AS customer_inn_err, c.customer_kpp_err AS customer_kpp_err,c.seller_inn_err AS seller_inn_err, c.seller_kpp_err AS seller_kpp_err,  " +
            "c.total_with_tax_err AS total_with_tax_err, c.total_without_tax_err AS total_without_tax_err " +
            "FROM customer_errors c INNER JOIN seller_errors s ON " +
            "c.seller_inn_err = s.seller_inn_err AND s.seller_kpp_err = c.seller_kpp_err ";

    // #7
    public static String QUERY_COMPARE_SELLER_ERROR_CUSTOMER_HAS_PAIR = "CREATE TABLE IF NOT EXISTS seller_errors_customer_has_pair AS SELECT " +
            "s.seller_inn_err AS seller_inn_err, s.seller_kpp_err AS seller_kpp_err,s.customer_inn_err AS customer_inn_err, s.customer_kpp_err AS customer_kpp_err, " +
            "s.total_with_tax_err AS total_with_tax_err, s.total_without_tax_err AS total_without_tax_err " +
            "FROM seller_errors s INNER JOIN customer_errors c ON " +
            "s.customer_inn_err = c.customer_inn_err AND s.customer_kpp_err = c.customer_kpp_err ";

    public static String QUERY_COMPARE_CUSTOMER_ERROR_CUSTOMER_HAS_PAIR = "CREATE TABLE IF NOT EXISTS customer_errors_customer_has_pair AS SELECT" +
            " c.customer_inn_err AS customer_inn_err, c.customer_kpp_err AS customer_kpp_err,c.seller_inn_err AS seller_inn_err, c.seller_kpp_err AS seller_kpp_err,  " +
            " c.total_with_tax_err AS total_with_tax_err, c.total_without_tax_err AS total_without_tax_err" +
            " FROM customer_errors c INNER JOIN seller_errors s ON" +
            " c.customer_inn_err = s.customer_inn_err AND s.customer_kpp_err = c.customer_kpp_err ";
    // #8
    public static String QUERY_COMPARE_CUSTOMER_ERROR_HAS_NO_PAIR = "CREATE TABLE IF NOT EXISTS customer_errors_has_no_pair AS SELECT " +
            "c.customer_inn_err AS customer_inn_err, c.customer_kpp_err AS customer_kpp_err," +
            "c.seller_inn_err AS seller_inn_err, c.seller_kpp_err AS seller_kpp_err, " +
            "c.total_with_tax_err AS total_with_tax_err, c.total_without_tax_err AS total_without_tax_err " +
            "FROM customer_errors c left JOIN " +
            "(SELECT " +
            "chp.customer_inn_err AS customer_inn_err, " +
            "chp.customer_kpp_err AS customer_kpp_err," +
            "chp.seller_inn_err AS seller_inn_err, " +
            "chp.seller_kpp_err AS seller_kpp_err, " +
            "chp.total_with_tax_err AS total_with_tax_err, " +
            "chp.total_without_tax_err AS total_without_tax_err " +
            "FROM  customer_errors_customer_has_pair chp FULL JOIN customer_errors_seller_has_pair shp " +
            "ON chp.customer_inn_err = shp.customer_inn_err AND chp.customer_kpp_err = shp.customer_kpp_err AND " +
            "chp.total_with_tax_err = shp.total_with_tax_err AND chp.total_without_tax_err = shp.total_without_tax_err " +
            ") s " +
            "ON s.customer_inn_err = c.customer_inn_err AND s.customer_kpp_err = c.customer_kpp_err and " +
            "c.seller_inn_err = s.seller_inn_err AND s.seller_kpp_err = c.seller_kpp_err " +
            "Where s.customer_inn_err IS null AND s.customer_kpp_err IS null and " +
            "s.seller_inn_err IS null AND s.seller_kpp_err IS null";

    // #9
    public static String QUERY_COMPARE_SELLER_ERROR_HAS_NO_PAIR = "CREATE TABLE IF NOT EXISTS seller_errors_has_no_pair AS SELECT " +
            "s.seller_inn_err AS seller_inn_err, s.seller_kpp_err AS seller_kpp_err, " +
            "s.customer_inn_err AS customer_inn_err, s.customer_kpp_err AS customer_kpp_err, " +
            "s.total_with_tax_err AS total_with_tax_err, s.total_without_tax_err AS total_without_tax_err " +
            "FROM seller_errors s left JOIN    " +
            "(SELECT  " +
            "shp.customer_inn_err AS customer_inn_err,  " +
            "shp.customer_kpp_err AS customer_kpp_err, " +
            "shp.seller_inn_err AS seller_inn_err,  " +
            "shp.seller_kpp_err AS seller_kpp_err,  " +
            "shp.total_with_tax_err AS total_with_tax_err,  " +
            "shp.total_without_tax_err AS total_without_tax_err " +
            "FROM seller_errors_seller_has_pair shp FULL JOIN seller_errors_customer_has_pair chp " +
            "ON chp.customer_inn_err = shp.customer_inn_err AND chp.customer_kpp_err = shp.customer_kpp_err AND " +
            "chp.total_with_tax_err = shp.total_with_tax_err AND chp.total_without_tax_err = shp.total_without_tax_err " +
            ") c " +
            "ON s.customer_inn_err = c.customer_inn_err AND s.customer_kpp_err = c.customer_kpp_err and  " +
            "c.seller_inn_err = s.seller_inn_err AND s.seller_kpp_err = c.seller_kpp_err " +
            "Where c.customer_inn_err IS null AND c.customer_kpp_err IS null and  " +
            "c.seller_inn_err IS null AND c.seller_kpp_err IS null ";

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

    //static String csv_path ="'/home/student1/Documents/hadoop-tax-fl/data.csv'";
    //public static String QUERY_LOAD = "LOAD DATA LOCAL INPATH " + csv_path +  " OVERWRITE INTO TABLE employee";

    public static void CreateTables(Connection con) {

        try {
            Statement stmt = con.createStatement();
            stmt.execute(QUERY_CREATE_SELLER);
            stmt.execute(QUERY_CREATE_CUSTOMER);
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
            stmt.execute(QUERY_COMPARE_SELLER_ERR);
            stmt.execute(QUERY_COMPARE_CUSTOMER_ERR);
            //stmt.execute(QUERY_COMPARE_SELL_CORR);
            //stmt.execute(QUERY_COMPARE_CUST_CORR);
            //stmt.execute(QUERY_COMPARE_CORR_COMPLETELY);
            //stmt.execute(QUERY_COMPARE_CORR_TOTAL_DIFF);
            stmt.execute(QUERY_COMPARE_SELLER_ERROR_CUSTOMER_HAS_PAIR);
            stmt.execute(QUERY_COMPARE_SELLER_ERROR_SELLER_HAS_PAIR);
            stmt.execute(QUERY_COMPARE_CUSTOMER_ERROR_CUSTOMER_HAS_PAIR);
            stmt.execute(QUERY_COMPARE_CUSTOMER_ERROR_SELLER_HAS_PAIR);
            stmt.execute(QUERY_COMPARE_SELLER_ERROR_HAS_NO_PAIR);
            stmt.execute(QUERY_COMPARE_CUSTOMER_ERROR_HAS_NO_PAIR);
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
            //stmt.execute(QUERY_DROP_SELLER_ERROR_HAS_NO_PAIR);
            //stmt.execute(QUERY_DROP_CUSTOMER_ERROR_HAS_NO_PAIR);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Connection Connect() {
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

    public static List<TableRecord> getDataFromTable(Connection con, String TableName) throws SQLException {
        String tmp[] = TableName.split("_");
        RecordType rt = RecordType.valueOf(tmp[0]);
        Statement stmt = null;
        try{
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

            table.add(new TableRecord(rec, rt));
        }

        for (int i = 0; i < table.size(); i++) {
            System.out.println(table.get(i).toString());
        }

        return table;
    }

    public static void main(String[] args) throws SQLException {

        Connection con = Connect();

    }
}