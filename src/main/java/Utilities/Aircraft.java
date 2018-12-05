package Utilities;

public class Aircraft {
    private String name;
    private String origin;
    private String destination;
    private Double latitude;
    private Double longitude;
    private Double speed;
    private String status;
    private Double totalDistance;
    private Double remainingDistance;

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Double getRemainingDistance() {
        return remainingDistance;
    }

    public void setRemainingDistance(Double remainingDistance) {
        this.remainingDistance = remainingDistance;
    }

    public int getWayPoint() {
        return wayPoint;
    }

    public void setWayPoint(int wayPoint) {
        this.wayPoint = wayPoint;
    }

    private int wayPoint;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public Aircraft(String name, String origin, String destination, double latitude, double longitude, double speed, String status) {
        this.name = name;
        this.origin = origin;
        this.destination = destination;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.status = status;
    }
    public Aircraft() {
    }
}
