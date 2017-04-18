package com.example.galtzemach.saveit.MonthlyBills;

import java.util.ArrayList;
import java.util.Arrays;

public class MonthlyBills {

    public enum Category {General, Electricity, Water, Arnona, Gas, Phones, Internet, TV}
    private Category category;

    private int year;
    private int month;
    private float sum;
    private String notes;
    private String[] refPhotoArr;

    private ArrayList<String> downloadUriArr;

    public MonthlyBills() {
    }

    public MonthlyBills(Category category, int year, int month, float sum, String notes) {
        this.category = category;
        this.year = year;
        this.month = month;
        this.sum = sum;
        this.refPhotoArr = refPhotoArr;
        this.notes = notes;
    }

    public String getCategory() {
        return category.name();
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

    public ArrayList<String> getDownloadUriArr() {
        return downloadUriArr;
    }

    public void setDownloadUriArr(ArrayList<String> downloadUriArr) {
        this.downloadUriArr = downloadUriArr;
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
                ", downloadUriArr=" + downloadUriArr +
                '}';
    }


}
