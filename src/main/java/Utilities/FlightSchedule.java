package Utilities;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class FlightSchedule {
    /**
     * Data Member
     */
    private String flightName;
    private Airport origin;
    private Airport destination;

    public LocalDateTime getDepartTime() {
        return departTime;
    }

    public void setDepartTime(LocalDateTime departTime) {
        this.departTime = departTime;
    }

    public LocalDateTime getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        ArrivalTime = arrivalTime;
    }

    private LocalDateTime departTime;
    private LocalDateTime ArrivalTime;
    private int wayPoints;
    private FlightStatus status;
    private double speed;
    private double altitude;
    private String flightType;
    private List<LatLng> flightTrajectories;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getFlightType() {
        return flightType;
    }

    public void setFlightType(String flightType) {
        this.flightType = flightType;
    }



    public FlightSchedule() {
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public Airport getOrigin() {
        return origin;
    }

    public void setOrigin(Airport origin) {
        this.origin = origin;
    }

    public Airport getDestination() {
        return destination;
    }

    public void setDestination(Airport destination) {
        this.destination = destination;
    }

    public void setWayPoints(int wayPoints) {
        this.wayPoints = wayPoints;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public void setFlightTrajectories(List<LatLng> flightTrajectories) {
        this.flightTrajectories = flightTrajectories;
    }

    public double getDistance() {
        Double totalDistance = 0.0;
        for(int i = 0 ; i < flightTrajectories.size() && i != flightTrajectories.size() - 2 ; ++i){
            Double distance = LatLngTool.distance(flightTrajectories.get(i),flightTrajectories.get(i+1), LengthUnit.KILOMETER);
            totalDistance += distance;
        }
        return totalDistance;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public List<LatLng> getFlightTrajectories() {
        return flightTrajectories;
    }



    public int getWayPoints() { return wayPoints; }
}
