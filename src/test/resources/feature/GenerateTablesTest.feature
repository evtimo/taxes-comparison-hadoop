Feature: HiveTest

  Scenario Outline:
    Given Table <seller> and <customer>
    When we put data into DataBase
    Then  <table1> and <table2> should contain this fields

    Examples:
      | table1                          | table2                            | seller                                               | customer                                             |
      | seller_errors_customer_has_pair | customer_errors_customer_has_pair | 4296298695,429604509,4146244349,414604344,1045461.12 | 4146244349,414604344,4296298695,400000000,1045461.12 |

