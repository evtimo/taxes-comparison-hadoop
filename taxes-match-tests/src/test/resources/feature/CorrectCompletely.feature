Feature: CorrectCompletely

  Scenario Outline:
    Given Table <seller> and <customer> with same fields
    When we put data with the same fields into DataBase
    Then  <table1> should contain correct record

    Examples:
      | table1                    | seller                        | customer                      |
      | seller_correct_completely | seller_correct_completely.csv | seller_correct_completely.csv |


