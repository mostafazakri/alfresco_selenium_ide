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

public class SupprimerModuleSiteTest extends Keywords{
	
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
	public void supprimerModule() {
		boolean testResult = false;
		
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		String NOM_SITE= "site test_"+timeStamp;
		String URL = "sitetest"+timeStamp;
		String DESC = "description test";
		String MODULE = "discussions";
		
		//creer un site
		creer_Un_Site(driver, NOM_SITE, URL, DESC, false, false);
		
		try {Thread.sleep(3000);} catch (InterruptedException e1) {e1.printStackTrace();}
		
		//ajouter module
		ajouterModuleSite(driver, MODULE, NOM_SITE);
		
		//rechercher le site
		rechercherUnSite(driver, NOM_SITE);
		
	    //Suppression du module
		testResult = supprimerModuleSite(driver, NOM_SITE, MODULE);
		
		//Assertion de la suppression
	    Assert.assertEquals(testResult, true);
	    
	    
	    //PRECONDTION: renverser l'ajout du module au site
	    //supprimer le site
		supprimer_Un_Site(driver, NOM_SITE);
	}
}
