//package com.example.galtzemach.saveit.Salary;
//
//import android.app.Activity;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.example.galtzemach.saveit.R;
//
//import java.util.ArrayList;
//
//public class SalaryAdapter extends ArrayAdapter {
//    private final Activity context;
//    private final ArrayList<String> salariesArrayList;
//
//
//    public SalaryAdapter(Activity context, ArrayList<String> salariesArrayList) {
//        super(context, R.layout.salalry_row, salariesArrayList);
//        this.context = context;
//        this.salariesArrayList = salariesArrayList;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View rowView = context.getLayoutInflater().inflate(R.layout.salalry_row, null, true);
//
//        TextView yearTextView = (TextView) rowView.findViewById(R.id.s_row_month);
//        yearTextView.setText(salariesArrayList.get(position));///get month
//
//        TextView grossNetTextView = (TextView) rowView.findViewById(R.id.s_row_gross_net);
//        grossNetTextView.setText(salariesArrayList.get(position));///get gross&net
//
//        TextView notesTextView = (TextView) rowView.findViewById(R.id.s_row_notes);
//        notesTextView.setText(salariesArrayList.get(position));///get notes
//
//        return rowView;
//    }
//}
