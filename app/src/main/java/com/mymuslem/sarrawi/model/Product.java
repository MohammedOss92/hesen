package com.mymuslem.sarrawi.model;

public class Product {
    private int id;
    private String name;
    private int fav;
    private String name_filter;

    public Product(int id, String name, int fav, String name_filter) {
        this.id = id;
        this.name = name;
        this.fav = fav;
        this.name_filter = name_filter;
    }
    public Product(String name_filter,String name) {
        this.name_filter = name_filter;
        this.name = name;
    }

    public Product() {
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




    public String getName_filter() {
        return name_filter;
    }

    public void setName_filter(String name_filter) {
        this.name_filter = name_filter;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }
}
