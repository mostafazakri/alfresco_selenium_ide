# Projet d'automatisation du CMS Alfresco

This project is a complete test framwork that aims to test 5 functionnalities, using a keywords-driven tests approach

The source code is managed using Git bash, Github,and is written in java syntax using Eclipse Ide


## Technologies used :
This project uses Maven to manage the the dependencies, TestNG to run tests and manage reports
*  Maven
*  TestNG
*  Selenium Ide (Using Java Library)


## Structue :
*  src/main/java ->

    contains all the keywords used in the tests (written in Java Language)
*  src/test/java ->

    contains all the test cases using TestNG assertions and annotations
*  config -> 

    loginProps.properties : contains tests configuration for tests
*  pom.xml: dependencies file
    
*  testng.xml : contains the testNG configuration

*  test-output ->

    contains the test result files (with HTML report)
    
## Project Deployment :
You can fork this repository on your machine, and 

Install Alfresco community edition via : [Containerized Deployment (6.2 GA)](https://www.alfresco.com/thank-you/thank-you-downloading-alfresco-community-edition) 
