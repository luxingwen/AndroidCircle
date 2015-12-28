package com.luxin.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by luxin on 15-12-8.
 *  http://luxin.gitcafe.io
 */
public class MyUser extends BmobUser {
    private Integer score;//用户积分
    private BmobFile auvter;
    private String phone;
    private Integer sex;
    private String personality;

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public BmobFile getAuvter() {
        return auvter;
    }

    public void setAuvter(BmobFile auvter) {
        this.auvter = auvter;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
