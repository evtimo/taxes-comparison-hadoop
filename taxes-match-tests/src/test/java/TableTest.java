import cucumber.api.testng.AbstractTestNGCucumberTests;
import lombok.Cleanup;
import lombok.experimental.FieldDefaults;
import lombok.var;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.testng.Assert;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
abstract class TableTest extends AbstractTestNGCucumberTests {

    TableRecord seller;
    TableRecord customer;
    protected Connection con;
    protected CSVRecord sellerRecord;
    protected CSVRecord customerRecord;

    TableTest() { 
       con = HiveConnectionImpl.getInstance().getCon();
	System.out.println("Connection OK");
	System.out.println(con);   
 }

    protected void given(String sellerTable, String customerTable) throws IOException {

        var csv_file = new FileReader("src/test/resources/" + sellerTable);

        @Cleanup var parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().parse(csv_file);
        sellerRecord = parser.getRecords().get(0);
        this.seller = new TableRecord(sellerRecord, RecordType.SELLER);

        csv_file = new FileReader("src/test/resources/" + customerTable);

        parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().parse(csv_file);
        customerRecord = parser.getRecords().get(0);
        this.customer = new TableRecord(customerRecord, RecordType.CUSTOMER);

        System.out.println(seller);
        System.out.println(customer);
    }


    protected void when() {

        /* HiveConnection to Hive Database. */
        String jarPath = "../taxes-generation/target/";
        String jarName = "taxes-generation-1.0-SNAPSHOT-jar-with-dependencies.jar";
//        try {
//            Process process = Runtime.getRuntime().exec("java -jar " + jarPath + jarName);
//            process.waitFor();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

          seller.insertIntoTable(con);
          customer.insertIntoTable(con);

    }


    public void then(String tableName1, String tableName2) throws SQLException {
	ResultSet sellerFromTable = null;
	ResultSet customerFromTable = null;
	System.out.println(con);
        try {
		sellerFromTable = getResultSetFromTable(con, tableName1);
        	customerFromTable = getResultSetFromTable(con, tableName2);
	} catch (Exception e) { 
		e.printStackTrace(); 
	}
        Assert.assertEquals(sellerRecord.get("total_with_tax_err"),
                sellerFromTable.getString("total_with_tax_err"));
        Assert.assertEquals(customerRecord.get("total_with_tax_err"),
                customerFromTable.getString("total_with_tax_err"));

    }

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
        return rs;
    }

}

