package de.hska.lkit.blogux.model;

public class NewPostMsg {

    private String name;

    public NewPostMsg() {
    }

    public NewPostMsg(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
