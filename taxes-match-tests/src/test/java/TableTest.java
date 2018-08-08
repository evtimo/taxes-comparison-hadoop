import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.testng.Assert;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

abstract class TableTest extends AbstractTestNGCucumberTests {

    private TableRecord seller;
    private TableRecord customer;
    //public GenerateTables tab = new GenerateTables();
    private Connection con = HiveConnection.getInstance();

    CSVRecord sellerRecord;
    CSVRecord customerRecord;

    protected void given(String a, String b) throws IOException {

        Reader csv_file = new FileReader("src/test/resources/" + a);

        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().parse(csv_file);
        sellerRecord = parser.getRecords().get(0);
        this.seller = new TableRecord(sellerRecord, RecordType.SELLER);

        csv_file = new FileReader("src/test/resources/" + b);

        parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().parse(csv_file);
        customerRecord = parser.getRecords().get(0);
        this.customer = new TableRecord(customerRecord, RecordType.CUSTOMER);

        parser.close();
        System.out.println(seller);
        System.out.println(customer);
    }


    protected void when() {

        /* HiveConnection to Hive Database.

        con = tab.Connect();

        tab.DeleteTables(con);
        tab.CreateTables(con);

        SELLER.insertIntoTable(con);
        CUSTOMER.insertIntoTable(con);

        tab.DropCompareTables(con);
        tab.CompareTables(con);*/
    }


    /*public void then(String tableName1, String tableName2) throws SQLException {

        ResultSet sellerFromTable = getResultSetFromTable(con, tableName1);
        ResultSet customerFromTable = getResultSetFromTable(con, tableName2);

        Assert.assertEquals(sellerRecord.get("total_with_tax_err"),
                sellerFromTable.getString("total_with_tax_err"));
        Assert.assertEquals(customerRecord.get("total_with_tax_err"),
                customerFromTable.getString("total_with_tax_err"));

    }*/

    public static ResultSet getResultSetFromTable(Connection con, String TableName) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String QUERY_SHOW = "SELECT * FROM " + TableName;
        try {
            stmt.execute(QUERY_SHOW);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        ResultSet rs = stmt.getResultSet();
        rs.last();
        for (int i = 0; i < (rs.getRow()); i++) {
            System.out.println(rs.getString(i));
        }

        return rs;
    }

}

