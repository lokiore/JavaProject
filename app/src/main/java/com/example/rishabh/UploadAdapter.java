package com.example.rishabh;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lokiore on 11/4/18.
 */

public class UploadAdapter extends ArrayAdapter<Upload> {
    public UploadAdapter(Activity context, ArrayList<Upload> uploads) {
        super(context, 0, uploads);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listViewItem = convertView;
        if (listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
        }

        //Timeline timeline = getItem(position);
        Upload upload = getItem(position);


        ImageView timelineProfilePhoto = listViewItem.findViewById(R.id.upload_profile_photo);
        //timelineProfilePhoto.setImageResource(timeline.getProfilePic());
        Uri profUri = Uri.parse(upload.getImageUrl());
        Picasso.get().load(profUri).into(timelineProfilePhoto);


        TextView timelineUsername = listViewItem.findViewById(R.id.upload_username);
        timelineUsername.setText(upload.getName());


        return listViewItem;
    }
}
