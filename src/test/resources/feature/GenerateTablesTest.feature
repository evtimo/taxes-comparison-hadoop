Feature: HiveTest

  Scenario Outline:
    Given Table <seller> and <customer>
    When we put correct data into DataBase
    Then result should be

    Examples:
      | seller                                               | customer                                             |
      | 1845411480,184504719,1216291511,121603240,1095764.52 | 1845411480,184504719,1216291511,121603240,1095764.52 |