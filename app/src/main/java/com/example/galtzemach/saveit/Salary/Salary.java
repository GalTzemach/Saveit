package com.example.galtzemach.saveit.Salary;

import java.util.ArrayList;

public class Salary {

    private String employer;
    private int year;
    private int month;
    private float grossRevenue;
    private float netRevenue;
    private String notes;

    private ArrayList<String> downloadUriArr;

    public Salary() {
    }

    public Salary(String employer, int year, int month, float totalRevenue, float netRevenue, String notes) {
        this.employer = employer;
        this.year = year;
        this.month = month;
        this.grossRevenue = totalRevenue;
        this.netRevenue = netRevenue;
        this.notes = notes;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public float getGrossRevenue() {
        return grossRevenue;
    }

    public void setGrossRevenue(float grossRevenue) {
        this.grossRevenue = grossRevenue;
    }

    public float getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(float netRevenue) {
        this.netRevenue = netRevenue;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<String> getDownloadUriArr() {
        return downloadUriArr;
    }

    public void setDownloadUriArr(ArrayList<String> downloadUriArr) {
        this.downloadUriArr = downloadUriArr;
    }

    @Override
    public String toString() {
        return "Salary{" +
                "employer='" + employer + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", grossRevenue=" + grossRevenue +
                ", netRevenue=" + netRevenue +
                ", notes='" + notes + '\'' +
                ", downloadUriArr=" + downloadUriArr +
                '}';
    }

}
