package com.example.project.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class File {
    // Action实体类
    public static class Action {
        private String fileKey;
        private Integer user_id;
        private Integer sku_id;
        private Date date;
        private Integer num;

        public Action(String fileKey, Integer userId, Integer skuId, Date date, Integer num) {
            this.fileKey = fileKey;
            this.user_id = userId;
            this.sku_id = skuId;
            this.date = date;
            this.num = num;
        }

        public String getFileKey() {return fileKey;}

        public void setFileKey(String fileKey) {this.fileKey = fileKey;}

        public Integer getUser_id() {
            return user_id;
        }

        public void setUser_id(Integer user_id) {
            this.user_id = user_id;
        }

        public Integer getSku_id() {
            return sku_id;
        }

        public void setSku_id(Integer sku_id) {
            this.sku_id = sku_id;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }
    }

    // Order实体类
    public static class Order {
        private String fileKey;
        private Integer user_id;
        private Integer sku_id;
        private Integer o_id;
        private Date date;
        private Integer area;
        private Integer num;

        public Order(String fileKey, Integer userId, Integer skuId, Integer orderId, Date date, Integer area, Integer num) {
            this.fileKey = fileKey;
            this.user_id = userId;
            this.sku_id = skuId;
            this.o_id = orderId;
            this.date = date;
            this.area = area;
            this.num = num;
        }

        public String getFileKey() {return fileKey;}

        public void setFileKey(String fileKey) {this.fileKey = fileKey;}

        public Integer getUser_id() {
            return user_id;
        }

        public void setUser_id(Integer user_id) {
            this.user_id = user_id;
        }

        public Integer getSku_id() {
            return sku_id;
        }

        public void setSku_id(Integer sku_id) {
            this.sku_id = sku_id;
        }

        public Integer getO_id() {
            return o_id;
        }

        public void setO_id(Integer o_id) {
            this.o_id = o_id;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Integer getArea() {
            return area;
        }

        public void setArea(Integer area) {
            this.area = area;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }
    }

    // Sku实体类
    public static class Sku {
        private String fileKey;
        private Integer sku_id;
        private Integer price;
        private Integer cate;

        public Sku(String fileKey, Integer skuId, Integer price, Integer category) {
            this.fileKey = fileKey;
            this.sku_id = skuId;
            this.price = price;
            this.cate = category;
        }


        public String getFileKey() {return fileKey;}

        public void setFileKey(String fileKey) {this.fileKey = fileKey;}

        public Integer getSku_id() {
            return sku_id;
        }

        public void setSku_id(Integer sku_id) {
            this.sku_id = sku_id;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Integer getCate() {
            return cate;
        }

        public void setCate(Integer cate) {
            this.cate = cate;
        }
    }


    // Comment实体类
    public static class Comment {
        private String fileKey;
        private Integer user_id;
        private Integer o_id;
        private Integer score;

        public Comment(String fileKey, Integer userId, Integer orderId, Integer score) {
            this.fileKey = fileKey;
            this.user_id = userId;
            this.o_id = orderId;
            this.score = score;
        }

        public String getFileKey() {return fileKey;}

        public void setFileKey(String fileKey) {this.fileKey = fileKey;}

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

}
