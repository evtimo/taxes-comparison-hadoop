Feature: CustomerHasPair

  Scenario Outline:
    Given Table <seller> and <customer> with incorrect seller fields
    When we put data with incorrect seller fields into DataBase
    Then  <table1> and <table2> should have different seller fields

    Examples:
      | table1                          | table2                            | seller                              | customer                              |
      | seller_errors_customer_has_pair | customer_errors_customer_has_pair | seller_errors_customer_has_pair.csv | customer_errors_customer_has_pair.csv |


