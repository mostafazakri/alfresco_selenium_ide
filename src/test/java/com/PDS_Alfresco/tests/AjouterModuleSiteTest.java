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

public class AjouterModuleSiteTest extends Keywords{
	
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
	public void AjouterModule() {
		boolean testResult = false;
		
		//generer un Timestamp
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		String NOM_SITE= "site test_"+timeStamp;
		String URL = "sitetest"+timeStamp;
		String DESC = "description test";
		String MODULE = "discussions";
		
		//creer un site
		creer_Un_Site(driver, NOM_SITE, URL, DESC, false, false);
		
		try {Thread.sleep(3000);} catch (InterruptedException e1) {e1.printStackTrace();}
		
		//Ajouter un Module au site
		testResult = ajouterModuleSite(driver, MODULE, NOM_SITE);
		
		//Assertion de l'ajout
	    Assert.assertEquals(testResult, true);
	    
		//rechercher le site
		rechercherUnSite(driver, NOM_SITE);
		
	    //PRECONDTION: renverser l'ajout du module au site
		supprimerModuleSite(driver, NOM_SITE, MODULE);
		
        //supprimer le site
		supprimer_Un_Site(driver, NOM_SITE);
	}
	
}
