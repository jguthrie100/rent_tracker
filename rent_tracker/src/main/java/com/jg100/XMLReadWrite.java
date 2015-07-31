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
 
class XMLReadWrite {
  
  private static DocumentBuilder docBuilder;
  
  private static Transformer transformer;
  
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
  
  public void writeTenantsXML(ArrayList<House> houseList) {
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    DecimalFormat df = new DecimalFormat("0.00");
    Document doc = docBuilder.newDocument();
    
    // create base element
    Element level0 = doc.createElement("houseList");
		doc.appendChild(level0);
		
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
		  level2.appendChild(doc.createTextNode(df.format(h.getRent())));
		  level1.appendChild(level2);
		  
		  level2 = doc.createElement("agencyFees");
		  level2.appendChild(doc.createTextNode(df.format(h.getAgencyFees())));
		  level1.appendChild(level2);
		  
		  level2 = doc.createElement("tenantList");
		  
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
		    
		    level4 = doc.createElement("leaseEnd");
		    if(t.getLeaseEnd() != null) {
		      level4.appendChild(doc.createTextNode(sdf.format(t.getLeaseEnd())));
		    } else {
		      level4.appendChild(doc.createTextNode(""));
		    }
		    level3.appendChild(level4);
		    
		    level4 = doc.createElement("rentAmount");
		    level4.appendChild(doc.createTextNode(df.format(t.getRentAmount())));
		    level3.appendChild(level4);
		    
		    level4 = doc.createElement("rentFrequency");
		    level4.appendChild(doc.createTextNode("" + t.getRentFrequency()));
		    level3.appendChild(level4);
		    
		    level4 = doc.createElement("transactionList");
		
        for (Transaction tr : t.getTransactionList()) {
          
          Element level5 = doc.createElement("transaction_id");
          level5.appendChild(doc.createTextNode("" + tr.getFullId()));
  		    level4.appendChild(level5);
          
        }
        level3.appendChild(level4);
      }
      level1.appendChild(level2);
    }
  
    // write the content into xml file
  	DOMSource source = new DOMSource(doc);
  	StreamResult result = new StreamResult(new File("/home/jg100/.config/tenants.xml"));
  //	StreamResult result = new StreamResult(System.out); // testing to System.out
  	
  	try {
  	  transformer.transform(source, result);
  	} catch (TransformerException tfe) {
  	  tfe.printStackTrace();
    }
  }
}