package com.example.galtzemach.saveit.BL;

public class Salary {

    private String employer;
    private int year;
    private int month;
    private float totalRevenue;
    private float netRenevnue;
    private String[] refPhotoArr;
    private String notes;

    public Salary(String employer, int year, int month, float totalRevenue, float netRenevnue, String[] refPhotoArr, String notes) {
        this.employer = employer;
        this.year = year;
        this.month = month;
        this.totalRevenue = totalRevenue;
        this.netRenevnue = netRenevnue;
        this.refPhotoArr = refPhotoArr;
        this.notes = notes;
    }


}
