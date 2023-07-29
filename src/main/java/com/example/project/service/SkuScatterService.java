package com.example.project.service;

import com.example.project.mapper.SkuScatterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class SkuScatterService {
    @Autowired
    private SkuScatterMapper skuScatterMapper;

    public SkuScatterService(SkuScatterMapper skuScatterMapper) {
        this.skuScatterMapper = skuScatterMapper;
    }

    public List<Map<String, Object>> getSkuDetails(String fileKey) {
        String tableName = "sku_" + fileKey;
        return skuScatterMapper.findSkuDetails(tableName);
    }
}
