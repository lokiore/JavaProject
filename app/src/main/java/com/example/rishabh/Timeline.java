package com.example.rishabh;

public class Timeline {

    private final int NO_IMAGE = -1;
    private String Username;
    private String Time;
    private int ProfileImage;
    private String Post;
    private int PostImage=NO_IMAGE;

    public Timeline(String username,String time,int profileImage,String post){
        Username=username;
        Time=time;
        ProfileImage=profileImage;
        Post=post;
    }
    public Timeline(String username,String time,int profileImage,int postImage,String post){
        Username=username;
        Time=time;
        ProfileImage=profileImage;
        Post=post;
        PostImage=postImage;
    }

    public String getUsername() {
        return Username;
    }

    public String getTime() {
        return Time;
    }

    public int getProfileImage() {
        return ProfileImage;
    }

    public int getPostImage() {
        return PostImage;
    }

    public String getPost() {
        return Post;
    }
    public boolean hashPostImage(){
        return PostImage!=NO_IMAGE;
    }
}