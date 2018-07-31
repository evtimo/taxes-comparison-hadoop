import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.testng.Assert;

import java.io.IOException;
import java.sql.SQLException;

public class TableTestCustomerHasNoPair extends TableTest {

    @Given("^Table (.*) and (.*) with completely damaged seller field$")
    public void given(String a, String b) throws IOException {
        superGiven(a,b);
    }

    @When("^we put data with completely damaged seller field into DataBase$")
    public void When() {
        superWhen();

    }

    @Then("^(.*) should contain original customer record$")
    public void then (String tableName1) throws SQLException {
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
                customerRecord.get("total_with_tax_err"));

/*
        ResultSet customerFromTable = getResultSetFromTable(con,tableName1);

        Assert.assertEquals(customerRecord.get("seller_inn_corr"),
                customerFromTable.getString("seller_inn_corr"));
        Assert.assertEquals(customerRecord.get("seller_kpp_corr"),
                customerFromTable.getString("seller_kpp_corr"));
        Assert.assertEquals(customerRecord.get("customer_inn_corr"),
                customerFromTable.getString("customer_inn_corr"));
        Assert.assertEquals(customerRecord.get("customer_kpp_corr"),
                customerFromTable.getString("customer_kpp_corr"));
        Assert.assertEquals(customerRecord.get("total_without_tax_corr"),
                customerFromTable.getString("total_without_tax_corr"));
        Assert.assertEquals(customerRecord.get("total_with_tax_corr"),
                customerFromTable.getString("total_with_tax_corr"));
*/
    }
}