package com.example.galtzemach.saveit.Warranty;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.galtzemach.saveit.R;

import java.util.ArrayList;

public class YearsWarrantyAdapter extends ArrayAdapter {

    private final Activity context;
    private final ArrayList<String> yearsArrayList;


    public YearsWarrantyAdapter(Activity context, ArrayList<String> yearArrayList) {
        super(context, R.layout.one_row, yearArrayList);
        this.context = context;
        this.yearsArrayList = yearArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = context.getLayoutInflater().inflate(R.layout.one_row, null, true);

        TextView yearTextView = (TextView) rowView.findViewById(R.id.y_year);
        yearTextView.setText(yearsArrayList.get(position));


        return rowView;
    }
}
