package com.example.galtzemach.saveit.BL;

import java.util.Date;

import java.util.ArrayList;

public class Warranty {

    public enum Category {a, b, c, d, e, f, g, h}
    private enum Category {salary, monthlyBills, warranty}

    private Category category;
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

    public Warranty(Category category, int periodInMonths, DatePicker purchaseDate, DatePicker expiryDate, float cost, String notes) {
    public Warranty(Category category, String name, int periodInMonths, Date purchaseDate, Date expiryDate, float cost, String[] refPhotoArr, String notes) {
        this.category = category;
        this.name = name;
        this.periodInMonths = periodInMonths;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
        this.cost = cost;
        this.refPhotoArr = refPhotoArr;
        this.notes = notes;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getPeriodInMonths() {
        return periodInMonths;
    }

    public void setPeriodInMonths(int periodInMonths) {
        this.periodInMonths = periodInMonths;
    }

    public DatePicker getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(DatePicker purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public DatePicker getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(DatePicker expiryDate) {
        this.expiryDate = expiryDate;
    }

    public float getCost() {
        return cost;
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
                "category=" + category +
                ", periodInMonths=" + periodInMonths +
                ", purchaseDate=" + purchaseDate +
                ", expiryDate=" + expiryDate +
                ", cost=" + cost +
                ", notes='" + notes + '\'' +
                ", downloadUriArr=" + downloadUriArr +
                '}';
    }

}
