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

public class CategoryAdapter extends ArrayAdapter {

    private final Activity context;
    private final ArrayList<String> categorysArrayList;


    public CategoryAdapter(Activity context, ArrayList<String> categorysArrayList) {
        super(context, R.layout.category_row, categorysArrayList);
        this.context = context;
        this.categorysArrayList = categorysArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = context.getLayoutInflater().inflate(R.layout.category_row, null, true);

        TextView yearTextView = (TextView) rowView.findViewById(R.id.category);
        yearTextView.setText(categorysArrayList.get(position));

        return rowView;
    }
}
