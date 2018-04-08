package com.example.rishabh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by lokiore on 8/4/18.
 */

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_home);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager =  findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab tabAdd = tabLayout.getTabAt(0);
        tabAdd.setCustomView(R.layout.tab_layout_custom_view);
        tabAdd.setText("Add");
        tabAdd.setIcon(R.drawable.ic_account_circle_black_24dp).setText("Add");
        TabLayout.Tab tabAdd1 = tabLayout.getTabAt(1);
        tabAdd1.setCustomView(R.layout.tab_layout_custom_view);
        tabAdd1.setText("Add");
        tabAdd1.setIcon(R.drawable.ic_keyboard_arrow_right_black_24dp).setText("Add");
        TabLayout.Tab tabAdd2 = tabLayout.getTabAt(0);
        tabAdd2.setCustomView(R.layout.tab_layout_custom_view);
        tabAdd2.setText("Add");
        tabAdd2.setIcon(R.drawable.ic_account_circle_black_24dp).setText("Add");
    }
}
