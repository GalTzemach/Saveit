package com.example.galtzemach.saveit.BL;

import java.util.Arrays;
import java.util.Date;

import java.util.ArrayList;

public class Warranty {

    private String name;
    private int periodInMonths;
    private Date purchaseDate;
    private Date expiryDate;
    private float cost;
    private String notes;
    private String[] refPhotoArr;

    private ArrayList<String> downloadUriArr;

    public Warranty() {
    }

    public Warranty(String name, int periodInMonths, Date purchaseDate, Date expiryDate, float cost, String notes) {

        this.name = name;
        this.periodInMonths = periodInMonths;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
        this.cost = cost;
        this.refPhotoArr = refPhotoArr;
        this.notes = notes;
    }

    public int getPeriodInMonths() {
        return periodInMonths;
    }

    public void setPeriodInMonths(int periodInMonths) {
        this.periodInMonths = periodInMonths;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public float getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public void setCost(float cost) {
        this.cost = cost;
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
        return "Warranty{" +
                "name='" + name + '\'' +
                ", periodInMonths=" + periodInMonths +
                ", purchaseDate=" + purchaseDate +
                ", expiryDate=" + expiryDate +
                ", cost=" + cost +
                ", notes='" + notes + '\'' +
                ", refPhotoArr=" + Arrays.toString(refPhotoArr) +
                ", downloadUriArr=" + downloadUriArr +
                '}';
    }

}
