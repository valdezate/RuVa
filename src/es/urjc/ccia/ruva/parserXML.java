package es.urjc.ccia.ruva;

//Librerías de I/O 
import java.io.*;

//Librerías de DOM 
import org.w3c.dom.*;

//Clases JAXP 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.ParserConfigurationException;

//Clases SAX 
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class parserXML {
	static Document document; 
	protected static PrintWriter out;
	public parserXML() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();		
		
	    try {
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

			document = builder.parse("sample/his.xml");
			parserXML writer = new parserXML(); 
			writer.processXMLNodes(document);
		
		} catch (ParserConfigurationException pexc) { 
		  pexc.printStackTrace();
		} catch (Exception exc) {
		    System.out.println("   " + exc.getMessage() );
		
		}		
	}


	public void processXMLNodes(Node node) {
	
	    // Si ya no existen nodos por Imprimir salir.....
	    if ( node == null ) {
	        return;
	    }
	
	    /** Investigar el Tipo de Nodo */
	
	    int type = node.getNodeType();
	 
	    /** En base al Tipo de Nodo ejecutar */
	    switch ( type ) {
	
	      // Imprimir Documento 
	      case Node.DOCUMENT_NODE: {
	         NodeList children = node.getChildNodes();
	         for ( int iChild = 0; iChild < children.getLength(); iChild++ ) {
	               processXMLNodes(children.item(iChild));
	         }
	         out.flush();
	         break;
	      }
	
	      // Imprimir elementos con atributos
	      case Node.ELEMENT_NODE: {
	       out.print('<');
	       out.print(node.getNodeName());
	       Attr attrs[] = sortAttributes(node.getAttributes());
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
	       }
	       out.print('>');
	       NodeList children = node.getChildNodes();
	       if ( children != null ) {
	          int len = children.getLength();
	          for ( int i = 0; i < len; i++ ) {
	             processXMLNodes(children.item(i));
	          }
	       }
	       break;
	      }
	
	      // Imprimir Texto 
	      case Node.TEXT_NODE: {
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

}	

