package com.example.rishabh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OtherUsersFragment extends Fragment{

    public OtherUsersFragment(){
        //Requires this to be empty
    }

    public static OtherUsersFragment newInstance(String param1, String param2) {
        OtherUsersFragment fragment = new OtherUsersFragment();

        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.time_list, container, false);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser muser = FirebaseAuth.getInstance().getCurrentUser();
        final String memail = muser.getEmail().replaceAll("\\.","_dot_");
        DatabaseReference reference = databaseReference.child("Photos");
        //final HashMap<String,Upload> photoMap = new HashMap<>();
        final ArrayList<Upload> totalUsers = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot photoSnapshot: dataSnapshot.getChildren()){

                    String email= photoSnapshot.getKey().toString();
                    if(!email.equals(memail)) {
                        for (DataSnapshot profileSnapshot : photoSnapshot.getChildren()) {
                            if (profileSnapshot.getKey().toString().equals("profile")) {
                                Upload upload = profileSnapshot.getValue(Upload.class);
                                upload.setEmail(email);

                                //photoMap.put(email, upload);
                                totalUsers.add(upload);

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        UploadAdapter postsAdapter = new UploadAdapter(getActivity(), totalUsers);

        ListView listView = view.findViewById(R.id.list);
        listView.setAdapter(postsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                Upload item = (Upload) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), OtherUserMainProfile.class);
                LinearLayout linearLayout =  v.findViewById(R.id.userID);

                // Interesting data to pass across are the thumbnail size/location, the
                // resourceId of the source bitmap, the picture description, and the
                // orientation (to avoid returning back to an obsolete configuration if
                // the device rotates again in the meantime)

                int[] screenLocation = new int[2];
                linearLayout.getLocationOnScreen(screenLocation);

                //Pass the image title and url to DetailsActivity
                intent.putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("Email", item.getEmail());

                //Start details activity
                startActivity(intent);
            }
        });

        return view;
    }
}
