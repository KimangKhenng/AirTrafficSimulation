package Utilities;

import com.javadocmd.simplelatlng.LatLng;

public class Airport {
    private String code;
    private String name;
    private LatLng location;
    private int numbersPlane;

    public Airport() {
    }

    public Airport(String code, String name, LatLng location, int numerofPlane) {
        this.code = code;
        this.name = name;
        this.location = location;
        this.numbersPlane = numerofPlane;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getNumerofPlane() {
        return numbersPlane;
    }

    public void setNumerofPlane(int numerofPlane) {
        this.numbersPlane = numerofPlane;
    }
}
