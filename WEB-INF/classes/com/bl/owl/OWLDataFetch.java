package com.bl.owl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import com.hp.hpl.jena.util.FileManager;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;

public class OWLDataFetch {

//	static final String inputFileName = "C:\\Users\\Ankur\\git\\stupidstuff\\JenaApi\\src\\SecOntV2.owl";
//	static final String inputFileName = System.getProperty("user.dir")+"\\OwlFile\\SecOntV2.owl";

	public static void main(String[] args) throws Exception {
		System.out.println(new OWLDataFetch().getOwlData("ACK_piggybacking"));
		System.out.println(new OWLDataFetch().getBookPage("queryString"));
//		System.out.println(new TestOwlReader().getOwlData("Password"));
		
//		new OwlReader().getOwlAllClasses();
	}
	
	
	
//	public void getOwlAllClasses(){
//		String fileName="C:\\Users\\Ankur\\Desktop\\OWLNEW.owl";
//		StringBuffer sBuffer = new StringBuffer();
//		try {
//			
//			System.out.println();
////			InputStream in = FileManager.get().open(inputFileName);
//			JenaOWLModel owlModel = ProtegeOWL
//					.createJenaOWLModelFromInputStream(getClass().getResourceAsStream(fileName));
//			
//			 owlModel.getOWLClasses();
////			owlModel.getOWLLogicalClassClass().getOverriddenTemplateFacets(arg0);
//			owlModel.flushCache();
//			owlModel.close();
//
//		} catch (Exception ae) {
//			ae.printStackTrace();
//		}
//	}

	public String getOwlData(String queryString) {
		String fileName="SecOntV2.owl";
		//getClass().getResourceAsStream("SecOntV2.owl");
		StringBuffer sBuffer = new StringBuffer();
		try {
			
			System.out.println();
//			InputStream in = FileManager.get().open(inputFileName);
			JenaOWLModel owlModel = ProtegeOWL
					.createJenaOWLModelFromInputStream(getClass().getResourceAsStream(fileName));
			OWLDataFetch owlReader = new OWLDataFetch();
			OWLNamedClass cls = owlModel.getOWLNamedClass(queryString.replace(" ", ""));

			sBuffer.append("<OwlRootNode>");

			String SANSDes = owlReader.PrintSANSDesc(cls, owlModel);
			sBuffer.append("<SANSDescription>" + SANSDes + "</SANSDescription>");
			String Des = owlReader.PrintDesc(cls, owlModel);
			sBuffer.append("<Description>" + Des + "</Description>");
			String NIST = owlReader.PrintNIST(cls, owlModel);
			sBuffer.append("<NistDef>" + NIST + "</NistDef>");

			owlReader.getSuperClasses(cls, sBuffer);

			owlReader.getSubClasses(cls, sBuffer);

			owlReader.getEquivalentClasses(cls, sBuffer);
			
			sBuffer.append("</OwlRootNode>");
//		sBuffer.notify();
		
			owlModel.flushCache();
			owlModel.close();

		} catch (Exception ae) {
			ae.printStackTrace();
		}
		return sBuffer.toString();
	}

	public String PrintSANSDesc(OWLNamedClass cls, JenaOWLModel owlModel)
			throws FileNotFoundException, OntologyLoadException {

		RDFProperty description;
		description = owlModel.getRDFProperty("literature:hasDefinition");
//		description = owlModel.getRDFProperty("dc:description");

		String des = "";
		try {
			des = cls.getPropertyValue(description).toString();
		} catch (Exception e) {
			des = "";
		}

		return des;

	}
	
	public String PrintDesc(OWLNamedClass cls, JenaOWLModel owlModel)
			throws FileNotFoundException, OntologyLoadException {

		RDFProperty description;
//		description = owlModel.getRDFProperty("literature:hasDefinition");
		description = owlModel.getRDFProperty("dc:description");

		String des = "";
		try {
			des = cls.getPropertyValue(description).toString();
		} catch (Exception e) {
			des = "";
		}

		return des;

	}

