package com.example.rishabh;

/**
 * Created by lokiore on 8/4/18.
 */


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TimelineAdapter extends ArrayAdapter<Timeline> {

    public TimelineAdapter(Activity context, ArrayList<Timeline> timelines) {
        super(context, 0, timelines);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listViewItem = convertView;
        if (listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.time_list_item, parent, false);
        }

        Timeline timeline = getItem(position);

        ImageView timelineProfilePhoto = listViewItem.findViewById(R.id.timeline_profile_photo);
        timelineProfilePhoto.setImageResource(timeline.getProfileImage());

        TextView timelineUsername = listViewItem.findViewById(R.id.post_username);
        timelineUsername.setText(timeline.getUsername());

        TextView timelineTime = listViewItem.findViewById(R.id.time);
        timelineTime.setText(timeline.getTime());

        TextView timelinePost = listViewItem.findViewById(R.id.timeline_post);
        timelinePost.setText(timeline.getPost());

        ImageView timelinePostImage = listViewItem.findViewById(R.id.timeline_post_photo);

        if (timeline.hashPostImage()) {
            timelinePostImage.setImageResource(timeline.getPostImage());
            timelinePostImage.setVisibility(View.VISIBLE);
        } else {
            timelinePostImage.setVisibility(View.GONE);
        }

        return listViewItem;
    }
}