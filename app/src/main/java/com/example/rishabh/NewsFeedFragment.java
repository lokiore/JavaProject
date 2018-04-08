package com.example.rishabh;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class NewsFeedFragment extends Fragment {



    public NewsFeedFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NewsFeedFragment newInstance(String param1, String param2) {
        NewsFeedFragment fragment = new NewsFeedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.time_list, container, false);
        final ArrayList<Timeline> posts = new ArrayList<Timeline>();
        posts.add(new Timeline("lokesh","12:30 PM",R.drawable.common_google_signin_btn_icon_dark,"AAj Gand maregi"));
        posts.add(new Timeline("lokesh","12:30 PM",R.drawable.common_google_signin_btn_icon_dark,"AAj Gand nahi maregi"));

        TimelineAdapter postsAdapter = new TimelineAdapter(getActivity(),posts);

        ListView listView = rootView.findViewById(R.id.time_list);
        listView.setAdapter(postsAdapter);
        return rootView;
    }


}
