package com.example.rishabh;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by lokiore on 11/4/18.
 */

public class MyPost extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);

/*        ArrayList<Timeline> posts = new ArrayList<Timeline>();
        posts.add(new Timeline(1,"lokesh",R.drawable.photo,"Status",R.drawable.common_google_signin_btn_icon_dark,"12:2320","AAj Gand maregi"));
        posts.add(new Timeline(2,"lokesh","Status",R.drawable.common_google_signin_btn_icon_dark,"ADF","AAj Gand maregi"));
        posts.add(new Timeline(3,"lokesh","Status",R.drawable.common_google_signin_btn_icon_dark,"AFF","AAj Gand maregi"));

        TimelineAdapter postsAdapter = new TimelineAdapter(this,posts);

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(postsAdapter);*/


        findViewById(R.id.new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPost.this,NewPostActivity.class);
                startActivity(intent);
            }
        });
    }
}
