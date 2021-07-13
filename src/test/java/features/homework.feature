Feature: Adding goods to compare list

  Scenario Outline: Add two phones of brand "<brand1>" and "<brand2>" to compare list
    Given Open Yandex Market and go to smartphones
    When Filter by brand "<brand1>" and "<brand2>" and sort by price ascending
    And Add first phone of brand "<brand1>" and "<brand2>" to compare list
    Then Verify compare list has added positions
    Examples:
      | brand1  | brand2 |
      | Samsung | Xiaomi |