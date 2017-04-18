package com.example.galtzemach.saveit.MonthlyBills;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.galtzemach.saveit.R;

import java.util.ArrayList;

public class MonthMonthlyBillsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> yearsArrayList;


    public MonthMonthlyBillsAdapter(Activity context, ArrayList<String> yearArrayList) {
        super(context, R.layout.year_row, yearArrayList);
        this.context = context;
        this.yearsArrayList = yearArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = context.getLayoutInflater().inflate(R.layout.year_row, null, true);

        TextView yearTextView = (TextView) rowView.findViewById(R.id.y_year);
        yearTextView.setText(yearsArrayList.get(position));

        return rowView;
    }


}
