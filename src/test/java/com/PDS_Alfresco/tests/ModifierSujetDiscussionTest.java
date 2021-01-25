package com.PDS_Alfresco.tests;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.PDS_Alfresco.cases.Keywords;

public class ModifierSujetDiscussionTest extends Keywords{
	
	private  WebDriver driver;
	
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
	public void modifierSujetDiscussion() {
		
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		//Donees du site
		String NOM_SITE = "site test "+timeStamp;
		String URL = "123sitetest"+timeStamp;
		String DESC = "description test";
		
		//Donnees de test
		String nomSujet = "sujet "+timeStamp;
		String tags = "aTag";
		String contenu = "some text";
		//donnees modifies
		String nomSujet_Nouveau = "sujetX Nouveau "+timeStamp; 
		String Content_Nouveau = "some text new";
		String tags_Nouveau = "aNEWTag";
		
		boolean testResult = false;
		
		try {
			//Creer un site
			creer_Un_Site(driver, NOM_SITE, URL, DESC, false, false);
			
			//rechercher le site
			rechercherUnSite(driver, NOM_SITE);
			
			//activer le module discussion
			ajouterModuleSite(driver, "discussions", NOM_SITE);
			
			//Ajouter discussion
			ajouterSujet(driver, nomSujet, contenu, tags);
			
			//modifier sujets et Assertion
			testResult = modifierSujet(driver, nomSujet, tags, nomSujet_Nouveau, Content_Nouveau, tags_Nouveau);
			Assert.assertEquals(testResult, true);
			
			//Supprimer dicussion
			supprimerSujet(driver, nomSujet_Nouveau);
		}finally {
			//Supprimer le site
			supprimer_Un_Site(driver, NOM_SITE);
		}
	}
}
