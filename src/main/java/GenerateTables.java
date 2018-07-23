import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

    public static Map<String, String> queries;
    private String pathname;
    private SQLReader sqlread;

    //System.out.println(queries_01.get("TEST NAME1"));

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

    public GenerateTables(String queriesCreateFilePath, String queriesCompareFilePath) {
        this.pathname = queriesCreateFilePath;
        this.sqlread = new SQLReader();
        this.queries = sqlread.readQueries(pathname);  //Read CREATE / DROP queries
        this.pathname = queriesCompareFilePath;
        this.queries = sqlread.readQueries(pathname);  //Read COMPARE queries

        for (Map.Entry<String, String> entry : queries.entrySet()) {
            System.out.println(entry.getKey());
        }

    }

    // Load from csv file
    //static String csv_path ="'/home/student1/Documents/hadoop-tax-fl/data.csv'";
    //public static String QUERY_LOAD = "LOAD DATA LOCAL INPATH " + csv_path +  " OVERWRITE INTO TABLE employee";

    public static void CreateTables(Connection con) {

        try {
            Statement stmt = con.createStatement();
            stmt.execute(queries.get("QUERY_CREATE_SELLER"));
            stmt.execute(queries.get("QUERY_CREATE_CUSTOMER"));
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
//            stmt.execute(queries.get("QUERY_COMPARE_SELLER_ERROR_HAS_NO_PAIR)"));
//            stmt.execute(queries.get("QUERY_COMPARE_CUSTOMER_ERROR_HAS_NO_PAIR"));
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

            table.add(new TableRecord(rec, rt));
        }

        for (int i = 0; i < table.size(); i++) {
            System.out.println(table.get(i).toString());
        }

        return table;
    }

    public static void main(String[] args) throws SQLException {

      /*GenerateTables gt = new GenerateTables();
        Connection con = Connect();
        gt.DeleteTables(con);
        gt.CreateTables(con);

        InsertSellerIntoTable(con,"111,222,333,444,555 ");
        InsertSellerIntoTable(con,"112,223,334,445,555 ");
        InsertCustomerIntoTable(con,"333,444,111,222,555");
        InsertCustomerIntoTable(con,"334,445,112,223,556"); */

    }
}