import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.testng.Assert;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@CucumberOptions(features = "src/test/resources/feature/SellerHasNoPair.feature")
public class TableTestSellerHasNoPair extends TableTest {

    @Given("^Table (.*) and (.*) with completely damaged customer field$")
    public void given(String sellerTable, String customerTable) throws IOException {
        super.given(sellerTable, customerTable);
    }

    @When("^we put data with completely damaged customer field into DataBase$")
    public void when() {
        super.when();
    }

    @Then("^(.*) should contain original seller record$")
    public void then(String tableName1) throws SQLException {

        // Testing with local data

        /* Assert.assertEquals(sellerRecord.get("seller_inn_err"),
                sellerRecord.get("seller_inn_err"));
        Assert.assertEquals(sellerRecord.get("seller_kpp_err"),
                sellerRecord.get("seller_kpp_err"));
        Assert.assertEquals(sellerRecord.get("customer_inn_err"),
                sellerRecord.get("customer_inn_err"));
        Assert.assertEquals(sellerRecord.get("customer_kpp_err"),
                sellerRecord.get("customer_kpp_err"));
        Assert.assertEquals(sellerRecord.get("total_without_tax_err"),
                sellerRecord.get("total_without_tax_err"));
        Assert.assertEquals(sellerRecord.get("total_with_tax_err"),
                sellerRecord.get("total_with_tax_err")); */

        // Testing with data from Hive


        ResultSet sellerFromTable = getResultSetFromTable(con, tableName1);

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
    }
}
