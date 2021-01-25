package com.PDS_Alfresco.cases;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class Keywords {
	
	private WebDriver  driver = null;
	
	public WebDriver ConfigureBrowser(String browserName, String language){
        if (browserName.equalsIgnoreCase("Firefox")) {
            System.setProperty("webdriver.gecko.driver", "drivers//geckodriver.exe");
            /*FirefoxProfile profile = new ProfilesIni().getProfile("default");
            profile.setPreference("intl.accept_languages",language);
            DesiredCapabilities dc = DesiredCapabilities.firefox();
            dc.setCapability(FirefoxDriver.PROFILE, profile);
            driver = new FirefoxDriver(dc);
            */
            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("intl.accept_languages",language);
            FirefoxOptions FFoptions = new FirefoxOptions();
            FFoptions.setProfile(profile);
    		driver = new FirefoxDriver(FFoptions);
        } else if (browserName.equalsIgnoreCase("Chrome")) {
            System.setProperty("webdriver.chrome.driver", "drivers//chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
    		options.addArguments("--lang="+language);
    		driver = new ChromeDriver(options);
        } else if (browserName.equalsIgnoreCase("IE")) {
        	//language is to be changed manually only on IE, no profiles supported
            System.setProperty("webdriver.ie.driver", "drivers//IEDriverServer.exe");
            driver = new InternetExplorerDriver();
        }
        
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        
        return driver;
	}
	
	public WebDriver initiateBrowser() throws IOException {
		//server 2
		//http://localhost:8091/share/page/
		
		File propFile= new File("config//loginProps.properties");
		Properties prop = new Properties();
		FileInputStream  fis = new FileInputStream(propFile);
		prop.load(fis);
		
		driver = ConfigureBrowser(prop.getProperty("BROWSER"),prop.getProperty("BROWSER_LANGUAGE"));
		Login(driver, prop.getProperty("LOGIN"), prop.getProperty("PSW"), prop.getProperty("SERVER_NAME"),prop.getProperty("ASSERTION_MESSAGE"));
		return driver;
	}
	
	public String Login(WebDriver driver, String UserName, String PSW, String URl, String title) {
		
		driver.get(URl);
		String pageTitle = driver.getTitle();
		if(pageTitle.equalsIgnoreCase(title)) {
			driver.findElement(By.id("page_x002e_components_x002e_slingshot-login_x0023_default-username")).sendKeys(UserName);
			driver.findElement(By.id("page_x002e_components_x002e_slingshot-login_x0023_default-password")).sendKeys(PSW);
			driver.findElement(By.id("page_x002e_components_x002e_slingshot-login_x0023_default-submit-button")).click();
		}
		
		return driver.findElement(By.xpath("//*[@id=\"HEADER_TITLE\"]/span")).getText();
	}
	
	public void logout(WebDriver driver){
		driver.findElement(By.id("HEADER_USER_MENU_POPUP")).click();
		driver.findElement(By.id("HEADER_USER_MENU_LOGOUT_text")).click();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.quit();
	}
	
	public boolean creerTypePersonalise(WebDriver driver, String modele, String nomTypePerso,String typeParent,String  libelleAffichage,String description) {	
		
		//ces constantes pourront etre change, au cas de test du CMS dans une autre langue, ou changement du contenu du CMS
		String BUTTON_AJOUTER_TEXT = "Créer un type personnalisé";
		String TITRE_FORMULAIRE_AJOUT = "Créer un type personnalisé";
		boolean testResult = false;
		WebDriverWait wait = new WebDriverWait(driver, 60);
		
		goToModel();
		//aller sur la page ajout type personalise
	    driver.findElement(By.xpath("//div[@class='alfresco-lists-AlfList']/div[3]/div/table/tbody/tr/td/span/span/span[2][text() = '"+modele+"']")).click();
		driver.findElement(By.xpath("//div[@id='alfresco-console']/descendant::span[text() = '"+BUTTON_AJOUTER_TEXT+"']")).click();
		
		//remplire les valeurs du formulaire
		driver.findElement(By.xpath("//div/span[text() = '"+TITRE_FORMULAIRE_AJOUT+"']/parent::div/following-sibling::div/div/div/div/form/div[1]/div[2]/div/div/div[2]/input")).sendKeys(nomTypePerso);
		driver.findElement(By.xpath("//div/span[text() = '"+TITRE_FORMULAIRE_AJOUT+"']/parent::div/following-sibling::div/div/div/div/form/div[3]/div[2]/div/div/div[2]/input")).sendKeys(libelleAffichage);
		driver.findElement(By.xpath("//div/span[text() = '"+TITRE_FORMULAIRE_AJOUT+"']/parent::div/following-sibling::div/div/div/div/form/div/div[2]//textarea")).sendKeys(description);
		
		//attendre le chargement du dropdrown dynamique et on cliquer sur la fleche pour le visualiser
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div/span[text() = '"+TITRE_FORMULAIRE_AJOUT+"']/parent::div/following-sibling::div/div/div/div/form/div[2]/div[2]/div/table/tbody/tr/td[1]/div/span")));
		driver.findElement(By.xpath("//div/span[text() = '"+TITRE_FORMULAIRE_AJOUT+"']/parent::div/following-sibling::div/div/div/div/form/div[2]/div[2]/div/table/tbody/tr/td[2]")).click();
		
		//on verifie si l'element exist reelement dans la liste, puis on le chhoisi
		if(driver.findElements(By.xpath("//div/span[text() = '"+TITRE_FORMULAIRE_AJOUT+"']/parent::div/parent::div/following-sibling::div/following-sibling::div/table/tbody//descendant::td[text() = '"+typeParent+"']")).size()>0) {
			driver.findElement(By.xpath("//div/span[text() = '"+TITRE_FORMULAIRE_AJOUT+"']/parent::div/parent::div/following-sibling::div/following-sibling::div/table/tbody//descendant::td[text() = '"+typeParent+"']")).click();	
		}
		
		//valider la suppression
		driver.findElement(By.id("CMM_CREATE_TYPE_DIALOG_OK_label")).click();
				
		//on cherche le text du type personalise dans la liste de types, si il exist une seule fois, le test est reussi
		if(driver.findElements(By.xpath("//span[contains(text(), '"+nomTypePerso+"')]")).size() == 1)
			testResult = true;
		
		//on retourne une booleen vrai, si ajoute, faux si non ajoute
		return testResult ;
	}
	
	public boolean supprimerTypePersonalise(WebDriver driver, String modele, String nomTypePerso) {
		boolean present = false;
		String currentElemToDelete = null;
		boolean typeSupprime =  false;
		WebDriverWait wait = new WebDriverWait(driver, 60);
		
	    //on va sur la page liste des modele, on clique sur action, pour le modele en question, pour verifier s'il est actif ou non
		this.goToModel();
		driver.findElement(By.xpath("//div[@class='alfresco-lists-AlfList']/div[3]/div/table/tbody/tr/td/span/span/span[2][text() = '"+modele+"']//ancestor::td[1]//following-sibling::td[3]")).click();
		
		
		//si le modele est actif, on le sauvegarde dans une variable present
		List<WebElement> listElems = driver.findElements(By.xpath("//div[contains(@class, 'Popup')]/div/div/div[2]/table/tbody/tr[1]/td[2][text() =  'Désactiver']"));
		if(listElems.size()>0) present = true;
		
		//desactiver le modele temporairement afin de pouvoir supprimer le type personalise 
		if(present) driver.findElement(By.xpath("//div[contains(@class, 'Popup')]/div/div/div[2]/table/tbody/tr[1]/td[2][text() =  'Désactiver']")).click();
		
		//choisir et cliquer sur la page du modele
	    driver.findElement(By.xpath("//div[@class='alfresco-lists-AlfList']/div[3]/div/table/tbody/tr/td/span/span/span[2][contains(text(),'"+modele+"')]")).click();
	      
	    //cliquer sur actions
	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='TYPES_LIST']//span[contains(text(), '"+nomTypePerso+"')]/ancestor::td/following-sibling::td[4]/div/div/div/span[2]")));
	    driver.findElement(By.xpath("//div[@id='TYPES_LIST']//span[contains(text(), '"+nomTypePerso+"')]/ancestor::td/following-sibling::td[4]/div/div/div/span[2]")).click();
	    
	    //recuperer l'identifiant du dropdown pour cette liste d'actions
	    currentElemToDelete = driver.findElement(By.xpath("//div[@id='TYPES_LIST']//span[contains(text(), '"+nomTypePerso+"')]/ancestor::td/following-sibling::td[4]/div[1]/div[1]")).getAttribute("id");
    	//attendre le dropdown

	    //Attendre et cliquer sur supprimer 
	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@dijitpopupparent, '"+currentElemToDelete+"')]/div/div/div[2]/table/tbody/tr[3]/td[2][text() = 'Supprimer']")));
	    driver.findElement(By.xpath("//div[contains(@dijitpopupparent, '"+currentElemToDelete+"')]/div/div/div[2]/table/tbody/tr[3]/td[2][text() = 'Supprimer']")).click();
	    
	    //confirmer suppression
	    driver.findElement(By.xpath("//div[@id='CMM_DELETE_TYPE_DIALOG']/div[2]/div[2]/span/span/span/span[text() = 'Supprimer']")).click();
	    
	    //si le modele etait activee avant le test, on le reactive
	    if(present) {
	    	goToModel();
	    	driver.findElement(By.xpath("//div[@class='alfresco-lists-AlfList']/div[3]/div/table/tbody/tr/td/span/span/span[2][text() = '"+modele+"']//ancestor::td[1]//following-sibling::td[3]")).click();
	    	driver.findElement(By.xpath("//div[contains(@class, 'Popup')]/div/div/div[2]/table/tbody/tr[1]/td[2][text() = 'Activer']")).click();
	    }
	    
	    //on verifie si l'element est supprime
	    if(driver.findElements(By.xpath("//div[@id='TYPES_LIST']//span[text() = '"+nomTypePerso+"']/ancestor::td/following-sibling::td[4]")).size() == 0){
	    	typeSupprime =  true;
	    }
    
	    //le keyword retourn la bouleen vrai si supprime, faux si non supprime
		return typeSupprime;
	}
	
	public void creerModele(WebDriver driver, String espaceNom, String prefix, String nom, String createur, String description) {
		
		goToModel();
		
		//cliquer sur le boutton ajouter
		driver.findElement(By.xpath("//div[@id='CMM_PANE_CONTAINER']//span[text()='Créer un modèle']")).click();
		
		//remplir les champs du formulaire
		driver.findElement(By.xpath("//div[@id='CMM_CREATE_MODEL_DIALOG']//input[@name='namespace']")).sendKeys(espaceNom);
		driver.findElement(By.xpath("//div[@id='CMM_CREATE_MODEL_DIALOG']//input[@name='prefix']")).sendKeys(prefix);
		driver.findElement(By.xpath("//div[@id='CMM_CREATE_MODEL_DIALOG']//input[@name='name']")).sendKeys(nom);
		driver.findElement(By.xpath("//div[@id='CMM_CREATE_MODEL_DIALOG']//input[@name='author']")).sendKeys(createur);
		driver.findElement(By.xpath("//div[@id='CMM_CREATE_MODEL_DIALOG']//textarea[@name='description']")).sendKeys(description);
		
		//cliquer sur ajouter
		driver.findElement(By.xpath("//div[@id='CMM_CREATE_MODEL_DIALOG']//span[@id='CMM_CREATE_MODEL_DIALOG_OK_label']")).click();
		
	    //redirection sur accueil
	    driver.findElement(By.xpath("//div[@id='HEADER_APP_MENU_BAR']//a[text() = 'Accueil']"));
	}
	
	public void supprimerModele(WebDriver driver, String nomModele) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		
		goToModel();
		
		//cliquer sur actions
	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='MODELS_LIST']//span[contains(text(), '"+nomModele+"')]/ancestor::td/following-sibling::td[3]/div/div/div/span[2]")));
	    driver.findElement(By.xpath("//div[@id='MODELS_LIST']//span[contains(text(), '"+nomModele+"')]/ancestor::td/following-sibling::td[3]/div/div/div/span[2]")).click();
	    
		//recuperer l'identifiant du dropdown pour cette liste d'actions
	    String currentElemToDelete = driver.findElement(By.xpath("//div[@id='MODELS_LIST']//span[contains(text(), '"+nomModele+"')]/ancestor::td/following-sibling::td[3]/div[1]/div[1]")).getAttribute("id");
	    System.out.println(currentElemToDelete);
	    //Attendre le dropdown et cliquer sur supprimer 
	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@dijitpopupparent, '"+currentElemToDelete+"')]/div/div/div[2]/table/tbody/tr[3]/td[2][text() = 'Supprimer']")));
	    driver.findElement(By.xpath("//div[contains(@dijitpopupparent, '"+currentElemToDelete+"')]/div/div/div[2]/table/tbody/tr[3]/td[2][text() = 'Supprimer']")).click();
	    
	    //confirmer suppression
	    driver.findElement(By.xpath("//div[@id='CMM_DELETE_MODEL_DIALOG']/div[2]/div[2]/span/span/span/span[text() = 'Supprimer']")).click();
	    
	}
	
	public void goToModel() {
		//ce keyword nous emmenent sur la page gestion des modeles
		
		WebDriverWait wait = new WebDriverWait(driver, 300);
		String TEXT_BUTTON_ADMIN = "Outils admin";
		String TEXT_GEST_MODELE ="Gestionnaire de modèles";
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text() = '"+TEXT_BUTTON_ADMIN+"']")));
		
		driver.findElement(By.xpath("//a[text() = '"+TEXT_BUTTON_ADMIN+"']")).click();
		driver.findElement(By.xpath("//a[text() = '"+TEXT_GEST_MODELE+"']")).click();
	}
	
	
	public void creerGroupe(WebDriver driver,String identifiant, String Nom_affiche){
	     //find and click the element 'Outils admin'
	     driver.findElement(By.linkText("Outils admin")).click();
	     //Find and click the element 'Groupes'
	     driver.findElement(By.linkText("Groupes")).click();
	     try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
	     //Find and click the button Parcourir
	     driver.findElement(By.id("page_x002e_ctool_x002e_admin-console_x0023_default-browse-button-button")).click();
	     try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
	     //Find and click the button +
	     driver.findElement(By.className("groups-newgroup-button")).click();
	     //Tape the identifiant
		 driver.findElement(By.id("page_x002e_ctool_x002e_admin-console_x0023_default-create-shortname")).sendKeys(identifiant);
		//Tape the Nom affiche
		 driver.findElement(By.id("page_x002e_ctool_x002e_admin-console_x0023_default-create-displayname")).sendKeys(Nom_affiche);
	     //# Click on the button Creer un groupe
		 driver.findElement(By.id("page_x002e_ctool_x002e_admin-console_x0023_default-creategroup-ok-button-button")).click();
	}
	
	public void SupprimerUnGroupe(WebDriver driver, String vGroupe) {
		// Saisir le nom du Groupe exact à supprimer vGroupe
		// Cliquez sur Accueil
		driver.findElement(By.xpath("//div[@id='HEADER_HOME']")).click();
		// Cliquer sur Outils Admin
		driver.findElement(By.xpath("//a[text()='Outils admin']")).click();
		// Cliquer sur Groupes
		driver.findElement(By.xpath("//a[@title=\"Gestion des groupes\"]")).click();
		// Saisir le nom de groupe a supprimer dans la barre de recherche
		driver.findElement(By.xpath("//div[@class='groups']//div[@class='search-text']/input[1]")).sendKeys(vGroupe);
		// Cliquer sur Rechercher
		driver.findElement(By.xpath("//div[@class='search-button']/span[1]/span[1]/button[1]")).click();
		// Cliquer sur Supprimer
		driver.findElement(By.xpath("//a[@class='delete']")).click();
		// Cliquez sur le boutton supprimer
		driver.findElement(By.xpath("//div[@class='bdft']/span[1]/span[1]/button[text()='Supprimer']")).click();
	}
	
	public void creer_Un_Site(WebDriver driver, String vNomSite, String vUrlSite, String vDescriptionSite, boolean cocherModere, boolean cocherPrive) {
		// Click sur le menu site
        driver.findElement(By.id("HEADER_SITES_MENU_text")).click();
        // Temps d'attente
        WebDriverWait wait = new WebDriverWait(driver, 5);
        // Attendre que l'élement créer un site soit présent
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("HEADER_SITES_MENU_CREATE_SITE_text")));
        // Click sur l'option créer un site
        driver.findElement(By.id("HEADER_SITES_MENU_CREATE_SITE_text")).click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("CREATE_SITE_DIALOG_title")));
        // Vérifier que le type du site est bien "Site de Collaboration", valeur unique par défaut
        driver.findElement(By.xpath("//div[@class='dijitReset dijitInputField dijitButtonText']")).getText().equals("Site de Collaboration");
        // Saisie du nom du site
        driver.findElement(By.name("title")).sendKeys(vNomSite);
        //Effacer la valeur par défaut du champ Nom de l'URL
        driver.findElement(By.name("shortName")).clear();
        //Donner un autre nom d'URL au site
        driver.findElement(By.name("shortName")).sendKeys(vUrlSite);
        // Description du site
        driver.findElement(By.name("description")).sendKeys(vDescriptionSite);
        
        // Choix de la visibilité du site
        // Mettre cocherModere à true pour créer un site avec une visibilité modéré
        if(cocherModere) {
        	driver.findElement(By.id("CREATE_SITE_FIELD_VISIBILITY_CONTROL_OPTION1_BUTTON")).click();
        	}
        // Mettre cocherPrive à true pour créer un site avec une visibilité Liste privé
        if(cocherPrive) {
        	driver.findElement(By.id("CREATE_SITE_FIELD_VISIBILITY_CONTROL_OPTION2_BUTTON")).click();
        	}
        	
        // Click buton créer
        wait.until(ExpectedConditions.elementToBeClickable(By.id("CREATE_SITE_DIALOG_OK_label"))).click();	                  
	}
	
	
	public void rechercherUnSite(WebDriver driver, String nomSite) {
		// Temps d'attente implicite
		WebDriverWait wait = new WebDriverWait(driver, 60);
		
		//attendre la fin du chargement de la page du site
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()='"+nomSite+"']")));
				
		// Click sur le menu site
		driver.findElement(By.id("HEADER_SITES_MENU_text")).click();
		
		// Attendre que l'élement Mes sites soit présent
		wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Mes sites")));
		// Click sur l'option Mes sites
		driver.findElement(By.linkText("Mes sites")).click();
		// Click sur le nom du site 
		driver.findElement(By.xpath("//a[text()='"+nomSite+"']")).click();
	}
	
	
	public void supprimer_Un_Site(WebDriver driver, String nomSite) {
		// Temps d'attente implicite
		WebDriverWait wait = new WebDriverWait(driver, 60);
		
		// Click sur le menu site
		driver.findElement(By.xpath("//span[@id='HEADER_SITES_MENU_text']")).click();
		// Attendre que l'élement Mes sites soit présent
		wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Mes sites")));
		// Click sur l'option Mes sites
		driver.findElement(By.linkText("Mes sites")).click();
		// Click sur le nom du site à supprimer
		driver.findElement(By.xpath("//a[text()='"+nomSite+"']")).click();
		// Click sur la roue dentee 
		driver.findElement(By.xpath("//*[@id=\"HEADER_SITE_CONFIGURATION_DROPDOWN\"]/img ")).click();
		// Attendre que le bouton supprimer soit visible
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("HEADER_DELETE_SITE_text")));
		//Click sur le bouton Supprimer
		driver.findElement(By.id("HEADER_DELETE_SITE_text")).click();
		// Click sur le OK de confirmation de suppression
		driver.findElement(By.id("ALF_SITE_SERVICE_DIALOG_CONFIRMATION_label")).click();
		// Click sur le menu site
		driver.findElement(By.id("HEADER_SITES_MENU_text")).click();
		// Attendre que l'élement Mes sites soit présent et click sur cet élement
		wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Mes sites"))).click();
		// Vérifier si le site a bien été supprimé
		//wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//a[text()='"+nomSite+"']")));		               
	}
	

	
	public void ajouterSujet(WebDriver driver, String nomSujet, String Content, String tags){
		
		// Temps d'attente
        WebDriverWait wait = new WebDriverWait(driver, 30);
        
		//cliquer sur boutton dicussions
		driver.findElement(By.xpath("//div[@id='HEADER_SITE_DISCUSSIONS-TOPICLIST']")).click();
		
		//cliquer sur le boutton nouveau sujet
		driver.findElement(By.xpath("//button[text()='Nouveau sujet']")).click();
		////div[contains(@class,'mce-edit-area mce-container mce-panel mce-stack-layout-item')]/iframe
		//remplire les champs
		driver.findElement(By.xpath("//input[@name='title']")).sendKeys(nomSujet);
		
		//attendre et changer vers le frame contenant tinymce
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(1));
		
		//creer JSExe remplir le champ contenu
		JavascriptExecutor jse =  (JavascriptExecutor) driver;
		jse.executeScript("document.getElementById('tinymce').innerText = arguments[0]", Content);
		
		//revenir sur le main frame
		driver.switchTo().defaultContent();
		
		//remplir le champ tags
		driver.findElement(By.xpath("//input[@name='-']")).sendKeys(tags);
		//cliquer sur boutton ajouter tags
		driver.findElement(By.xpath("//button[text()='Ajouter']")).click();
		
		//cliquer sur enregistrer
		driver.findElement(By.xpath("//button[text()='Enregistrer']")).click();
	}
	
	
	public void supprimerSujet(WebDriver driver, String nomSujet){
		//cliquer sur boutton dicussions
		driver.findElement(By.xpath("//div[@id='HEADER_SITE_DISCUSSIONS-TOPICLIST']")).click();
		//cliquer sur le boutton supprimer
		driver.findElement(By.xpath("//a[text()='"+nomSujet+"']//../../../div[@class='nodeEdit']//a[text()='Supprimer']")).click();
		//Cliquer sur confirmer
		driver.findElement(By.xpath("//button[text()='Supprimer']")).click();
	}
	
	
	public boolean modifierSujet(WebDriver driver, String nomSujet, String tags, String nomSujet_New, String Content_New, String tags_New){
		
		// Temps d'attente
        WebDriverWait wait = new WebDriverWait(driver, 15);
		boolean testResult = false;
		
		//cliquer sur boutton dicussions
		driver.findElement(By.xpath("//div[@id='HEADER_SITE_DISCUSSIONS-TOPICLIST']")).click();
		//cliquer sur le boutton supprimer
		driver.findElement(By.xpath("//a[text()='"+nomSujet+"']//../../../div[@class='nodeEdit']//a[text()='Modifier']")).click();
		
		//clear the fields
		driver.findElement(By.xpath("//input[@name='title']")).clear();
		//remplire les champs
		driver.findElement(By.xpath("//input[@name='title']")).sendKeys(nomSujet_New);
		
		//attendre et changer vers le frame contenant tinymce
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(1));
		
		//prepare JSExe
		JavascriptExecutor jse =  (JavascriptExecutor) driver;
		
		//send empty text
		jse.executeScript("document.getElementById('tinymce').innerText = arguments[0]", "");
		//fill with new text
		jse.executeScript("document.getElementById('tinymce').innerText = arguments[0]", Content_New);
		
		//switch to default html doc
		driver.switchTo().defaultContent();
		
		//remove cuurent tag
		driver.findElement(By.xpath("//span[text()='"+tags.toLowerCase()+"']/parent::a/parent::li")).click();
		//send text for new tag
		driver.findElement(By.xpath("//input[@name='-']")).sendKeys(tags_New);
		//cliquer sur boutton ajouter tags
		driver.findElement(By.xpath("//button[text()='Ajouter']")).click();
		
		//cliquer sur enregistrer
		driver.findElement(By.xpath("//button[text()='Enregistrer']")).click();
		
		testResult = driver.findElements(By.xpath("//a[text()='"+nomSujet_New+"']")).size() == 1 ;
		
		return testResult;
	}
	
	
	
	public boolean ajouterModuleSite(WebDriver driver, String module, String nomSite) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		boolean testResult = false;
	
		// Click sur le nom du site cree
		driver.findElement(By.xpath("//a[text()='"+nomSite+"']")).click();
		
		//cliquer sur la roue dentee
		driver.findElement(By.xpath("//div[@id='HEADER_SITE_CONFIGURATION_DROPDOWN']/span[2]")).click();
		
		//cliquer sur personaliser le site
		driver.findElement(By.xpath("//div[@class='dijitPopup Popup']/div/div[3]/div[2]")).click();

		//Drag And drop Module
		Actions actions = new Actions(driver);
        WebElement draggable = driver.findElement(By.xpath("//div[contains(@class,'available-pages')]/ul/li[contains(@id,'"+module+"')]"));
        WebElement droppable = driver.findElement(By.xpath("//div[contains(@class,'current-pages')]"));
        actions.dragAndDrop(draggable, droppable).build().perform();
        
        //cliquer sur ok
        driver.findElement(By.xpath("//button[text()='OK']")).click();
        
        //attendre d'etre sur le dashboard
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[@class='sub-title']")));
        

        //cliquer sur la roue dentee
  		driver.findElement(By.xpath("//div[@id='HEADER_SITE_CONFIGURATION_DROPDOWN']/span[2]")).click();
  		
  		//cliquer sur personaliser le site
  		driver.findElement(By.xpath("//div[@class='dijitPopup Popup']/div/div[3]/div[2]")).click();
      	
        //verifier que le module a ete ajoute
	    if(driver.findElements(By.xpath("//div[contains(@class,'current-pages')]/ul/li[contains(@id,'"+module+"')]")).size() == 1){
	    	testResult =  true;
	    }
	    
		//retourner le resultat pret pour assertion
		return testResult;
	}
	
	
	
	public boolean supprimerModuleSite(WebDriver driver, String site,  String module){
		WebDriverWait wait = new WebDriverWait(driver, 60);
		boolean testResult = false;
		
		//cliquer sur la roue dentee
		driver.findElement(By.xpath("//div[@id='HEADER_SITE_CONFIGURATION_DROPDOWN']/span[2]")).click();
		
		//cliquer sur personaliser le site
		driver.findElement(By.xpath("//div[@class='dijitPopup Popup']/div/div[3]/div[2]")).click();
	
		//Retirer le module
		driver.findElement(By.xpath("//div[contains(@class,'current-pages')]/ul/li[contains(@id,'discussions')]//a[text()='Retirer']")).click();
		
	    //cliquer sur ok
	    driver.findElement(By.xpath("//button[text()='OK']")).click();
	    
	    //attendre d'etre sur le dashboard
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[@class='sub-title']")));
	    
	    //cliquer sur la roue dentee
		driver.findElement(By.xpath("//div[@id='HEADER_SITE_CONFIGURATION_DROPDOWN']/span[2]")).click();
		
		//cliquer sur personaliser le site
		driver.findElement(By.xpath("//div[@class='dijitPopup Popup']/div/div[3]/div[2]")).click();
		
	    //verifier que le module a ete supprime
	    if(driver.findElements(By.xpath("//div[contains(@class,'current-pages')]/ul/li[contains(@id,'"+module+"')]")).size() == 0){
	    	testResult =  true;
	    }
	    
		return testResult;
	}
	
	
	
	
}
