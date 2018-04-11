package com.example.rishabh;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String email;
    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        email=null;
        mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String memail){
        email=memail;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
