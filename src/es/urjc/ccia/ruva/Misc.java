package es.urjc.ccia.ruva;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Misc {

	public Misc() {
		// TODO Auto-generated constructor stub
	}
	public ArrayList<String> convertArrayToArrayListString(String[] entrada){
		ArrayList<String> salida=new ArrayList<String>();
		if (entrada.length>0) {
			for (int i=0;i<entrada.length;i++) {
				salida.add(entrada[i].trim());
			}
		}
		return salida;
	}
	public String convertArrayListStringToString(ArrayList<String> entrada){
		String salida="";
		if (entrada.size()>0) {
			Iterator itr=entrada.iterator();
			while (itr.hasNext()) {
				String nextItem="";
				nextItem=(String) itr.next();
				salida=salida+nextItem+",";
			}
			salida=salida.substring(0, salida.length()-1);
		}
		return salida;
	}
	public int compareArrayList(ArrayList<String> list1,ArrayList<String> list2){
		int matchCounter=0;
		if (list1.size()>0) {
			Iterator itr=list1.iterator();
			while (itr.hasNext()) {
				String item1="";
				item1=(String) itr.next();
				Iterator itr2=list2.iterator();
				while (itr2.hasNext()) {
					String item2="";
					item2=(String) itr2.next();
					if (item1.equals(item2)) {
						matchCounter++;
					}
				}
			}
		}
		return matchCounter;
	}	
	
	public ArrayList<TreeNode> sortArrayListMatch(ArrayList<HitsTreeNode> entrada) {
		ArrayList<HitsTreeNode> salida=new ArrayList<HitsTreeNode>();
		ArrayList<TreeNode> resul=new ArrayList<TreeNode>();
		TreeNode aux=new TreeNode();
		salida=entrada;
		int tope=salida.size()-1;
		if (tope>0) {
			for (int i=0;i<tope;i++) {
				for (int j=0;j<tope;j++) {
					if (salida.get(j).hits<salida.get(j+1).hits) {
						Collections.swap(salida, j, j+1);
					} else if ((salida.get(j).hits==salida.get(j+1).hits) && (salida.get(j).totChildren<salida.get(j+1).totChildren)) {
						Collections.swap(salida, j, j+1);
					}
				}
			}
		}
		
		//fill resul with TreeNodeList sorted
		Iterator itr = salida.iterator();
		while (itr.hasNext()) {
			HitsTreeNode elem=(HitsTreeNode) itr.next();
			resul.add(elem.treenode);
		}
		
		
		return resul;
	}
	public void printFileToConsole(String thePath ) {
		boolean resul=false;
		System.out.println("----- Begin printing of file "+thePath+" ------");
		//first, cleaning ArrayList to proceed to populate
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(thePath));
			String line = reader.readLine();
			while (line != null) {
				//System.out.println(line);
				System.out.println(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			resul=false;
		}		
		System.out.println("----- End printing of file "+thePath+" ------");
						
	}
	public boolean isInteger(String s) {
	    return isInteger(s,10);
	}

	public boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}	

   public static String getHTML(String urlToRead) throws Exception {
      StringBuilder result = new StringBuilder();
      URL url = new URL(urlToRead);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
          for (String line; (line = reader.readLine()) != null; ) {
              result.append(line);
          }
      }
      return result.toString();
   }

   public static void main(String[] args) throws Exception
   {
     System.out.println(getHTML(args[0]));
   }	
}
