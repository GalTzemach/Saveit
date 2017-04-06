package com.example.galtzemach.saveit.BL;

import android.widget.DatePicker;

public class Warranty {

    private enum Category {salary, monthlyBills, warranty}
    private Category category;
    private int periodInMonths;
    private DatePicker purchaseDate;
    private DatePicker expiryDate;
    private float cost;
    private String notes;
    private String[] refPhotoArr;


    public Warranty(Category category, int periodInMonths, DatePicker purchaseDate, DatePicker expiryDate, float cost, String[] refPhotoArr, String notes) {
        this.category = category;
        this.periodInMonths = periodInMonths;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
        this.cost = cost;
        this.refPhotoArr = refPhotoArr;
        this.notes = notes;
    }


}
