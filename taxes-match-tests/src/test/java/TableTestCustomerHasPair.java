import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.testng.Assert;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@CucumberOptions(features = "src/test/resources/feature/CustomerHasPair.feature")
public class TableTestCustomerHasPair extends TableTest {

    @Given("^Table (.*) and (.*) with incorrect seller fields$")
    public void given(String sellerTable, String customerTable) throws IOException {
        super.given(sellerTable, customerTable);
    }

    @When("^we put data with incorrect seller fields into DataBase$")
    public void when() {
        super.when();
    }

    @Then("^(.*) and (.*) should have different seller fields$")
    public void then(String tableName1, String tableName2) throws SQLException {

        Assert.assertEquals(customerRecord.get("seller_inn_err"),
                customerRecord.get("seller_inn_err"));
        Assert.assertEquals(customerRecord.get("seller_kpp_err"),
                customerRecord.get("seller_kpp_err"));
        Assert.assertEquals(customerRecord.get("customer_inn_err"),
                customerRecord.get("customer_inn_err"));
        Assert.assertEquals(customerRecord.get("customer_kpp_err"),
                customerRecord.get("customer_kpp_err"));
        Assert.assertEquals(customerRecord.get("total_without_tax_err"),
                customerRecord.get("total_without_tax_err"));
        Assert.assertEquals(customerRecord.get("total_with_tax_err"),
                customerRecord.get("total_with_tax_err")); */

        // Testing with data from Hive

        ResultSet sellerFromTable = getResultSetFromTable(con,tableName1);
        ResultSet customerFromTable = getResultSetFromTable(con,tableName2);

        Assert.assertEquals(sellerRecord.get("seller_inn_err"),
                sellerFromTable.getString("seller_inn_err"));
        Assert.assertEquals(sellerRecord.get("seller_kpp_err"),
                sellerFromTable.getString("seller_kpp_err"));
        Assert.assertEquals(sellerRecord.get("customer_inn_err"),
                sellerFromTable.getString("customer_inn_err"));
        Assert.assertEquals(sellerRecord.get("customer_kpp_err"),
                sellerFromTable.getString("customer_kpp_err"));
        Assert.assertEquals(sellerRecord.get("total_without_tax_err"),
                sellerFromTable.getString("total_without_tax_err"));
        Assert.assertEquals(sellerRecord.get("total_with_tax_err"),
                sellerFromTable.getString("total_with_tax_err"));

         Assert.assertEquals(customerRecord.get("seller_inn_err"),
                customerFromTable.getString("seller_inn_err"));
        Assert.assertEquals(customerRecord.get("seller_kpp_err"),
                customerFromTable.getString("seller_kpp_err"));
        Assert.assertEquals(customerRecord.get("customer_inn_err"),
                customerFromTable.getString("customer_inn_err"));
        Assert.assertEquals(customerRecord.get("customer_kpp_err"),
                customerFromTable.getString("customer_kpp_err"));
        Assert.assertEquals(customerRecord.get("total_without_tax_err"),
                customerFromTable.getString("total_without_tax_err"));
        Assert.assertEquals(customerRecord.get("total_with_tax_err"),
                customerFromTable.getString("total_with_tax_err"));
    }
}
