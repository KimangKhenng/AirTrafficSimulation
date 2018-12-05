package GUI;

import Utilities.Aircraft;
import Utilities.ScheduleFactory;
import com.javadocmd.simplelatlng.LatLng;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;

public class AgentInterface {
    private static volatile AgentInterface instance;
    private static Object mutex = new Object();
    private static HashMap<String, ShortInfo> aircraftHashMap;
    private static List<Aircraft> aircraftList;

    private class ShortInfo{
        private LatLng location;
        private double speed;
        private String status;
        private int wayPoint;

        public double getRemainingDistance() {
            return remainingDistance;
        }

        public void setRemainingDistance(double remainingDistance) {
            this.remainingDistance = remainingDistance;
        }

        private double remainingDistance;

        public ShortInfo(LatLng location, double speed, String status, int wayPoint, double remainingDistance) {
            this.location = location;
            this.speed = speed;
            this.status = status;
            this.wayPoint = wayPoint;
            this.remainingDistance = remainingDistance;
        }

        public LatLng getLocation() {
            return location;
        }

        public void setLocation(LatLng location) {
            this.location = location;
        }

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getWayPoint() {
            return wayPoint;
        }

        public void setWayPoint(int wayPoint) {
            this.wayPoint = wayPoint;
        }
    }

    private AgentInterface() {
        aircraftHashMap = new HashMap<>();
        aircraftList = ScheduleFactory.getAllAircraft();
        for(int i = 0 ; i < aircraftList.size() ; ++i){
            String name = aircraftList.get(i).getName();
            LatLng location = new LatLng(aircraftList.get(i).getLatitude(),aircraftList.get(i).getLongitude());
            double speed = aircraftList.get(i).getSpeed();
            String status = aircraftList.get(i).getStatus();
            int way = aircraftList.get(i).getWayPoint();
            ShortInfo info = new ShortInfo(location,speed,status,way,0);
            aircraftHashMap.put(name,info);
        }
    }

    public static AgentInterface getInstance() {
        AgentInterface result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new AgentInterface();
            }
        }
        return result;
    }
    public ObservableList<Aircraft> getData(){
        //System.out.println("Called from Interface");

        for(int i = 0 ; i < aircraftList.size() ; ++i){
            String name = aircraftList.get(i).getName();
            if(name != null){
                ShortInfo info = aircraftHashMap.get(name);
                aircraftList.get(i).setLatitude(info.getLocation().getLatitude());
                aircraftList.get(i).setLongitude(info.getLocation().getLongitude());
                aircraftList.get(i).setWayPoint(info.getWayPoint());
                aircraftList.get(i).setSpeed(info.getSpeed());
                aircraftList.get(i).setStatus(info.getStatus());
                aircraftList.get(i).setRemainingDistance(info.getRemainingDistance());
                //System.out.println("Remain: " + info.getRemainingDistance());
            }
        }

        return FXCollections.observableArrayList(aircraftList);
    }
    public void setData(String name,LatLng location, double speed, String status,int wayPoint, double remain){
        //System.out.println("Call from Agent: " + status);
        if(aircraftHashMap.get(name) != null){
            aircraftHashMap.put(name,new ShortInfo(location,speed,status,wayPoint,remain));

        }
    }

}
