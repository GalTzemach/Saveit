package com.example.galtzemach.saveit.Salary;

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

public class SalaryRowAdapter extends ArrayAdapter<Salary> {

    private final Context context;
    private final ArrayList<Salary> salariesArrayList;


    public SalaryRowAdapter(Context context, ArrayList<Salary> salariesArrayList) {
        super(context, R.layout.one_row, salariesArrayList);

        this.context = context;
        this.salariesArrayList = salariesArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View rowView=  LayoutInflater.from(this.context).inflate(R.layout.salalry_row, null, true);

        TextView yearTextView = (TextView) rowView.findViewById(R.id.s_row_month);
        yearTextView.setText("Month " + salariesArrayList.get(position).getMonth());

        TextView grossTextView = (TextView) rowView.findViewById(R.id.s_row_gross);
        grossTextView.setText("Gross: " + salariesArrayList.get(position).getGrossRevenue());

        TextView NetTextView = (TextView) rowView.findViewById(R.id.s_row_net);
        NetTextView.setText("Net: " + salariesArrayList.get(position).getNetRevenue());

        TextView notesTextView = (TextView) rowView.findViewById(R.id.s_row_notes);
        notesTextView.setText(salariesArrayList.get(position).getNotes());

        return rowView;
    }

    public ArrayList<Salary> getSalariesArrayList() {
        return salariesArrayList;
    }
}
