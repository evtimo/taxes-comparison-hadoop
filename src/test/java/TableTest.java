import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import javafx.scene.control.Tab;

import java.io.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.codehaus.jackson.node.DoubleNode;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;

abstract class TableTest extends AbstractTestNGCucumberTests {

    public TableRecord customer;
    public GenerateTables tab = new GenerateTables();
    public Connection con;

    public CSVRecord sellerRecord;
    public CSVRecord customerRecord;

    protected void superGiven(String a, String b) throws IOException {

        Reader csv_file = new FileReader(a);

        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().parse(csv_file);
        sellerRecord = parser.getRecords().get(0);
        this.seller = new TableRecord(sellerRecord, RecordType.seller);

        csv_file = new FileReader(b);

        parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().parse(csv_file);
        customerRecord = parser.getRecords().get(0);
        this.customer = new TableRecord(customerRecord, RecordType.customer);

        parser.close();
        System.out.println(seller);
        System.out.println(customer);
    }

    public TableRecord seller;

    protected void superWhen() {

/*        con = tab.Connect();

        tab.DeleteTables(con);
        tab.CreateTables(con);

        seller.insertIntoTable(con);
        customer.insertIntoTable(con);

        tab.DropCompareTables(con);
        tab.CompareTables(con);
*/

    }

   // public abstract void then (String tableName1, String tableName2) throws SQLException;
/*    {

        ResultSet sellerFromTable = getResultSetFromTable(con,tableName1);
        ResultSet customerFromTable = getResultSetFromTable(con,tableName2);

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
        }

        ResultSet rs = stmt.getResultSet();
        rs.last();
        for (int i = 0; i < (rs.getRow()); i++) {
            System.out.println(rs.getString(i));
        }

        return rs;
    }
}

