import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.testng.Assert;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@CucumberOptions(features = "src/test/resources/feature/CustomerHasNoPair.feature")
public class TableTestCustomerHasNoPair extends TableTest {

    @Given("^Table (.*) and (.*) with completely damaged seller field$")
    public void given(String sellerTable, String customerTable) throws IOException {
        super.given(sellerTable, customerTable);
    }

    @When("^we put data with completely damaged seller field into DataBase$")
    public void when() {
        super.when();
    }

    @Then("^(.*) should contain original customer record$")
    public void then(String tableName1) throws SQLException {

        ResultSet customerFromTable = getResultSetFromTable(con,tableName1);

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
