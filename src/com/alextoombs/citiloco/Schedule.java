package com.alextoombs.citiloco;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Part of the Citiloco app.
 * @author Shane MacQuillan
 * @date 4/6/2013
 * @version 1.0
 *
 */
public class Schedule {
	
	private ArrayList<Option> options;

	public Schedule(String xmlSchedule) {
		this.options = new ArrayList<Option>();
	    try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlSchedule));
			Document doc = db.parse(is);
			
		    NodeList optionsXML  = doc.getElementsByTagName("option");
		    for (int i = 0; i < optionsXML.getLength(); i++) {
		        Element element = (Element) optionsXML.item(i);
		        ArrayList<Location> locations = new ArrayList<Location>();
		        NodeList locationsXML = element.getElementsByTagName("location");
		        for(int j = 0; j < locationsXML.getLength(); j++) {
			        Element locationXML = (Element) locationsXML.item(j);
			        String location = getNodeValue((Element) locationXML.getElementsByTagName("name").item(0));
			        double latitude = Double.parseDouble(getNodeValue((Element) locationXML.getElementsByTagName("lat").item(0)));
			        double longtitude = Double.parseDouble(getNodeValue((Element) locationXML.getElementsByTagName("lng").item(0)));
			        double price = Double.parseDouble(getNodeValue((Element) locationXML.getElementsByTagName("price").item(0)));
			        locations.add(new Location(location, latitude, longtitude, price));
		        }
		        double startTime = Double.parseDouble(getNodeValue((Element) doc.getElementsByTagName("startTime").item(0)));;
		        double endTime = Double.parseDouble(getNodeValue((Element) doc.getElementsByTagName("endTime").item(0)));;
		        options.add(new Option(locations, startTime, endTime));
		      }
			
		} catch (Exception e) {	
			System.out.println("exception");
		}
	}
	
	private String getNodeValue(Element node) {
	    Node child = node.getFirstChild();
	    if (child instanceof CharacterData) {
	      CharacterData cd = (CharacterData) child;
	      return cd.getData();
	    }
	    return "";
	}
	
}
