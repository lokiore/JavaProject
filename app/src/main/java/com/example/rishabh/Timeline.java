package com.example.rishabh;

public class Timeline {
    private int id;
    private String name, status, timeStamp, url;
    private final int NO_IMAGE = -1;
    private int image=NO_IMAGE, profilePic;
    public Timeline() {
    }

    public Timeline(int id, String name, int image, String status,
                    int profilePic, String timeStamp, String url) {
        super();
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.profilePic = profilePic;
        this.timeStamp = timeStamp;
        this.url = url;
    }

    public Timeline(int id, String name, String status,
                    int profilePic, String timeStamp, String url) {
        super();
        this.id = id;
        this.name = name;
        //this.image = image;
        this.status = status;
        this.profilePic = profilePic;
        this.timeStamp = timeStamp;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImge() {
        return image;
    }

    public void setImge(int image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public boolean hashPostImage(){
        return image!=NO_IMAGE;
    }
}
