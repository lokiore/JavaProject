package com.example.rishabh;

import android.util.Log;

public class User {
    String username;
    String name;
    String email;
    String password;
    String mobile;

    public User(){}
    public User( String n , String e, String p, String m)
    {
        username = "";
        name = n;
        email = e;
        password = p;
        mobile = m;
        Log.v("LoginActivity", "Registered object created");
    }
}
