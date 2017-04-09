package com.example.galtzemach.saveit.UI;

import com.example.galtzemach.saveit.BL.MonthlyBills;
import com.example.galtzemach.saveit.BL.Salary;
import com.example.galtzemach.saveit.BL.Warranty;

import java.util.ArrayList;

/**
 * Created by Tal on 07/04/2017.
 */

public interface DataReadyListener {

    // Salary
    void onAddSalaryComplete();

    void onEmployersListReady(ArrayList<String> employersList);
    void onYearsListReady_Salary(ArrayList<Integer> yearsList);
    void onSalaryListReady(ArrayList<Salary> salaryList);

    // Warranty
    void onAddWarrantyComplete();

    void onYearsListReady_Warranty(ArrayList<Integer> yearsList);
    void onWarrantyListReady(ArrayList<Warranty> warrantyList);

    // MonthlyBills
    void onAddMonthlyBillsComplete();

    void onCategoryListReady(ArrayList<String> CategoryList);
    void onYearsListReady_MonthlyBills(ArrayList<Integer> yearsList);
    void onMonthlyBillsListReady(ArrayList<MonthlyBills> monthlyBillsList);

}
