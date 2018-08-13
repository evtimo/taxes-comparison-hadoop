Feature: SellerHasNoPair

  Scenario Outline:
    Given Table <seller> and <customer> with completely damaged customer field
    When we put data with completely damaged customer field into DataBase
    Then  <table1> should contain original seller record

    Examples:
      | table1                    | seller                        | customer                        |
      | seller_errors_has_no_pair | seller_errors_has_no_pair.csv | customer_errors_has_no_pair.csv |