	public String PrintNIST(OWLNamedClass cls, JenaOWLModel owlModel)
			throws FileNotFoundException, OntologyLoadException {


		OWLDatatypeProperty hasNISTIRDef;
//		hasNISTIRDef = owlModel.getOWLDatatypeProperty("hasDefinition");
		hasNISTIRDef = owlModel.getOWLDatatypeProperty("hasNISTIRDef");

		String Nist = "";

		try {
			Nist = cls.getPropertyValue(hasNISTIRDef).toString();

		} catch (Exception e) {
			Nist = "";
		}

		List NISIR_DEF = new ArrayList<String>();
		int count = 0;
		int j = 0;
		for (int i = 0; i < Nist.length(); i++) {
			if (Nist.charAt(i) == '\n') {
				NISIR_DEF.add(Nist.subSequence(j, i));
				j = i + 1;
			}
		}

		String New_NIST = "";
		for (int i = 0; i < NISIR_DEF.size(); i++) {
			String part1 = NISIR_DEF
					.get(i)
					.toString()
					.substring(0, NISIR_DEF.get(i).toString().indexOf("SOURCE"))
					.replaceAll("\"", "");
			String part2 = NISIR_DEF
					.get(i)
					.toString()
					.substring(NISIR_DEF.get(i).toString().indexOf("SOURCE"),
							NISIR_DEF.get(i).toString().length());
	
			New_NIST += part1 + part2;
		}
		return New_NIST;

	}

	public void getSuperClasses(OWLNamedClass cls, StringBuffer sBuffer) {

		Collection<OWLNamedClass> superClasses = cls.getNamedSuperclasses();
		sBuffer.append("<SuperClass>");
		String bufferString = "";
		for (Iterator<OWLNamedClass> t = superClasses.iterator(); t.hasNext();) {
			OWLClass class1 = (OWLClass) t.next();
			sBuffer.append(class1.getBrowserText() + " | ");
		}
		sBuffer.append("</SuperClass>");

	}

	public void getEquivalentClasses(OWLNamedClass cls, StringBuffer sBuffer) {

		Collection<OWLNamedClass> EquiClasses = cls.getEquivalentClasses();
		sBuffer.append("<EquivalentClass>");
		for (Iterator<OWLNamedClass> t = EquiClasses.iterator(); t.hasNext();) {
			OWLClass class1 = (OWLClass) t.next();

			sBuffer.append(class1.getBrowserText() + " | ");

		}
		sBuffer.append("</EquivalentClass>");

	}

	public void getSubClasses(OWLNamedClass cls, StringBuffer sBuffer) {

		Collection<OWLNamedClass> subClasses = cls.getNamedSubclasses(false);
		sBuffer.append("<SubClass>");
		for (Iterator<OWLNamedClass> t = subClasses.iterator(); t.hasNext();) {
			OWLClass class1 = (OWLClass) t.next();

			sBuffer.append(class1.getBrowserText() + " | ");

		}
		sBuffer.append("</SubClass>");

	}

	public String getBookPage(String queryString) {
		StringBuffer sBuffer = new StringBuffer();
		try {

			OWLDataFetch owlReader = new OWLDataFetch();
	

			sBuffer.append("<OwlRootNode>");

			owlReader.getPageNumber(sBuffer, queryString);
			sBuffer.append("</OwlRootNode>");

		} catch (Exception ae) {
			ae.printStackTrace();
		}
		return sBuffer.toString();
	}

	/*public void getPageNumber(StringBuffer sBuffer,
			String queryString) {
		String fileName="SecOntV2.owl";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc=builder.parse(getClass().getResourceAsStream(fileName));
//			doc = builder.parse(fileName);
			XPathFactory xpathFactory = XPathFactory.newInstance();

			XPath xpath = xpathFactory.newXPath();

			String pageNumber;

			XPathExpression expr = xpath.compile("/*[local-name()='RDF']/"
					+ queryString + "/hasPageNumbers/text()");
			pageNumber = expr.evaluate(doc, XPathConstants.STRING).toString();

			sBuffer.append("<BookPage>" + pageNumber + "</BookPage>");
		

		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/
	public void getPageNumber(StringBuffer sBuffer,
			   String queryString) {
			  String fileName="SecOntV22.owl";
			  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			  DocumentBuilder builder;
			  Document doc = null;
			  try {
			   builder = factory.newDocumentBuilder();
			   doc=builder.parse(getClass().getResourceAsStream(fileName));
			//   doc = builder.parse(fileName);
			   XPathFactory xpathFactory = XPathFactory.newInstance();

			   XPath xpath = xpathFactory.newXPath();

			   String pageNumber;

			   XPathExpression expr = xpath.compile("/*[local-name()='RDF']/"
			     + queryString + "/hasPageNumbers/text()");
			   pageNumber = expr.evaluate(doc, XPathConstants.STRING).toString();

			   sBuffer.append("<BookPage>" + pageNumber + "</BookPage>");
			  
			System.out.println(sBuffer.toString());
			  } catch (Exception e) {
			   e.printStackTrace();
			  }

			 }

}
