
Feature: CustomerHasNoPair

  Scenario Outline:
    Given Table <seller> and <customer> with completely damaged seller field
    When we put data with completely damaged seller field into DataBase
    Then  <table2> should contain original customer record

    Examples:
      | table2                      | seller                        | customer                        |
      | customer_errors_has_no_pair | seller_errors_has_no_pair.csv | customer_errors_has_no_pair.csv |


