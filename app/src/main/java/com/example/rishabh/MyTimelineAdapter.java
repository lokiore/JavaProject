package com.example.rishabh;

/**
 * Created by lokiore on 8/4/18.
 */


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.PopupMenu;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//import info.androidhive.listviewfeed.FeedImageView;

public class MyTimelineAdapter extends ArrayAdapter<Timeline> {
    static String post_uri;
    static String post_desc;
    public MyTimelineAdapter(Activity context, ArrayList<Timeline> timelines) {
        super(context, 0, timelines);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listViewItem = convertView;
        if (listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.my_feed_item, parent, false);
        }

        final Timeline timeline = getItem(position);
        ImageView imageView = listViewItem.findViewById(R.id.post_menu);
        try {
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    switch (v.getId()) {
                        case R.id.post_menu:

                            PopupMenu popup = new PopupMenu(getContext(), v);
                            popup.getMenuInflater().inflate(R.menu.menu_main,
                                    popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

                                    switch (item.getItemId()) {
                                        case R.id.delete:

                                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                            String email = mUser.getEmail();
                                            email=email.replaceAll("\\.","_dot_");
                                            String timeStamp = timeline.getTimeStamp().replaceAll("\\.",":");
                                            mDatabase.child("Posts").child(email).child(timeStamp).removeValue();
                                            Intent intent = new Intent(getContext(),MyPost.class);
                                            getContext().startActivity(intent);
                                            ((Activity)getContext()).finish();
                                            break;

                                        default:
                                            break;
                                    }

                                    return true;
                                }
                            });

                            break;

                        default:
                            break;
                    }


                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

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