package com.example.project.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class File {
    // Action实体类
    public static class Action {
        private Integer user_id;
        private Integer sku_id;
        private Date date;
        private Integer num;

        public Action(Integer userId, Integer skuId, Date date, Integer num) {
            this.user_id = user_id;
            this.sku_id = sku_id;
            this.date = date;
            this.num = num;
        }

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
        private Integer user_id;
        private Integer sku_id;
        private Integer o_id;
        private Date date;
        private Integer area;
        private Integer num;

        public Order(Integer userId, Integer skuId, Integer orderId, Date date, Integer area, Integer num) {
            this.user_id = userId;
            this.sku_id = skuId;
            this.o_id = orderId;
            this.date = date;
            this.area = area;
            this.num = num;
        }

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
        private Integer sku_id;
        private Integer price;
        private Integer cate;

        public Sku(Integer skuId, Integer price, Integer category) {
            this.sku_id = skuId;
            this.price = price;
            this.cate = cate;
        }

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
        private Integer user_id;
        private Integer o_id;
        private Integer score;

        public Comment(Integer userId, Integer orderId, Integer score) {
            this.user_id = userId;
            this.o_id = orderId;
            this.score = score;
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
    private static List<Action> actionList = new ArrayList<>();
    private static List<Order> orderList = new ArrayList<>();
    private static List<Sku> skuList = new ArrayList<>();
    private static List<Comment> commentList = new ArrayList<>();

    public static List<Action> getActionList() {
        return actionList;
    }

    public static void setActionList(List<Action> actionList) {
        File.actionList = actionList;
    }

    public static List<Order> getOrderList() {
        return orderList;
    }

    public static void setOrderList(List<Order> orderList) {
        File.orderList = orderList;
    }

    public static List<Sku> getSkuList() {
        return skuList;
    }

    public static void setSkuList(List<Sku> skuList) {
        File.skuList = skuList;
    }

    public static List<Comment> getCommentList() {
        return commentList;
    }

    public static void setCommentList(List<Comment> commentList) {
        File.commentList = commentList;
    }
}
