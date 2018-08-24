import cucumber.api.testng.AbstractTestNGCucumberTests;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.val;
import lombok.var;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.hadoop.hbase.TestChoreService.log;

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

    @SneakyThrows
    private void runJar(String jarPath, String jarName, String... args) {
        String command = "java -jar " + jarPath + jarName + " " + String.join(" ", args);
        log.info("Running: " + command);
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        log.info("Jar executed successfully");
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

        runJar(jarPath, jarName, "create");

        log.info("Inserting data into tables SELLER and CUSTOMER");
        seller.insertIntoTable(con);
        customer.insertIntoTable(con);
        log.info("Successfully inserted!");

        runJar(jarPath, jarName, "compare");
    }

    @SneakyThrows
    public static ResultSet getResultSetFromTable(Connection con, String TableName) {

        val stmt = con.createStatement();

        String QUERY_SHOW = "SELECT * FROM " + TableName;
        stmt.execute(QUERY_SHOW);

        ResultSet rs = stmt.getResultSet();
        while (rs.next()) {
            rs.getString(1);
        }
        return rs;
    }

}

