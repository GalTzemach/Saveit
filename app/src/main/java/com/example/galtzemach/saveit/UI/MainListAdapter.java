package com.example.galtzemach.saveit.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.galtzemach.saveit.R;

import java.util.ArrayList;

public class MainListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> myArr;
    private  Context context;


    public MainListAdapter(@NonNull Context context, ArrayList<String> myArr) {
        super(context, 0, myArr);
        this.myArr = myArr;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        super.getView(position, convertView, parent);
        String item = myArr.get(position);

        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.year_item, parent, false);


        return convertView;
    }

}
