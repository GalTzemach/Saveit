package com.example.galtzemach.saveit;

import com.example.galtzemach.saveit.MonthlyBills.MonthlyBills;
import com.example.galtzemach.saveit.Salary.Salary;
import com.example.galtzemach.saveit.Warranty.Warranty;

import java.util.ArrayList;

/**
 * Created by Tal on 07/04/2017.
 */

public interface DataReadyListener {

    // Salary
    void onCreateSalaryComplete();
    void onYearsListReady_Salary(ArrayList<String> yearsList);
    void onSalaryListReady(ArrayList<Salary> salaryList);

    // Warranty
    void onCreateWarrantyComplete();
    void onYearsListReady_Warranty(ArrayList<String> yearsList);
    void onWarrantyListReady(ArrayList<Warranty> warrantyList);

    // MonthlyBills
    void onCreateMonthlyBillsComplete();
    void onCategoryListReady(ArrayList<String> CategoryList);
    void onYearsListReady_MonthlyBills(ArrayList<String> yearsList);
    void onMonthlyBillsListReady(ArrayList<MonthlyBills> monthlyBillsList);

}
