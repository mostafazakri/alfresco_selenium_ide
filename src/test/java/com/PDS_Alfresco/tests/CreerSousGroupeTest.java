package com.PDS_Alfresco.tests;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.PDS_Alfresco.cases.Keywords;

public class CreerSousGroupeTest extends Keywords{
	
	public WebDriver driver;
	
	@BeforeMethod
	public void openBrowser() throws IOException {
		String assertionTitle= "Tableau de bord de ";
		driver = initiateBrowser();
		Assert.assertEquals(driver.getPageSource().contains(assertionTitle), true);
	}
	
	@AfterMethod
    public void closeBrowser(){
    	logout(driver);
    }
	
	
	@Test
	public void creerSousGroupe() {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		boolean testResult = false;
		
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		//Donnees de tests
		String IdentifiantGroupe = "groupe_"+timeStamp;
		String nomAfficheGroupe= "groupe_Test";
		String SGroupeIdentifiant = "sous groupe id"+timeStamp;
		String SGroupeNomAffiche = "sous groupe nom"+timeStamp;
		
		//Creer un groupe
		creerGroupe(driver, IdentifiantGroupe, nomAfficheGroupe);
		
		//aller sur la page de gestion des groupes
		driver.findElement(By.xpath("//a[text() = 'Outils admin']")).click();
		driver.findElement(By.xpath("//a[text() = 'Groupes']")).click();
		
		//cliquer sur screation sous groupe
		driver.findElement(By.id("page_x002e_ctool_x002e_admin-console_x0023_default-browse-button-button")).click();
		driver.findElement(By.xpath("//div[@id='page_x002e_ctool_x002e_admin-console_x0023_default-columnbrowser']//descendant::span[text() = '"+nomAfficheGroupe+" ("+IdentifiantGroupe+")']")).click();
		driver.findElement(By.xpath("//div[@id='page_x002e_ctool_x002e_admin-console_x0023_default-columnbrowser']/div[2]/ul/li[2]/div[1]//span[@title = 'Nouveau sous-groupe']")).click();
		
		//remplire les champs de creation du sous groupe
		driver.findElement(By.xpath("//input[@id='page_x002e_ctool_x002e_admin-console_x0023_default-create-shortname']")).sendKeys(SGroupeIdentifiant);
		driver.findElement(By.xpath("//input[@id='page_x002e_ctool_x002e_admin-console_x0023_default-create-displayname']")).sendKeys(SGroupeNomAffiche);
		
		//Cliquer sur creer groupe
		driver.findElement(By.xpath("//button[@id='page_x002e_ctool_x002e_admin-console_x0023_default-creategroup-ok-button-button']")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".message")));
		
		//Creer un sous-groupe
		testResult = driver.getPageSource().contains("Succès de la création du nouveau groupe.");
		
		//Assertion du cas de test
		Assert.assertEquals(testResult, true);
		
		//Preconditions
		//supprimer le sous-groupe
		SupprimerUnGroupe(driver, SGroupeIdentifiant);
		
		//supprimer le groupe
		SupprimerUnGroupe(driver, IdentifiantGroupe);
		
	}
}
