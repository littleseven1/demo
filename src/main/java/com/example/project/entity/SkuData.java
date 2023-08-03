package com.example.project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class SkuData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer iindex;
    private Integer sku_id;
    private Float price;
    private Integer cate;
    private String cateName;


    public Integer getIindex() {
        return iindex;
    }

    public void setIindex(Integer iindex) {
        this.iindex = iindex;
    }

    public Integer getSku_id() {
        return sku_id;
    }

    public void setSku_id(Integer sku_id) {
        this.sku_id = sku_id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getCate() {
        return cate;
    }

    public void setCate(Integer cate) {
        this.cate = cate;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }
}
