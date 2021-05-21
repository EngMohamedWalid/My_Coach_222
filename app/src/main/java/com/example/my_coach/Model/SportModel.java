package com.example.my_coach.Model;

public class SportModel {
    private String id ;
    private String category_id ;
    private String name ;
    private String image ;

    public SportModel() {
    }

    public SportModel(String id, String category_id, String name, String image) {
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
