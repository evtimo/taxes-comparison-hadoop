Feature: CorrectTotalDiff

  Scenario Outline:
    Given Table <seller> and <customer> with different 'total' fields
    When we put data with different 'total' fields into DataBase
    Then  <table1> should contain correct record with different 'total'

    Examples:
      | table1                          | seller                              | customer                        |
      | seller_correct_total_diff       | seller_correct_total_diff.csv       | customer_correct_total_diff.csv |


