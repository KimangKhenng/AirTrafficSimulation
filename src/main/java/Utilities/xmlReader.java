package Utilities;

import com.javadocmd.simplelatlng.LatLng;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class xmlReader {


    public static List<FlightSchedule> flightSchedules(){
        /**
         * Read XML data
         */
        List<FlightSchedule> flightSchedules = new ArrayList<>();
        try {
            //Get file from resources folder
            File schedule = new File("resources/flightscheduels.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(schedule);
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("flightSchedule");
            //System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                flightSchedules.add(getSchedule(nNode));

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return flightSchedules;

    }
    private static FlightSchedule getSchedule(Node node){
        FlightSchedule schedule = new FlightSchedule();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            schedule.setFlightName(getTagValue("flightName",element));
            schedule.setAltitude(Double.parseDouble(getTagValue("altitude",element)));
            schedule.setFlightType(getTagValue("flightType",element));
            schedule.setSpeed(Double.parseDouble(getTagValue("speed",element)));
            schedule.setWayPoints(Integer.parseInt(getTagValue("wayPoints",element)));
            schedule.setFlightType(getTagValue("flightType",element));

            Node originAirport = element.getElementsByTagName("originAirport").item(0);
            schedule.setOrigin(getAirport(originAirport));

            Node desAirport = element.getElementsByTagName("desAirport").item(0);
            //System.out.println("Airport Name " + desAirport.getTextContent());
            schedule.setDestination(getAirport(desAirport));

            Node lat = element.getElementsByTagName("Latitudes").item(0);
            //System.out.println("latitude Va"+lat.getTextContent());
            Node lg = element.getElementsByTagName("Longitudes").item(0);
            //System.out.println("Longitude Va"+lg.getTextContent());

            schedule.setFlightTrajectories(getLatLong(lat,lg));
        }
        return schedule;

    }
    private static List<LatLng> getLatLong(Node lat , Node lg){
        List<LatLng> latLngs = new ArrayList<>();
        List<Double> latList = getLatLongList(lat);
        //System.out.println("Lat Size " + latList.size());
        List<Double> longList = getLatLongList(lg);
        //System.out.println("Long Size " + longList.size());
        for(int i = 0 ; i < latList.size() ; ++i){
            latLngs.add(new LatLng(latList.get(i),longList.get(i)));
        }
        return latLngs;

    }
    private static List<Double> getLatLongList(Node node){
        List<Double> latlong = new ArrayList<>();
        if(node.getNodeType() == node.ELEMENT_NODE){
            Element element = (Element)node;
            NodeList list = element.getElementsByTagName("value");
            //System.out.println("Number of values: " + list.getLength());
            for(int i = 0 ; i < list.getLength() ; i++){
                Node node1 = list.item(i);
                if(node1.getNodeType() == node1.ELEMENT_NODE){
                    //System.out.println("Latlong Value: " + node1.getTextContent());
                    latlong.add(Double.parseDouble(node1.getTextContent()));
                }
            }
        }
        return latlong;
    }
    private static Airport getAirport(Node node){
        Airport airport = new Airport();
        if(node.getNodeType() == node.ELEMENT_NODE){
            Element element = (Element)node;
            airport.setCode(getTagValue("code",element));
            LatLng position = new LatLng(Double.parseDouble(getTagValue("Latitude",element)),
                    Double.parseDouble(getTagValue("Longitude",element)));
            airport.setLocation(position);
            airport.setName(getTagValue("name",element));
            airport.setNumerofPlane(Integer.parseInt(getTagValue("numberOfAirplane",element)));
        }
        return airport;
    }
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        //System.out.println(nodeList.getLength());
        Node node = nodeList.item(0);
        //System.out.println(node.getNodeValue());
        return node.getNodeValue();
    }
    public static void main(String args[]){
        List<FlightSchedule> list = flightSchedules();
        System.out.println("Number of Schedule: " + list.size());
        for (int i = 0 ; i < list.size() ; i++){
            System.out.println(list.get(i).getFlightName());
        }
    }
}
