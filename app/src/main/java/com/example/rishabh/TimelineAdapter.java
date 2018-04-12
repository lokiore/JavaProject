package com.example.rishabh;

/**
 * Created by lokiore on 8/4/18.
 */


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

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//import info.androidhive.listviewfeed.FeedImageView;

public class TimelineAdapter extends ArrayAdapter<Timeline> {

    public TimelineAdapter(Activity context, ArrayList<Timeline> timelines) {
        super(context, 0, timelines);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listViewItem = convertView;
        if (listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.feed_item, parent, false);
        }

        Timeline timeline = getItem(position);

        ImageView timelineProfilePhoto = listViewItem.findViewById(R.id.timeline_profile_photo);
        //timelineProfilePhoto.setImageResource(timeline.getProfilePic());
        Uri profUri = timeline.getProfilePic();
        Picasso.get().load(profUri).into(timelineProfilePhoto);


        TextView timelineUsername = listViewItem.findViewById(R.id.post_username);
        timelineUsername.setText(timeline.getName());

        TextView timelineTime = listViewItem.findViewById(R.id.time);
        String mdate = timeline.getTimeStamp().substring(0,10);
        String mtime = timeline.getTimeStamp().substring(11,19);
        timelineTime.setText(mtime);

        TextView dateTime = listViewItem.findViewById(R.id.date);
        dateTime.setText(mdate);

        TextView timelinePost = listViewItem.findViewById(R.id.timeline_post);
        timelinePost.setText(timeline.getStatus());
        //timelinePost.setVisibility(View.VISIBLE);
        Log.v("TAG TEXT",timelinePost.getText().toString());
        Log.v("TAG LLLL",timeline.getStatus());

        ImageView timelinePostImage = listViewItem.findViewById(R.id.timeline_post_photo);

        if (timeline.hashPostImage()) {
            //timelinePostImage.setImageResource(timeline.getImge());
            Uri uri = timeline.getImge();
            Picasso.get().load(uri).into(timelinePostImage);

            timelinePostImage.setVisibility(View.VISIBLE);
        } else {
            timelinePostImage.setVisibility(View.GONE);
        }

        return listViewItem;
    }
}