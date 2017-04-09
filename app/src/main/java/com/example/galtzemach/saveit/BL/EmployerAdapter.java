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

public class EmployerAdapter extends ArrayAdapter {

    private final Activity context;
    private final ArrayList<String> employersArrayList;


    public EmployerAdapter(Activity context, ArrayList<String> employerArrayList) {
        super(context, R.layout.employer_row, employerArrayList);
        this.context = context;
        this.employersArrayList = employerArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = context.getLayoutInflater().inflate(R.layout.employer_row, null, true);

        TextView yearTextView = (TextView) rowView.findViewById(R.id.employer);
        yearTextView.setText(employersArrayList.get(position));

        return rowView;
    }
}
