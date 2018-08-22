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
    String jarPath = "../taxes-generation/target/";
    String jarName = "taxes-generation-1.0-SNAPSHOT-jar-with-dependencies.jar";

    TableTest() {
        con = HiveConnectionImpl.getInstance().getCon();
    }

    private void runJar(String jarPath, String jarName, String... args) {

        String command = "java -jar " + jarPath + jarName + " " + String.join(" ", args);
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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
    }


    protected void when() {

//        runJar(jarPath, jarName, "create");

//        seller.insertIntoTable(con);
//        customer.insertIntoTable(con);

//        runJar(jarPath, jarName, "compare");
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
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }

        ResultSet rs = stmt.getResultSet();
	while (rs.next()) {
 	   rs.getString(1);
	}
	return rs;
    }

}

