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

import java.util.ArrayList;

public class YearArrayListAdapter extends ArrayAdapter{

    private final Activity context;
    private final ArrayList<String> yearsArr;

    public YearArrayListAdapter(Activity context, ArrayList<String> yearsArr) {
        super(context, R.layout.year_list_old, yearsArr);
        this.context = context;
        this.yearsArr = yearsArr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.year_list_old, null, true);
        TextView yearTextView = (TextView) rowView.findViewById(R.id.year_txt);
        yearTextView.setText(yearsArr.get(position));
        return rowView;
    }
}
