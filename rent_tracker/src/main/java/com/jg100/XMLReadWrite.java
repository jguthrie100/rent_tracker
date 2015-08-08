import java.io.File;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** XMLReadWrite reads and writes various pieces of information (relating to tenants, houses, transactions etc) from/to XML files
 *  Essentialy acts as a sort of database handler, wherein information is transferred from XML files to the program
 */
class XMLReadWrite {
  
  private static DocumentBuilder docBuilder;
  
  private static Transformer transformer;
  
  /* Default xml file to read from/write to */
  private static String xmlFile = "/home/jg100/.config/tenants.xml";
  
  /**
   * XMLReadWrite constructor - Initiates the core XML read/write structures and objects so that they can be used in later methods
   */
  public XMLReadWrite() {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		  docBuilder = docFactory.newDocumentBuilder();
		  
		  TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    transformer = transformerFactory.newTransformer();
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
    } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
  }
  
  /**
   * Writes the list of houses/tenants/transactions to the default XML file 
   * Essentially stores an XML database of all the tenants and houses and rent payments
   * 
   * @param houseList   ArrayList of House objects that are to be sorted and saved in XML format
   */
  public void writeTenantsXML(ArrayList<House> houseList) {
    writeTenantsXML(houseList, xmlFile);
  }
  
  /**
   * Writes the list of houses/tenants/transactions to the specified file
   * Essentially stores an XML database of all the tenants and houses and rent payments
   * 
   * @param houseList       ArrayList of House objects that are to be sorted and saved in XML format 
   * @param outputXMLFile   Filename of the file that the XML should be saved to (can be a new file)
   */
  public void writeTenantsXML(ArrayList<House> houseList, String outputXMLFile) {
    if(houseList == null) {
      throw new IllegalArgumentException("Error: houseList arrayList == null");
    }
    if(outputXMLFile == null || outputXMLFile.isEmpty()) {
      throw new IllegalArgumentException("Error: Output XML filename cannot be null or empty");
    }
    
    /*
    XML format is:
    <houseList>
      <house>
        <name>xx</name>
        <address>xx</address>
        <...>xx</...>
        <tenantList>
          <tenant>
            <name>xx</name>
            <paymentHandle>xx</paymentHandle>
            <...>xx</...>
            <transactionList>
              <transaction id="xx"/>
              <transaction id="xx"/>
            </transactionList>
          </tenant>
        </tenantList>
      </house>
    </houseList>
    */
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    DecimalFormat df = new DecimalFormat("0.00");
    Document doc = docBuilder.newDocument();
    
    // create base element
    Element level0 = doc.createElement("houseList");
		doc.appendChild(level0);
		
		// build House elements (i.e. Houses in the list of Houses)
    for(House h : houseList) {
		  Element level1 = doc.createElement("house");
		  level0.appendChild(level1);
		  
		  Element level2 = doc.createElement("name");
		  level2.appendChild(doc.createTextNode(h.getName()));
		  level1.appendChild(level2);
		  
		  level2 = doc.createElement("address");
		  level2.appendChild(doc.createTextNode(h.getAddress()));
		  level1.appendChild(level2);
		  
		  level2 = doc.createElement("numBedrooms");
		  level2.appendChild(doc.createTextNode("" + h.getNumBedrooms()));
		  level1.appendChild(level2);
		  
		  level2 = doc.createElement("rent");
		  level2.appendChild(doc.createTextNode(df.format(h.getWeeklyRent())));
		  level1.appendChild(level2);
		  
		  level2 = doc.createElement("agencyFees");
		  level2.appendChild(doc.createTextNode(df.format(h.getAgencyFees())));
		  level1.appendChild(level2);
		  
		  level2 = doc.createElement("tenantList");
		  
		  // build Tenant elements (i.e. tenants belonging to the current House node)
      for(Tenant t : h.getTenantList()) {
        
        Element level3 = doc.createElement("tenant");
		    level2.appendChild(level3);
		    
		    Element level4 = doc.createElement("name");
		    level4.appendChild(doc.createTextNode(t.getName()));
		    level3.appendChild(level4);
		    
		    level4 = doc.createElement("paymentHandle");
		    level4.appendChild(doc.createTextNode(t.getPaymentHandle()));
		    level3.appendChild(level4);
		    
		    level4 = doc.createElement("phoneNum");
		    level4.appendChild(doc.createTextNode(t.getPhoneNum()));
		    level3.appendChild(level4);
		    
		    level4 = doc.createElement("email");
		    level4.appendChild(doc.createTextNode(t.getEmail()));
		    level3.appendChild(level4);
		    
		    level4 = doc.createElement("leaseStart");
		    level4.appendChild(doc.createTextNode(sdf.format(t.getLeaseStart())));
		    level3.appendChild(level4);
		    
		    // Need to catch null return, just incase the leaseEnd date hasn't been set 
		    //  - it crashes the DOM parser if we don't handle it
		    level4 = doc.createElement("leaseEnd");
		    if(t.getLeaseEnd() != null) {
		      level4.appendChild(doc.createTextNode(sdf.format(t.getLeaseEnd())));
		    } else {
		      level4.appendChild(doc.createTextNode(""));
		    }
		    level3.appendChild(level4);
		    
		    level4 = doc.createElement("rentAmount");
		    level4.appendChild(doc.createTextNode(df.format(t.getWeeklyRent())));
		    level3.appendChild(level4);
		    
		    level4 = doc.createElement("rentFrequency");
		    level4.appendChild(doc.createTextNode("" + t.getRentFrequency()));
		    level3.appendChild(level4);
		    
		    level4 = doc.createElement("transactionList");
		
		    // build Transaction elements (i.e. Transactions belonging to the current Tenant node)
        for (Transaction tr : t.getTransactionList()) {
          
          Element level5 = doc.createElement("transaction");
          
          // set attribute to transaction id
		      Attr attr = doc.createAttribute("id");
		      attr.setValue("" + tr.getFullId());
		      level5.setAttributeNode(attr);
  		    level4.appendChild(level5);
          
        }
        level3.appendChild(level4);
      }
      level1.appendChild(level2);
    }
  
    // write the content into xml file
  	DOMSource source = new DOMSource(doc);
  	StreamResult result = new StreamResult(new File(outputXMLFile));
  //	StreamResult result = new StreamResult(System.out); // test to System.out
  	
  	try {
  	  System.out.println("Writing XML representation of houses and tenants to '" + outputXMLFile + "'...");
  	  transformer.transform(source, result);
  	  System.out.println("...successfully wrote to '" + outputXMLFile + "'");
  	} catch (TransformerException tfe) {
  	  tfe.printStackTrace();
    }
  }
}