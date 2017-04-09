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

public class SalaryRowAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> salariesArrayList;


    public SalaryRowAdapter(Context context, ArrayList<String> salariesArrayList) {
        super(context, R.layout.year_row, salariesArrayList);

        this.context = context;
        this.salariesArrayList = salariesArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View rowView=  LayoutInflater.from(this.context).inflate(R.layout.salalry_row, null, true);

        TextView yearTextView = (TextView) rowView.findViewById(R.id.s_row_month);
        yearTextView.setText(salariesArrayList.get(position));///get month

        TextView grossNetTextView = (TextView) rowView.findViewById(R.id.s_row_gross_net);
        grossNetTextView.setText(salariesArrayList.get(position));///get gross&net

        TextView notesTextView = (TextView) rowView.findViewById(R.id.s_row_notes);
        notesTextView.setText(salariesArrayList.get(position));///get notes

        return rowView;
    }
}
