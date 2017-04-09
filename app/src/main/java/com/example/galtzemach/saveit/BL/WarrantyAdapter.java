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

public class WarrantyAdapter extends ArrayAdapter {

    private final Activity context;
    private final ArrayList<String> warrantiesArrayList;


    public WarrantyAdapter(Activity context, ArrayList<String> warrantiesArrayList) {
        super(context, R.layout.warranty_row, warrantiesArrayList);
        this.context = context;
        this.warrantiesArrayList = warrantiesArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = context.getLayoutInflater().inflate(R.layout.warranty_row, null, true);

        TextView nameTextView = (TextView) rowView.findViewById(R.id.w__row_name);
        nameTextView.setText(warrantiesArrayList.get(position));///get name

        TextView expDateTextView = (TextView) rowView.findViewById(R.id.w__row_exp_date);
        expDateTextView.setText(warrantiesArrayList.get(position));///get exp

        TextView notesTextView = (TextView) rowView.findViewById(R.id.w_row_notes);
        notesTextView.setText(warrantiesArrayList.get(position));///get notes

        return rowView;
    }
}
