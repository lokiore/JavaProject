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
        ArrayList<Timeline> posts = new ArrayList<Timeline>();
        posts.add(new Timeline(1,"lokesh",R.drawable.photo,"Status",R.drawable.common_google_signin_btn_icon_dark,"12:2320","AAj Gand maregi"));
        posts.add(new Timeline(2,"lokesh","Status",R.drawable.common_google_signin_btn_icon_dark,"ADF","AAj Gand maregi"));
        posts.add(new Timeline(3,"lokesh","Status",R.drawable.common_google_signin_btn_icon_dark,"AFF","AAj Gand maregi"));

        TimelineAdapter postsAdapter = new TimelineAdapter(getActivity(),posts);

        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(postsAdapter);
        return rootView;
    }


}
