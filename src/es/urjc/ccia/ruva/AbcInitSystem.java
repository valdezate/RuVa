package es.urjc.ccia.ruva;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class AbcInitSystem {
	private static boolean isFinal=false;
	private static String pathConfigFile="D:\\workspace\\202112\\features\\ruva.cfg";
	private static AbcSystemLibrary sl=new AbcSystemLibrary();
	
	public static void main(String[] args) {		
		System.out.println("Starting execution main code");
		pathConfigFile=pathConfigFile.replace("\\", "/");
		processConfig(pathConfigFile);
		
		boolean isError=false;
		if (args.length > 1) {
			System.out.println("Hay demasiados parámetros");
			isError=true;
		} else if (args.length == 0) {      
			//mode=1; 
		} else {
			sl.gMode=0;
			sl.gFMScript=args[0];
		}
		if (sl.gFMScript.length()>0) {
			sl.gFMScript=sl.gFMScript.replace("\\", "/");
			processScript(sl.gFMScript);
		}
		
		switch (sl.gMode) {
		case 1:
			sl.isError=false; 
			String rootName="his";
			String extension="xml";
			String myFile="D:/workspace/201903/features/fm/"+rootName+"."+extension;
			if (!sl.isError) {
				loadConfiguration();
				if (!sl.isError) {
					loadRepositories();
					if (!sl.isError) {
						sl.typeOfFMFile=sl.getTypeFMFile(myFile);
						
					    switch (sl.typeOfFMFile) {  //1=ruva;2=fm;3=xml(FaMa);4=xml(FeatureIde);5=afm(FaMa)
					    case 1: //ruva
					    	break;
					    case 2: //.fm
							sl.readFMFile(myFile);
							sl.populateFMfromFMFile(sl.fmFile);
					    	break;
					    case 3: // xml fama
					    	sl.populateFMfromXMLFile(myFile);
					    	break;
					    case 4: // xml FeatureIDE
					    	break;
					    case 5: // afm FaMa 
					    	break;
					    default:
					    	break;
					    }
	
					}
				} else {
					System.err.println("Can't load configuration. Exist database? \n\nExiting now ... ->");
				}
			}
			break;
		case 0:
			break;
		case 2:
			processScript(sl.scriptPath);
			break;
		default:
			System.out.println("MODE not valid (not 0,1 or 2");
			break;
		}
	}

	private static boolean sendRuvaCommand(String msg) {
		return true;
	}

	
	private static void askCommand() {
		System.out.println("");
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.println("Enter a command (m for menu): ");		
		String userCommand = reader.nextLine(); // Scans the next token of the input as an int.
		switch (userCommand) {
        case "sf": case "1": 
        	showFeatures();
        	break;
        case "sst": case "4": 
        	showSupertypes();
        	break;
        case "sfs": case "5": 
        	showFeatureSupertypeRelations();
        	break;
        case "afs": case "11":
        	//addFeatureOld();
        	break;
        case "sfm": case "16":  
        	showFeatureModel();
        	break;
        case "sc": case "17":
        	showConstraints();
        	break;
        case "vfm": case "18":
        	validateFeatureModel();
        	break;
        case "n":  case "21":
        	newFeatureModel();
        	break;
        case "l":  case "22":
        	loadFeatureModel();
        	break;
        case "s": case "23":  
        	saveFeatureModel();
        	break;
        case "sa": case "24":  
        	saveFeatureModelAs();
        	break;
        case "gfm": case "25":  
        	generateFMFile();
        	break;
        case "lfm": case "26":  
        	loadFromFMFile();
        	break;
        case "me":  case "m": case "98":
        	showMenu();
        	break;
        case "ex": case "e": case "exit": case "99":
        	exitSystem();
        	break;

        default: 
        	System.out.println("Option not valid. Type again");
            break;
        }
	}	
	private static void addRestriction() {
	}		
	private static void exitSystem() {
		isFinal=true;
		System.out.println("End of application");		
	}	
	private static void loadConfiguration() {
		sl.loadConfiguration();
	}	
	private static void loadRepositories() {
		//sl.loadFeaturesDB();
		sl.loadFeaturesFile();
		sl.loadFeaturesSupertypesFile();
	}		
	private static void loadFeatureModel() {
		System.out.println("");
		sl.showFeatureModelNames();
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.println("Enter the number of system to load (c for cancel): ");
		String userCommand = reader.nextLine(); // Scans the next token of the input as an int.
		if (userCommand.equals("c")) {
		} else {
			// loading system
			int systemNumber=0;
			systemNumber=Integer.parseInt(userCommand);
			//sl.loadSystem(systemNumber);
		}
	}		
	private static void newFeatureModel() {
		System.out.println("");
		sl.resetSystem();
	}	
	private static void loadFromFMFile() {
		boolean isError=false;
		System.out.println("");
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.println("Enter path: ");		
		String userCommand = reader.nextLine(); // Scans the next token of the input as an int.
		boolean resul=false;
		if (userCommand.length()>0) {
			sl.thePath=userCommand;
			resul=sl.readFMFile(userCommand);
			if (resul) {
				resul=sl.populateFMfromFMFile(sl.fmFile);
			}
		}else {
			isError=true;
		}
	}
	private static void generateFMFile() {
		sl.generateFM();
	}	
	private static void validateFeatureModel() {
		sl.validateFM();
	}		
	private static void showMenu() {
		System.out.println("------------------------------------------");
		System.out.println("--- FEATURE MANAGER MODEL-DRIVEN MENU ----");
		System.out.println("------------------------------------------");
		System.out.println("21. New (n)");
		System.out.println("22. Load (l)");
		System.out.println("23. Save (s)");
		System.out.println("24. Save as (sa)");
		System.out.println("25. Generate .fm file (gfm)");
		System.out.println("26. Load from .fm file (lfm)");
		System.out.println("------------------------------------------");
		System.out.println("98. m. Show menu");
		System.out.println("99. e. Exit");
		System.out.println("------------------------------------------");
	}


	private static void showFeatureModel() {
		sl.showFeatureModelTextDefault();
	}	
	private static void showConstraints() {
		sl.showConstraints();
	}	
	private static void showFeatures() {
		sl.showFeatures();
	}	
	private static void showSupertypes(){
		sl.showSupertypes(sl.eTree);
	}
	private static void  showFeatureSupertypeRelations(){
		sl.showFeatureSupertypeRelations();
	}
	private static void testConfiguration() {
	}	
	private static void saveFeatureModel() {
		boolean isError=false;
		boolean resul=false;
		System.out.println("");
		if (sl.thePath.length()>0){
			sl.generateFMFile(1,sl.eTree,sl.fmFile);
			resul=sl.writeFMFile(sl.thePath);
		}else  {
			isError=true;
		}
 
	}	
	private static void saveFeatureModelAs() {
		boolean isError=false;
		boolean resul=false;
		System.out.println("");
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.println("Enter path: ("+sl.thePath+")");		
		String userCommand = reader.nextLine(); // Scans the next token of the input as an int.
		if ((userCommand.length()==0) && (sl.thePath.length()>0)){
			sl.generateFMFile(1,sl.eTree,sl.fmFile);
			resul=sl.writeFMFile(sl.thePath);
		} else if (userCommand.length()>0) {
			sl.thePath=userCommand;
			sl.generateFMFile(1,sl.eTree,sl.fmFile);
			resul=sl.writeFMFile(sl.thePath);
		}else  {
			isError=true;
		} 
	}	
	private static void showFeatureModelNames(){
		sl.showFeatureModelNames();
	}
	
	private static void processScript (String pathScript) {
		int resul=0;
		sl.processScript(pathScript);
		
	}
	private static void processConfig (String pathScript) {
		int resul=0;
		sl.processConfig(pathConfigFile);
		
	}	
}
