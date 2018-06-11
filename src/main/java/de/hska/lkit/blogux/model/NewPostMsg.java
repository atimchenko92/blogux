package de.hska.lkit.blogux.model;

public class NewPostMsg {

    private String name;
    private String msg;

    public NewPostMsg() {
    }

    public NewPostMsg(String name, String msg) {
        this.name = name;
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
