package com.example.galtzemach.saveit.BL;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.galtzemach.saveit.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotosAdapter extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> photosArrayList;


    public PhotosAdapter(Activity context, ArrayList<String> photosArrayList) {
        super(context, R.layout.photos_view, photosArrayList);
        this.context = context;
        this.photosArrayList = photosArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = context.getLayoutInflater().inflate(R.layout.photos_view, null, true);

        ImageView photoImageView = (ImageView) rowView.findViewById(R.id.image_view);
        Picasso.with(context.getApplicationContext()).load(photosArrayList.get(position)).into(photoImageView);

        return rowView;
    }
}
