package com.example.project.service;

import com.example.project.entity.File;
import com.example.project.mapper.ActionMapper;
import com.example.project.mapper.CommentMapper;
import com.example.project.mapper.OrderMapper;
import com.example.project.mapper.SkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataSaveService {

    private ActionMapper actionMapper;
    private OrderMapper orderMapper;
    private SkuMapper skuMapper;
    private CommentMapper commentMapper;

    @Autowired
    public DataSaveService(ActionMapper actionMapper,
                           OrderMapper orderMapper,
                           SkuMapper skuMapper,
                           CommentMapper commentMapper) {
        this.actionMapper = actionMapper;
        this.orderMapper = orderMapper;
        this.skuMapper = skuMapper;
        this.commentMapper = commentMapper;
    }

    public void saveDataToDatabase() {
        // 从之前的处理结果获取数据
        List<File.Action> actions = File.getActionList();
        List<File.Order> orders = File.getOrderList();
        List<File.Sku> skus = File.getSkuList();
        List<File.Comment> comments = File.getCommentList();

        // 将数据插入数据库
        for (File.Action action : actions) {

            actionMapper.insertAction(action);
        }

        for (File.Order order : orders) {
            orderMapper.insertOrder(order);
        }

        for (File.Sku sku : skus) {
            skuMapper.insertSku(sku);
        }

        for (File.Comment comment : comments) {
            commentMapper.insertComment(comment);
        }
    }
}
