package org.me.gcu.equakestartercode;

import java.io.Serializable;
/**Created by ismail adam on 25/03/2021
 * Student ID: S1908016 */
public class Description implements Serializable {
    private String dateTime;
    private String location;
    private double latitude;
    private double longitude;
    private double depth;
    private double magnitude;

    public Description(String dateTime,
                       String location,
                       double latitude,
                       double longitude,
                       double depth,
                       double magnitude) {
        this.dateTime = dateTime;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.depth = depth;
        this.magnitude = magnitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String date) {
        this.dateTime = date;
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

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }
}
