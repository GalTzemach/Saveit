package com.example.galtzemach.saveit.BL;

public class MonthlyBills {

    private enum Category {ab, bc, cd}
    private Category category;
    private int year;
    private int month;
    private float sum;
    private String notes;
    private String[] refPhotoArr;


    public MonthlyBills(Category category, int year, int month, float sum, String[] refPhotoArr, String notes) {
        this.category = category;
        this.year = year;
        this.month = month;
        this.sum = sum;
        this.refPhotoArr = refPhotoArr;
        this.notes = notes;
    }

}
