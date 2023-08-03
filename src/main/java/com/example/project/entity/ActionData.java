package com.example.project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class ActionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer iindex;
    private Integer user_id;
    private String userName;
    private Integer sku_id;
    private Date date;
    private Integer num;

    public Integer getIindex() {
        return iindex;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getSku_id() {
        return sku_id;
    }

    public Date getDate() {
        return date;
    }

    public Integer getNum() {
        return num;
    }

    // Setter methods
    public void setIindex(Integer iindex) {
        this.iindex = iindex;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setSku_id(Integer sku_id) {
        this.sku_id = sku_id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
