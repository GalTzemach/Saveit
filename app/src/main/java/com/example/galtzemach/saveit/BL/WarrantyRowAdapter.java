package com.example.galtzemach.saveit.BL;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.galtzemach.saveit.R;

import java.util.ArrayList;

public class WarrantyRowAdapter extends ArrayAdapter {

    private final Context context;
    private final ArrayList<String> warrantiesArrayList;


    public WarrantyRowAdapter(Context context, ArrayList<String> warrantiesArrayList) {
        super(context, R.layout.warranty_row, warrantiesArrayList);
        this.context = context;
        this.warrantiesArrayList = warrantiesArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.warranty_row, null, true);

        TextView nameTextView = (TextView) rowView.findViewById(R.id.w__row_name);
        nameTextView.setText(warrantiesArrayList.get(position));///get name

        TextView expDateTextView = (TextView) rowView.findViewById(R.id.w__row_exp_date);
        expDateTextView.setText(warrantiesArrayList.get(position));///get exp

        TextView notesTextView = (TextView) rowView.findViewById(R.id.w_row_notes);
        notesTextView.setText(warrantiesArrayList.get(position));///get notes

        return rowView;
    }
}
