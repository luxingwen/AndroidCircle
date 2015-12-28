package com.luxin.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by luxin on 15-12-23.
 * http://luxin.gitcafe.io
 */
public class NotifyMsg extends BmobObject {

    private Helps helps;
    private String message;
    private Comment comment;
    private MyUser user;
    private boolean status;
    private MyUser author;

    public MyUser getAuthor() {
        return author;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Helps getHelps() {
        return helps;
    }

    public void setHelps(Helps helps) {
        this.helps = helps;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}
