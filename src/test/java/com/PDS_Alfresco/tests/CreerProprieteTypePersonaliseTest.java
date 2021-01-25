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

public class CreerProprieteTypePersonaliseTest extends Keywords{
	
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
	public void creerProprieteTypePersonalise() {
		
		WebDriverWait wait = new WebDriverWait(driver, 60);
		boolean testResult = false;
		
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		//TestData
		String nomPropriete = "nom_Propriete"+timeStamp;
		String libele = "libele";
		String desc = "Description";
		String dataType = "d:text";
		String requise = "Obligatoire";
		boolean multiple = true;
		String defaultValue = "text2";
		String indexationValue = "Liste de valeurs - correspondance partielle";
		String contrainte = "Longueur minimum / maximum";
		String constrainte1 = "3";
		String constrainte2 = "30";
		
		//Data Modele
		String MODELEESPACENOM = "Modele_test_"+timeStamp;
		String MODELENOM = "Modele_test_"+timeStamp;
		String DESCRIPTIONMODELE = "Description "+timeStamp;
		String CREATEUR = "tester "+timeStamp;
		String PREFIX = timeStamp;
		
		//data Type Personalise
		String TYPE_PARENT = "cm:dictionaryModel (Dictionary Model)";
		String LIBELLE_AFFICHAGE = "libelle Affichage";
		String DESCRIPTION_TYPEPERSO = "description";
		String NOMTYPEPERSO = "nom_Type_Perso"+timeStamp;
		
		String widgetId= null;
		String indexationFieldType = "text";
		
		
		creerModele(driver, MODELEESPACENOM, PREFIX, MODELENOM, CREATEUR, DESCRIPTIONMODELE);

		creerTypePersonalise(driver,  MODELENOM,  NOMTYPEPERSO, TYPE_PARENT,  LIBELLE_AFFICHAGE, DESCRIPTION_TYPEPERSO);
		
		driver.findElement(By.xpath("//div[@id='TYPES_LIST']//span[contains(text(), '"+NOMTYPEPERSO+"')]")).click();
		driver.findElement(By.xpath("//div[@id='CMM_PANE_CONTAINER']//div[@aria-label='properties']//span[text()='Créer une propriété']")).click();
		
		//checkbox Multiple si vrai
		if(multiple) driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']//input[@name='multiple']")).click();
		
		//remplire les champs du formulaire
		driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']//input[@name='name']")).sendKeys(nomPropriete);
		driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']//input[@name='title']")).sendKeys(libele);
		driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']//textarea[@name='description']")).sendKeys(desc);

		//[champ type de donnees] recuperer le widgetId, verifier si l'element exist, puis le selectionner
		widgetId = driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']/div/div/div/div/form/div[4]/div[2]/div/table")).getAttribute("widgetid");
		driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']/div/div/div/div/form/div[4]/div[2]/div/table/tbody/tr/td[2]")).click();
		if(driver.findElements(By.xpath("//div[@dijitpopupparent='"+widgetId+"']//td[text()='"+dataType+"']")).size()>0){
			driver.findElement(By.xpath("//div[@dijitpopupparent='"+widgetId+"']//td[text()='"+dataType+"']")).click();
		}
		
		//[champ requise] recuperer le widgetId, verifier si l'element exist, puis le selectionner
		widgetId = driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']/div/div/div/div/form/div[5]/div[2]/div/table")).getAttribute("widgetid");
		driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']/div/div/div/div/form/div[5]/div[2]/div/table/tbody/tr/td[2]")).click();
		if(driver.findElements(By.xpath("//div[@dijitpopupparent='"+widgetId+"']//td[text()='"+requise+"']")).size()>0) {
			driver.findElement(By.xpath("//div[@dijitpopupparent='"+widgetId+"']//td[text()='"+requise+"']")).click();
		}
				
		//decider le type du champ indexation avec la variable indexationFieldType
		//remplire le champ valeur par defaut, en fonction du champ type de donnees 
		//
		String[] parts = dataType.split(":");
		if(parts[1].equalsIgnoreCase("text") || parts[1].equalsIgnoreCase("mltext")) { 
			indexationFieldType= "text";
			driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']/div/div/div/div/form/div[contains(@class, 'create-property-default text')]//input[@name='defaultValue']")).sendKeys(defaultValue);
		}else if(parts[1].equalsIgnoreCase("double") || parts[1].equalsIgnoreCase("int") || parts[1].equalsIgnoreCase("float") || parts[1].equalsIgnoreCase("long")){
			indexationFieldType= "nontext";
			driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']/div/div/div/div/form/div[contains(@class, 'create-property-default number')]//input[@name='defaultValue']")).sendKeys(defaultValue);
		}else if(parts[1].equalsIgnoreCase("date") || parts[1].equalsIgnoreCase("datetime")){
			indexationFieldType= "nontext";
			String[] dateVal = defaultValue.split("/");
			
			//cliquer sur la fleche en bas, pour afficher le calendrier
			driver.findElement(By.xpath("//div[contains(@class,'create-property-default date')]/div/div/div/div[1]")).click();
			
			//choisir l'annee
			String currenteDate = driver.findElement(By.xpath("//span[contains(@data-dojo-attach-point,'currentYearLabelNode')]")).getText();
			if(Integer.parseInt(dateVal[2]) != Integer.parseInt(currenteDate)) {
				if(Integer.parseInt(dateVal[2]) < Integer.parseInt(currenteDate)) {
					int difference = Integer.parseInt(driver.findElement(By.xpath("//span[contains(@data-dojo-attach-point,'currentYearLabelNode')]")).getText()) - Integer.parseInt(dateVal[2]);
					for (int i = 0; i < difference; i++) {
						driver.findElement(By.xpath("//span[contains(@data-dojo-attach-point,'previousYearLabelNode')]")).click();
					}
				}else {
					int difference = Integer.parseInt(dateVal[2]) - Integer.parseInt(currenteDate);
					for (int i = 0; i < difference; i++) {
						driver.findElement(By.xpath("//span[contains(@data-dojo-attach-point,'nextYearLabelNode')]")).click();
					}
				}
			}
			
			//choisir le mois
			driver.findElement(By.xpath("//div[contains(@class,'dijitPopup dijitCalendarPopup')]/table/thead/tr/th[2]/span/span")).click();
			driver.findElement(By.xpath("//div[contains(@class,'dijitPopup dijitCalendarMonthMenuPopup')]/div/div[@month='"+(Integer.parseInt(dateVal[1])-1)+"']")).click();
			
			//choisir le jour
			driver.findElement(By.xpath("//div[contains(@class,'dijitPopup dijitCalendarPopup')]/table/tbody/tr/td[contains(@class,'dijitCalendarCurrentMonth')]/span[text() = '"+(Integer.parseInt(dateVal[0])+"']"))).click();
			
		}else if(parts[1].equalsIgnoreCase("boolean")){
			indexationFieldType= "boolean";
			driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']/div/div/div/div/form/div[contains(@class, 'create-property-default boolean')]/div[2]/div/table/tbody/tr/td[2]")).click();
			driver.findElement(By.xpath("//div[@class='dijitPopup dijitMenuPopup create-property-default boolean']/table/tbody/tr/td[2][text()='True']")).click();
		}	
		
		//[champ indexation] recuperer le widgetId, verifier si l'element exist, puis le selectionner
		//ce champ est dynamique, il change en fonction du type de donnees selectionne, la varibale indexationFieldType indique le type de champs (text, nontext, boolean) 
		widgetId = driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']/div/div/div/div/form/div[contains(@class,'create-property-indexing "+indexationFieldType+"')]/div[2]/div/table")).getAttribute("widgetid");
		driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']/div/div/div/div/form/div[contains(@class,'create-property-indexing "+indexationFieldType+"')]/div[2]/div/table/tbody/tr/td[2]")).click();
		if(driver.findElements(By.xpath("//div[@dijitpopupparent='"+widgetId+"']//td[text()='"+indexationValue+"']")).size()>0){
			driver.findElement(By.xpath("//div[@dijitpopupparent='"+widgetId+"']//td[text()='"+indexationValue+"']")).click();
		}
		
		//[champ Contrainte] recuperer le widgetId, verifier si l'element exist, puis le selectionner
		widgetId = driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']/div/div/div/div/form/div[11]/div[2]/div/table")).getAttribute("widgetid");
		driver.findElement(By.xpath("//div[@id='CMM_CREATE_PROPERTY_DIALOG']/div/div/div/div/form/div[11]/div[2]/div/table/tbody/tr/td[2]")).click();
		if(driver.findElements(By.xpath("//div[@dijitpopupparent='"+widgetId+"']//td[text()='"+contrainte+"']")).size()>0) {
			driver.findElement(By.xpath("//div[@dijitpopupparent='"+widgetId+"']//td[text()='"+contrainte+"']")).click();
		}
		
		//remplire les sous-champs des contraintes, au cas ou on a choisi autres valeur que "Aucun"
		if(contrainte.equalsIgnoreCase("Expression régulière")){
			driver.findElement(By.xpath("//div[contains(@class, 'create-property-constraint-expression')]/div[2]/div/div/div[2]/input")).sendKeys(constrainte1);
		}else if(contrainte.equalsIgnoreCase("Classe Java")){
			driver.findElement(By.xpath("//div[contains(@class, 'create-property-constraint-class alfresco-forms-controls-TextBox')]/div[2]/div/div/div[2]/input")).sendKeys(constrainte1);
		}else if(contrainte.equalsIgnoreCase("Longueur minimum / maximum")){
			driver.findElement(By.xpath("//div[contains(@class, 'create-property-constraint-min-length alfresco-forms-controls-NumberSpinner')]/div[2]/div/div/div[3]/input")).clear();
			driver.findElement(By.xpath("//div[contains(@class, 'create-property-constraint-min-length alfresco-forms-controls-NumberSpinner')]/div[2]/div/div/div[3]/input")).sendKeys(constrainte1);
			driver.findElement(By.xpath("//div[contains(@class, 'create-property-constraint-max-length alfresco-forms-controls-NumberSpinner')]/div[2]/div/div/div[3]/input")).clear();
			driver.findElement(By.xpath("//div[contains(@class, 'create-property-constraint-max-length alfresco-forms-controls-NumberSpinner')]/div[2]/div/div/div[3]/input")).sendKeys(constrainte2);
		}else if(contrainte.equalsIgnoreCase("Valeur minimum / maximum")){
			driver.findElement(By.xpath("//div[contains(@class, 'create-property-constraint-min-value alfresco-forms-controls-NumberSpinner')]/div[2]/div/div/div[3]/input")).clear();
			driver.findElement(By.xpath("//div[contains(@class, 'create-property-constraint-max-value alfresco-forms-controls-NumberSpinner')]/div[2]/div/div/div[3]/input")).clear();
			driver.findElement(By.xpath("//div[contains(@class, 'create-property-constraint-min-value alfresco-forms-controls-NumberSpinner')]/div[2]/div/div/div[3]/input")).sendKeys(constrainte1);
			driver.findElement(By.xpath("//div[contains(@class, 'create-property-constraint-max-value alfresco-forms-controls-NumberSpinner')]/div[2]/div/div/div[3]/input")).sendKeys(constrainte2);
		}else if(contrainte.equalsIgnoreCase("Liste de valeurs")){
			driver.findElement(By.xpath("//div[contains(@class, 'create-property-constraint-allowed-values alfresco-forms-controls-TextArea')]/div[2]/div/textarea")).sendKeys(constrainte1);
			if(constrainte2.equalsIgnoreCase("true")) driver.findElement(By.xpath("//div[contains(@class, 'create-property-constraint-sorted alfresco-forms-controls-CheckBox')]/div[2]/div/div/input")).click();
		}
						
		driver.findElement(By.xpath("//span[@id='CMM_CREATE_PROPERTY_DIALOG_CREATE_label']")).click();
		
		//on verifie si l'element est supprime
	    if(driver.findElements(By.xpath("//div[@id='PROPERTIES_LIST']//td/span/span/span[contains(text() , '"+nomPropriete+"')]")).size() == 1){
	    	testResult =  true;
	    }
	    
	    driver.findElement(By.xpath("//div[@id='PROPERTIES_LIST']//td/span/span/span[contains(text() , '"+nomPropriete+"')]/ancestor::tr/td[7]")).click();
	    
	    //recuperer l'identifiant du dropdown pour cette liste d'actions
	    String currentElemToDelete = driver.findElement(By.xpath("//div[@id='PROPERTIES_LIST']//td/span/span/span[contains(text() , '"+nomPropriete+"')]/ancestor::tr/td[7]/div/div")).getAttribute("id");
    	
	    //Attendre le dropdown et cliquer sur supprimer 
	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@dijitpopupparent, '"+currentElemToDelete+"')]/div/div/div[2]/table/tbody/tr[2]/td[2][text() = 'Supprimer']")));
	    driver.findElement(By.xpath("//div[contains(@dijitpopupparent, '"+currentElemToDelete+"')]/div/div/div[2]/table/tbody/tr[2]/td[2][text() = 'Supprimer']")).click();  
		
	    //cliquer sur supprimer
	    driver.findElement(By.xpath("//div[@id='CMM_DELETE_PROPERTY_DIALOG']/div[2]/div[2]/span/span/span/span[3]")).click();
	    
	    //Assertion de la creation de propriete de type personalise
	    Assert.assertEquals(testResult, true);
	    
	    
	    //PRECONDITIONS
	    //supprimer le type personalise
		supprimerTypePersonalise(driver, MODELENOM, NOMTYPEPERSO);
		supprimerModele(driver, MODELENOM);
	}
}
