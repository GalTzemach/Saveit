package com.example.galtzemach.saveit.BL;

import java.util.Date;

public class Warranty {

    public enum Category {a, b, c, d, e, f, g, h}
    private Category category;
    private String name;
    private int periodInMonths;
    private Date purchaseDate;
    private Date expiryDate;
    private float cost;
    private String notes;
    private String[] refPhotoArr;


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


}
