package com.example.galtzemach.saveit.BL;

public class MonthlyBills {

    private enum Category {salary, monthlyBills, warranty}
    private Category category;
    private int year;
    private int month;
    private float sum;
    private String[] refPhotoArr;
    private String notes;

    public MonthlyBills(Category category, int year, int month, float sum, String[] refPhotoArr, String notes) {
        this.category = category;
        this.year = year;
        this.month = month;
        this.sum = sum;
        this.refPhotoArr = refPhotoArr;
        this.notes = notes;
    }

}
