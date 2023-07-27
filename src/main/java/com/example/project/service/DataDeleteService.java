package com.example.project.service;

import com.example.project.mapper.DeleteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataDeleteService {
    @Autowired
    private DeleteMapper deleteMapper;
    public void delete(String fileKey){
        deleteMapper.deleteFromOverview(fileKey);
        deleteMapper.dropActionTable("action_"+fileKey);
        deleteMapper.dropCommentTable("comment_"+fileKey);
        deleteMapper.dropSkuTable("sku_"+fileKey);
        deleteMapper.dropOrderTable("order_"+fileKey);
    }
}
