package com.example.galtzemach.saveit.BL;

import android.widget.DatePicker;

import java.util.Arrays;

public class Warranty {

    private enum Category {salary, monthlyBills, warranty}
    private Category category;
    private int periodInMonths;
    private DatePicker purchaseDate;
    private DatePicker expiryDate;
    private float cost;
    private String notes;
    private String[] refPhotoArr;

    public Warranty() {
    }

    public Warranty(Category category, int periodInMonths, DatePicker purchaseDate, DatePicker expiryDate, float cost, String[] refPhotoArr, String notes) {
        this.category = category;
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

    public String[] getRefPhotoArr() {
        return refPhotoArr;
    }

    public void setRefPhotoArr(String[] refPhotoArr) {
        this.refPhotoArr = refPhotoArr;
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
                ", refPhotoArr=" + Arrays.toString(refPhotoArr) +
                '}';
    }


}
