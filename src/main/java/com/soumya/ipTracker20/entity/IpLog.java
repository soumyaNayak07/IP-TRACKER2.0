package com.soumya.ipTracker20.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class IpLog {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int sno;
    private String date;
    private  String time;
    private String ip;
    private String country;
    private String region;
    private String city;
    private String  zip;
    private double lat;
    private double lon;
    private String timezone;
    private String isp;
    private String link;
    private String username;



    public int getSno() {
        return sno;
    }

    public String getIp() {
        return ip;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getIsp() {
        return isp;
    }

    public String getLink() {
        return link;
    }

    public String getUsername() {
        return username;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
