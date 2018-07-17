import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.testng.Assert;

public class TableTest extends AbstractTestNGCucumberTests {
    private TableRecord seller;
    private TableRecord customer;


    @Given("^Table (.*) and (.*)")
    public void given(String a, String b) {

        this.seller = new TableRecord(a);
        this.customer = new TableRecord(b);
        System.out.println(seller);
        System.out.println(customer);
    }

    @When("^we put correct data into DataBase$")
    public void we_put_correct_data_into_DataBase() {
        //INSERT
    }


    @Then("^result should be$")
    public void then() {
        //SELECT
        Assert.assertEquals(seller.getTotal(), customer.getTotal());
    }

}
