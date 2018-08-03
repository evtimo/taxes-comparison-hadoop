import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.testng.Assert;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@CucumberOptions(features ="src/test/resources/feature/CorrectCompletely.feature")
public class TableTestCorrectCompletely extends TableTest {

    @Given("^Table (.*) and (.*) with same fields$")
    public void given(String a, String b) throws IOException {
        super.given(a, b);
    }

    @When("^we put data with the same fields into DataBase$")
    public void when() {
        super.when();

    }

    @Then("^(.*) should contain correct record$")
    public void then(String tableName1) throws SQLException {

        // Testing with local data

        Assert.assertEquals(sellerRecord.get("seller_inn_corr"),
                sellerRecord.get("seller_inn_corr"));
        Assert.assertEquals(sellerRecord.get("seller_kpp_corr"),
                sellerRecord.get("seller_kpp_corr"));
        Assert.assertEquals(sellerRecord.get("customer_inn_corr"),
                sellerRecord.get("customer_inn_corr"));
        Assert.assertEquals(sellerRecord.get("customer_kpp_corr"),
                sellerRecord.get("customer_kpp_corr"));
        Assert.assertEquals(sellerRecord.get("total_without_tax_corr"),
                sellerRecord.get("total_without_tax_corr"));
        Assert.assertEquals(sellerRecord.get("total_with_tax_corr"),
                sellerRecord.get("total_with_tax_corr"));

        // Testing with data from Hive
/*

        ResultSet sellerFromTable = getResultSetFromTable(con,tableName1);

        Assert.assertEquals(sellerRecord.get("seller_inn_corr"),
                sellerFromTable.getString("seller_inn_corr"));
        Assert.assertEquals(sellerRecord.get("seller_kpp_corr"),
                sellerFromTable.getString("seller_kpp_corr"));
        Assert.assertEquals(sellerRecord.get("customer_inn_corr"),
                sellerFromTable.getString("customer_inn_corr"));
        Assert.assertEquals(sellerRecord.get("customer_kpp_corr"),
                sellerFromTable.getString("customer_kpp_corr"));
        Assert.assertEquals(sellerRecord.get("total_without_tax_corr"),
                sellerFromTable.getString("total_without_tax_corr"));
        Assert.assertEquals(sellerRecord.get("total_with_tax_corr"),
                sellerFromTable.getString("total_with_tax_corr"));
*/

    }
}