package es.urjc.ccia.ruva;

/*
 	Querido colega programador:

	Cuando escrib� este c�digo, s�lo Dios y yo
	sab�amos c�mo funcionaba.

	Ahora, �s�lo Dios lo sabe!

	As� que si est� tratando de 'optimizar'
	esta rutina y fracasa (seguramente),
	por favor, incremente el siguiente contador
	como una advertencia
	para el siguiente colega:

 	total_horas_perdidas_aqu�

*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import javax.print.attribute.IntegerSyntax;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

//import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;

import es.us.isa.FAMA.Reasoner.QuestionTrader;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.models.variabilityModel.VariabilityModel;


 class AbcSystemLibrary {
	//String connection database and declarations
	String myDriver = "com.mysql.jdbc.Driver";
	String myUrl = "jdbc:mysql://localhost:3306/features?autoReconnect=true&useSSL=false";
	int gMode=1;
	int gOut=0;
	String gFileout="";
	String gFeatureModel="";
	String gFMScript="";
	String gLogPathName="default_ruva.log";
	String theSegundos="";
	String theMilisegundos="";
	
	boolean gLogAccumulative=false;
	String gPathFMFile="";
	ArrayList<String> gLog=new ArrayList<String>();
	ArrayList<String> gLogResult=new ArrayList<String>();	
	ArrayList<String> scriptFileBase = new ArrayList<String>();
	
	
	Collection eTree = new ArrayList<TreeNode>();                       // the tree with feature model
	Collection eTreeWinner = new ArrayList<TreeNode>();                       // the tree with feature model
	Collection eConstraints = new ArrayList<Constraint>();              // list of constraints applied to feature model
	Collection eFeatures = new ArrayList<Feature>();                    // list of features available
	Collection eSupertypes = new ArrayList<Supertype>();                // list of supertypes available
	Collection eFeaturesSupertypes = new ArrayList<FeatureSupertype>(); // relation supertypes of each feature of the model
	Collection eAttributes = new ArrayList<String>();
	FeatureModel theFM=new FeatureModel();
	ArrayList<String> eComments=new ArrayList<>();
	String[] eSupertypesPriority;
	Misc utiles=new Misc();
	Stack nodeParents=new Stack();
	int nodeParentNow=0;
	 int contSubSet=0;
	 String lastXMLNode="";
	 String lastName="";
	 String scriptPath="";
	 String feaTesting="";
	 String feaParentTesting="";
	 int totalSuccess=0;
	 int totalFailures=0;
	
	

	double insertTimeStart = System.nanoTime();
	double insertTimeEnd = System.nanoTime();
	NumberFormat formatter = new DecimalFormat("#0.00");  

	double insertTimeRound = 0;
	double insertTimeRound2 = 0;
	
	static Document document; 
	protected static PrintWriter out;


	Hashtable config=new Hashtable();
	Connection conn;
	int nodeCounter=0;
	int featCounter=0;
	int systemId=0;
	int idParentNodeXML=0;
	int xmlMinCardin=0;
	int xmlMaxCardin=0;
	
	String systemName="";
	 int errCode=0;
	 String errMessage="";
	 boolean isError=false;
	 String fmPathFilename="";
	 int typeOfFMFile=0;  //1=ruva;2=fm;3=xml(FaMa);4=xml(FeatureIde);5=afm(FaMa)
	 ArrayList<String> fmFile = new ArrayList<String>();
	 int maxLevel=0;
	String thePath="";
	 String nameFM="";

	 void addFAuto(String featureName,String description,String relType,String relSiblings, String supertypes) {
		//EXAMPLE: ADD_FEATURE(Fx11,MANDATORY,AND,[ST3 ST4 ST2])
		Feature newFeature;
		newFeature=new Feature();
		newFeature.name=featureName;
		newFeature.description=description;
		
		if (supertypes.substring(0,1).contentEquals("[")) {
			supertypes=supertypes.substring(1,supertypes.length()-1);
		}
				
		ArrayList<String> theSupertypes=new ArrayList<String>();
		String[] arr=supertypes.split(" ");
		if (arr.length>0) {
			for (int i=0;i<arr.length;i++) { 
				theSupertypes.add(arr[i]);
			}
			newFeature.supertypes=theSupertypes;
			newFeature.relSiblings=relSiblings;
			newFeature.relType=relType;
		} else {

		}
		gLog.add(featureName+" - adding - |RT->"+relType+" |RS->"+relSiblings+" |ST->"+supertypes);
		double insertTime1 = System.nanoTime();
		double insertTime2 = System.nanoTime();
		double insertTimeEach = System.nanoTime();
		
		insertTime1 = System.nanoTime();
		addFeatureAuto(newFeature);
		insertTime2 = System.nanoTime();
		insertTimeEach = (insertTime2 - insertTime1) / 1000000000; //seconds
		//insertTime = (insertTimeEnd - insertTimeStart) / 1000000; //milisegundos
	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	    formatter = new DecimalFormat("#0.00");  
		//gLog.add("INSERTION TIME ("+newFeature.name+") "+ String.format("%.2f", insertTimeEach) + " second(s) "+dtf.format(LocalDateTime.now()));
		//gLog.add("");
		System.out.println("INSERTION TIME ("+newFeature.name+") "+  String.format("%.3f", insertTimeEach) + " seconds");
		//showFeatureModelTextDefault();
		
	}

	boolean addFeatureAuto(Feature theFeature) {
		//1     Determine places to set the feature taking sense of supertypes
		//2     Determining best solution
		//2.1   Populate & evaluate HitsTreeNode
		//2.2   Creating temporal eTree with new Feature
		//2.3   Generating FM from temporal eTree
		//2.4   check constraints invoking FaMa
		//2.5   Worked?
		//2.6.1 Yes, retrieve choice
		//2.6.2 No, check next choice

		boolean resul=false;
		Collection nodesToPut=new ArrayList<TreeNode>();
		boolean choiceSelected=false;
		String reasons="";

		if (theFeature.relType.contentEquals("MANDATORY")) {
			theFeature.relType ="M";
		} else {
			theFeature.relType="O";
		}

		//1.  Determinar donde se puede colocar la caracteristica
		//Input: caracter�stica y supertipos
		//Output: posibilidades (en forma de nodo y supertipos).

		nodesToPut=getNodesToPutFeature(theFeature);
		System.out.println("\nAvailable choices: "+nodesToPut.size());

		//2. Testing what choice is best. 
		// For this purpose, we iterate nodesToPut to get max match hits.
		//2.1 populate HitsTreeNode
		int max=0;
		Iterator itr = nodesToPut.iterator();
		ArrayList<HitsTreeNode> hitsTN=new ArrayList<HitsTreeNode>();

		if (nodesToPut.size()>0) {
			while (itr.hasNext()) {
				TreeNode elemTN=(TreeNode) itr.next();
				HitsTreeNode htn=new HitsTreeNode();
				htn.hits=elemTN.match;
				htn.treenode=elemTN;
				htn.totChildren=elemTN.totChildren;
				hitsTN.add(htn);
			}
		}
		//2.2 sort result by match (top one is best, bottom is worst)
		if (nodesToPut.size()>0) {
			nodesToPut=utiles.sortArrayListMatch(hitsTN);
		}


		/*
		 *  Si hay m�s de una opcion posible, entonces recorremos nodesToPut y en cuanto encontramos una soluci�n v�lida, paramos 
		 *  y salimos. Lo normal ser�a usar la primera soluci�n salvo que incumpla las reglas, y entonces se pasar�a a la segunda y as�.
		 *  
		 *  Si s�lo hay 1 soluci�nn, y esta adem�s es en el nodo ra�z, insertamos ah�.
		 *  
		 */
 

		Iterator itr2 = nodesToPut.iterator();
		int numIter=0;
		while ((resul==false) && itr2.hasNext()) {
			numIter++;
			boolean isSolution=false;
			
			TreeNode nodeToCheck=new TreeNode();
			nodeToCheck=(TreeNode) itr2.next();
			String reason="";
			Collection eTree2 = new ArrayList<TreeNode>(eTree); 
			
			if (nodeToCheck.idNode==1) { // caso 4.2.1. No hay supertipos compatibles

				TreeNode newNode=new TreeNode();
				newNode.idNode=eTree2.size()+1;
				newNode.idParentNode=nodeToCheck.idNode;
				newNode.nameFeature=theFeature.name;
				newNode.alias=theFeature.name;
				newNode.nameNode=theFeature.name;
				newNode.relSiblings="AND";
				newNode.relType=theFeature.relType;
				newNode.type="BASIC";
				newNode.feature=theFeature;
				newNode.level=nodeToCheck.level+1;
				newNode.feature.supertypes=theFeature.supertypes; 
				eTree2.add(newNode);
				feaParentTesting=nodeToCheck.nameNode;
				isSolution=true;
				
			} else {
			
			
				// buscamos los nodos hijos del nodo del que va a colgar la nueva caracter�stica
				RelationTree theSiblings=getDescendants(eTree, nodeToCheck.idNode);
	
				//3 testing solution
				//3.1 adding feature to experimental eTree
				System.out.println("Buscamos las relaciones que tiene el �rbol en cuesti�n");
				int res=0;
	
				
				/*
				 * Ahora hay que buscar los hijos del nodo en el que se va a colgar la nueva feature. Hay que comparar las relaciones
				 * que haya (OR y XOR tendr�n una feature auxiliar, el AND no). Por lo que hay que utilizar una funci�n que nos diga 
				 * cu�ntas relaciones y cu�les tiene el nodo y conocido esto, decidir CUAL ser� el nodo padre de la nueva caracter�stica.
				 * 
				 * Para ello se usar� el array theSiblings.
				 * 
				 * Hay 2 opciones:
				 * 
				 *  1. Que cuelgue directamente del nodo padre.
				 *  2. Que cuelgue del nodo auxiliar (OR y XOR)
				 *  
				 *  En el caso 2 hay que obtener el node-ID de ese auxfeat y usarlo para rellenar el parentnodeID de la nueva caracter�stica.
				 *  
				 */
				
				
				/* ----------------------------- */
				int nodeFoundId=0;
				
				if (theSiblings.siblingsTotal==0) {
					TreeNode newNode=new TreeNode();
					newNode.idNode=eTree2.size()+1;
					newNode.idParentNode=nodeToCheck.idNode;
					newNode.nameFeature=theFeature.name;
					newNode.alias=theFeature.name;
					newNode.nameNode=theFeature.name;
					newNode.relSiblings="AND";
					newNode.relType=theFeature.relType;
					newNode.type="BASIC";
					newNode.feature=theFeature;
					newNode.level=nodeToCheck.level+1;
					newNode.feature.supertypes=theFeature.supertypes; 
					eTree2.add(newNode);
					feaParentTesting=nodeToCheck.nameNode;
					isSolution=true;
				} else if ((theSiblings.siblingsTotal==1) ) {  // CASO 4.4.1
					if ((theFeature.relSiblings.contentEquals("OR")) || (theFeature.relSiblings.contentEquals("XOR"))) {
	
						// 1. se crea un nodo extra para el SET
	
						TreeNode newNode=new TreeNode();
						newNode.idNode=eTree2.size()+1;
	//					newNode.idParentNode=getParentNodeId(theSiblings.idNode,eTree);
						newNode.idParentNode=theSiblings.idNode;					
						feaParentTesting=getNameNode(newNode.idParentNode,eTree);
	
						int stor=0;
						stor=newNode.idNode;
						newNode.totChildren=2;
						newNode.type="SET";
						newNode.nameNode="TO-"+nodeToCheck.nameFeature+"_"+theFeature.name+"-SET";
						newNode.nameFeature=newNode.nameNode;
						newNode.minCardinality=1;
						
						if (theFeature.relSiblings.contentEquals("OR")) {
							newNode.maxCardinality=2;
							newNode.relSiblings="OR";
						} else if (theFeature.relSiblings.contentEquals("XOR")){ 
							newNode.maxCardinality=1;
							newNode.relSiblings="XOR";
						}else {
							System.out.println("Ha pasado algo imprevisto");
						}
						
						newNode.level=nodeToCheck.level;
						newNode.parentName=nodeToCheck.nameNode;
						newNode.feature=new Feature();
						newNode.feature.name=newNode.nameNode;
						newNode.feature.relSiblings=newNode.relSiblings;
						newNode.feature.relType=newNode.relType;
						newNode.feature.type=newNode.type;
						newNode.feature.supertypes=new ArrayList<String>();
						eTree2.add(newNode);
						
						// 2. se cuelga de aqu� el nodo hijo anterior, para ello cambiando el idParent
						int idExistentNode=0;
						boolean nodeFound=false;
						Iterator itr3 = eTree2.iterator();
						while (!nodeFound && itr3.hasNext()) {
							TreeNode theTreeNode2=(TreeNode) itr3.next();
							if (theTreeNode2.idParentNode==theSiblings.idNode) {
								theTreeNode2.idParentNode=stor;
								nodeFound=true;
							}
						}				
						
						
						// 3. se mete el nodo nuevo
						TreeNode newNode3=new TreeNode();
						newNode3.idNode=eTree2.size()+1;
						newNode3.idParentNode=stor;
						newNode3.nameFeature=theFeature.name;
						newNode3.alias=theFeature.name;
						newNode3.nameNode=theFeature.name;
						newNode3.relSiblings=newNode.relSiblings;
						newNode3.relType=theFeature.relType;
						newNode3.type="BASIC";
						newNode3.feature=theFeature;
						newNode3.level=nodeToCheck.level+1;
						newNode3.feature.supertypes=theFeature.supertypes; 
						eTree2.add(newNode3);	
						isSolution=true;
						
						
						// 
					} else { // si  no es OR ni XOR, es un AND
						TreeNode newNode=new TreeNode();
						newNode.idNode=eTree2.size()+1;
						newNode.idParentNode=nodeToCheck.idNode;
						feaParentTesting=nodeToCheck.nameNode;
						
						newNode.nameFeature=theFeature.name;
						newNode.alias=theFeature.name;
						newNode.nameNode=theFeature.name;
						newNode.relSiblings="AND";
						newNode.relType=theFeature.relType;
						newNode.type="BASIC";
						newNode.feature=theFeature;
						newNode.level=nodeToCheck.level+1;
						newNode.feature.supertypes=theFeature.supertypes; 
						eTree2.add(newNode);
						isSolution=true;
						
					}
				}  else if ((theSiblings.siblingsTotal>1) ) { 
					
					if ((theSiblings.rOR==0) && (theSiblings.rXOR==0)  && (theSiblings.rAND>0)) {  // CASO 4.4.2
						if (theFeature.relSiblings.contentEquals("AND")||theFeature.relSiblings.contentEquals("")) {
							TreeNode newNode=new TreeNode();
							newNode.idNode=eTree2.size()+1;
							newNode.idParentNode=nodeToCheck.idNode;
							newNode.nameFeature=theFeature.name;
							newNode.alias=theFeature.name;
							newNode.nameNode=theFeature.name;
							newNode.relSiblings="AND";
							newNode.relType=theFeature.relType;
							newNode.type="BASIC";
							newNode.feature=theFeature;
							newNode.level=nodeToCheck.level+1;
							newNode.feature.supertypes=theFeature.supertypes; 
							eTree2.add(newNode);
							feaParentTesting=nodeToCheck.nameNode;
							isSolution=true;
							
						} else {
							System.out.println("SE DESCARTA");
							reasons+="node "+ nodeToCheck.nameNode +" is AND, siblings>0, new-feature OR/XOR, must be AND or BLANK to match |";
						}
					} else if ((theSiblings.rOR>0) && (theSiblings.rXOR==0) && (theSiblings.rAND==0)) { //CASO 4.4.3.1
						if (theFeature.relSiblings.contentEquals("OR")) {// nos quedamos con el id del nodo para ponerlo como padre del newNode
							//buscamos el primer nodo que sea OR en la relaci�n.
							Iterator itr3 = theSiblings.eNodes.iterator();
							ArrayList<RelationNode> siblingNode=new ArrayList<RelationNode>();
							while ((nodeFoundId==0) && itr3.hasNext()) {
								RelationNode relNode=new RelationNode();
								relNode=(RelationNode) itr3.next();
								if (relNode.relSiblings.contentEquals("OR")) {
									nodeFoundId=relNode.idNode;
								}
							}
							if (nodeFoundId>0) {
								// una vez encontrado se lo ponemos a parentId
								TreeNode newNode=new TreeNode();
								newNode.idNode=eTree2.size()+1;
								newNode.idParentNode=nodeFoundId;
								newNode.nameFeature=theFeature.name;
								newNode.alias=theFeature.name;
								newNode.nameNode=theFeature.name;
								newNode.relSiblings="OR";
								newNode.relType=theFeature.relType;
								newNode.type="BASIC";
								newNode.feature=theFeature;
								newNode.level=nodeToCheck.level+1;
								newNode.feature.supertypes=theFeature.supertypes;
								feaParentTesting=getNameNode(nodeFoundId,eTree2);
								eTree2.add(newNode);	
								isSolution=true;
								
								
							}else {
								System.out.println("no se ha encontrado nodeFoundId. Algo mal");
							}
						} else if (theFeature.relSiblings.contentEquals("AND"))  {
							TreeNode newNode=new TreeNode();
							newNode.idNode=eTree2.size()+1;
							newNode.idParentNode=nodeToCheck.idNode;
							newNode.nameFeature=theFeature.name;
							newNode.alias=theFeature.name;
							newNode.nameNode=theFeature.name;
							newNode.relSiblings="AND";
							newNode.relType=theFeature.relType;
							newNode.type="BASIC";
							newNode.feature=theFeature;
							newNode.level=nodeToCheck.level+1;
							newNode.feature.supertypes=theFeature.supertypes; 
							eTree2.add(newNode);
							feaParentTesting=nodeToCheck.nameNode;
							isSolution=true;						
						} else {
							System.out.println("�qu� hacemos?");
						}
					} else if ((theSiblings.rOR==0) && (theSiblings.rXOR>0) && (theSiblings.rAND==0)) { //CASO 4.4.3.2
						if (theFeature.relSiblings.contentEquals("XOR")) {// nos quedamos con el id del nodo para ponerlo como padre del newNode
							//buscamos el primer nodo que sea OR en la relaci�n.
							Iterator itr3 = theSiblings.eNodes.iterator();
							ArrayList<RelationNode> siblingNode=new ArrayList<RelationNode>();
							while ((nodeFoundId==0) && itr3.hasNext()) {
								RelationNode relNode=new RelationNode();
								relNode=(RelationNode) itr3.next();
								if (relNode.relSiblings.contentEquals("XOR")) {
									nodeFoundId=relNode.idNode;
								}
							}
							if (nodeFoundId>0) {
								// una vez encontrado se lo ponemos a parentId
								TreeNode newNode=new TreeNode();
								newNode.idNode=eTree2.size()+1;
								newNode.idParentNode=nodeFoundId;
								newNode.nameFeature=theFeature.name;
								newNode.alias=theFeature.name;
								newNode.nameNode=theFeature.name;
								newNode.relSiblings="XOR";
								newNode.relType=theFeature.relType;
								newNode.type="BASIC";
								newNode.feature=theFeature;
								newNode.level=nodeToCheck.level+1;
								newNode.feature.supertypes=theFeature.supertypes;
								feaParentTesting=getNameNode(nodeFoundId,eTree2);
								eTree2.add(newNode);	
								isSolution=true;
								
							}else {
								System.out.println("no se ha encontrado nodeFoundId. Algo mal");
								System.exit(13);
							}
						} else if (theFeature.relSiblings.contentEquals("AND"))  {
							TreeNode newNode=new TreeNode();
							newNode.idNode=eTree2.size()+1;
							newNode.idParentNode=nodeToCheck.idNode;
							newNode.nameFeature=theFeature.name;
							newNode.alias=theFeature.name;
							newNode.nameNode=theFeature.name;
							newNode.relSiblings="AND";
							newNode.relType=theFeature.relType;
							newNode.type="BASIC";
							newNode.feature=theFeature;
							newNode.level=nodeToCheck.level+1;
							newNode.feature.supertypes=theFeature.supertypes; 
							eTree2.add(newNode);
							feaParentTesting=nodeToCheck.nameNode;
							isSolution=true;							
						} else {
							System.out.println("�qu� hacemos?");
						}
					} else if ((theSiblings.rAND>0) && (theSiblings.rOR>0) && (theSiblings.rXOR==0)) { //CASO 4.4.4
						if (theFeature.relSiblings.contentEquals("AND")) {// nos quedamos con el id del nodo para ponerlo como padre del newNode
							TreeNode newNode=new TreeNode();
							newNode.idNode=eTree2.size()+1;
							newNode.idParentNode=nodeToCheck.idNode;
							newNode.nameFeature=theFeature.name;
							newNode.alias=theFeature.name;
							newNode.nameNode=theFeature.name;
							newNode.relSiblings="AND";
							newNode.relType=theFeature.relType;
							newNode.type="BASIC";
							newNode.feature=theFeature;
							newNode.level=nodeToCheck.level+1;
							newNode.feature.supertypes=theFeature.supertypes; 
							feaParentTesting=nodeToCheck.nameNode;
							eTree2.add(newNode);	
							isSolution=true;
							
						} else if (theFeature.relSiblings.contentEquals("OR")) {// nos quedamos con el id del nodo para ponerlo como padre del newNode
							//buscamos el primer nodo que sea OR en la relaci�n.
							Iterator itr3 = theSiblings.eNodes.iterator();
							ArrayList<RelationNode> siblingNode=new ArrayList<RelationNode>();
							while ((nodeFoundId==0) && itr3.hasNext()) {
								RelationNode relNode=new RelationNode();
								relNode=(RelationNode) itr3.next();
								if (relNode.relSiblings.contentEquals("OR")) {
									nodeFoundId=relNode.idNode;
								}
							}
							if (nodeFoundId>0) {
								// una vez encontrado se lo ponemos a parentId
								TreeNode newNode=new TreeNode();
								newNode.idNode=eTree2.size()+1;
								newNode.idParentNode=nodeFoundId;
								newNode.nameFeature=theFeature.name;
								newNode.alias=theFeature.name;
								newNode.nameNode=theFeature.name;
								newNode.relSiblings="OR";
								newNode.relType=theFeature.relType;
								newNode.type="BASIC";
								newNode.feature=theFeature;
								newNode.level=nodeToCheck.level+1;
								newNode.feature.supertypes=theFeature.supertypes; 
								feaParentTesting=getNameNode(nodeFoundId,eTree2);
								eTree2.add(newNode);	
								isSolution=true;
								
							}else {
								System.out.println("no se ha encontrado nodeFoundId. Algo mal");
								System.exit(14);
							}
						} else {
							System.out.println("DESCARTAMOS");
						}					
					} else if ((theSiblings.rAND>0) && (theSiblings.rOR==0) && (theSiblings.rXOR>0)) { //CASO 4.4.4 AND & XOR
						if (theFeature.relSiblings.contentEquals("AND")) {// nos quedamos con el id del nodo para ponerlo como padre del newNode
							TreeNode newNode=new TreeNode();
							newNode.idNode=eTree2.size()+1;
							newNode.idParentNode=nodeToCheck.idNode;
							newNode.nameFeature=theFeature.name;
							newNode.alias=theFeature.name;
							newNode.nameNode=theFeature.name;
							newNode.relSiblings="AND";
							newNode.relType=theFeature.relType;
							newNode.type="BASIC";
							newNode.feature=theFeature;
							newNode.level=nodeToCheck.level+1;
							newNode.feature.supertypes=theFeature.supertypes; 
							feaParentTesting=nodeToCheck.nameNode;
							eTree2.add(newNode);	
							isSolution=true;
							
						} else if (theFeature.relSiblings.contentEquals("XOR")) {// nos quedamos con el id del nodo para ponerlo como padre del newNode
							//buscamos el primer nodo que sea OR en la relaci�n.
							Iterator itr3 = theSiblings.eNodes.iterator();
							ArrayList<RelationNode> siblingNode=new ArrayList<RelationNode>();
							while ((nodeFoundId==0) && itr3.hasNext()) {
								RelationNode relNode=new RelationNode();
								relNode=(RelationNode) itr3.next();
								if (relNode.relSiblings.contentEquals("XOR")) {
									nodeFoundId=relNode.idNode;
								}
							}
							if (nodeFoundId>0) {
								// una vez encontrado se lo ponemos a parentId
								TreeNode newNode=new TreeNode();
								newNode.idNode=eTree2.size()+1;
								newNode.idParentNode=nodeFoundId;
								newNode.nameFeature=theFeature.name;
								newNode.alias=theFeature.name;
								newNode.nameNode=theFeature.name;
								newNode.relSiblings="XOR";
								newNode.relType=theFeature.relType;
								newNode.type="BASIC";
								newNode.feature=theFeature;
								newNode.level=nodeToCheck.level+1;
								newNode.feature.supertypes=theFeature.supertypes;
								feaParentTesting=getNameNode(nodeFoundId,eTree2);
								eTree2.add(newNode);
								isSolution=true;
								
							}else {
								System.out.println("no se ha encontrado nodeFoundId. Algo mal");
							}
						} else {
							System.out.println("DESCARTAMOS");
							reasons+="node "+ nodeToCheck.nameNode +" has AND+XOR relations, siblings>0, new-feature type found is "+theFeature.relType+", must be AND or XOR  to match |";
						}					
					} else if ((theSiblings.rAND==0) && (theSiblings.rOR>0) && (theSiblings.rXOR>0)) { //CASO 4.4.4 - OR & XOR 
						if (theFeature.relSiblings.contentEquals("XOR")) { //CASO 4.4.4 - OR & XOR - XOR
							//buscamos el primer nodo que sea OR en la relaci�n.
							Iterator itr3 = theSiblings.eNodes.iterator();
							ArrayList<RelationNode> siblingNode=new ArrayList<RelationNode>();
							while ((nodeFoundId==0) && itr3.hasNext()) {
								RelationNode relNode=new RelationNode();
								relNode=(RelationNode) itr3.next();
								if (relNode.relSiblings.contentEquals("XOR")) {
									nodeFoundId=relNode.idNode;
								}
							}
							if (nodeFoundId>0) {
								// una vez encontrado se lo ponemos a parentId
								TreeNode newNode=new TreeNode();
								newNode.idNode=eTree2.size()+1;
								newNode.idParentNode=nodeFoundId;
								newNode.nameFeature=theFeature.name;
								newNode.alias=theFeature.name;
								newNode.nameNode=theFeature.name;
								newNode.relSiblings="XOR";
								newNode.relType=theFeature.relType;
								newNode.type="BASIC";
								newNode.feature=theFeature;
								newNode.level=nodeToCheck.level+1;
								newNode.feature.supertypes=theFeature.supertypes; 
								feaParentTesting=getNameNode(nodeFoundId,eTree2);
								
								eTree2.add(newNode);	
								isSolution=true;
								
							}else {
								System.out.println("no se ha encontrado nodeFoundId. Algo mal");
							}	
						} else if (theFeature.relSiblings.contentEquals("OR")) {  //CASO 4.4.4 - OR & XOR - OR
							//buscamos el primer nodo que sea OR en la relaci�n.
							Iterator itr3 = theSiblings.eNodes.iterator();
							ArrayList<RelationNode> siblingNode=new ArrayList<RelationNode>();
							while ((nodeFoundId==0) && itr3.hasNext()) {
								RelationNode relNode=new RelationNode();
								relNode=(RelationNode) itr3.next();
								if (relNode.relSiblings.contentEquals("OR") ) {
									nodeFoundId=relNode.idNode;
								}
							}
							if (nodeFoundId>0) {
								// una vez encontrado se lo ponemos a parentId
								TreeNode newNode=new TreeNode();
								newNode.idNode=eTree2.size()+1;
								newNode.idParentNode=nodeFoundId;
								newNode.nameFeature=theFeature.name;
								newNode.alias=theFeature.name;
								newNode.nameNode=theFeature.name;
								newNode.relSiblings="OR";
								newNode.relType=theFeature.relType;
								newNode.type="BASIC";
								newNode.feature=theFeature;
								newNode.level=nodeToCheck.level+2;
								newNode.feature.supertypes=theFeature.supertypes; 
								feaParentTesting=getNameNode(nodeFoundId,eTree2);
								
								eTree2.add(newNode);	
								isSolution=true;
							}else {
								System.out.println("no se ha encontrado nodeFoundId. Algo mal");
							}
						} else if (theFeature.relSiblings.contentEquals("AND")) { // 4.4.4 - OR & XOR - AND
							TreeNode newNode=new TreeNode();
							newNode.idNode=eTree2.size()+1;
							newNode.idParentNode=nodeToCheck.idNode;
							newNode.nameFeature=theFeature.name;
							newNode.alias=theFeature.name;
							newNode.nameNode=theFeature.name;
							newNode.relSiblings="AND";
							newNode.relType=theFeature.relType;
							newNode.type="BASIC";
							newNode.feature=theFeature;
							newNode.level=nodeToCheck.level+1;
							newNode.feature.supertypes=theFeature.supertypes; 
							feaParentTesting=nodeToCheck.nameNode;
							eTree2.add(newNode);	
							isSolution=true;						
						} else {
							System.out.println("DESCARTAMOS");
							reasons+="node "+ nodeToCheck.nameNode +" has OR+XOR relations, siblings>0, new-feature type found is "+theFeature.relType+", must be AND, OR or XOR  to match |";
						}					
					} else if ((theSiblings.rAND>0) &&(theSiblings.rOR>0) && (theSiblings.rXOR>0)) { //CASO 4.4.4 - AND & OR
						if (theFeature.relSiblings.contentEquals("AND")) {// nos quedamos con el id del nodo para ponerlo como padre del newNode
							TreeNode newNode=new TreeNode();
							newNode.idNode=eTree2.size()+1;
							newNode.idParentNode=nodeToCheck.idNode;
							newNode.nameFeature=theFeature.name;
							newNode.alias=theFeature.name;
							newNode.nameNode=theFeature.name;
							newNode.relSiblings="AND";
							newNode.relType=theFeature.relType;
							newNode.type="BASIC";
							newNode.feature=theFeature;
							newNode.level=nodeToCheck.level+1;
							newNode.feature.supertypes=theFeature.supertypes; 
							feaParentTesting=nodeToCheck.nameNode;
							
							eTree2.add(newNode);
							isSolution=true;
							
						} else if (theFeature.relSiblings.contentEquals("XOR")) {// nos quedamos con el id del nodo para ponerlo como padre del newNode
							//buscamos el primer nodo que sea OR en la relaci�n.
							Iterator itr3 = theSiblings.eNodes.iterator();
							ArrayList<RelationNode> siblingNode=new ArrayList<RelationNode>();
							while ((nodeFoundId==0) && itr3.hasNext()) {
								RelationNode relNode=new RelationNode();
								relNode=(RelationNode) itr3.next();
								if (relNode.relSiblings.contentEquals("XOR")) {
									nodeFoundId=relNode.idNode;
								}
							}
							if (nodeFoundId>0) {
								// una vez encontrado se lo ponemos a parentId
								TreeNode newNode=new TreeNode();
								newNode.idNode=eTree2.size()+1;
								newNode.idParentNode=nodeFoundId;
								newNode.nameFeature=theFeature.name;
								newNode.alias=theFeature.name;
								newNode.nameNode=theFeature.name;
								newNode.relSiblings="XOR";
								newNode.relType=theFeature.relType;
								newNode.type="BASIC";
								newNode.feature=theFeature;
								newNode.level=nodeToCheck.level+1;
								newNode.feature.supertypes=theFeature.supertypes;
								feaParentTesting=getNameNode(nodeFoundId,eTree2);
								
								eTree2.add(newNode);	
								isSolution=true;
								
							}else {
								System.out.println("no se ha encontrado nodeFoundId. Algo mal");
							}	
						} else if (theFeature.relSiblings.contentEquals("OR")) {// nos quedamos con el id del nodo para ponerlo como padre del newNode
							//buscamos el primer nodo que sea OR en la relaci�n.
							Iterator itr3 = theSiblings.eNodes.iterator();
							ArrayList<RelationNode> siblingNode=new ArrayList<RelationNode>();
							while ((nodeFoundId==0) && itr3.hasNext()) {
								RelationNode relNode=new RelationNode();
								relNode=(RelationNode) itr3.next();
								if (relNode.relSiblings.contentEquals("OR")) {
									nodeFoundId=relNode.idNode;
								}
							}
							if (nodeFoundId>0) {
								// una vez encontrado se lo ponemos a parentId
								TreeNode newNode=new TreeNode();
								newNode.idNode=eTree2.size()+1;
								newNode.idParentNode=nodeFoundId;
								newNode.nameFeature=theFeature.name;
								newNode.alias=theFeature.name;
								newNode.nameNode=theFeature.name;
								newNode.relSiblings="OR";
								newNode.relType=theFeature.relType;
								newNode.type="BASIC";
								newNode.feature=theFeature;
								newNode.level=nodeToCheck.level+1;
								newNode.feature.supertypes=theFeature.supertypes; 
								feaParentTesting=getNameNode(nodeFoundId,eTree2);
								
								eTree2.add(newNode);		
								isSolution=true;
								
							}else {
								System.out.println("no se ha encontrado nodeFoundId. Algo mal");
								System.exit(15);
							}
						} else {
							System.out.println("DESCARTAMOS");
						}						
							
					} else {
						System.out.println("Caso no contemplado");
		
					}
					/* ----------------------------- */
				}
			} 
			
			if (isSolution) {
				//showFeatureModelText(eTree2);			
				//4 Testing if choices are compatible with constraints of tree
				resul=false;
				System.out.println("Start of evaluation of feature model. Please wait ...");
				// 4.1. generate .fm (aux.fm)
				generateFMFile(0,eTree2,fmFile);
				writeFMFile("fm/famatemp.fm");
				// 4.2. Invoke FAMA with .fm generated
				QuestionTrader qt = new QuestionTrader();
				VariabilityModel fm = qt.openFile("fm/famatemp.fm");
				qt.setVariabilityModel(fm);
				
	//			
	
				// 4.3. Check consistency
				try { 
					ValidQuestion vq = (ValidQuestion) qt.createQuestion("Valid");
					qt.ask(vq);
	
					// 4.4 retrieve result
					System.out.println("End of evaluation of feature model.");
					if (vq.isValid()) {
						System.out.println("Feature model consistency OK. All constraints are satisfied.");
						choiceSelected=true;
						System.out.println("Feature added succesfully!");
						System.out.println("Winner Solution:"+numIter);
						
						eTree=eTree2; 
						resul=true;
						System.out.println("Returning to menu");
					} else {
						resul=false;
						System.out.println("Feature model inconsistency. Constraints not satisfied.");
					}
				} catch (Exception e) {
					System.out.println("Feature model not valid. Check relationships, features and cardinality.");
					e.printStackTrace();
					resul=false;
				}
			}else {
				System.out.println("SOLUCION DESCARTADA "+numIter+" "+nodeToCheck.nameNode);
				resul=false;
			}
		} 
		if (resul) {
			totalSuccess++;
			gLog.add(theFeature.name+" - adding - 1 SUCCESS - Feature "+theFeature.name+" inserted under "+feaParentTesting+" - Solution:"+numIter+"/"+nodesToPut.size());
		}else {
//			System.out.println("NINGUNA SOLUCION VIABLE");
			totalFailures++;
			gLog.add(theFeature.name+" - adding - 0 FAILURE - Feature "+theFeature.name + " | Reasons: "+ reasons);
		}


		return resul;
	}
	 void addTreenode(TreeNode nodeToInsert) {
		// add a feature to a node increasing 1 to last idNode

		//search max node id
		int maxId=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode theTreeNode=(TreeNode) itr.next();
			if (theTreeNode.idNode>maxId) {
				maxId=theTreeNode.idNode;
			}
		}				
		maxId++;
		nodeToInsert.idNode=maxId;

		//adding node
		eTree.add(nodeToInsert);
	}

	 int calculateLevel() {
		// Calculate max depth level of a tree. 
		int maxLevel=0;
		int levelFound=0;
		Iterator<TreeNode> it = eTree.iterator();

		boolean hayMas=false;
		hayMas=it.hasNext();
		while (hayMas) {
			TreeNode theTreeNode=it.next();
			levelFound=theTreeNode.level;
			if (levelFound>maxLevel) {
				maxLevel=levelFound;
			}
			hayMas=it.hasNext();

		}	
		this.maxLevel=maxLevel;
		System.out.println("Level: "+maxLevel);
		return maxLevel;
	}
	boolean csv2xmlfi(String source, String target) {
		// convert from csv to xml FeatureIde Format
		//1. declarations
		boolean resul=true;
		ArrayList<String> textCSV=new ArrayList<>();
		ArrayList<String> textXML=new ArrayList<>();
		ArrayList<String> textRules=new ArrayList<>();

		//2. read file
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(source));
			String line = reader.readLine();
			line=line.trim();
			while (line != null) {
				//System.out.println(line);
				if (line.length()>0) {
					textCSV.add(line);
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			resul=false;
		}	
		
		String myField="";
		String myParent="";
		String myLogicalRelType="";
		String myRelationshipType="";
		String myRelConIn="";
		String myProperty="";
		String myMultiplicity="";
		String myValue="";
		String[] myRelConInChunks;	
		String rela="";
		String manda="";
		String frase="";
		Stack pilaRel=new Stack();		
		Stack pilaPadres=new Stack();
		
		String myFieldPrev="";
		String myParentPrev="";
		String myLogicalRelTypePrev="";
		String myRelationshipTypePrev="";
		String myRelConInPrev="";
		String myPropertyPrev="";
		String myMultiplicityPrev="";
		String myValuePrev="";
		String[] myRelConInChunksPrev;		
		int tabulation=0;
		String topePila="";
		String value="";
		
		pilaRel.push("NADA");
		pilaPadres.push("NADA");
		
		
		//3. converting
		textXML.add("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		textXML.add("<featureModel>");
		textXML.add("	<properties>");
		textXML.add("		<graphics key=\"showhiddenfeatures\" value=\"true\"/>");
		textXML.add("		<graphics key=\"legendposition\" value=\"1217,115\"/>");
		textXML.add("		<graphics key=\"legendautolayout\" value=\"false\"/>");
		textXML.add("		<graphics key=\"showshortnames\" value=\"false\"/>");
		textXML.add("		<graphics key=\"layout\" value=\"horizontal\"/>");
		textXML.add("		<graphics key=\"showcollapsedconstraints\" value=\"true\"/>");
		textXML.add("		<graphics key=\"legendhidden\" value=\"false\"/>");
		textXML.add("		<graphics key=\"layoutalgorithm\" value=\"1\"/>");
		textXML.add("	</properties>");
		textXML.add("<struct>");	
		tabulation++;

		textRules.add("<constraints>");
		
		boolean isEnd=false;
		int lineCounter=0;
		String line="";
		int x=0;
		String repeatedLine="";
		boolean myf=false;
		boolean isOntology=false;
		
		while (!isEnd) {
			boolean isLine=false;
		
			while ((!isEnd) && (!isLine)) {
				line=textCSV.get(x).trim();
				lineCounter++;
				System.out.println("Linea "+lineCounter+":"+line);
	
				if ((line.length()>0) && (lineCounter>1)) {
					isLine=true;
				}
				x++;
				if (x==textCSV.size()) {
					isEnd=true;
				}
			}
			line=line+"EOF";
			if (isLine) {
				// descomponemos linea
				rela="";
				manda="";
				String[] lineChunks=line.split(";");
				if (lineChunks.length==12) {
					lineChunks[11]=lineChunks[11].substring(0,lineChunks[11].length()-3);
					myField=lineChunks[0].trim();
					myParent=lineChunks[1].trim();
					myLogicalRelType=lineChunks[3].trim().toLowerCase();
					myRelationshipType=lineChunks[2].trim().toLowerCase();
					myRelConIn=lineChunks[4].trim();
					myProperty=lineChunks[5].trim().toLowerCase();
					myMultiplicity=lineChunks[6].trim().toLowerCase();
					myValue=lineChunks[7].trim().toLowerCase();
					if (myRelConIn.length()>0) {
						myRelConInChunks=myRelConIn.split(",");
					}
					isOntology=false;
					if ((myRelationshipType.contains("consistsof")) || (myRelationshipType.contains("consitsof"))) {
						isOntology=true;
					}
					if ((myParent.length()==0) && (myRelationshipType.length()==0) && (myLogicalRelType.length()==0)) {
						isOntology=true;
					}
					
					if (!isOntology) {
						topePila=(String) pilaPadres.peek();
						while (!myParent.contentEquals(topePila) && pilaPadres.size()>1) {
							String relac="";
							pilaPadres.pop();
							relac=(String) pilaRel.pop();
							frase="</"+relac+">";
							tabulation--;
							repeatedLine=new String(new char[tabulation]).replace("\0", " ");						
							textXML.add(repeatedLine+frase);
							topePila=(String) pilaPadres.peek();
		 				
						}	
						if (myLogicalRelType.length()>0) {
							if (myLogicalRelType.contentEquals("and")) {
								rela="and";
							} else if (myLogicalRelType.contentEquals("or")) {
								rela="or";
							} else if (myLogicalRelType.contentEquals("xor")) {
								rela="alt";
							} else if (myRelationshipType.contentEquals("consistsof")) {
								rela="cons";
							} else {
								System.out.println("ERRORRR");
							}
							pilaPadres.add(myField);
							pilaRel.add(rela);
							
							if (myProperty.contentEquals("mandatory")) {
								manda=" mandatory=\"true\" ";
							} else if (myProperty.contentEquals("optional")) {
								manda="";
							}
							if (myValue.length()>0) {
								value=" value=\""+myValue+"\" ";
							} else  {
								value="";
							}
							frase="<"+rela+manda+" name=\""+myField+"\" "+value+">";
							repeatedLine=new String(new char[tabulation]).replace("\0", " ");						
							textXML.add(repeatedLine+frase);
							tabulation++;					
						}else {
							if (myProperty.contentEquals("mandatory")) {
								manda=" mandatory=\"true\" ";
							} else if (myProperty.contentEquals("optional")) {
								manda="";
							} else {
								manda=" mandatory=\"true\" ";
							}
							
							if (myValue.length()>0) {
								value=" value=\""+myValue+"\" ";
							} else  {
								value="";
							}
		
							frase="<feature "+manda+"name=\""+myField+"\""+value+"/>";
							repeatedLine=new String(new char[tabulation]).replace("\0", " ");						
							textXML.add(repeatedLine+frase);
						}
									
					
		
						
						if (myRelationshipType.length()>0) {
							if (myRelationshipType.contentEquals("consistsof")) {
								
		 
								
							}else if (myRelationshipType.contentEquals("requires")) {
								textRules.add("<rule>");
								textRules.add("  <imp>");
								textRules.add("    <var>"+myField+"</var>");
								textRules.add("    <var>"+myRelConIn+"</var>");
								textRules.add("  </imp>");
								textRules.add("</rule>");						
							}else if (myRelationshipType.contentEquals("excludes")) {
								textRules.add("<rule>");
								textRules.add("  <not>");
								textRules.add("    <var>"+myField+"</var>");
								textRules.add("    <var>"+myRelConIn+"</var>");
								textRules.add("  </not>");
								textRules.add("</rule>");													
							} else { 
							
							}
						
						} 	
	 
						myFieldPrev=myField;
						myParentPrev=myParent;
						myRelationshipTypePrev=myRelationshipType;
						myLogicalRelTypePrev=myLogicalRelType;
						myRelConInPrev=myRelConIn;
						myPropertyPrev=myProperty;
						myMultiplicityPrev=myMultiplicity;
						myValuePrev=myValue;
					}
				}		
			}
		}
		while (pilaRel.size()>1) {
			String relac="";
			pilaPadres.pop();
			relac=(String) pilaRel.pop();
			frase="</"+relac+">";
			tabulation--;
			repeatedLine=new String(new char[tabulation]).replace("\0", " ");						
			textXML.add(repeatedLine+frase);			
		}
		
		textRules.add("</constraints>");
			
		textXML.add("</struct>");
		
		if (textRules.size()>2) {
			for (int i = 0; i < textRules.size(); i++){
				textXML.add(textRules.get(i));
			}
		}
		textXML.add("</featureModel>");			
 
		//4. writeing
		writeFile(target,textXML);
		return resul;		
	}

	
	boolean fm2xmlfi(String source, String target) {
		// convert from fm to xml FeatureIde Format
		//1. declarations
		boolean resul=true;
		ArrayList<String> textCSV=new ArrayList<>();
		ArrayList<String> textXML=new ArrayList<>();
		ArrayList<String> textRules=new ArrayList<>();

		//2. read file
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(source));
			String line = reader.readLine();
			line=line.trim();
			while (line != null) {
				//System.out.println(line);
				if (line.length()>0) {
					textCSV.add(line);
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			resul=false;
		}	
		
		String myField="";
		String myParent="";
		String myLogicalRelType="";
		String myRelationshipType="";
		String myRelConIn="";
		String myProperty="";
		String myMultiplicity="";
		String myValue="";
		String[] myRelConInChunks;	
		String rela="";
		String manda="";
		String frase="";
		Stack pilaRel=new Stack();		
		Stack pilaPadres=new Stack();
		
		String myFieldPrev="";
		String myParentPrev="";
		String myLogicalRelTypePrev="";
		String myRelationshipTypePrev="";
		String myRelConInPrev="";
		String myPropertyPrev="";
		String myMultiplicityPrev="";
		String myValuePrev="";
		String[] myRelConInChunksPrev;		
		int tabulation=0;
		String topePila="";
		String value="";
		
		pilaRel.push("NADA");
		pilaPadres.push("NADA");
		
		
		//3. converting
		textXML.add("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		textXML.add("<featureModel>");
		textXML.add("	<properties>");
		textXML.add("		<graphics key=\"showhiddenfeatures\" value=\"true\"/>");
		textXML.add("		<graphics key=\"legendposition\" value=\"1217,115\"/>");
		textXML.add("		<graphics key=\"legendautolayout\" value=\"false\"/>");
		textXML.add("		<graphics key=\"showshortnames\" value=\"false\"/>");
		textXML.add("		<graphics key=\"layout\" value=\"horizontal\"/>");
		textXML.add("		<graphics key=\"showcollapsedconstraints\" value=\"true\"/>");
		textXML.add("		<graphics key=\"legendhidden\" value=\"false\"/>");
		textXML.add("		<graphics key=\"layoutalgorithm\" value=\"1\"/>");
		textXML.add("	</properties>");
		textXML.add("<struct>");	
		tabulation++;

		textRules.add("<constraints>");
		
		boolean isEnd=false;
		int lineCounter=0;
		String line="";
		int x=0;
		String repeatedLine="";
		while (!isEnd) {
			boolean isLine=false;
		
			while ((!isEnd) && (!isLine)) {
				line=textCSV.get(x).trim();
				lineCounter++;
				System.out.println("Linea "+lineCounter+":"+line);
	
				if ((line.length()>0) && (lineCounter>1)) {
					isLine=true;
				}
				x++;
				if (x==textCSV.size()) {
					isEnd=true;
				}
			}
			line=line+"EOF";
			if (isLine) {
				// descomponemos linea
				rela="";
				manda="";
				String[] lineChunks=line.split(";");
				if (lineChunks.length==12) {
					lineChunks[11]=lineChunks[11].substring(0,lineChunks[11].length()-3);
					myField=lineChunks[0].trim();
					myParent=lineChunks[1].trim();
					myRelationshipType=lineChunks[2].trim().toLowerCase();
					myLogicalRelType=lineChunks[3].trim().toLowerCase();
					myRelConIn=lineChunks[4].trim();
					myProperty=lineChunks[5].trim().toLowerCase();
					myMultiplicity=lineChunks[6].trim().toLowerCase();
					myValue=lineChunks[7].trim().toLowerCase();
					if (myRelConIn.length()>0) {
						myRelConInChunks=myRelConIn.split(",");
					}
				}
				topePila=(String) pilaPadres.peek();
				if (!myParent.contentEquals(topePila) && pilaPadres.size()>1) {
					String relac="";
					pilaPadres.pop();
					relac=(String) pilaRel.pop();
					frase="</"+relac+">";
					tabulation--;
					repeatedLine=new String(new char[tabulation]).replace("\0", " ");						
					textXML.add(repeatedLine+frase);
				}
				

				if (myRelationshipType.contentEquals("consistsof")) {
					System.out.println("BLANK");					
				}else if (myRelationshipType.contentEquals("requires")) {
					textRules.add("<rule>");
					textRules.add("  <imp>");
					textRules.add("    <var>"+myField+"</var>");
					textRules.add("    <var>"+myRelConIn+"</var>");
					textRules.add("  </imp>");
					textRules.add("</rule>");						
				}else if (myRelationshipType.contentEquals("excludes")) {
					textRules.add("<rule>");
					textRules.add("  <not>");
					textRules.add("    <var>"+myField+"</var>");
					textRules.add("    <var>"+myRelConIn+"</var>");
					textRules.add("  </not>");
					textRules.add("</rule>");													
				}  		
				
				if (myLogicalRelType.length()>0) {
					
					if (myLogicalRelType.contentEquals("and")) {
						rela="and";
					} else if (myLogicalRelType.contentEquals("or")) {
						rela="or";
					} else if (myLogicalRelType.contentEquals("xor")) {
						rela="alt";
					} else {
						System.out.println("ERRORRR");
					}
					pilaPadres.add(myField);
					pilaRel.add(rela);
					
					if (myProperty.contentEquals("mandatory")) {
						manda=" mandatory=\"true\" ";
					} else if (myProperty.contentEquals("optional")) {
						manda="";
					}
					if (myValue.length()>0) {
						value=" value=\""+myValue+"\" ";
					} else  {
						value="";
					}
				
					frase="<"+rela+manda+" name=\""+myField+"\" "+value+">";
					repeatedLine=new String(new char[tabulation]).replace("\0", " ");						
					textXML.add(repeatedLine+frase);
					tabulation++;
				
				} else {
					if (myProperty.contentEquals("mandatory")) {
						manda=" mandatory=\"true\" ";
					} else if (myProperty.contentEquals("optional")) {
						manda="";
					}
					
					if (myValue.length()>0) {
						value=" value=\""+myValue+"\" ";
					} else  {
						value="";
					}

					frase="<feature "+manda+"name=\""+myField+"\""+value+"/>";
					repeatedLine=new String(new char[tabulation]).replace("\0", " ");						
					textXML.add(repeatedLine+frase);
				}
								
				myFieldPrev=myField;
				myParentPrev=myParent;
				myLogicalRelTypePrev=myLogicalRelType;
				myRelationshipTypePrev=myRelationshipType;
				myRelConInPrev=myRelConIn;
				myPropertyPrev=myProperty;
				myMultiplicityPrev=myMultiplicity;
				myValuePrev=myValue;	
						
			}
		}
		while (pilaRel.size()>1) {
			String relac="";
			pilaPadres.pop();
			relac=(String) pilaRel.pop();
			frase="</"+relac+">";
			tabulation--;
			repeatedLine=new String(new char[tabulation]).replace("\0", " ");						
			textXML.add(repeatedLine+frase);			
		}
		
		textRules.add("</constraints>");
			
		textXML.add("</struct>");
		
		if (textRules.size()>2) {
			for (int i = 0; i < textRules.size(); i++){
				textXML.add(textRules.get(i));
			}
		}
		textXML.add("</featureModel>");			
 
		//4. writeing
		writeFile(target,textXML);
		return resul;		
		
	}	
	
	ArrayList<String> fileRead (String pathScript) {
		// readfile
		ArrayList<String> myFile=new ArrayList<>();		

		//first, cleaning ArrayList to proceed to populate
		fmFile.clear();
		BufferedReader reader;
		try {
			System.out.println("Working directory path: "+ System.getProperty("user.dir"));
			reader = new BufferedReader(new FileReader(pathScript));
			String line = reader.readLine();
			while (line != null) {
				//System.out.println(line);
				myFile.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Fichero no existe: "+pathScript);
			System.exit(20);
		}		
		return myFile;			
	}
	 
	void generateAddFeatureSet(int sizeSet, int supertypeSet, int maxSupertypes, String fichero) {
		ArrayList<String> gSet=new ArrayList<String>();
		int num1=0;
		int fx=0;
		String theType="";
		String theRel="";
		String theSupertypes="";
		String phrase="";
		
		if (sizeSet<1) {
			System.out.println("El valor tiene que ser mayor que 0. Ha llegado: " + sizeSet);
			System.exit(38);
		}
		
		
		gSet.add("LOG_SET_NAME(default)");
		gSet.add("cls");
		gSet.add("RESET");
		gSet.add("LOAD_FM(D:\\workspace\\202109\\features\\fmexperimentacion\\0050.fm)");
		gSet.add("SHOW_STATISTICS");
		gSet.add("write_line(---------------------)");
		gSet.add("RECORD_TIME_START");
		
		for (int i=1;i<=sizeSet;i++) {
			fx++;
			int conta=0;
			theType="";
			theRel="";
			theSupertypes="";
			phrase="";
			Random rn = new Random();
			num1 = rn.nextInt(supertypeSet) + 1;
			
			rn = new Random();
			int answer = rn.nextInt(2) + 1;
			
			if (answer==1) {
				theType="MANDATORY";
				theRel="AND";
			}else {
				theType="OPTIONAL";
				rn = new Random();
				int answer2 = rn.nextInt(2) + 1;
				if (answer2==1) {
					theRel="OR";
				} else if (answer2==2) {
					theRel="XOR";
				} else {
					System.out.println("random raro: "+answer2);
				}
				
			}	
			
			int answer3 = rn.nextInt(maxSupertypes) + 1;
			for(int j=1;j<=answer3;j++) {
				boolean found=false;
				while (!found) {
					int answer4 = rn.nextInt(supertypeSet) + 1;
					String paso="ST"+answer4;
					if (theSupertypes.indexOf(paso)<0) {
						if (conta>0) {
							theSupertypes+=" "+paso;
						} else  {
							theSupertypes+=paso;
						}
						found=true;
						conta++;
					}
				}
			}
			phrase="ADD_FEATURE (FX"+fx+","+theType+","+theRel+",["+theSupertypes+"])";
			gSet.add(phrase);
		}
		gSet.add("RECORD_TIME_STOP");
		gSet.add("SHOW_TIME");
		gSet.add("SUMARIZE");
		gSet.add("SHOW_TREE_VALUES");
		gSet.add("SHOW_FM");
		gSet.add("FLUSH_LOG");
		
		
		writeFile(fichero,gSet);
		gLog.add("Generated AddFeature SET: "+fichero);
	}

	 void generateFM() {
		FileWriter fichero = null;
		PrintWriter pw = null;
		try
		{
			fichero = new FileWriter("featuremodel.fm");
			pw = new PrintWriter(fichero);

			String linea="";

			//RELACIONES
			pw.println("%Relationships\n");
			// RECORREMOS ARBOL Y PARA CADA NODO, COLOCAMOS UNA LINEA EN ESTE FORMATO:
			// NOMBRE: CARACTERISTICAS [OPCIONALES];
			int numNodos=0;
			int numReglas=0;
			int theLevel=0;
			int theNode=0;
			String salidaAcum="";
			salidaAcum=iterateNodes(eTree, theLevel, theNode,salidaAcum);
			pw.println(salidaAcum);

			//REGLAS
			pw.println("%Constraints\n");
			// RECORREMOS ARBOL Y PARA CADA NODO, COLOCAMOS UNA LINEA EN ESTE FORMATO:
			// NOMBRE: CARACTERISTICAS [OPCIONALES];
			for (int i = 0; i < numReglas; i++) {
				pw.println("Linea " + i);
			}


			//SUPERTIPOS
			pw.println("%Supertypes\n");



		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Nuevamente aprovechamos el finally para 
				// asegurarnos que se cierra el fichero.
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		utiles.printFileToConsole("featuremodel.fm");

	}

	void generateFMFile(int mode, Collection inputTree,ArrayList<String> fmFile) {
		// generates a file to be stored with relationships, constraints and supertypes
		// mode 0 -> FAMA
		// mode 1 -> Variability Run-Time Tool

		boolean isEnd=false;
		int nodePointer=0;
		Queue s=new LinkedList();
		Queue t=new LinkedList();
		boolean isFound=false;
		Iterator<TreeNode> it = inputTree.iterator();
		boolean hayMas=false;
		String theFeature="";
		int theIdNode=0;
		String subject="";
		String predicate="";



		// 1 RELATIONSHIPS

		fmFile.clear();
		fmFile.add("%Relationships");

		hayMas=it.hasNext();
		TreeNode theTreeNode=it.next();
		String firstFeature=theTreeNode.nameFeature;
		s.add(firstFeature);
		t.add(theTreeNode.idNode);
		it=null;

		while (!isEnd) {
			if (s.size()>0) {
				theFeature=(String) s.remove();
				theIdNode=(int) t.remove();
				subject=theFeature;
				it = inputTree.iterator();
				hayMas=it.hasNext();
				Queue predi=new LinkedList();
				String cad="";
				int carMin=0;
				int carMax=0;
				String theFeatSet="";
				while (hayMas) {
					theTreeNode=it.next();
					String theRelType=theTreeNode.relType;
					String theRelSiblings=theTreeNode.relSiblings;
					String theType=theTreeNode.type;
					int tpn=theTreeNode.idParentNode;
					int theNode3=theTreeNode.idNode;
					String theFeature3=theTreeNode.nameFeature;
					if (tpn==theIdNode) {
						if (theType.contentEquals("SET")) {
							theFeatSet="";
							carMin=theTreeNode.minCardinality;
							carMax=theTreeNode.maxCardinality;
							int nodoPadre2=0;
							nodoPadre2=theTreeNode.idNode;
							Iterator<TreeNode> it2 = inputTree.iterator();
							boolean hayMas2=false;
							hayMas2=it2.hasNext();
							while (hayMas2) {
								TreeNode theTreeNode2=it2.next();
								int elNodo=theTreeNode2.idParentNode;
								if (elNodo==nodoPadre2) {
									theFeatSet+=" "+theTreeNode2.nameFeature;
									s.add(theTreeNode2.nameFeature);
									t.add(theTreeNode2.idNode);
								}
								if (!it2.hasNext()) {
									hayMas2=false;
								}
							}
							theFeatSet=theFeatSet.substring(1);
							cad="["+carMin+","+carMax+"]"+"{"+theFeatSet+"}";
							predi.add(cad);
						}else if (theRelType.contentEquals("O")) {
							cad="["+theFeature3+"]";
							s.add(theFeature3);
							t.add(theNode3);
							predi.add(cad);
						}else if (theRelType.contentEquals("M")) {
							cad=theFeature3;
							s.add(theFeature3);
							t.add(theNode3);
							predi.add(cad);
						}else {
							System.out.println("TYPE MISMATCH (not optional, mandatory). FOUND: "+theRelType+ " "+theRelSiblings+"   "+theType);
							System.exit(17);
						}
					}
					hayMas=it.hasNext();
				}
				predicate="";
				while (predi.size()>0) {
					predicate+=" "+predi.remove();
				}
				// writing line
				if (predicate.length()>0) {
					String tl=subject+":"+predicate+";";
					System.out.println(tl); 
					fmFile.add(tl);
				}


			} else {
				isEnd=true;
			}
		}
		// 2 CONSTRAINTS

		Iterator<Constraint> it3 = eConstraints.iterator();
		boolean hayMas2=false;
		boolean hayMas3=it3.hasNext();
		if (hayMas3) {
			fmFile.add("");
			fmFile.add("%Constraints");
		}
		while (hayMas3) {
			String lit="";
			Constraint theConstraint=it3.next();
			switch (theConstraint.relationType) {
			case "R":
				lit=theConstraint.featureSubject+" REQUIRES "+theConstraint.featurePredicate+";";
				break;
			case "E":
				lit=theConstraint.featureSubject+" EXCLUDES "+theConstraint.featurePredicate+";";
				break;
			case "I":
				lit=theConstraint.featureSubject+" IMPLIES "+theConstraint.featurePredicate+";";
				break;
			default:
				System.out.println("Aqu� ha pasado algo extra�o. Hay una condici�n que no deber�a estar."); 
				break;
			}
			fmFile.add(lit);

			if (!it3.hasNext()) {
				hayMas3=false;
			}
		}

		// 3 %ATTRIBUTES

		Iterator<String> it4 = eAttributes.iterator();
		boolean hayMas4=it4.hasNext();
		if (hayMas4) {
			fmFile.add("");
			fmFile.add("%Attributes");
		}
		while (hayMas4) {
			String lit="";
			String theAttribute=it4.next();
			fmFile.add(theAttribute);
			if (!it4.hasNext()) {
				hayMas4=false;
			}
		}



		if (mode==1) { 
			// FICHERO MODO MEJORADO = 1
			// 3 SUPERTYPES
			Iterator itr = inputTree.iterator();
			String fea="";
			String sup="";
			int stCounter=0;
			while (itr.hasNext()) {
				String  line="";
				TreeNode theTreeNode3=(TreeNode) itr.next();
				try {
					ArrayList<String> theSupertypes3=theTreeNode3.feature.supertypes;
					fea=theTreeNode3.feature.name;
					sup=utiles.convertArrayListStringToString(theTreeNode3.feature.supertypes);
					if (sup.trim().length()>0) {
						if (stCounter<1) {
							fmFile.add("");
							fmFile.add("%Supertypes");
						}
						stCounter++;

						line=fea+": "+sup+";"; 
						if (line.length()>3) {
							fmFile.add(line);
						}
					}
				} catch (Exception e) {
					System.out.println(fea+" Nodo sin supertipos");
				}

			}


			if (eComments.size()>0) {
				// 4 COMMENTS
				fmFile.add("");
				fmFile.add("%Comments");
				itr = eComments.iterator();
				while (itr.hasNext()) {
					String line="";
					String comment=(String) itr.next();
					fmFile.add(comment);			
				}
			}
		}
	}

	void generateXMLFIFile(int mode, Collection inputTree,ArrayList<String> fmFile) {
		// generates a XML FeatureIde file to be stored with relationships and constraints 
		
		// mode 0 -> FAMA
		// mode 1 -> Variability Run-Time Tool

		boolean isEnd=false;
		int nodePointer=0;
		Queue s=new LinkedList();
		Queue t=new LinkedList();
		boolean isFound=false;
		Iterator<TreeNode> it = inputTree.iterator();
		boolean hayMas=false;
		String theFeature="";
		int theIdNode=0;
		String subject="";
		String predicate="";



		// 1 RELATIONSHIPS

		fmFile.clear();
		fmFile.add("%Relationships");

		hayMas=it.hasNext();
		TreeNode theTreeNode=it.next();
		String firstFeature=theTreeNode.nameFeature;
		s.add(firstFeature);
		t.add(theTreeNode.idNode);
		it=null;

		while (!isEnd) {
			if (s.size()>0) {
				theFeature=(String) s.remove();
				theIdNode=(int) t.remove();
				subject=theFeature;
				it = inputTree.iterator();
				hayMas=it.hasNext();
				Queue predi=new LinkedList();
				String cad="";
				int carMin=0;
				int carMax=0;
				String theFeatSet="";
				while (hayMas) {
					theTreeNode=it.next();
					String theRelType=theTreeNode.relType;
					String theRelSiblings=theTreeNode.relSiblings;
					String theType=theTreeNode.type;
					int tpn=theTreeNode.idParentNode;
					int theNode3=theTreeNode.idNode;
					String theFeature3=theTreeNode.nameFeature;
					if (tpn==theIdNode) {
						if (theType.contentEquals("SET")) {
							theFeatSet="";
							carMin=theTreeNode.minCardinality;
							carMax=theTreeNode.maxCardinality;
							int nodoPadre2=0;
							nodoPadre2=theTreeNode.idNode;
							Iterator<TreeNode> it2 = inputTree.iterator();
							boolean hayMas2=false;
							hayMas2=it2.hasNext();
							while (hayMas2) {
								TreeNode theTreeNode2=it2.next();
								int elNodo=theTreeNode2.idParentNode;
								if (elNodo==nodoPadre2) {
									theFeatSet+=" "+theTreeNode2.nameFeature;
									s.add(theTreeNode2.nameFeature);
									t.add(theTreeNode2.idNode);
								}
								if (!it2.hasNext()) {
									hayMas2=false;
								}
							}
							theFeatSet=theFeatSet.substring(1);
							cad="["+carMin+","+carMax+"]"+"{"+theFeatSet+"}";
							predi.add(cad);
						}else if (theRelType.contentEquals("O")) {
							cad="["+theFeature3+"]";
							s.add(theFeature3);
							t.add(theNode3);
							predi.add(cad);
						}else if (theRelType.contentEquals("M")) {
							cad=theFeature3;
							s.add(theFeature3);
							t.add(theNode3);
							predi.add(cad);
						}else {
							System.out.println("TYPE MISMATCH (not optional, mandatory). FOUND: "+theRelType+ " "+theRelSiblings+"   "+theType);
							System.exit(17);
						}
					}
					hayMas=it.hasNext();
				}
				predicate="";
				while (predi.size()>0) {
					predicate+=" "+predi.remove();
				}
				// writing line
				if (predicate.length()>0) {
					String tl=subject+":"+predicate+";";
					System.out.println(tl); 
					fmFile.add(tl);
				}


			} else {
				isEnd=true;
			}
		}
		// 2 CONSTRAINTS

		Iterator<Constraint> it3 = eConstraints.iterator();
		boolean hayMas2=false;
		boolean hayMas3=it3.hasNext();
		if (hayMas3) {
			fmFile.add("");
			fmFile.add("%Constraints");
		}
		while (hayMas3) {
			String lit="";
			Constraint theConstraint=it3.next();
			switch (theConstraint.relationType) {
			case "R":
				lit=theConstraint.featureSubject+" REQUIRES "+theConstraint.featurePredicate+";";
				break;
			case "E":
				lit=theConstraint.featureSubject+" EXCLUDES "+theConstraint.featurePredicate+";";
				break;
			case "I":
				lit=theConstraint.featureSubject+" IMPLIES "+theConstraint.featurePredicate+";";
				break;
			default:
				System.out.println("Aqu� ha pasado algo extra�o. Hay una condici�n que no deber�a estar."); 
				break;
			}
			fmFile.add(lit);

			if (!it3.hasNext()) {
				hayMas3=false;
			}
		}

		// 3 %ATTRIBUTES

		Iterator<String> it4 = eAttributes.iterator();
		boolean hayMas4=it4.hasNext();
		if (hayMas4) {
			fmFile.add("");
			fmFile.add("%Attributes");
		}
		while (hayMas4) {
			String lit="";
			String theAttribute=it4.next();
			fmFile.add(theAttribute);
			if (!it4.hasNext()) {
				hayMas4=false;
			}
		}



		if (mode==1) { 
			// FICHERO MODO MEJORADO = 1
			// 3 SUPERTYPES
			Iterator itr = inputTree.iterator();
			String fea="";
			String sup="";
			int stCounter=0;
			while (itr.hasNext()) {
				String  line="";
				TreeNode theTreeNode3=(TreeNode) itr.next();
				try {
					ArrayList<String> theSupertypes3=theTreeNode3.feature.supertypes;
					fea=theTreeNode3.feature.name;
					sup=utiles.convertArrayListStringToString(theTreeNode3.feature.supertypes);
					if (sup.trim().length()>0) {
						if (stCounter<1) {
							fmFile.add("");
							fmFile.add("%Supertypes");
						}
						stCounter++;

						line=fea+": "+sup+";"; 
						if (line.length()>3) {
							fmFile.add(line);
						}
					}
				} catch (Exception e) {
					System.out.println(fea+" Nodo sin supertipos");
				}

			}


			if (eComments.size()>0) {
				// 4 COMMENTS
				fmFile.add("");
				fmFile.add("%Comments");
				itr = eComments.iterator();
				while (itr.hasNext()) {
					String line="";
					String comment=(String) itr.next();
					fmFile.add(comment);			
				}
			}
		}
	}	
	
	RelationTree getDescendants(Collection eTree, int idNode) {
		//loop por los nodos del arbol eTree
		RelationTree eDescendants=new RelationTree();
		eDescendants.idNode=idNode;
		eDescendants.nameNode=getNameNode(idNode,eTree);
		int andNodesCounter=0;
		int descendants=0;
		TreeNode theNode=new TreeNode();
		//Collection eDescendants=new ArrayList<TreeNode>();
		for (Iterator<TreeNode> it = eTree.iterator(); it.hasNext();) {
			TreeNode actualNode=it.next(); // cursor que recorre el �rbol
			if (actualNode.idParentNode==idNode) { // si actualNode es hijo de idNode, procesamos actualNode
				if (actualNode.type.contentEquals("SET")) { //si es variable auxiliar (SET), comprobamos y sumamos totales
					RelationNode elNodo=new RelationNode();
					elNodo.idNode=actualNode.idNode;
					if ((actualNode.minCardinality==1) && (actualNode.maxCardinality==1)) { // XOR
						eDescendants.rXOR++;
						elNodo.relSiblings="XOR";
					} else { // OR
						eDescendants.rOR++;
						elNodo.relSiblings="OR";
					}
					for (Iterator<TreeNode> it2 = eTree.iterator(); it2.hasNext();) { //sumamos los nodos que encontremos por debajo del SET
						TreeNode actualNode2=it2.next(); //contenido del cursor que apunta
						if (actualNode2.idParentNode==actualNode.idNode) { 
							descendants++;
						}
						
					}
					
					
					eDescendants.eNodes.add(elNodo);	
				} else { //si no es SET, es un AND (no lleva nodo AUX
					andNodesCounter++;
					descendants++;
					
				}
				
			} else { // si no es hijo suyo, no hacemos nada
				
			}
		}
		if (andNodesCounter>0) { // si hay m�s de un nodo en el AND, hay posibilidades de a�adir otro como hermano. En ese caso el padre es el mismo nodo
			eDescendants.rAND=1;
		}
		eDescendants.siblingsTotal=descendants;

		//returning list of only compatible nodes
		return eDescendants;
	}
	List<String> getDescendantsAll(Collection eTree,int idNode, List<String> base) {
		Iterator itr = eTree.iterator();
		boolean found=false;
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			if (tn.idParentNode==idNode) {
				if (tn.type.contentEquals("BASIC")) {
					base.add(tn.nameNode);
				}
				base=getDescendantsAll(eTree,tn.idNode,base);
				found=true;
			}
		}
		/*
		if (!found) { //no hay hijos
			String nameNode=getNameNode(idNode,eTree);
			base.add(nameNode);
		}
		*/
		return base;
	}
	int getMaxDifferentSupertypesInNode(Collection eTree) {
		int max=0;
		int amount=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
//			System.out.println("Loc "+ loc);
			TreeNode tn=(TreeNode) itr.next();
			Feature theFeature=tn.feature;
			amount=theFeature.supertypes.size();
			if (amount>max) {
				max=amount;
			}
		}		
		return max;
	}
	
	 Feature getFeatureByName(String theName) {
		// IN: feature name
		// OUT: the feature object from collection eFeatures;
		Feature featureFound=new Feature();
		boolean isFound=false;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			Feature theFeature=(Feature) itr.next();
			if (theName.equals(theFeature.name)) {
				isFound=true;
				featureFound=theFeature;
			}
		}		
		return featureFound;
	}	
	
	 int getAndSets(Collection eTree) {
		int total=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			if (tn.tAND>0)  {
				total++;
			}
		}		
		return total;
	}	
	double getAverageBranchingFactor(Collection eTree) {
		double ratio=0;
		int suma=0;
		int numNodes=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			suma=suma+tn.totChildren;
			if (tn.type.contentEquals("BASIC")) {
				numNodes++;
			} else {
				suma--;
			}
		}		
		ratio=Double.valueOf(suma)/Double.valueOf(numNodes);
		return ratio;
	}	
	int getMaxBranchingFactor(Collection eTree) {
		int max=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			if (tn.totChildren>max) {
				max=tn.totChildren;
			}
		}		
		return max;
	}	
	int getMaxChildrenSET(Collection eTree) {
		int max=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			if (tn.type.contentEquals("SET")) {
				if (tn.totChildren>max) {
					max=tn.totChildren;
				}
			}
		}		
		return max;
	}		
	int getOrSets(Collection eTree) {
		int total=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			if (tn.tOR>0)  {
				total++;
			}
		}		
		return total;
	}	
	 int getXorSets(Collection eTree) {
		int total=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			if (tn.tXOR>0)  {
				total++;
			}
		}		
		return total;
	}	
	int getMandatoryNumber(Collection eTree) {
		int total=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			if ((tn.type.contentEquals("BASIC")) && (tn.relType.contentEquals("M")))  {
				total++;
			}
		}		
		return total;
	}	
	 int getOptionalNumber(Collection eTree) {
		int total=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			if ((tn.type.contentEquals("BASIC")) && (tn.relType.contentEquals("O")))  {
				total++;
			}
		}		
		return total;
	}	

	int getMaxLevel(Collection eTree) {
		int maxLevel=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			if (tn.level>maxLevel) {
				maxLevel=tn.level;
			}
		}		
		return maxLevel;
	}	

	SortedSet getSupertypesFromTree(Collection eTree) {
	    SortedSet s=new TreeSet();
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			for (int i=0;i<tn.feature.supertypes.size();i++) {
				s.add(tn.feature.supertypes.get(i));
			}
		}		
		return s;
	}
	
	int getNumberNodesWithSupertypes(Collection eTree) {
		int total=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			if (tn.feature.supertypes.size()>0) {
				total++;
			}
		}		
		return total;
	}
	
	int getNumberReqConstraints(Collection eConstraints) {
		int total=0;
		Iterator itr = eConstraints.iterator();
		while (itr.hasNext()) {
			Constraint con=(Constraint) itr.next();
			if (con.relationType.contentEquals("R")) {
				total++;
			}
		}		
		return total;
	}
	int getFeatureNumber(Collection eTree) {
		int total=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			if (tn.type.contentEquals("BASIC")) {
				total++;
			}
		}		
		return total;
	}
	
	int getBreedingNodesNumber(Collection eTree) {
		// Calculates total amount of son nodes in a tree
		int total=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			if ((tn.totChildren>0)&&(tn.type.contentEquals("BASIC"))) {
				total++;
			}
		}		
		return total;
	}	
	int getChildlessNodesNumber(Collection eTree) {
		int total=0;
		Iterator itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode tn=(TreeNode) itr.next();
			if (tn.totChildren==0) {
				total++;
			}
		}		
		return total;
	}		
	
	String getNameNode(int idNode, Collection eTreeAux) {
		String resul="";
		boolean isFound=false;
		Iterator itr = eTreeAux.iterator();
		while ((isFound==false) && (itr.hasNext())) {
			TreeNode theTreeNode=(TreeNode) itr.next();
			if (theTreeNode.idNode==idNode) {
				resul=theTreeNode.nameNode;
				isFound=true;
			}
		}
		if (!isFound ) {
			System.out.println("ERROR. Function: getNameNode. Arguments| idNode="+idNode);
			System.exit(16);
		}
		return resul;
	}	
	
	int getNodeId(String featureName, Collection eTreeAux) {
		int resul=0;
		boolean isFound=false;
		Iterator itr = eTreeAux.iterator();
		while ((isFound==false) && (itr.hasNext())) {
			TreeNode theTreeNode=(TreeNode) itr.next();
			if (theTreeNode.nameFeature.contentEquals(featureName)) {
				resul=theTreeNode.idNode;
				isFound=true;
			}
		}		
		return resul;
	 }

	 Collection getNodesToPutFeature(Feature theFeature) {
		ArrayList<Integer> eCompatibleFeatures = new ArrayList<Integer>();    //caracter�sticas que son compatibles
		Collection<TreeNode> eCompatibleNodes = new ArrayList<TreeNode>();    //nodos donde se puede colgar la nueva caracter�stica
		ArrayList<String> eCompatibleSupertypes = new ArrayList<String>();    //supertipos que resultan compatibles ��??
		ArrayList<String> listSupertypesNewFeature = new ArrayList<String>();    //list of supertypes of theFeature (input)
		//Get supertypes of idFeature
		listSupertypesNewFeature=theFeature.supertypes;
		int counterLoop=0;
		/*
		 * loop triple anidado por los nodos del arbol eTree (1). Para cada nodo del �rbol se obtienen los supertipos (2) 
		 * y se compara cada uno con cada uno de los que trae theFeature, y en caso de que coincida alguno, se incrementa matchCounter. 
		 * 
		 * Si cuando acabe de recorrer el bucle principal for resulta que eCompatibleNodes es 0, significa que no ning�n nodo tiene
		 * supertipos compatibles, as� que la �nica posibilidad es que vaya al ra�z.
		 */
		
		for (Iterator<TreeNode> it = eTree.iterator(); it.hasNext();) {
			counterLoop++;
			int matchCounter=0;
			TreeNode actualNode=it.next(); // cursor del bucle
			actualNode.match=0; 
			System.out.println(actualNode.nameFeature);
			ArrayList<String> listSupertypesTreeNode=new ArrayList<String>(); //variable para guardar los supertipos del nodo
			listSupertypesTreeNode=actualNode.feature.supertypes;  //se guardan

			//loop por los supertipos del nodo TreeNode (listSupertypesTreeNode
			Iterator it2 = listSupertypesTreeNode.iterator();
			while (it2.hasNext()) {
				String stNodo;
				stNodo=(String) it2.next();
				Iterator it3 = listSupertypesNewFeature.iterator();
				while (it3.hasNext()) { //loop por los supertipos de la Feature a a�adir
					String stNuevo;
					stNuevo=(String) it3.next();
					if (stNuevo.equals(stNodo)) {
						matchCounter++;
					}
				}
			}
			actualNode.match=matchCounter;
			if (matchCounter>0) { 
				eCompatibleNodes.add(actualNode);
			} 
		}
		
		if (eCompatibleNodes.size()==0) {
			Iterator<TreeNode> it4 = eTree.iterator();
			TreeNode actualNode2=it4.next(); 
			eCompatibleNodes.add(actualNode2);
		}

		//returning list of only compatible nodes
		return eCompatibleNodes;
	 }
	 
	 String getParentFeatureName(String featureName, Collection eTreeAux) {
		String resul="";
		boolean isFound=false;
		Iterator itr = eTreeAux.iterator();
		while ((isFound==false) && (itr.hasNext())) {
			TreeNode theTreeNode=(TreeNode) itr.next();
			if (theTreeNode.parentName.contentEquals(featureName)) {
				resul=theTreeNode.parentName;
				isFound=true;
			}
		}		
		return resul;
	}
	int getParentNodeId(int idNode, Collection eTreeAux) {
		int resul=0;
		boolean isFound=false;
		Iterator itr = eTreeAux.iterator();
		while ((isFound==false) && (itr.hasNext())) {
			TreeNode theTreeNode=(TreeNode) itr.next();
			if (theTreeNode.idNode==idNode) {
				resul=theTreeNode.idParentNode;
				isFound=true;
			}
		}		
		return resul;
	}	
	int getParentNodeId(String featureName, Collection eTreeAux) {
		int resul=0;
		boolean isFound=false;
		Iterator itr = eTreeAux.iterator();
		while ((isFound==false) && (itr.hasNext())) {
			TreeNode theTreeNode=(TreeNode) itr.next();
			if (theTreeNode.parentName.contentEquals(featureName)) {
				resul=theTreeNode.idParentNode;
				isFound=true;
			}
		}		
		return resul;
	}
	 
	int getTypeFMFile(String name) {
		int res=0;
		String ext="";
		ext=FilenameUtils.getExtension(name).toLowerCase();

		//1=ruva;2=fm;3=xml(FaMa);4=xml(FeatureIde);5=afm(FaMa)
		switch (ext) { 
		case "ruva":
			res=1;
			break;
		case "fm":
			res=2;
			break;
		case "fama":
		case "xml":
			res=3;
			break;
		case "afm":
			res=5;
			break;
		default:
			res=0;
			break;
		}
		return res;
	}

	
	int getTotalSupertypesFromTree(Collection eTree) {
		return getSupertypesFromTree(eTree).size();
	}
	
	int getTotalSiblings(String featureName) {
		int theSiblings=0; 
		String parentFeatureName="";
		boolean isFound=false;

		//search for parent name
		Iterator itr = eTree.iterator();
		while ((!isFound) && (itr.hasNext())) {
			TreeNode theTreeNode=(TreeNode) itr.next();
			if (theTreeNode.nameFeature.contentEquals(featureName)) {
				parentFeatureName=theTreeNode.parentName;
				isFound=true;
			}
		}				

		
		itr = eTree.iterator();
		while (itr.hasNext()) {
			TreeNode theTreeNode=(TreeNode) itr.next();
			if (theTreeNode.parentName.contentEquals(parentFeatureName)) {
				theSiblings++;
			}
		}	
		if (theSiblings>0) {
			theSiblings--;
		}

		return theSiblings;
	}


	 boolean isFeatureUsed(String featName, Collection eTree) {
		boolean isFound=false;
		Iterator itr = eTree.iterator();
		while ((isFound==false) && (itr.hasNext())) {
			TreeNode theTreeNode=(TreeNode) itr.next();
			if (theTreeNode.nameFeature.contentEquals(featName)) {
				isFound=true;
			}
		}		
		return isFound;
	}
	 boolean isAncestor(String featureTarget,String featureSource, Collection eTreeCheck) {
		boolean isFound=false;
		boolean rootNodeReached=false;
		boolean resul=false;
		String parentToCheck=featureSource;
		String lastParent=featureSource;
		String theParent="";
		String theNode="";
		while ((!isFound)&&(!rootNodeReached)) {
			theParent=getParentFeatureName(lastParent,eTreeCheck);
			if (theParent.contentEquals("root")) { //top reached
				rootNodeReached=true;
			}
			if (theParent.contentEquals(featureSource)) {
				isFound=true;
				resul=true;
			}
		}
		return resul;
	}
	 boolean isDescendant(String featureTarget,String featureSource, Collection eTreeCheck) {
		boolean isFound=false;
		boolean rootNodeReached=false;
		boolean resul=false;
		
		return resul;
	}
	
	 String iterateNodes(Collection theTree,int theLevel, int theNode, String entrada){
		theLevel++;
		Iterator itr = theTree.iterator();
		String theLine="--";
		String repeatedLine="";
		String separator="";
		String salida="";
		String theRest="";
		String prefix="";
		String suffix=""; 

		while (itr.hasNext()) {
			String theChildren="";
			String theChild="";
			TreeNode theTreeNode=(TreeNode) itr.next();
			int theParentNode=theTreeNode.idParentNode;
			int idFoundNode=0;
			// 1. Obtain feature name
			prefix=theTreeNode.nameFeature+":";

			// 2. Obtain children features
			if (theParentNode==theNode) {
				idFoundNode=theTreeNode.idNode;
				String relSiblings=theTreeNode.relSiblings;
				String relType=theTreeNode.relType;	
				String type=theTreeNode.type;	
				String theSupertypes;
				String salida2="";
				theChild=theTreeNode.nameFeature;
				if (relType.contentEquals("O")) {
					theChild="["+theChild+"]";
				}
				theChildren+=" "+theChild;
				suffix+=" "+theChild;

				String sentence="";
				sentence=theTreeNode.nameNode+": ";
				System.out.println(sentence);
				salida2=iterateNodes(theTree, theLevel, idFoundNode,entrada);
				theRest+=salida2;
			}

		}
		if (suffix.length()>0) {
			salida=prefix+suffix+"\n"+theRest;
		}else {
			salida=theRest;
		}
		return salida;
	}
	 void loadConfiguration(){
		config.put("representation", "text");
	}
	 boolean loadFeaturesFile() {
		//TODO: Adaptar fichero a nuevo formato de Features

		Feature theFeature=new Feature();
		boolean resul=false;

		//1. cleaning ArrayList to proceed to populate
		eFeatures.clear();


		//2. read file & Populating
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("repository/features.repo"));
			String line = reader.readLine();
			line=line.trim();
			while (line != null) {
				//System.out.println(line);
				if (line.length()>0) {
					theFeature.name=line;
					eFeatures.add(theFeature);
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			resul=false;
		}		
		return resul;			

	}
	 boolean loadFeaturesSupertypesFile() {

		Feature theFeature=new Feature();
		Supertype theSupertype=new Supertype();
		FeatureSupertype theFeatureSupertype=new FeatureSupertype();


		// readfile
		boolean resul=false;
		//first, cleaning ArrayList to proceed to populate
		eFeaturesSupertypes.clear();

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("repository/features-supertypes.repo"));
			String line = reader.readLine();
			line=line.trim();
			while (line != null) {
				//System.out.println(line);
				if (line.length()>0) {
					String firstChar=line.substring(0,1).trim();
					if (!firstChar.contentEquals("#") ) {
						// its a sentence
						String[] parts = line.split("IS COMPATIBLE WITH");
						parts[0]=parts[0].trim();
						parts[1]=parts[1].trim();
						theFeature.name=parts[0];
						theFeatureSupertype.theFeature=theFeature;
						if (parts[1].indexOf("WITH PRIORITY")>0) {
							String[] parts2 = parts[1].split("WITH PRIORITY");
							parts2[0]=parts2[0].trim();
							parts2[1]=parts2[1].trim();
							theSupertype.name=parts2[0];
							theFeatureSupertype.priority=Integer.parseInt(parts2[1]);

						} else {
							theSupertype.name=parts[1];
							theFeatureSupertype.priority=1;

						}
						theFeatureSupertype.theSupertype=theSupertype;
						eFeaturesSupertypes.add(theFeatureSupertype);

					} else {
						// its a comment, nothing to do here
					}
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			resul=false;
		}		
		return resul;			

	}


	/*
	 void loadSystem(int theIndex){
		systemName=getProjectName(theIndex);
		populateTree(theIndex);
		loadConstraintsDB(theIndex);
		loadSupertypes();
		loadFeaturesSupertypesRelationsDB();

	}
	 */

	 boolean populateFMfromFMFile(ArrayList<String> fmFile) {
		String line="";
		int lineCounter=0;
		int curSection=0;
		int currentSection=0;
		String theChar="";
		int lineLen=0;
		String newNode="";		
		int contSubSet=0;
		boolean endFile=false;
		int parentLevel=0;
		eTree.clear();
		eConstraints.clear();
		int x=0;
		while (!endFile) {
			boolean isLine=false;
			while ((!endFile) && (!isLine)) {
				line=fmFile.get(x).trim();
				lineCounter++;
				System.out.println("Linea "+lineCounter+":"+line);

				if (line.length()>0) {
					isLine=true;
				}
				x++;
				if (x==fmFile.size()) {
					endFile=true;
				}
			}

			if (isLine) {
				//System.out.println("+|"+line+"|+");
				theChar=line.substring(0,1);
				if (theChar.contentEquals("%")) {
					switch (line) {
					case "%Relationships":  // NEW SECTION
						curSection=1;
						break;
					case "%Constraints":  // NEW SECTION
						curSection=2;
						break;
					case "%Attributes":  // NEW SECTION
						curSection=3;
						break;
					case "%Supertypes":  // NEW SECTION
						curSection=4;
						break;
					case "%SupertypesPriority":  // NEW SECTION
						curSection=5;
						break;
					case "%Comments":  // NEW SECTION
						curSection=6;
						break;
					default: 
						System.out.println("Option not valid. Type again");
						break;
					}				
				} else if ((theChar.contentEquals(";"))||(theChar.contentEquals("#"))) {
					// It's a comment, bypassing it
				} else {
					// its not a comment or section, so it's a sentence. Store its content attending section.
					if (curSection>0) {
						String lastChar="";
						lineLen=line.length();
						lastChar=line.substring(lineLen - 1);
						//System.out.println(line);
						switch (curSection) {
						case 1:
							// Relationships

							// 1. separating subject from predicate
							if (!line.substring(lineLen-1).contentEquals(";")) { //si falta el ; al final de l�nea nos recuperamos en vez de dar error
								line+=";";
								lineLen=line.length();
								lastChar=line.substring(lineLen - 1);								
							}
							
							if (!lastChar.contentEquals(";")) {
								System.out.println("ERROR DE SINTAXIS EN FM LINEA "+lineCounter+" - FALTA ; AL FINAL - ENCONTRADO|"+line);
								System.exit(12);
							}
							
							int idFound=0;
							int idFeatureParent;
							int idParentNode=0;
							String newFeature="";

							String[] lineParts = line.split(":"); //separa sujeto y predicado

							//System.out.println("Subject:"+lineParts[0]);
							//System.out.println("Predicate:"+lineParts[1]);

							newFeature=lineParts[0];

							// 1.1 Searching id for feature
							int idParentFeature=0;
							if (nodeCounter==0) {
								// FIRST NODE 

								// there is no need to search parent node in root node. In first node there is no parent node
								newNode="";
								newNode=lineParts[0].trim();
/*
								String featureSet=lineParts[1].trim();
								String[] features = featureSet.split(" ");
								int childrenTotal=features.length;
*/								
								Feature myFeature=new Feature();
								myFeature.name=newNode;
								myFeature.supertypes=new ArrayList<String>();


								nodeCounter++;
								TreeNode theNode=new TreeNode();
								theNode.feature=myFeature; 
								theNode.idNode=nodeCounter;
								theNode.idParentNode=0;
								theNode.nameNode=newNode;
								theNode.idFeature=0;
								theNode.nameFeature=newNode;
								theNode.alias=newNode;
								theNode.relType="M";
//								theNode.totChildren=childrenTotal;
								theNode.totChildren=0;
								theNode.relSiblings="AND";
								theNode.type="BASIC";
								theNode.level=0;
								//theNode.relation=rs.getString("relation");
								//theNode.requirement=rs.getString("requirement");
								eTree.add(theNode);	
								newNode="";
								idParentNode=theNode.idNode;

							} else { 
								// Second Node and above

								// Searching for parent node
								boolean isFound=false;
								Iterator<TreeNode> it = eTree.iterator();
								boolean hayMas=false;
								hayMas=it.hasNext();
								while ((!isFound) && (hayMas)) {
									TreeNode theTreeNode=it.next();
									if (newFeature.contentEquals(theTreeNode.nameNode)) {
										idParentNode=theTreeNode.idNode;
										parentLevel=theTreeNode.level;
										isFound=true;
									}
									hayMas=it.hasNext();
								}	
								if (!isFound) {
									System.out.println("Error, feature parent not found Feature:"+newFeature);
									System.exit(9);
								}
							}
							// 2. separing features
							// 2.1 searching features and populating list
							List<String> predicates = new ArrayList<>();
							String predicate="";
							boolean isFinal=false; //se pone a true cuando llegamos al final de la linea
							int pos=0;
							String myCar="";
							String theFeature="";

							predicate=lineParts[1];
							//System.out.println("+|"+predicate+"|+"); 

							int posBeginSet=0;
							int posEndSet=0;
							boolean isCardinality=false;
							String newSet="";
							int pos2=0;
							int lon=0;
							lon=predicate.length();
							int contSubSets=0;
							boolean setDelimiterFound=false;
							int tChildren=0;
							int tAND=0;
							int tOR=0;
							int tXOR=0;
							while (!isFinal) {  //bucle que recorre la linea de principio a final y la procesa
								pos++;
								if (pos<lon) {
									myCar=predicate.substring(pos,pos+1);
									if (myCar.contentEquals("{")) {
										isCardinality=true;
										newNode=newNode+myCar;
										//System.out.println(newNode);
									}else if (myCar.contentEquals("}")) {
										isCardinality=false;
										newNode=newNode+myCar;
										//System.out.println(newNode); 
									} else if ((myCar.contentEquals(" ")) && (isCardinality)) {
										newNode=newNode+myCar;
										//System.out.println(newNode); 
									} else if ((myCar.contentEquals(" ")) || (myCar.contentEquals(";"))) {
										if (myCar.contentEquals(";")) {
											isFinal=true;
										}
										/*
										if ((newNode.indexOf("[")==0) && (newNode.indexOf("{")<1)) {
											System.out.println("ERROR DE SINTAXIS EN FM LINEA "+lineCounter+" - ESPACIO NO ESPERADO TRAS RANGO - ENCONTRADO:"+newNode);
											System.exit(11);
										} 
										*/
										if (newNode.indexOf("{")>0) {
											// SET  (SET)


											// split string into 2 parts: cardinality and set of features
											String[] theParts = newNode.split("]");
											if (theParts.length==2) {
												theParts[0]=theParts[0].trim();
												theParts[1]=theParts[1].trim();
											} else {
												System.out.println("ERROR DE SINTAXIS EN FM LINEA "+lineCounter+" - FALTA CORCHETE CIERRE - ENCONTRADO:"+newNode);
												System.exit(8);
											}

											String cardinality="";
											String featureSet="";
											cardinality=theParts[0].trim();
											featureSet=theParts[1].trim();
											featureSet=featureSet.substring(1);
											featureSet=featureSet.substring(0,featureSet.length()-1);

											// managing cardinality  (SET)
											int min=0;
											int max=0;

											cardinality=cardinality.substring(1);
											String[] carParts = cardinality.split(",");
											if (carParts.length!=2) {
												System.out.println("ERROR DE SINTAXIS EN FM LINEA "+lineCounter+" - FALTA MIN O MAX EN CARDINALIDAD - ENCONTRADO:"+cardinality);
												System.exit(5);
											}
											if ((carParts[0].length()>0) && (utiles.isInteger(carParts[0]))) {
												min=Integer.parseInt(carParts[0]);	
											} else {
												System.out.println("ERROR DE SINTAXIS EN FM LINEA "+lineCounter+" - �CORCHETE APERTURA? - ENCONTRADO:"+cardinality);
												System.exit(6);
												
											}
											if ((carParts[1].length()>0) && (utiles.isInteger(carParts[1]))) {
												min=Integer.parseInt(carParts[0]);	
											} else {
												System.out.println("ERROR DE SINTAXIS EN FM LINEA "+lineCounter+" - �CORCHETE CIERRE? - ENCONTRADO:"+cardinality);
												System.exit(7);
												
											}											
											
											max=Integer.parseInt(carParts[1]);


											// managing features (SET)

											// creating subset  (SET)
											String newSubSet="";
											contSubSet++;
											newSubSet="TO-"+featureSet.replace(" ", "_")+"-SET";
											String[] features = featureSet.split(" ");
											int childrenTotal=0;
											childrenTotal=features.length;

											nodeCounter++;
											TreeNode theNode=new TreeNode(); 
											theNode.idNode=nodeCounter;
											theNode.idParentNode=idParentNode;
											theNode.relType="M";
											theNode.type="SET";
											theNode.nameNode=newSubSet;
											theNode.idFeature=0;
											theNode.nameFeature=newSubSet;
											theNode.alias=newSubSet;
											theNode.minCardinality=min;
											theNode.maxCardinality=max;
											theNode.totChildren=childrenTotal;
											if ((max==1) && (min==1)) {
												theNode.relSiblings="XOR";
												tXOR++;
											}else if (childrenTotal==max) {
												theNode.relSiblings="OR";
												tOR++;
											} else {
												theNode.relSiblings="YOR";
												System.out.println("ERROR SINTACTICO - Encontrada relaci�n YOR|"+line);
											}
											theNode.level=parentLevel+1;
											Feature thFeature=new Feature();
											thFeature.description="";
											thFeature.name=newSubSet;
											thFeature.supertypes=new ArrayList<String>();										        
											theNode.feature=thFeature;
											eTree.add(theNode);		
											String parentRelSiblings="";
											parentRelSiblings=theNode.relSiblings;
											int nodeParent=0;
											nodeParent=nodeCounter;

											// processing features  (SET)
											for (int i=0;i<features.length;i++) {
												if (isFeatureUsed(features[i],eTree)) {
													System.out.println("Caracteristica repetida:"+features[i]);
													System.exit(34);
												}
														
												nodeCounter++;
												theNode=new TreeNode(); 
												theNode.idNode=nodeCounter;
												theNode.idParentNode=nodeParent;
												theNode.relType="M";
												theNode.relSiblings=parentRelSiblings;
												theNode.type="BASIC";
												theNode.nameNode=features[i];
												theNode.idFeature=0;
												theNode.nameFeature=features[i];
												theNode.alias=features[i];
												theNode.level=parentLevel+2;
												theNode.totChildren=0;
												thFeature=new Feature();
												thFeature.description="";
												thFeature.name=features[i];
												ArrayList<String> stDummy=new ArrayList<String>();
												thFeature.supertypes=stDummy;
												theNode.feature=thFeature;

												eTree.add(theNode);		
												tChildren++;
											}
											newNode="";
											isCardinality=false;

										} else {  //AND
											// Optional or Mandatory 
											tAND++;
											String newChar2="";
											String relParent="";
											newChar2=newNode.substring(0,1);
											if (newChar2.contentEquals("[")) {
												relParent="O";
												newNode=newNode.substring(1,newNode.length()-1);
											} else {
												relParent="M";
											}


											// ADDING NEW NODE

											nodeCounter++;
											TreeNode theNode=new TreeNode(); 
											theNode.idNode=nodeCounter;
											theNode.idParentNode=idParentNode;
											theNode.relType=relParent;
											theNode.relSiblings="AND";  //OJO CON EL EJE
											theNode.type="BASIC";
											theNode.nameNode=newNode;
											theNode.idFeature=0;
											theNode.nameFeature=newNode;
											theNode.alias=newNode;
											theNode.totChildren=0;
											theNode.level=parentLevel+1;
											Feature thFeature=new Feature();
											thFeature.description="";
											thFeature.name=newNode;
											ArrayList<String> stDummy=new ArrayList<String>();
											thFeature.supertypes=stDummy;
											theNode.feature=thFeature;

											//theNode.relation=rs.getString("relation");
											//theNode.requirement=rs.getString("requirement");
											eTree.add(theNode);		
											newNode="";
											tChildren++;
										}
									} else {
										newNode=newNode+myCar;
										//System.out.println(newNode); 
									}
								} else {
									isFinal=true;
								}
							} // fin bucle while que recorre la l�nea
							
							//actualizar contadores del padre (totChildren)
							Iterator<TreeNode> it6 = eTree.iterator();
							boolean moreNodes=it6.hasNext();
							boolean isFound2=false;
							int idNo=0;
							while ((!isFound2) && (moreNodes)) {
								TreeNode tn=it6.next();
								if (newFeature.contentEquals(tn.nameNode)) {
									if (tAND>1) tAND=1;
									tn.tAND=tAND;
									tn.tOR=tOR;
									tn.tXOR=tXOR;
									tn.totChildren=tChildren;
									isFound2=true;
									tAND=0;
									tOR=0;
									tXOR=0;
								}
								moreNodes=it6.hasNext();
							}	
							break;
						case 2:
							// %Constraints

							// This case decompose phrase in subject, verb and predicate. Check if all of these are correct and store the information
							// in eConstraints. You can bypass all the checks, so store directly the information (the features are not transformed in
							// this process in any way.

							// LOOKING FOR INCLUDES/EXCLUDES/IMPLIES WORD
							if ((line.indexOf("REQUIRES")>0)||(line.indexOf("EXCLUDES")>0)||(line.indexOf("IMPLIES")>0)) {
								// There are nodes in the tree
								boolean isFound=false;
								Iterator<TreeNode> it = eTree.iterator();
								boolean hayMas=false;
								boolean isSubjectFound=false;
								boolean isPredicateFound=false;
								boolean isDependency=false;
								String dependency="";

								line=line.substring(0,line.length()-1);
								String[] parts = line.trim().split(" ");

								if (parts.length>=3 ) {

									String theSubject=parts[0];
									String thePredicate=parts[2];
									String theDependency=parts[1];

									it = eTree.iterator();
									hayMas=it.hasNext();

									hayMas=it.hasNext();
									int idFeatureSubject=0;
									int idFeaturePredicate=0;

									// LOOKING FOR VALID SUBJECT
									while ((hayMas) && (!isSubjectFound)) {
										TreeNode theTreeNode=it.next();
										if (theSubject.contentEquals(theTreeNode.nameFeature)) {
											idFeatureSubject=theTreeNode.idNode;					        		
											isSubjectFound=true;
										}
										hayMas= it.hasNext();
									}	
									if (!isSubjectFound) {
										System.out.println("Error, subject feature "+ theSubject + " not found! ... "+line); 
										System.exit(31);
									}			

									
									it = eTree.iterator();
									hayMas=it.hasNext();

									// LOOKING FOR VALID PREDICATE
						        	String nameFea="";
									while ((hayMas) && (!isPredicateFound)) {
							        	TreeNode theTreeNode=it.next();
							        	nameFea=theTreeNode.nameFeature;
							        	if (thePredicate.contentEquals(nameFea)) {
							        		idFeaturePredicate=theTreeNode.idNode;					        		
							        		isPredicateFound=true;
							        	}
										hayMas= it.hasNext();
							        }	
									if (!isPredicateFound) {
										System.out.println("Error, subject feature "+ thePredicate + " not found! ... "+line); 
										System.exit(32);
									} 
									
									isPredicateFound=true;

									if (theDependency.indexOf("REQUIRES")>-1) {
										dependency="R";
										isDependency=true;
										String[] thePre = line.trim().split(" REQUIRES ");
										thePredicate=thePre[1];
									} else if (theDependency.indexOf("IMPLIES")>-1) {
										dependency="I";
										isDependency=true;
										String[] thePre = line.trim().split(" IMPLIES ");
										thePredicate=thePre[1];
									} else if (theDependency.indexOf("EXCLUDES")>-1) {
										dependency="E";
										isDependency=true;
										String[] thePre = line.trim().split(" EXCLUDES ");
										thePredicate=thePre[1];

									} else {
										System.out.println(theDependency+" Dependency not identified. Type not supported");
									}

									if (( isSubjectFound) && (isPredicateFound) && (isDependency)) {



										Constraint theConstraint=new Constraint();
										theConstraint.featureSubject=theSubject;
										theConstraint.featurePredicate=thePredicate;
										theConstraint.relationType=dependency;
										eConstraints.add(theConstraint);
									} else {
										System.out.println("�Pero esto qu� es!"); 
									}

								} else {
									System.out.println("Sentence error. Too much words");

								}

							} else {
								System.out.println("REQUIRES/EXCLUDES/IMPLIES MISSSING. Bypassing sentence"); 

							}

							// 
							break;
						case 3:
							// %Attributes
							eAttributes.add(line);
							break;
						case 4:
							// %Supertypes
							//
							// 1. split into subject and predicate
							String theSubject="";
							String thePredicate="";
							String sep="";
							String[] theParts=null;
							if (line.indexOf(":")>0) {
								sep=":";
							} else if (line.indexOf("IS COMPATIBLE WITH")>0) { 
								sep="IS COMPATIBLE WITH";
							} else {
								System.out.println("ERROR DE SINTAXIS EN DEFINICION DE SUPERTIPO - faltan dos puntos - "+line);
								System.exit(1);
							}
							theParts = line.split(sep);
							if (theParts.length!=2) {
								System.out.println("ERROR DE SINTAXIS EN DEFINICION DE SUPERTIPO - falta sujeto o predicado - "+line);
								System.exit(2);
							} else if ((theParts[0].length()==0) || (theParts[1].length()==0)) {
								System.out.println("ERROR DE SINTAXIS EN DEFINICION DE SUPERTIPO - falta sujeto o predicado - "+line);
								System.exit(3);
							}

							theSubject=theParts[0].trim();
							thePredicate=theParts[1].trim();
							if (thePredicate.substring(thePredicate.length()-1, thePredicate.length()).equals(";")) {
								thePredicate=thePredicate.substring(0, thePredicate.length()-1);
							}
							int idFeature=0;

							//convert from String[] to ArrayList<String>
							String[] aux=thePredicate.split(",");
							for (int j=0;j<aux.length;j++){
								if (aux[j].contains(" ")) {
									System.out.println("ERROR EN DEFINICION DE SUPERTIPO - separador de supertipos es la coma - "+line);
									System.exit(36);
								}
							}							
							ArrayList<String> theBits=new ArrayList<String>();
							theBits=utiles.convertArrayToArrayListString(aux);
							boolean res3=false;
							res3=setSupertypesFeatureNode(theSubject,theBits);
							if (res3) {
								System.out.println("Supertipos "+thePredicate + " conectados a "+theSubject);
							} else {
								System.out.println("ERROR, FEATURE NOT FOUND, SUPERTYPES NOT ATTACHED");
								
							}

							break;
						case 5:
							// SupertypesPriority
							System.out.println("Seccion SupertypesPriority");
							String[] supertypeList = line.split(",");
							eSupertypesPriority=supertypeList;

							break;
						case 6:
							// Comments
							eComments.add(line);
							break;
						default:
						}
					} else {
						errCode=1;
						errMessage="Section not identified";
					}

				}

			}
		}
		calculateLevel();
		return true;
	}	
	 boolean populateFMfromXMLFile(String xmlFile) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			out = new PrintWriter (System.out);
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(
					new org.xml.sax.ErrorHandler() {
						 public void fatalError(SAXParseException exception)	throws SAXException {
						}
						 public void error(SAXParseException e) throws SAXParseException  {
							throw e;
						}
						 public void warning(SAXParseException err) throws SAXParseException   {
							System.out.println("** Warning"
									+ ", line " + err.getLineNumber()
									+ ", uri " + err.getSystemId());
							System.out.println("   " + err.getMessage());
						}
					}
					); 

			document = builder.parse(xmlFile);
			int parentLevel=0;
			eTree.clear();
			eConstraints.clear();
			nodeParents.clear();
			contSubSet=0;

			processXMLNodes(document);

		} catch (ParserConfigurationException pexc) { 
			pexc.printStackTrace();
		} catch (Exception exc) {
			System.out.println("   " + exc.getMessage() );

		}	

		return true;
	}

	 void processXMLNodes(Node node) {
		String valor;
		String atributo;
		TreeNode theNode=null;     
		int parentLevel=0;
		int idFeatureParent=0;
		int idParentNode=0;
		int nodeParent=0;
		
		
		if ( node == null ) {
			return;
		}
		int type = node.getNodeType();
		switch ( type ) {
		// Imprimir Documento 
		case Node.DOCUMENT_NODE: { //typeNode 0
			NodeList children = node.getChildNodes();
			for ( int iChild = 0; iChild < children.getLength(); iChild++ ) {
				processXMLNodes(children.item(iChild));
			}
			out.flush();
			break;
		}

		// Imprimir elementos con atributos
		case Node.ELEMENT_NODE: {  //typeNode 1
			out.print('<');
			out.print(node.getNodeName());
			String elementName="";
			elementName=node.getNodeName();
			NodeList children=null;
			String theChild="";
			
			Attr attrs[] = sortAttributes(node.getAttributes());
			out.flush();

			switch (elementName) { 
			case "feature-model":  // cabecera, no hace falta procesar esto
				out.print('>');
				out.flush();
				children = node.getChildNodes();
				if ( children != null ) {
					int len = children.getLength();
					for ( int i = 0; i < len; i++ ) {
						processXMLNodes(children.item(i));
					}
				}

				break;
			case "feature": //nodo raiz
				for ( int i = 0; i < attrs.length; i++ ) {
					Attr attr = attrs[i];
					out.print(' ');
					out.print(attr.getNodeName());
					out.flush();
					out.print("=\"");
					out.print(attr.getNodeValue());
					out.flush();
					out.print('"');
					out.flush();
					theChild=attr.getNodeName();
					switch (theChild) {
					case "name":
						theFM.name= attr.getNodeValue();
						break;
					default:
						break;
					}
				}
				out.print('>');

				children = node.getChildNodes();
				nodeCounter++;
				Feature myFeature=new Feature();
				myFeature.name=theFM.name;
				myFeature.supertypes=new ArrayList<String>();

				theNode=new TreeNode();
				theNode.feature=myFeature; 
				theNode.idNode=nodeCounter;
				theNode.idParentNode=0;
				theNode.nameNode=theFM.name;
				theNode.idFeature=0;
				theNode.nameFeature=theFM.name;
				theNode.alias=theFM.name;
				theNode.relType="M";
				theNode.totChildren=children.getLength();
				theNode.relSiblings="AND";
				theNode.level=0;
				eTree.add(theNode);
				nodeParents.push(theNode.idNode);
				nodeParentNow=theNode.idNode;
				
				lastXMLNode="feature";


				if ( children != null ) {
					int len = children.getLength();
					for ( int i = 0; i < len; i++ ) {
						processXMLNodes(children.item(i));
					}
				}	       			
				break;
			case "binaryRelation":
				out.print('>');
				out.flush();
				xmlMinCardin=0;
				xmlMaxCardin=0;

				lastXMLNode="binaryRelation";

				children = node.getChildNodes();
				if ( children != null ) {
					int len = children.getLength();
					for ( int i = 0; i < len; i++ ) {
						//System.out.println(children.item(i).getNodeName());
						processXMLNodes(children.item(i));
					}
				}		       			
				break;
			case "cardinality":
				for ( int i = 0; i < attrs.length; i++ ) {
					Attr attr = attrs[i];
					out.print(' ');
					out.print(attr.getNodeName());
					out.flush();
					out.print("=\"");
					out.print(attr.getNodeValue());
					out.flush();
					out.print('"');
					out.flush();

					atributo=attr.getNodeName();
					valor=attr.getNodeValue();
					switch (atributo) {
					case ("max"):
						xmlMaxCardin=Integer.parseInt(valor);
					break;
					case ("min"):
						xmlMinCardin=Integer.parseInt(valor);
					break;
					default:
						System.out.println("ATTR NO ESPERADA EN BINARY RELATION");
						break;
					}	       			
 
				}	   
				out.print('>');
				out.flush();
				
				if (lastXMLNode.equals("setRelation")) {
					boolean isFound=false;
					TreeNode theTreeNode1;
					TreeNode theTreeNode2;
					Iterator itr = eTree.iterator();
					while ((isFound==false) && (itr.hasNext())) {
						theTreeNode1=(TreeNode) itr.next();
						if (theTreeNode1.nameFeature.contentEquals(lastName)) {
							theTreeNode2=theTreeNode1;
							theTreeNode2.maxCardinality=xmlMaxCardin;
							theTreeNode2.minCardinality=xmlMinCardin;
							isFound=true;
							eTree.remove(theTreeNode1);
							eTree.add(theTreeNode2);
						}
					}
									
				}

				break;
			case "solitaryFeature":  
				//recorremos atributos 
				for ( int i = 0; i < attrs.length; i++ ) {
					Attr attr = attrs[i];
					out.print(' ');
					out.print(attr.getNodeName());
					out.flush();
					out.print("=\"");
					out.print(attr.getNodeValue());
					out.flush();
					out.print('"');
					out.flush();

					atributo=attr.getNodeName();
					valor=attr.getNodeValue();
					switch (atributo) {
					case ("name"):
						myFeature=new Feature();
						myFeature.name=valor;
						//myFeature.supertypes=new ArrayList<String>();
						nodeCounter++;
						theNode=new TreeNode();
						theNode.feature=myFeature; 
						theNode.idNode=nodeCounter;
						theNode.idParentNode=nodeParentNow;
						theNode.nameNode=valor;
						theNode.idFeature=0;
						theNode.nameFeature=valor;
						theNode.alias=valor;
						theNode.minCardinality=xmlMinCardin;
						theNode.maxCardinality=xmlMaxCardin;
						if ((xmlMinCardin==0)&&(xmlMaxCardin==1)) {
							theNode.relType="O";
						}else {
							theNode.relType="M";	
						}
					
						//theNode.totChildren=childrenTotal;
						theNode.relSiblings="AND";
						theNode.level=nodeParents.size();
						eTree.add(theNode);	
						//newNode="";
						nodeParents.push(theNode.idNode);
						nodeParentNow=theNode.idNode;

					break;
					default:
						System.out.println("ATTR NO ESPERADA EN solitaryFeature");
					}	       			
				}		
				out.print('>');
				out.flush();

				lastXMLNode="solitaryFeature";
				//recorremos hijos
				children = node.getChildNodes();
				if ( children != null ) {
					int len = children.getLength();
					for ( int i = 0; i < len; i++ ) {
						//System.out.println(children.item(i).getNodeName());
						processXMLNodes(children.item(i));
					}
				}
				nodeParentNow=(int) nodeParents.pop();
				nodeParentNow=(int) nodeParents.peek();

				

				break;
			case "setRelation":
				String newSubSet="";
				
				for ( int i = 0; i < attrs.length; i++ ) {
					Attr attr = attrs[i];
					out.print(' ');
					out.print(attr.getNodeName());
					out.flush();
					out.print("=\"");
					out.print(attr.getNodeValue());
					out.flush();
					out.print('"');
					out.flush();

					atributo=attr.getNodeName();
					valor=attr.getNodeValue();
					switch (atributo) {
					case ("name"):
						newSubSet=valor;
					    if (valor.contentEquals("R-4")) {
					    	System.out.println("ATENCION!");
					    }
					    if (valor.contentEquals("R-5")) {
					    	System.out.println("ATENCION!");
					    }
					break;
					default:
						System.out.println("ATTR NO ESPERADA EN SET-RELATION");
						break;
					}	       			

				}	
				
				out.print('>');
				out.flush();
				xmlMinCardin=0;
				xmlMaxCardin=0;

				//escribimos una feature auxiliar
				//System.out.println("escribimos feature set" );
				contSubSet++;
				if (newSubSet.contentEquals("")) {
					newSubSet="SET-"+contSubSet+"-FEATURE";
				}


				nodeCounter++;
				theNode=new TreeNode(); 
				theNode.idNode=nodeCounter;
				theNode.idParentNode=nodeParentNow;
				theNode.type="SET";
				theNode.nameNode=newSubSet;
				theNode.idFeature=0;
				theNode.nameFeature=newSubSet;
				theNode.alias=newSubSet;
				theNode.minCardinality=xmlMinCardin;
				theNode.maxCardinality=xmlMaxCardin;
				theNode.totChildren=0;
				if (xmlMaxCardin==1) {
					theNode.relSiblings="XOR";
				}else if (xmlMinCardin==xmlMaxCardin) {
					theNode.relSiblings="OR";
				} else {
					theNode.relSiblings="AND";
				}
				theNode.level=nodeParents.size();
				Feature thFeature=new Feature();
				thFeature.description="";
				thFeature.name=newSubSet;
				thFeature.supertypes=new ArrayList<String>();										        
				theNode.feature=thFeature;
				eTree.add(theNode);
				nodeParents.push(theNode.idNode);
				nodeParentNow=theNode.idNode;
				lastXMLNode="setRelation";
				lastName=newSubSet;
				children = node.getChildNodes();
				if ( children != null ) {
					int len = children.getLength();
					for ( int i = 0; i < len; i++ ) {
						//System.out.println(children.item(i).getNodeName());
						processXMLNodes(children.item(i));
					}
				}	
				nodeParentNow = (int)nodeParents.pop();
				nodeParentNow = (int)nodeParents.peek();				
				break;
			case "groupedFeature":
				//recorremos atributos para buscar el nombre, s�lo debe haber un atributo
				for ( int i = 0; i < attrs.length; i++ ) {
					Attr attr = attrs[i];
					out.print(' ');
					out.print(attr.getNodeName());
					out.flush();
					out.print("=\"");
					out.print(attr.getNodeValue());
					out.flush();
					out.print('"');
					out.flush();

					atributo=attr.getNodeName();
					valor=attr.getNodeValue();
					switch (atributo) {
					case ("name"):
						myFeature=new Feature();
					myFeature.name=valor;
					//myFeature.supertypes=new ArrayList<String>();


					nodeCounter++;
					theNode=new TreeNode();
					theNode.feature=myFeature; 
					theNode.idNode=nodeCounter;
					theNode.idParentNode=nodeParentNow;
					theNode.nameNode=valor;
					theNode.idFeature=0;
					theNode.nameFeature=valor;
					theNode.alias=valor;
					theNode.minCardinality=xmlMinCardin;
					theNode.maxCardinality=xmlMaxCardin;
					theNode.type="SET";
					//theNode.totChildren=childrenTotal;
					theNode.relSiblings="AND";
					theNode.level=nodeParents.size();
					eTree.add(theNode);	
					//newNode="";
					nodeParents.push(theNode.idNode);
					nodeParentNow=theNode.idNode;

				
					break;
					default:
						System.out.println("ATTR NO ESPERADA EN groupedFeature");
					}	       			
				}	
				
				out.print('>');
				out.flush();
				
				lastXMLNode="groupedRelation";
				

				//recorremos hijos por si hubiera
				children = node.getChildNodes();
				if ( children != null ) {
					int len = children.getLength();
					for ( int i = 0; i < len; i++ ) {
						//System.out.println(children.item(i).getNodeName());
						processXMLNodes(children.item(i));
					}
				}		
				nodeParentNow=(int) nodeParents.pop();
				nodeParentNow=(int) nodeParents.peek();

				break;
			case "excludes":
			case "requires":
				String constraintType="";
				String featureSubject="";
				String featureObject="";
				String dependency="";
				
				for ( int i = 0; i < attrs.length; i++ ) {
					Attr attr = attrs[i];
					out.print(' ');
					out.print(attr.getNodeName());
					out.flush();
					out.print("=\"");
					out.print(attr.getNodeValue());
					out.flush();
					out.print('"');
					out.flush();
					theChild=attr.getNodeName();
					switch (theChild) {
					case "name":
						break;
					case "feature":
						featureSubject=attr.getNodeValue();
						break;
					case "excludes":
						constraintType="excludes";
						dependency="E";
						featureObject=attr.getNodeValue();
						break;
					case "requires":
						constraintType="requires";
						dependency="R";
						featureObject=attr.getNodeValue();
						break;
					default:
						System.out.println("yoquese");
						break;
					}
				}	
				
				out.print('>');
				out.flush();				

				Iterator<TreeNode> it = eTree.iterator();

				boolean hayMas=it.hasNext();
				boolean isSubjectFound=false;
				boolean isObjectFound=false;
				int idFeatureSubject=0;
				int idFeaturePredicate=0;

				// SEEKING VALID SUBJECT
				while ((hayMas) && (!isSubjectFound)) {
					TreeNode theTreeNode=it.next();
					if (featureSubject.contentEquals(theTreeNode.nameFeature)) {
						idFeatureSubject=theTreeNode.idNode;					        		
						isSubjectFound=true;
					}
				}	
				if (!isSubjectFound) {
					System.out.println("Error, subject feature not found!"); 
				}			

				/*
					it = eTree.iterator();
					hayMas=it.hasNext();

					// LOOKING FOR VALID PREDICATE
		        	String nameFea="";
					while ((hayMas) && (!isPredicateFound)) {
			        	TreeNode theTreeNode=it.next();
			        	nameFea=theTreeNode.nameFeature;
			        	if (thePredicate.contentEquals(nameFea)) {
			        		idFeaturePredicate=theTreeNode.idNode;					        		
			        		isPredicateFound=true;
			        	}
			        }	
					if (!isPredicateFound) {
						System.out.println("Error, predicate feature not found!"); 
					} 
				 */
				isObjectFound=true;
				if (( isSubjectFound) && (isObjectFound) ) {
					Constraint theConstraint=new Constraint();
					theConstraint.featureSubject=featureSubject;
					theConstraint.featurePredicate=featureObject;
					theConstraint.relationType=dependency;
					eConstraints.add(theConstraint);
				} else {
					System.out.println("�Pero esto qu� es!"); 
				}				
				break;
			default:
				break;
			}
		}

		// Imprimir Texto 
		case Node.TEXT_NODE: { // type 3
			out.print(node.getNodeValue());
			out.flush();
			break;
		}

		// Imprimir Nodos con Instrucciones de Proceso 
		case Node.PROCESSING_INSTRUCTION_NODE: {
			out.print("<?");
			out.print(node.getNodeName());
			String data = node.getNodeValue();
			if ( data != null && data.length() > 0 ) {
				out.print(' ');
				out.print(data);
			}
			out.print("?>");
			out.flush();
			break;
		}

		// Imprimir Texto de Elementos CDATA
		case Node.CDATA_SECTION_NODE: {
			System.out.print(node.getNodeValue());
			break;  
		}

		} // Termina Switch

		// En caso de ser nodo de Elemento cerrar Tag en Pantalla 
		if ( type == Node.ELEMENT_NODE ) {
			out.print("</");
			out.print(node.getNodeName());
			out.print('>');
		}

		// Enviar a Pantalla Buffer
		out.flush();

	} // Termina Impresion de Nodos 

	 int processConfig (String pathScript) {
		int resul=0;
		int lineCounter=0;
		String line="";
		String theChar="";
		String firstPart="";
		String secondPart="";
		int x=0;
		scriptFileBase=fileRead(pathScript);
		boolean endFile=false;
		while (!endFile) {
			boolean isLine=false;
			while ((!endFile) && (!isLine)) {
				// 1. reading line
				String sentence="";
				String arguments="";
				line=scriptFileBase.get(x).trim();
				lineCounter++;
				System.out.println("Linea "+lineCounter+":"+line);

				if ((line.length()>0) && (!line.substring(0,1).contentEquals(";"))) { 
					// if first char is ; => is comment
					isLine=true;
				}
				x++;

				if (isLine) {
					//System.out.println("+|"+line+"|+");
					theChar=line.substring(0,1);
					if (line.contains("=")) {
						String[] parts=line.split("=");
						sentence=parts[0].toLowerCase().trim();
						arguments=parts[1].trim();
					} else {
						sentence=line;
						arguments="";
					}

					switch (sentence) {
					case "mode": 
						gMode=Integer.parseInt(arguments);
						break;
					case "out":
						gOut=Integer.parseInt(arguments);
						break;
					case "fileout":
						gFileout=arguments;
						break;
					case "featuremodel":
						gFeatureModel=arguments;
						break;
					case "fmscript":
						gFMScript=arguments;
						break;
					default: 
						System.out.println("Error reading script file. Sentence not processed.\n"+line);
						break;		
					}
					if (x==scriptFileBase.size()) {
						endFile=true;
					}
				}
			}
		}
		return resul;			
	}
		
	
	 int processScript (String pathScript) {
		int resul=0;
		int lineCounter=0;
		ArrayList<String> scriptFile = new ArrayList<String>();
		
		String line="";
		String theChar="";
		String firstPart="";
		String secondPart="";
		int x=0;
		scriptFile=fileRead(pathScript);
		boolean endFile=false; 
		boolean isLine=false;
		
		while (!endFile) {
			// 1. reading line
			String sentence="";
			String arguments="";
			boolean isComment=false;
			line=scriptFile.get(x).trim();
			lineCounter++;
			System.out.println("Linea "+lineCounter+":"+line);

			if (line.trim().length()==0) {
				isLine=false;
			} else {
				isLine=true;
			}
			x++;

			if (isLine) {
				//System.out.println("+|"+line+"|+");
				theChar=line.substring(0,1);
				if ((theChar.contentEquals(";"))||(theChar.contentEquals("%"))) { 
					// if first char is ; => is comment

				} else {
					if ((sentence.length()>0) && (sentence.substring(1,1).contentEquals(";") )) {
						isComment=true;
					}
					String miCa=line.substring(line.length()-1,line.length());
					if (miCa.contentEquals(";")) {
						line=line.substring(0,line.length()-1).trim();
					}
						
					
					if (line.contains("(")) {
						String[] parts;
						line=line.replace("\\", "/");
						parts=line.split("[(]");
						sentence=parts[0].toLowerCase().trim();
						arguments=parts[1].substring(0,parts[1].length()-1).trim();
					} else {
						sentence=line.toLowerCase();
						arguments="";
					}

					String[] arr=null;
					if (arguments.length()>0) {
						arr=arguments.split(",");
					}

					switch (sentence) {
					case "add_feature":
						if (gPathFMFile.trim().length()==0) {
							System.out.println("Fichero FM no establecido. �Se ha lanzado LOAD_FM?");
							System.exit(30);
						}
						//resetSystem();
						//readFMFile(gPathFMFile); //borrar
						//populateFMfromFMFile(fmFile); //borrar
						//String featureName,String description,String relation, String supertypes
						addFAuto(arr[0],"",arr[1],arr[2],arr[3]);
						//showFeatureModelTextLog(eTree);  //borrar
						//showTreeValues(eTree,"L"); //borrar							
						break;					
					case "check_for_changes":
						if (arr.length==2) {	
							int maxretrys=0;
							int secondsbetween=0;
							maxretrys=Integer.valueOf(arr[0]);
							secondsbetween=Integer.valueOf(arr[1]);
							String myUrl = "http://www.pclandia.net/ruva/reconf.inf";
							String salida="";
							boolean isChanged=false;
							int countretry=0;
							while ((!isChanged)&&(countretry<maxretrys)) {
								try {
									countretry++;
									salida=utiles.getHTML(myUrl);
									System.out.println("Cambios ENCONTRADOS");
									String[] arr2=null;
									arr2=salida.split(";");
									// addFAutoParent(arr2[1],arr[2]);
									isChanged=true;								
								} catch (FileNotFoundException fe){
									System.out.println("Cambios no encontrados. Reintentando en 60 segundos");
									try {
										TimeUnit.SECONDS.sleep(secondsbetween);
									} catch	(Exception e2) {
										e2.printStackTrace();
									}
									
								} catch (Exception e){
									e.printStackTrace();
								}
								
							}
						} else {
							System.out.println("Arguments number mismatch. Expected 1 (path). ");
						}
						break;
					case "cls":
						totalSuccess=0;
						totalFailures=0;
						// gLog.clear();
						break;
					case "csv2xmlfi":
						if (arr.length==2) {
							String source="";
							String target="";
							source=arr[0];
							target=arr[1];
							target=arr[0].substring(0,arr[0].length()-3)+"xml";
							csv2xmlfi(source,target);							    	
							gLog.add("Generated "+arr[1]);
						}
						break;
					case "flush_log":
						if (gLogAccumulative) {
							writeFileAccumulative(gLogPathName,gLog);
						} else {
							writeFile(gLogPathName,gLog);
						}
						gLog.clear();
						break;
					case "fm2xmlfi":
						if (arr.length==2) {
							String source="";
							String target="";
							source=arr[0];
							target=arr[1];
							target=arr[0].substring(0,arr[0].length()-3)+"xml";
							fm2xmlfi(source,target);							    	
							gLog.add("Generated "+arr[1]);
						}
						break;						
					case "generate_addfeature_set":
						if (arr.length==4) {
							int sizeSet=Integer.valueOf(arr[0]);
							int supertypeSet=Integer.valueOf(arr[1]);
							int maxSupertypes=Integer.valueOf(arr[2]);
							generateAddFeatureSet(sizeSet,supertypeSet,maxSupertypes,arr[3]);
						}else {
							System.out.println("Mas parametros de los esperados: "+arr.length);
							System.exit(18);
						}
						break;
					case "include_init":
						System.out.println(arr.length);
						// resetSystem();
						if (arr.length==1) {
							gLog.add("Procesando: "+arr[0]);							
							processScript(arr[0]);
							File ff=new File(arr[0]);
							gLogResult.add(ff.getName()+","+totalSuccess+ ","+totalFailures +","+theMilisegundos);
							writeFileAccumulative("D:\\workspace\\201903\\features\\resultado.log",gLogResult);
							gLogResult.clear();
						} else {
							System.out.println("Arguments number mismatch. Expected 1 (path). ");
						}
						break;
					case "load_fm": 
						System.out.println(arr.length);
						resetSystem();
						
						if (arr.length==1) {
							String myPath=arr[0].substring(0, arr[0].length());
							typeOfFMFile=getTypeFMFile(myPath);
							gPathFMFile=myPath;
							
						    switch (typeOfFMFile) {  //1=ruva;2=fm;3=xml(FaMa);4=xml(FeatureIde);5=afm(FaMa)
						    case 1: //ruva
						    	break;
						    case 2: //.fm
								readFMFile(gPathFMFile);
								populateFMfromFMFile(fmFile);
								gLog.add("Loaded "+gPathFMFile);
						    	break;
						    case 3: // xml fama
						    	populateFMfromXMLFile(gPathFMFile);
								gLog.add("Loaded "+gPathFMFile);
						    	break;
						    case 4: // xml FeatureIDE
						    	break;
						    case 5: // afm FaMa 
						    	break;
						    default:
						    	break;
						    }
							//showFeatureModel();								
						} else {
							System.out.println("Arguments number mismatch. Expected 1 (path). ");
						}
						break;
					case "load_fm_fama": 
						System.out.println(arr.length);
						resetSystem();
						if (arr.length==1) {
							String myPath=arr[0].substring(0, arr[0].length());
							gPathFMFile=myPath;
							readFMFile(gPathFMFile);
							populateFMfromFMFile(fmFile);
							gLog.add("Loaded "+gPathFMFile);
					    	break;
							//showFeatureModel();								
						} else {
							System.out.println("Arguments number mismatch. Expected 1 (path). ");
						}
						break;							
					case "load_fm_xml": 
						System.out.println(arr.length);
						resetSystem();
						if (arr.length==1) {
							String myPath=arr[0].substring(0, arr[0].length());
							gPathFMFile=myPath;
					    	populateFMfromXMLFile(myPath);
							gLog.add("Loaded "+gPathFMFile);
					    	break;
							//showFeatureModel();								
						} else {
							System.out.println("Arguments number mismatch. Expected 1 (path). ");
						}
						break;		
					case "log_accumulative":
						gLogAccumulative=true;
						break;
					case "log_absolute":
						gLogAccumulative=false;
						break;
					case "log_set_name":
						if (arr.length==1) { // si viene un parametro le metemos como path del log
							if (arr[0].trim().contentEquals("default")) {
								File f=new File(pathScript);
								gLogPathName=f.getParent()+"\\"+f.getName()+".log";
							}else {
								gLogPathName=arr[0].trim();
							}
							gLog.add("Path log: "+gLogPathName);
						} else {
							System.out.println("Movidon, hay menos o mas de 1 parametro!");
						}
						break;
					case "record_time_start":
						insertTimeStart = System.nanoTime();
						break;
					case "record_time_stop":
						insertTimeEnd = System.nanoTime();
						break;
					case "reload_fm": 
						resetSystem();
						
						if (gPathFMFile.length()==1) {
							String myPath=gPathFMFile;
							typeOfFMFile=getTypeFMFile(gPathFMFile);
							
						    switch (typeOfFMFile) {  //1=ruva;2=fm;3=xml(FaMa);4=xml(FeatureIde);5=afm(FaMa)
						    case 1: //ruva
						    	break;
						    case 2: //.fm
								readFMFile(gPathFMFile);
								populateFMfromFMFile(fmFile);
								gLog.add("Loaded "+gPathFMFile);
						    	break;
						    case 3: // xml fama
						    	populateFMfromXMLFile(gPathFMFile);
								gLog.add("Loaded "+gPathFMFile);
						    	break;
						    case 4: // xml FeatureIDE
						    	break;
						    case 5: // afm FaMa 
						    	break;
						    default:
						    	break;
						    }
							//showFeatureModel();								
						} else {
							System.out.println("Arguments number mismatch. Expected 1 (path). ");
						}		
						break;						
					case "remove_feature": 
						if (arr.length==1) { // si viene un parametro le metemos como path del log
							/*
							 * if (remoFeature(arr[0])) {
							 * 		gLog.add("Feature removed");
							 * }else{
							 * 		gLog.add("Feature NOT removed");
							 * }
							 */
							
						} else {
							System.out.println("Movidon, hay menos o mas de 1 parametro!");
						}						
						break;
					case "reset":
						resetSystem();
						// gLog.clear();
						break;
					case "show_feature_list":  
						showFeatureList(eTree,"L");
						break;
					case "show_feature_list_by_levels":  
						showFeaturesByLevels();
						break;
					case "show_fm":  
						showFeatureModelTextLog(eTree);
						break;
					case "show_fm_screen":
						showFeatureModelTextDefault();							
						break;
					case "show_branches":  
						showBranches(eTree,"L",3);
						break;
					case "show_statistics":  
						showStatistics();
						break;
					case "show_time":
						insertTimeRound = (insertTimeEnd - insertTimeStart) / 1000000000; //seconds
						insertTimeRound2 = (int) (insertTimeRound*1000); //seconds
					    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
					    formatter = new DecimalFormat("#0.00"); 	
					    theSegundos=String.format("%.2f", insertTimeRound);
					    theMilisegundos=String.format("%.0f", insertTimeRound2);
						gLog.add("INSERTION TIME USED: "+ theSegundos + " second(s) ");
						//gLog.add(dtf.format(LocalDateTime.now()));
						break;
					case "show_tree_values":  
						showTreeValues(eTree,"L");
						break;
					case "statistics":  
						showStatistics();
						break;
					case "sumarize":
						int tot=totalSuccess+totalFailures;
						gLog.add("TOTAL: "+tot);
						gLog.add("SUCCESS: "+totalSuccess);
						gLog.add("FAILURES: "+totalFailures);
						break;
					case "write_datetime":
						DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
						LocalDateTime now = LocalDateTime.now();  
						System.out.println(dtf2.format(now));
						gLog.add(dtf2.format(now));
						break;
					case "write_line":  
						System.out.println(arr[0]);
						gLog.add(arr[0]);
						break;
					case "xml2fm": //2 path separated by comma
						if (arr.length==2) {
							String source="";
							String target="";
							source=arr[0];
							target=arr[1];
							typeOfFMFile=getTypeFMFile(source);
							if (typeOfFMFile==3) {
						    	populateFMfromXMLFile(source);
								generateFMFile(0,eTree,fmFile);
								target=arr[0].substring(0,arr[0].length()-3)+"fm";
								writeFMFile(target);							    	
								gLog.add("Generated "+arr[1]);
							}
						}
				    	break;
					default: 
						gLog.add("Command not processed:"+sentence);
						System.out.println("Error reading script file. Sentence not processed:"+sentence);
						break;		
					}
				}
			}
			if (x==scriptFile.size()) {
				endFile=true;
			}
		}
		return resul;			
	}
	
	
	 boolean readFMFile(String fmPathFilename) {
		// readfile
		boolean resul=false;

		//first, cleaning ArrayList to proceed to populate
		fmFile.clear();
		BufferedReader reader;
		try {
			System.out.println("Working directory path: "+ System.getProperty("user.dir"));
			reader = new BufferedReader(new FileReader(fmPathFilename));
			String line = reader.readLine();
			while (line != null) {
				//System.out.println(line);
				fmFile.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			resul=false;
		} catch (Exception e) {
			System.out.println("Fichero no encontrado: "+fmPathFilename);
			System.exit(37);
		}
		return resul;			
	}
	 void removeFeatureAuto(String featureName) {
		// Clone eTree to eTree2
		Collection eTree2 = new ArrayList<TreeNode>();
		for (Iterator<TreeNode> it = eTree.iterator(); it.hasNext();) {
			TreeNode theTreeNode=it.next();
			if (isAncestor(theTreeNode.nameFeature,featureName,eTree)) {
				eTree2.add(theTreeNode);
			}
		}

		// Seek for son nodes
		
		// remove son nodes
		
		// check constraints affected and delete constraint if affected
		
		// check if model is valid
		
		// move eTree2 to eTree

	}
	
	 void replaceFeature(Collection eTree, Feature oldFeature, Feature newFeature ) {
		
	}
	 void resetSystem(){
//		eTree= new ArrayList<TreeNode>(); 
//		eTreeWinner= new ArrayList<TreeNode>();
		eTree.clear();
		eTreeWinner.clear();
		eConstraints.clear();
		eFeatures.clear();
		eSupertypes.clear();
		eFeaturesSupertypes.clear();
		eAttributes.clear();
		theFM=new FeatureModel();
		eComments.clear();
		//eSupertypesPriority=
		nodeParents.clear();
		nodeParentNow=0;
		contSubSet=0;
		lastXMLNode="";
		lastName="";
		feaTesting="";
		feaParentTesting="";
//		totalSuccess=0;
//		totalFailures=0;
		nodeCounter=0;
		featCounter=0;
		systemId=0;
		idParentNodeXML=0;
		xmlMinCardin=0;
		xmlMaxCardin=0;
		systemName="";
		errCode=0;
		errMessage="";
		isError=false;
		fmPathFilename="";
		maxLevel=0;

		
		
	}
	 boolean sendRuvaCommand(String msg) {
		boolean ret=false;

		return ret;
	}
	 void showConstraints() {
		try {
			Iterator itr = eConstraints.iterator();
			int count=0;
			while (itr.hasNext()) {
				count++;
				Constraint theConstraint=(Constraint) itr.next();
				String theFeatureSubject=theConstraint.featureSubject;
				String theFeaturePredicate=theConstraint.featurePredicate;

				System.out.println("Constraint "+count+": "+theFeatureSubject+" "+theConstraint.relationType+" "+theFeaturePredicate);			
			}
		}  
		catch (Exception e)	    {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}		
	}
	 void showFeatureModelTextLog(Collection theTree){
		int theLevel=0;
		int theNode=0;
		gLog.add("=================================================================================================================");
		
		if (systemName.length()>0) {
			gLog.add("System: "+systemName + " ("+systemId+")");
		}else {
			gLog.add("TREE");
		}
		gLog.add("---------------------------");
		gLog.add("--- BEGIN FEATURE MODEL ---");
		gLog.add("|");
		showSystemChildrenLog(theTree,theLevel,theNode);
		gLog.add("|");		
		gLog.add("---- END FEATURE MODEL ----");
	}
	
	 void showFeatureModelText(Collection theTree){
		int theLevel=0;
		int theNode=0;
		
		System.out.println("System: "+systemName + " ("+systemId+")");
		System.out.println("=========================");
		System.out.println("=== BEGIN FEATURE MODEL ===");
		System.out.println("|");
		showSystemChildren(theTree,theLevel,theNode);
		System.out.println("|");		
		System.out.println("=== END FEATURE MODEL ===");
	}

	 void showFeatureModelTextDefault(){
		showFeatureModelText(eTree);
	}	

	 void showFeatures() {
		try {
			int count=0;
			Iterator itr = eFeatures.iterator();
			while (itr.hasNext()) {
				count++;
				Feature theFeature=(Feature) itr.next();
				if (count<26){
					//System.out.println(theFeature.featureName+" (id="+theFeature.idFeature+")");			
					System.out.println(theFeature.name);	
				}
			}
		}  
		catch (Exception e)	    {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}		
	}	
	 void showFeaturesByLevels() {
		Iterator itr;	
		int depth=0;
		int lastCounter=0;
		int level=-1;
		String phrase="";
		gLog.add("FEATURE LEVEL LIST");
		gLog.add("==================");
		while ((lastCounter>0) || (level==-1)) {
			level++;
			int count=0;
			
			try {
				itr = eTree.iterator();
				phrase="";
				while (itr.hasNext()) {
					TreeNode theNode=(TreeNode) itr.next();
					if (theNode.level==level){
						count++;
						phrase+=" "+theNode.nameFeature;
					}
				}
				
			}  
			catch (Exception e)	    {
				System.err.println("Got an exception! ");
				System.err.println(e.getMessage());
			}
			lastCounter=count;
			if (phrase.length()>0) {
				gLog.add("LEVEL " + level  + ": " +phrase);
			}
			
		}
	}	
	 void showFeaturesToAdd() {
		try {
			int count=0;
			Iterator itr = eFeatures.iterator();
			while (itr.hasNext()) {
				count++;
				Feature theFeature=(Feature) itr.next();
				if (count>26){
					System.out.println(theFeature.name);
				}
			}
		}  
		catch (Exception e)	    {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}		
	}



	 boolean setSupertypesFeatureNode(String theFeatureNameNew,ArrayList<String> valores) {
		// IN: feature name
		// OUT: the feature object from collection eFeatures;
		Feature featureFound=new Feature();
		boolean isFound=false;
		boolean result=false;
		Iterator itr = eTree.iterator();
		while (itr.hasNext() && !isFound) {
			TreeNode theNodito=(TreeNode) itr.next();
			Feature theFeature=theNodito.feature;
			if (theFeatureNameNew.equals(theFeature.name)) {
				isFound=true;
				theNodito.feature.supertypes=valores;
				featureFound=theFeature;
				result=true;
			}
		}		
		if (!isFound) {
			System.out.println("ERROR 35: No existe la caracter�stica "+theFeatureNameNew+". No se puede adjuntar el supertipo");
			System.exit(35);
		}
		return result;
	}

		void showBranches(Collection eTree,String typeLog, int depth) {
			/*
			 * Show all branches as seen:
			 * 
			 *  R - F1 - F5 (F7,F9,F12)
			 *  R - F1 - F6 (F8,F11)
			 *  R - F2 
			 *  R - F3 - F4 (F13,F14,F15,F16,F17,F18)
			 *  
			 */
			TreeNode primero=new TreeNode();
			Iterator itr = eTree.iterator();
			primero=(TreeNode) eTree.iterator().next();
			String root=primero.nameFeature;
			gLog.add("============================================");
			gLog.add("TREE - BRANCHES");
			gLog.add("--------------------------------------------");
			while (itr.hasNext()) {
				String son="";
				TreeNode node=new TreeNode();
				node=(TreeNode) itr.next();
				if (node.type.contentEquals("BASIC")) {
					son=node.nameFeature;
				} else {
					son="";
				}
				if (node.idParentNode==primero.idNode) {
					String phrase="";
					
					List<String> lista=new ArrayList<String>();
					lista=getDescendantsAll(eTree,node.idNode,lista);
					String salida="";
					if (lista.size()>0) {
						// recorrer lista con las caracter�sticas y sacarlas en salida.
						Iterator<String> it = lista.iterator();
						String current = "";
						while(it.hasNext() ) { 
							current = it.next();
							salida+=","+current.toString();
						}
						String auxb=salida.substring(0,1);
						if (auxb.contentEquals(",")) {
							salida=salida.substring(1);
						}
					} else  {
						salida="";
					}
						
						
					if (son.length()>0) {
						if (salida.trim().length()>0) {
							phrase=root+"-"+son+"-"+salida;
						} else {
							phrase=root+"-"+son;
						}
					}else {
						System.out.println("RARO");
						System.exit(50);
					}
					gLog.add(phrase);
						
				}
			}
			//gLog.add("============================================");
		}
				 
	void showFeatureList(Collection eTree,String typeLog) {
	    ArrayList<String> s=new ArrayList<String>();
		Iterator itr = eTree.iterator();
		int counter=0;
		while (itr.hasNext()) {
			counter++;
			TreeNode tn=(TreeNode) itr.next();
			if (counter>1) {
				if (tn.type.contentEquals("BASIC")) {
					s.add(tn.nameNode);
				}
			}
		}
		
	    if (typeLog.contentEquals("L")) {
	    	gLog.add("FEATURE LIST");
	    	gLog.add("============");
	    	gLog.add("");
	    	gLog.add("LIST SIZE: "+s.size()+" features");
	    	gLog.add("");
	    } else {
	    	System.out.println("FEATURE LIST");
	    	System.out.println("============");
	    	System.out.println("");
	    	System.out.println("LIST SIZE: "+s.size()+" features");
	    	System.out.println("");
	    }
	    Collections.sort(s);
	    
	    String anterior="";
		for (String palabro: s) {
		    if (typeLog.contentEquals("L")) {
		    	gLog.add(palabro);
		    } else {
		    	System.out.println(palabro);
		    }
		    if (anterior.contentEquals(palabro)) {
		    	System.out.println("STRANGER THINGS");
		    	System.exit(51);
		    }
		    anterior=palabro;
		}
	    if (typeLog.contentEquals("L")) {
	    	gLog.add("============");
	    } else {
	    	System.out.println("============");
	    }		
	}		
		
	void showTreeValues(Collection eTree, String target) {
		// IN: feature name
		// OUT: the feature object from collection eFeatures;
		Iterator itr = eTree.iterator();
		String phrase="";
		
		phrase="===================================================================================================================================================";
		if (target.contentEquals("S")) {
			System.out.println(phrase);
		}else {
			gLog.add(phrase);
		}
		
		phrase=""
				+ String.format("%-4s", "ID")+"|"
				+ String.format("%-35s", "NAME")+"|"
				+ String.format("%-3s", "RS")+"|"
				+ String.format("%-3s", "RT")+"|"
				+ String.format("%-5s", "TYPE")+"|"
				+ String.format("%-4s", "IDPA")+"|"
				+ String.format("%-20s", "PARENT")+"|"
				+ String.format("%-5s", "TOTS")+"|"
				+ String.format("%-5s", "TOTC")+"|"
				+ String.format("%-5s", "MIN C")+"|"
				+ String.format("%-5s", "MAX C")+"|"
				+ String.format("%-5s", "TAND")+"|"
				+ String.format("%-5s", "TOR")+"|"
				+ String.format("%-5s", "TXOR")+"|"
				+ String.format("%-5s", "LEVEL")+"|"
				+ String.format("%-15s", "SUPERTYPES")+"|"
				+ String.format("%-1s", "S")+"|"
				+ "";
		if (target.contentEquals("S")) {
			System.out.println(phrase);
		}else {
			gLog.add(phrase);
		}
		phrase="---------------------------------------------------------------------------------------------------------------------------------------------------";
		if (target.contentEquals("S")) {
			System.out.println(phrase);
		}else {
			gLog.add(phrase);
		}
		
		while (itr.hasNext()) {
			TreeNode mtn;
			mtn=(TreeNode) itr.next();
			phrase=""
					+ String.format("%-4s", mtn.idNode)+"|"
					+ String.format("%-35s", mtn.nameNode)+"|"
					+ String.format("%-3s", mtn.relSiblings)+"|"
					+ String.format("%-3s", mtn.relType)+"|"
					+ String.format("%-5s", mtn.type)+"|"
					+ String.format("%-4s", mtn.idParentNode)+"|"
					+ String.format("%-20s", mtn.parentName)+"|"
					+ String.format("%-5s", mtn.totSiblings)+"|"
					+ String.format("%-5s", mtn.totChildren)+"|" 
					+ String.format("%-5s", mtn.minCardinality)+"|"
					+ String.format("%-5s", mtn.maxCardinality)+"|"
					+ String.format("%-5s", mtn.tAND)+"|"
					+ String.format("%-5s", mtn.tOR)+"|"
					+ String.format("%-5s", mtn.tXOR)+"|"
					+ String.format("%-5s", mtn.level)+"|"
					+ String.format("%-15s", utiles.convertArrayListStringToString(mtn.feature.supertypes))+"|"
					+ String.format("%-1s", mtn.feature.supertypes.size())+"|"
					
					+"";
			if (target.contentEquals("S")) {
				System.out.println(phrase);
			} else {
				gLog.add(phrase);
			}
		}
		phrase="===============================================================================================================";
		if (target.contentEquals("S")) {
			System.out.println(phrase);
		}else {
			gLog.add(phrase);
		}
	}	
	 void showFeatureSupertypeRelations() {
		try {
			Iterator itr = eFeaturesSupertypes.iterator();
			int count=0;
			while (itr.hasNext()) {
				count++;
				FeatureSupertype theFeatureSupertype=(FeatureSupertype) itr.next();
				String theFeatureName="";
				String theSupertypeName="";
				theFeatureName=theFeatureSupertype.theFeature.name;
				theSupertypeName=theFeatureSupertype.theSupertype.name;

				System.out.println("Feature "+theFeatureName+" provides the supertype "+theSupertypeName);			
			}
		}  
		catch (Exception e)	    {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}		
	}
	 void showOptionTree(int idParentNode, TreeNode nodeToInsert){
		//1 create a copy of eTree to operate
		Collection eTree2 = new ArrayList<TreeNode>();
		for (Iterator<TreeNode> it = eTree.iterator(); it.hasNext();) {
			TreeNode theTreeNode=it.next();
			eTree2.add(theTreeNode);
		}
		//2 adding new node
		eTree2.add(nodeToInsert);

		//3 show tree in console
		showFeatureModelText(eTree2);
	}
	 void showSupertypes(Collection theTree) {
		try {
			System.out.println("SUPERTYPES");
			System.out.println("==========");
			Iterator itr = theTree.iterator();
			while (itr.hasNext()) {
				TreeNode theTreeNode=(TreeNode) itr.next();
				if (theTreeNode.feature.supertypes.size()>0 ) {
					String theSupertypes="";
					theSupertypes=utiles.convertArrayListStringToString(theTreeNode.feature.supertypes);
					System.out.println("ST("+theTreeNode.feature.name+") = {"+theSupertypes+"}");
				}

			}




			/* Sistema antiguo de Supertipos. Ahora est�n metidos en cada nodo
			Iterator itr = eSupertypes.iterator();
			while (itr.hasNext()) {
				Supertype theSupertype=(Supertype) itr.next();
				System.out.println(theSupertype.name);			
			}
			 */
		}  
		catch (Exception e)	    {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}		
	}	

	private void showSystemChildren(Collection theTree,int theLevel, int theNode){
		theLevel++;
		Iterator itr = theTree.iterator();
		String theLine="--";
		String repeatedLine="";
		String separator="";
		while (itr.hasNext()) {
			repeatedLine=new String(new char[theLevel]).replace("\0", theLine);
			TreeNode theTreeNode=(TreeNode) itr.next();
			int theParentNode=theTreeNode.idParentNode;
			int idFoundNode=0;
			if (theParentNode==theNode) {
				idFoundNode=theTreeNode.idNode;
				//String parentRelation=theTreeNode.category.trim();
				String relSiblings=theTreeNode.relSiblings.trim();
				String relType=theTreeNode.relType.trim();
				String type=theTreeNode.type.trim();
				int myLevel=theTreeNode.level;		        
				ArrayList<String> theSupertypes;
				theSupertypes=theTreeNode.feature.supertypes;
				if (type.contentEquals("SET")) {
					separator="/";
				}else{
					separator="";
				}
				String suffix="";
				/* // ALEX
		        if (theSupertypes.size()>0) {
					suffix=" Supertypes:"+utiles.convertArrayListStringToString(theSupertypes)+")";
		        }
				 */ //FIN ALEX */ 
				System.out.println("|"+repeatedLine+"> "+theTreeNode.nameNode+" TYPE="+ type + " PR="+relType+" SR="+relSiblings+" ST:"+theSupertypes+separator+suffix+separator+" Level: "+myLevel);
				showSystemChildren(theTree, theLevel, idFoundNode);
			}
		}
	}
	private void showSystemChildrenLog(Collection theTree,int theLevel, int theNode){
		theLevel++;
		Iterator itr = theTree.iterator();
		String theLine="--";
		String repeatedLine="";
		String separator="";
		while (itr.hasNext()) {
			repeatedLine=new String(new char[theLevel]).replace("\0", theLine);
			TreeNode theTreeNode=(TreeNode) itr.next();
			int theParentNode=theTreeNode.idParentNode;
			int idFoundNode=0;
			if (theParentNode==theNode) {
				idFoundNode=theTreeNode.idNode;
				//String parentRelation=theTreeNode.category.trim();
				String relSiblings=theTreeNode.relSiblings.trim();
				String relType=theTreeNode.relType.trim();
				String type=theTreeNode.type.trim();
				int myLevel=theTreeNode.level;		        
				ArrayList<String> theSupertypes;
				theSupertypes=theTreeNode.feature.supertypes;
				if (type.contentEquals("SET")) {
					separator="/";
				}else{
					separator="";
				}
				String suffix="";
				/* // ALEX
		        if (theSupertypes.size()>0) {
					suffix=" Supertypes:"+utiles.convertArrayListStringToString(theSupertypes)+")";
		        }
				 */ //FIN ALEX */ 
				gLog.add("|"+repeatedLine+"> "+theTreeNode.nameNode+" TYPE="+ type + " PR="+relType+" SR="+relSiblings+" ST:"+theSupertypes+separator+suffix+separator+" Level: "+myLevel);
				showSystemChildrenLog(theTree, theLevel, idFoundNode);
			}
		}
	}	

	/** Funcion utilizada para Ordenar Atributos de Elementos */

	protected Attr[] sortAttributes(NamedNodeMap attrs) {

		int len = (attrs != null) ? attrs.getLength() : 0;
		Attr array[] = new Attr[len];
		for ( int i = 0; i < len; i++ ) {
			array[i] = (Attr)attrs.item(i);
		}
		for ( int i = 0; i < len - 1; i++ ) {
			String name  = array[i].getNodeName();
			int    index = i;
			for ( int j = i + 1; j < len; j++ ) {
				String curName = array[j].getNodeName();
				if ( curName.compareTo(name) < 0 ) {
					name  = curName;
					index = j;
				}
			}
			if ( index != i ) {
				Attr temp    = array[i];
				array[i]     = array[index];
				array[index] = temp;
			}
		}

		return(array);

	} // Terminar ordenar Atributos 	

	/*
	 * 
	 *  vuelca todo lo que se ha generado antes en generateFM 
	 * 
	 */
	 boolean writeFMFile(String thisPath) {
		// mode 0 FAMA 1 mine
		boolean resul=true;
		boolean flagFound=false;

		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter(thisPath);
			pw = new PrintWriter(fichero);
			for (int i = 0; i < fmFile.size(); i++) {
				String line;
				line=fmFile.get(i);
				pw.println(line);
				//System.out.println(fmFile.get(i));
			}

		} catch (Exception e) {
			e.printStackTrace();
			resul=false;
		} finally {
			try {
				// Ensure file is closed at the end 
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				resul=false;
			}
		}		
		return resul;
	}
	boolean writeFile(String thisPath, ArrayList<String> textBlock) {
		// mode 0 FAMA 1 mine
		boolean resul=true;
		boolean flagFound=false;

		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter(thisPath);
			pw = new PrintWriter(fichero);
			for (int i = 0; i < textBlock.size(); i++) {
				//System.out.println("Procesando l�nea "+i);
				String line;
				line=textBlock.get(i);
				pw.append(line+"\n");
				//pw.println(line);
				//System.out.println(fmFile.get(i));
			}

		} catch (Exception e) {
			resul=false;
			System.out.println("ERROR: Fichero no existe: "+thisPath);
			System.exit(19);
		} finally {
			try {
				// Ensure file is closed at the end 
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				resul=false;
			}
		}		
		return resul;
	}
	boolean writeFileAccumulative(String thisPath, ArrayList<String> textBlock) {
		// mode 0 FAMA 1 mine
		boolean resul=true;
		boolean flagFound=false;

		FileWriter fichero = null;
	    BufferedWriter bw = null;
		try {
			fichero = new FileWriter(thisPath,true);
			bw = new BufferedWriter(fichero);
			for (int i = 0; i < textBlock.size(); i++) {
				//System.out.println("Procesando l�nea "+i);
				String line;
				line=textBlock.get(i);
				bw.write(line);
				bw.newLine();
			}
			bw.close();

		} catch (Exception e) {
			resul=false;
			System.out.println("ERROR: Fichero no existe: "+thisPath);
			System.exit(19);
		} finally {
			try {
				// Ensure file is closed at the end 
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				resul=false;
			}
		}		
		return resul;
	}
	boolean validateFM() {
		boolean resul=false;
		System.out.println("Start of evaluation of feature model. Please wait ...");
		// 1. generate .fm (aux.fm)
		generateFMFile(0,eTree,fmFile);
		writeFMFile("fm/famatemp.fm");
		// 2. Invoke FAMA with .fm generated
		QuestionTrader qt = new QuestionTrader();
		VariabilityModel fm = qt.openFile("fm/famatemp.fm");
		qt.setVariabilityModel(fm);

		// 3. Check consistency
		try { 
			ValidQuestion vq = (ValidQuestion) qt.createQuestion("Valid");
			qt.ask(vq);

			// 4. retrieve result
			System.out.println("End of evaluation of feature model.");
			if (vq.isValid()) {
				resul=true;
				System.out.println("No inconsistency detected.\rFeature model is OK. ");
				NumberOfProductsQuestion npq = (NumberOfProductsQuestion) qt.createQuestion("#Products");
				qt.ask(npq);
				System.out.println("The number of products is: "+npq.getNumberOfProducts()); 
			} else {
				resul=false;
				System.out.println("Feature model inconsistency. Constraints not satisfied.");
			}
		} catch (Exception e) {
			System.out.println("Feature model not valid. Check relationships, features and cardinality.");
			e.printStackTrace();
			resul=false;
		}

		return resul;

	}
	void showFeatureModelNames() {
		File dir = new File(".");
		File [] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".fm");
			}
		});

		for (File fmFilesList : files) {
			System.out.println(fmFilesList);
		}		

	}
	void showStatistics() {
		/*
		 * Sacamos los siguientes datos:
		 * 
		 * n�mero de caracter�sticas
		 * n�mero de caracter�sticas con hijos
		 * n�mero de caracter�sticas sin hijos
		 * n�mero de nodos and
		 * n�mero de nodos or
		 * n�mero de nodos xor
		 * M�ximo nivel de profundidad (level)
		 * n�mero de supertipos
		 * n�mero de nodos con supertipos
		 */
		int tFea=0;
		int tBreedingNodes=0;
		int tChildlessNodes=0;
		int tNodesAND=0;
		int tNodesOR=0;
		int tNodesXOR=0;
		int tNodesMandatory=0;
		int tNodesOptional=0;
		int tLevel=0;
		int tSupertypes=0; // n�mero de supertipos presentes en el �rbol
		int tNumberNodesWithSupertypes=0; // N�mero de Nodos con supertipos
		int tMaxSupertypesInNode=0;
		int sumNodesSet=0; 
		int maxBranchingFactor=0;
		int maxChildrenSet=0;
		int reqConstraints=0;
		int excConstraints=0;
		Double porcenAND=0.0;
		Double porcenOR=0.0;
		Double porcenXOR=0.0;
		Double porcenMan=0.0;
		Double porcenOpt=0.0;
		Double avgBranchingFactor=0.0;
		//DecimalFormat df = new DecimalFormat("#.##");
		DecimalFormat df = new DecimalFormat("#0.00");
		
		tFea=getFeatureNumber(eTree);
		tBreedingNodes=getBreedingNodesNumber(eTree);
		tChildlessNodes=getChildlessNodesNumber(eTree);
		tNodesAND=getAndSets(eTree);
		tNodesOR=getOrSets(eTree);
		tNodesXOR=getXorSets(eTree);
		sumNodesSet=tNodesAND+tNodesOR+tNodesXOR;
		reqConstraints=getNumberReqConstraints(eConstraints);
		excConstraints=eConstraints.size()-reqConstraints;
		
		avgBranchingFactor=getAverageBranchingFactor(eTree);
		maxBranchingFactor=getMaxBranchingFactor(eTree);
		maxChildrenSet=getMaxChildrenSET(eTree);
		tNodesMandatory=getMandatoryNumber(eTree);
		tNodesOptional=getOptionalNumber(eTree);
		tLevel=getMaxLevel(eTree);
		tSupertypes=getTotalSupertypesFromTree(eTree); 
		tNumberNodesWithSupertypes=getNumberNodesWithSupertypes(eTree);
		tMaxSupertypesInNode=getMaxDifferentSupertypesInNode(eTree);
		porcenAND=Double.valueOf(tNodesAND)/Double.valueOf(sumNodesSet)*100;
		porcenOR=Double.valueOf(tNodesOR)/Double.valueOf(sumNodesSet)*100;
		porcenXOR=Double.valueOf(tNodesXOR)/Double.valueOf(sumNodesSet)*100;
		porcenMan=Double.valueOf(tNodesMandatory)/Double.valueOf(tFea)*100;
		porcenOpt=Double.valueOf(tNodesOptional)/Double.valueOf(tFea)*100;
		
		gLog.add("============================================");
		gLog.add("STATISTICS");
		gLog.add("-----------");
		gLog.add("");
		gLog.add("Features: "+tFea);
		gLog.add("Breeding features: "+tBreedingNodes);
		gLog.add("Childless features: "+tChildlessNodes);
		gLog.add("Nodes AND: "+tNodesAND+ " ("+df.format(porcenAND)+"%)");
		gLog.add("Nodes OR: "+tNodesOR+ " ("+df.format(porcenOR)+"%)");
		gLog.add("Nodes XOR: "+tNodesXOR+ " ("+df.format(porcenXOR)+"%)");
		gLog.add("Nodes Mandatory: "+tNodesMandatory+ " ("+df.format(porcenMan)+"%)");
		gLog.add("Nodes Optional: "+tNodesOptional+ " ("+df.format(porcenOpt)+"%)");
		gLog.add("Depth tree: "+tLevel);
		gLog.add("Average Branching Factor: "+df.format(avgBranchingFactor));
		gLog.add("Maximum Branching Factor: "+maxBranchingFactor);
		gLog.add("Maximum number of Nodes in SET relationship: "+maxChildrenSet);
		gLog.add("Supertypes: "+tSupertypes);
		gLog.add("Nodes with supertypes: "+tNumberNodesWithSupertypes);
		gLog.add("Max supertypes in a node: "+tMaxSupertypesInNode);
		gLog.add("Constraints: "+eConstraints.size());
		gLog.add("REQUIRE Constraints: "+reqConstraints);
		gLog.add("EXCLUDE Constraints: "+excConstraints);
		
	}
	

}
