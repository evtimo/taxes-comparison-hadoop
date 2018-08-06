Feature: SellerHasPair

  Scenario Outline:
    Given Table <seller> and <customer> with incorrect customer fields
    When we put data with incorrect customer fields into DataBase
    Then  <table1> and <table2> should have different customer fields

    Examples:
      | table1                          | table2                            | seller                              | customer                            |
      | seller_errors_seller_has_pair   | customer_errors_seller_has_pair   | seller_errors_seller_has_pair.csv   | customer_errors_seller_has_pair.csv |


