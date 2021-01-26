# Projet d'automatisation du CMS Alfresco


FR 

Ce projet est un cadre de test complet qui vise à tester 5 fonctionnalités, en utilisant une approche de tests par mots-clés


## Technologies utilisées:
Ce projet Maven pour gérer les dépendances, TestNG pour exécuter des tests et gérer les rapports
* Maven
* TestNG
* Selenium Ide (en utilisant la bibliothèque Java)


### Structue:
src / main / java ->
    contient tous les mots-clés utilisés dans les tests (écrits en langage Java)
src / test / java ->
    contient tous les cas de test utilisant des assertions et des annotations TestNG
config ->
    loginProps.properties: contient la configuration des tests pour les tests
pom.xml: contient tout appelé
testng.xml
sortie test ->
    contient les fichiers de résultats de test (avec rapport HTML)
    
#### Déploiement de projet:
Vous pouvez charger ce référentiel sur votre machine
Installez l'édition communautaire Alfresco (déploiement conteneurisé (6.2 GA) disponible via: https://www.alfresco.com/thank-you/thank-you-downloading-alfresco-community-edition)


EN


This project is a complete test framwork that aims to test 5 functionnalities, using a keywords-driven tests approach 


## Technologies used :
This project Maven to manage the the dependencies, TestNG to run tests and manage reports
*  Maven
*  TestNG
*  Selenium Ide (Using Java Library)


### Structue : 
src/main/java ->
    contains all the keywords used in the tests (written in Java Language)
src/test/java ->
    contains all the test cases using TestNG assertions and annotations
config -> 
    loginProps.properties : contains tests configuration for tests
pom.xml: contains all called
testng.xml
test-output ->
    contains the test result files (with HTML report)
    
#### Project Deployment :
You can fork this repository on your machine
Install Alfresco community edition (Containerized Deployment (6.2 GA) available via : https://www.alfresco.com/thank-you/thank-you-downloading-alfresco-community-edition)
