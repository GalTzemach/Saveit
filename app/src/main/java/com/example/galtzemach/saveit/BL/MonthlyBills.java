package com.example.galtzemach.saveit.BL;

import java.util.Arrays;

public class MonthlyBills {

    private enum Category {ab, bc, cd}
    private Category category;
    private int year;
    private int month;
    private float sum;
    private String notes;
    private String[] refPhotoArr;

    public MonthlyBills() {
    }

    public MonthlyBills(Category category, int year, int month, float sum, String[] refPhotoArr, String notes) {
        this.category = category;
        this.year = year;
        this.month = month;
        this.sum = sum;
        this.refPhotoArr = refPhotoArr;
        this.notes = notes;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
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
        return "MonthlyBills{" +
                "category=" + category +
                ", year=" + year +
                ", month=" + month +
                ", sum=" + sum +
                ", notes='" + notes + '\'' +
                ", refPhotoArr=" + Arrays.toString(refPhotoArr) +
                '}';
    }

}
