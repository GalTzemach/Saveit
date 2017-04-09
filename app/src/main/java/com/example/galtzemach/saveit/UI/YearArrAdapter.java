package com.example.galtzemach.saveit.UI;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.galtzemach.saveit.R;

public class YearArrAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] yearsArr;

    public YearArrAdapter(Activity context, String[] yearsArr) {
        super(context, R.layout.year_list, yearsArr);

        this.context = context;
        this.yearsArr = yearsArr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //super.getView(position, convertView, parent);
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.year_list, null, true);
        TextView yearTextView = (TextView) rowView.findViewById(R.id.year_txt);
        yearTextView.setText(yearsArr[position]);
        return rowView;
    }
}
