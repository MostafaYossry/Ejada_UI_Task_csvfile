#Author: mos.yossry.cufe@gmail.com
#Task for Ejada Company
#Failed Login Task
#Data in CSV file

Feature: SauceDemo Shop

# 1st scenario >> Login
# I use different data using csv file to run same TC many times with different data
  Scenario: Checking the login to website feature with invalid credentials (Data from CSV file)
    Given I connected to the Landing page using label "Accepted usernames"
	   When I login with Username, and Password then assert the MSG from CSV file