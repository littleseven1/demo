package com.example.project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CommentData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer iindex;
    private Integer user_id;
    private Integer o_id;
    private Integer score;


    public Integer getIindex() {
        return iindex;
    }

    public void setIindex(Integer iindex) {
        this.iindex = iindex;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getO_id() {
        return o_id;
    }

    public void setO_id(Integer o_id) {
        this.o_id = o_id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
