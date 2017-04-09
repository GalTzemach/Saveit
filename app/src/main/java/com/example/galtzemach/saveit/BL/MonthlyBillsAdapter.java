package com.example.galtzemach.saveit.BL;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.galtzemach.saveit.R;

import java.util.ArrayList;

public class MonthlyBillsAdapter extends ArrayAdapter {

    private final Activity context;
    private final ArrayList<String> monthlyBillsArrayList;


    public MonthlyBillsAdapter(Activity context, ArrayList<String> monthlyBillsArrayList) {
        super(context, R.layout.monthly_bills_row, monthlyBillsArrayList);
        this.context = context;
        this.monthlyBillsArrayList = monthlyBillsArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = context.getLayoutInflater().inflate(R.layout.monthly_bills_row, null, true);

        TextView categoryMYTextView = (TextView) rowView.findViewById(R.id.m_row_category_m_y);
        categoryMYTextView.setText(monthlyBillsArrayList.get(position));///get cat & m/y

        TextView sumTextView = (TextView) rowView.findViewById(R.id.m_row_sum);
        sumTextView.setText(monthlyBillsArrayList.get(position));///get sum

        TextView notesTextView = (TextView) rowView.findViewById(R.id.m_row_notes);
        notesTextView.setText(monthlyBillsArrayList.get(position));///get notes

        return rowView;
    }
}
