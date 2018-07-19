Feature: HiveTest

  Scenario Outline:
    Given Table <seller> and <customer>
    When we put data into DataBase
    Then  <table1> and <table2> should contain this fields

    Examples:
      | table1                          | table2                            | seller                                              | customer                                            |
      | seller_errors_customer_has_pair | customer_errors_customer_has_pair | 3000000000,300000000,3976815858,397604021,845467.64 | 3976815858,397604021,3615413234,361502122,845467.64 |

