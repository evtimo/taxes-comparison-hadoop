import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.testng.Assert;

import java.io.IOException;
import java.sql.SQLException;

public class TableTestCustomerHasPair extends TableTest {

    @Given("^Table (.*) and (.*) with incorrect seller fields$")
    public void given(String a, String b) throws IOException {
        superGiven(a,b);
    }

    @When("^we put data with incorrect seller fields into DataBase$")
    public void When() {
        superWhen();

    }

    @Then("^(.*) and (.*) should have different seller fields$")
    public void then (String tableName1, String tableName2) throws SQLException {
        Assert.assertEquals(sellerRecord.get("seller_inn_err"),
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
                sellerRecord.get("total_with_tax_err"));

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
        ResultSet sellerFromTable = getResultSetFromTable(con,tableName1);
        ResultSet customerFromTable = getResultSetFromTable(con,tableName1);

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