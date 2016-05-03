/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerBatch;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 *
 * @author adrien
 */
public class OsmParser {
    private String fileName;
    private String path;
    private List<Atm> listAtm = new ArrayList<>(); 
    private List<Supermarket> listSuper = new ArrayList<>();
    private List<Doctor> listDoctor = new ArrayList<>(); 
    private List<Kindergarten> listKindergarten = new ArrayList<>();
    
    public OsmParser(String fileName, String path, List<Atm> listA, List<Supermarket> listS, List<Doctor> listD, List<Kindergarten> listK){
        this.fileName = fileName;
        this.path = path;
        this.listAtm = listA;
        this.listSuper = listS;
        this.listDoctor = listD;
        this.listKindergarten = listK;
    }
    
    public List<Atm> getAtms(){
        return listAtm;        
    }
    public List<Supermarket> getSupermarkets(){
        return listSuper;        
    }
    
    public List<Doctor> getDoctors(){
        return listDoctor;        
    }
    public List<Kindergarten> getKindergartens(){
        return listKindergarten;        
    }
    
    public void parse(){
        try {	
            File inputFile = new File(path + fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            boolean isInteresting = false;
            String type = "";
            String name = "";
            float lat;
            float lon;
            float maxLat = -90f;
            float minLat = 90f;
            float maxLon = -180f;
            float minLon = 180f;

            Hashtable<String, ArrayList<String>> interesting = new Hashtable<String, ArrayList<String>>();
            ArrayList<String> shop = new ArrayList<String>();
            shop.add("supermarket");
            interesting.put("shop", shop);
            ArrayList<String> amenity = new ArrayList<String>();
            amenity.add("atm");
            amenity.add("doctors");
            amenity.add("kindergarten");

            interesting.put("amenity", amenity);
            NodeList nWays = doc.getElementsByTagName("way");
            NodeList nNodes = doc.getElementsByTagName("node");

            //Searching intersting nodes
            System.out.println("OSM Parser Nodes");
            System.out.println("nNodesLength : "+ nNodes.getLength());
            for (int n = 0; n < nNodes.getLength(); n++) {
                Node nNode = nNodes.item(n);
                Element eNode = (Element) nNode;
                // Inspection des tags pour savoir s'il nous intéresse 
                NodeList nTags = eNode.getElementsByTagName("tag");
                for (int t = 0; t < nTags.getLength(); t++) {
                        NamedNodeMap attributes = ((Element)nTags.item(t)).getAttributes();
                        String k = attributes.getNamedItem("k").getNodeValue();
                        String v = attributes.getNamedItem("v").getNodeValue();
                        if(interesting.containsKey(k) && interesting.get(k).contains(v)) {
                                isInteresting = true;
                                type = v;
                        }
                        if(k.equals("name")) {
                                name = v;
                        }
                }
                if(isInteresting) {
                        lat = Float.parseFloat(eNode.getAttribute("lat"));
                        lon = Float.parseFloat(eNode.getAttribute("lon"));

                        // CREATION OBJET 
                        CreateObject( type, name, lat, lon);
                }
                isInteresting = false;
                type = "";
                name = "";
            }

            //Searching intersting ways
            System.out.println("OSM Parser Ways");
            System.out.println("nWaysLength : "+ nWays.getLength());
            for (int w = 0; w < nWays.getLength(); w++) {
                    Node nWay = nWays.item(w);
                    Element eWay = (Element) nWay;

                    // Inspection des tags
                    NodeList nTags = eWay.getElementsByTagName("tag");
                    for (int t = 0; t < nTags.getLength(); t++) {
                            NamedNodeMap attributes = ((Element)nTags.item(t)).getAttributes();
                            String k = attributes.getNamedItem("k").getNodeValue();
                            String v = attributes.getNamedItem("v").getNodeValue();
                            if(interesting.containsKey(k) && interesting.get(k).contains(v)) {
                                    isInteresting = true;
                                    type = v;
                            }
                            if(k.equals("name")) {
                                    name = v;
                            }
                    }

                    // Récupération des infos des points qui composent la way
                    if(isInteresting) {
                            NodeList nNds = eWay.getElementsByTagName("nd");
                            for (int n = 0; n < nNds.getLength(); n++) {
                                    String ref = ((Element)nNds.item(n)).getAttribute("ref");
                                    boolean founded = false;
                                    for (int node = 0; !founded && node < nNodes.getLength(); node++) {
                                            Element eNode = (Element) nNodes.item(node);
                                            if(eNode.getAttribute("id").equals(ref)) {
                                                    founded = true;
                                                    lat = Float.parseFloat(eNode.getAttribute("lat"));
                                                    lon = Float.parseFloat(eNode.getAttribute("lon"));
                                                    if(maxLat < lat) maxLat = lat;
                                                    if(minLat > lat) minLat = lat;
                                                    if(maxLon < lon) maxLon = lon;
                                                    if(minLon > lon) minLon = lon;
                                            }
                                    }
                            }
                            lat = (maxLat + minLat)/2;
                            lon = (maxLon + minLon)/2;

                            // CREATION OBJET 
                            CreateObject(type, name, lat, lon);
                    }
                    isInteresting = false;
                    type = "";
                    name = "";
                    maxLat = -90f;
                    minLat = 90f;
                    maxLon = -180f;
                    minLon = 180f;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void CreateObject(String type, String name, float lat, float lon){
        switch (type) {
            case "atm" :
                Atm atm = new Atm(0f, 0f, 0f, 0f, "");
                atm.setLat(lat);
                atm.setLon(lon);
                atm.setName(name);
                listAtm.add(atm);
                break;
            case "supermarket" :
                Supermarket superm = new Supermarket(0f, 0f, 0f, 0f, "");
                superm.setLat(lat);
                superm.setLon(lon);
                superm.setName(name); 
                listSuper.add(superm);
                break;
            case "doctors" : 
                Doctor doctor = new Doctor(0f, 0f, 0f, 0f, "");
                doctor.setLat(lat);
                doctor.setLon(lon);
                doctor.setName(name);
                listDoctor.add(doctor);
                break;
            case "kindergarten" :
                Kindergarten kinder = new Kindergarten(0f, 0f, 0f, 0f, "");
                kinder.setLat(lat);
                kinder.setLon(lon);
                kinder.setName(name); 
                listKindergarten.add(kinder);
                break;
        }
    }
}
