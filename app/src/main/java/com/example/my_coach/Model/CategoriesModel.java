package com.example.my_coach.Model;

public class CategoriesModel {
    private String ID;
    private String Image;
    private String Name;

    public CategoriesModel(String ID, String image, String name) {
        this.ID = ID;
        Image = image;
        Name = name;
    }

    public CategoriesModel() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
