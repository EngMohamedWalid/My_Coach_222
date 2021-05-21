package com.example.my_coach.Model;

public class AbouteSportModel {
    private String id;
    private String sport_brief;
    private String sport_id;
    private String sport_image;

    public AbouteSportModel() {
    }

    public AbouteSportModel(String id, String sport_brief, String sport_id, String sport_image) {
        this.id = id;
        this.sport_brief = sport_brief;
        this.sport_id = sport_id;
        this.sport_image = sport_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSport_brief() {
        return sport_brief;
    }

    public void setSport_brief(String sport_brief) {
        this.sport_brief = sport_brief;
    }

    public String getSport_id() {
        return sport_id;
    }

    public void setSport_id(String sport_id) {
        this.sport_id = sport_id;
    }

    public String getSport_image() {
        return sport_image;
    }

    public void setSport_image(String sport_image) {
        this.sport_image = sport_image;
    }
}
