import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import javafx.scene.control.Tab;
import org.testng.Assert;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TableTest extends AbstractTestNGCucumberTests {
    /*private SellerRecord seller;
    private CustomerRecord customer;*/
    private TableRecord seller;
    private TableRecord customer;
    private GenerateTables tab = new GenerateTables();
    private Connection con;

    @Given("^Table (.*) and (.*)")
    public void given(String a, String b) {

      /*  this.seller = new SellerRecord(a);
        this.customer = new CustomerRecord(b);*/

        this.seller = new TableRecord(a, RecordType.seller);
        this.customer = new TableRecord(b, RecordType.customer);
        System.out.println(seller);
        System.out.println(customer);
    }

    @When("^we put data into DataBase$")
    public void we_put_data_into_DataBase() {

        con = tab.Connect();

        tab.DeleteTables(con);
        tab.CreateTables(con);

        /*seller.InsertIntoTable(con);
        customer.InsertIntoTable(con);*/

        seller.insertIntoTable(con);
        customer.insertIntoTable(con);

        tab.DropCompareTables(con);
        tab.CompareTables(con);

    }


    @Then("^(.*) and (.*) should contain this fields$")
    public void then(String tableName1, String tableName2) throws SQLException {

       /* List<SellerRecord> listOfSellers = tab.getSellerDataFromTable(con, tableName1);
        List<CustomerRecord> listOfCustomers = tab.getCustomerDataFromTable(con, tableName2);
        */
        List<TableRecord> listOfSellers = tab.getDataFromTable(con, tableName1);
        List<TableRecord> listOfCustomers = tab.getDataFromTable(con, tableName2);

        Assert.assertEquals(seller.getTotal(), listOfSellers.get(0).getTotal());
        Assert.assertEquals(customer.getTotal(), listOfCustomers.get(0).getTotal());
    }
}
