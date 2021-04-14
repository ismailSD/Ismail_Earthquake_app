package org.me.gcu.equakestartercode;

import java.io.Serializable;
/**Created by ismail adam on 25/03/2021
 * Student ID: S1908016 */

/** This class implements Serializable Java interface
 to allow the state of this class's object to be converted to
 a byte stream so that it can be reverted back into a copy of the object.
 By doing this it will help in transferring the object of this
 class to another android Activity as serializable item then convert back
 to its type (Description) object.
 */

public class Description implements Serializable {
    private String dateTime;
    private String location;
    private double latitude;
    private double longitude;
    private double depth;
    private double magnitude;

    // Constructor to allow the initialization of
    // description fields during the Description object initialization
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

    // getters and setters to enable the retrieving and setting
    // of description details

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
