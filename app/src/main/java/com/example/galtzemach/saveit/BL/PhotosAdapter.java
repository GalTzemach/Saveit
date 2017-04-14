package com.example.galtzemach.saveit.BL;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = context.getLayoutInflater().inflate(R.layout.photos_view, null, true);

        TextView numImageTextView = (TextView) rowView.findViewById(R.id.number_image_view);
        numImageTextView.setText("Item " + (position+1) + " from " + photosArrayList.size());

        ImageView photoImageView = (ImageView) rowView.findViewById(R.id.image_view);
        Picasso.with(context.getApplicationContext()).load(photosArrayList.get(position)).into(photoImageView);

        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(photosArrayList.get(position))));
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(photosArrayList.get(position))));



            }
        });

        return rowView;
    }
}
