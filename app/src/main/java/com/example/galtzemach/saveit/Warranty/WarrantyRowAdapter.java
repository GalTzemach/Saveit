package com.example.galtzemach.saveit.Warranty;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.galtzemach.saveit.MainActivity;
import com.example.galtzemach.saveit.R;

import java.util.ArrayList;

public class WarrantyRowAdapter extends ArrayAdapter<Warranty> {

    private final Context context;
    private final ArrayList<Warranty> warrantiesArrayList;


    public WarrantyRowAdapter(Context context, ArrayList<Warranty> warrantiesArrayList) {
        super(context, R.layout.warranty_row, warrantiesArrayList);
        this.context = context;
        this.warrantiesArrayList = warrantiesArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.warranty_row, null, true);

        TextView nameTextView = (TextView) rowView.findViewById(R.id.w__row_name);
        nameTextView.setText(warrantiesArrayList.get(position).getName());

        TextView expDateTextView = (TextView) rowView.findViewById(R.id.w__row_exp_date);
        expDateTextView.setText("EXP Date: " + warrantiesArrayList.get(position).getExpiryDate().getYear() + "/" + warrantiesArrayList.get(position).getExpiryDate().getMonth() + "/" + MainActivity.getDayOfMonth(warrantiesArrayList.get(position).getExpiryDate()));

        TextView notesTextView = (TextView) rowView.findViewById(R.id.w_row_notes);
        notesTextView.setText(warrantiesArrayList.get(position).getNotes());

        return rowView;
    }
}
