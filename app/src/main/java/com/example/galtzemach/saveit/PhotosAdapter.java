package com.example.galtzemach.saveit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotosAdapter extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> photosArrayList;

    private int counter;
    private ProgressDialog progressDialog;

    public PhotosAdapter(Activity context, ArrayList<String> photosArrayList) {
        super(context, R.layout.photos_view, photosArrayList);
        this.context = context;
        this.photosArrayList = photosArrayList;
        counter = 0;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading photos...");
        progressDialog.show();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        counter ++;
//        if (counter >= photosArrayList.size())
//            progressDialog.dismiss();

//        View rowView;
//        if(convertView == null)
//            rowView = context.getLayoutInflater().inflate(R.layout.photos_view, null, true);
//        else
//            rowView = convertView;

        View rowView = context.getLayoutInflater().inflate(R.layout.photos_view, null, true);

        TextView numImageTextView = (TextView) rowView.findViewById(R.id.number_image_view);
        numImageTextView.setText("Photo " + (position+1) + " from " + photosArrayList.size());

        ImageView photoImageView = (ImageView) rowView.findViewById(R.id.image_view);
        //Picasso.with(context.getApplicationContext()).load(photosArrayList.get(position)).into(photoImageView);
        Picasso.with(context.getApplicationContext())
                .load(photosArrayList.get(position))
                .into(photoImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
//                        Toast.makeText(getContext(), "A Photo " + (position+1) + " Success", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError() {
//                        Toast.makeText(getContext(), "A Photo " + (position+1) + " Error", Toast.LENGTH_SHORT).show();
                    }
                });

        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(photosArrayList.get(position))));
            }
        });

        return rowView;
    }
}
