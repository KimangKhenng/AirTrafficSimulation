package Utilities;




import java.time.LocalDateTime;
import java.util.*;

public class ScheduleFactory {
    public static int agentStartTime = 10;
    /**
     * Factory for flightSchedule
     * @return
     */
    public static List<Airport> getAllAirport(){
        List<Airport> airports = new ArrayList<>();
        List<FlightSchedule> flightSchedules = xmlReader.flightSchedules();
        for(int i = 0 ; i < flightSchedules.size() ; ++i ){
            airports.add(flightSchedules.get(i).getOrigin());
            airports.add(flightSchedules.get(i).getDestination());
        }
        for(int i = 0 ; i < airports.size() ; ++i){
            for(int j = i + 1 ; j < airports.size() ; ++j ){
                if(airports.get(i).getName().equals(airports.get(j).getName())){
                    airports.remove(j);
                }
            }
        }
        return airports;
    }
    public static Airport getAirport(String name){
        List<Airport> airportList = getAllAirport();
        for(int i = 0 ; i < airportList.size() ; ++i){
            if(airportList.get(i).getName().equals(name)){
                return airportList.get(i);
            }
        }
        return new Airport();
    }


    private static List<FlightSchedule> generateSchedules(){
        /**
         * Generate more schedules
         */
        List<FlightSchedule> newSchedules = xmlReader.flightSchedules();
        List<FlightSchedule> schedules = xmlReader.flightSchedules();
        for(int i = 0 ; i < schedules.size() ; ++i){
            /**
             * Swap Airport, direction, and flight name
             */
            FlightSchedule schedule = schedules.get(i);
            Airport airport = schedule.getOrigin();
            schedule.setOrigin(schedule.getDestination());
            schedule.setDestination(airport);
            /**
             * Swap flight Name
             */
            String[] flightName = schedule.getFlightName().split("-");
            String flight = flightName[1] + "-" + flightName[0];
            schedule.setFlightName(flight);
            /**
             * Swap latLong
             */
            Collections.reverse(schedule.getFlightTrajectories());
            newSchedules.add(schedule);
        }
        /**
         * Generate Time
         */
        for(int i = 0 ; i < newSchedules.size() ; ++i){
            Double distance = newSchedules.get(i).getDistance();
            Double speed = newSchedules.get(i).getSpeed();
            long arrive = (long) ((distance/speed)*3600);
//            System.out.println("Distance: " + distance);
//            System.out.println("Speed: " + speed);
//            System.out.println("Arrive: " + arrive);
            newSchedules.get(i).setDepartTime(LocalDateTime.now().plusSeconds(agentStartTime));
            newSchedules.get(i).setArrivalTime(LocalDateTime.now().plusSeconds(agentStartTime + arrive));
            newSchedules.get(i).setStatus(FlightStatus.SCHEDULED);
        }
        return newSchedules;
    }
    public static List<FlightSchedule> searchSchedule(String airport){
        List<FlightSchedule> scheduleList = new ArrayList<>();
        List<FlightSchedule> schedules = generateSchedules();
        for(int i = 0 ; i < schedules.size() ; ++i ){
            if(airport.equals(schedules.get(i).getOrigin().getName())){
                scheduleList.add(schedules.get(i));
            }
        }
        return scheduleList;
    }
    public static List<Aircraft> getAllAircraft(){
        List<Airport> airportList = getAllAirport();
        List<Aircraft> aircraftList = new ArrayList<>();
        for(int i = 0 ; i < airportList.size() ; i++){
            String Prefix = airportList.get(i).getName().substring(0,2).toUpperCase();
            String origin = airportList.get(i).getName();
            double latitude = airportList.get(i).getLocation().getLatitude();
            double longitude = airportList.get(i).getLocation().getLongitude();

            List<FlightSchedule> schedules = searchSchedule(origin);

            for(int j = 0 ; j < schedules.size() ; j++){
                Aircraft aircraft = new Aircraft();
                String aircraftName = Prefix + "0" + j;
                String destination = searchSchedule(origin).get(j).getDestination().getName();
                System.out.println(destination);
                aircraft.setName(aircraftName);
                aircraft.setOrigin(origin);
                aircraft.setDestination(destination);
                double speed = searchSchedule(origin).get(j).getSpeed();
                aircraft.setSpeed(speed);
                aircraft.setLatitude(latitude);
                aircraft.setLongitude(longitude);
                aircraft.setStatus(FlightStatus.SCHEDULED.toString());
                aircraft.setWayPoint(0);
                aircraft.setTotalDistance(schedules.get(j).getDistance());
                aircraft.setRemainingDistance(schedules.get(j).getDistance());
                aircraftList.add(aircraft);
            }
        }
        return aircraftList;
    }

    public static Aircraft searchAircraft(String name){
        List<Aircraft> list = getAllAircraft();
        for(int i = 0 ; i < list.size() ; i++){
            if(list.get(i).getName().equals(name)){
                return list.get(i);
            }
        }
        return new Aircraft();
    }

    public static void main(String args[]){
        List<FlightSchedule> schedules = generateSchedules();
        for(int i = 0 ; i < schedules.size() ; ++i){
            System.out.println(schedules.get(i).getFlightName());
            //System.out.println(schedules.get(i).getFlightTrajectories().toString());
            System.out.println("Origin: " + schedules.get(i).getOrigin().getName());
            System.out.println("Destination: " + schedules.get(i).getDestination().getName());
            System.out.println("Depart: " + schedules.get(i).getDepartTime());
            System.out.println("Arrive: " + schedules.get(i).getArrivalTime());
        }
    }

}
