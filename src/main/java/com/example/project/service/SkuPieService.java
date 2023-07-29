package com.example.project.service;

import com.example.project.mapper.SkuPieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkuPieService {
    @Autowired
    private SkuPieMapper skuPieMapper;

    public SkuPieService(SkuPieMapper skuPieMapper) {
        this.skuPieMapper = skuPieMapper;
    }

    public List<Map<String, Object>> getCategoryPercentages(String fileKey) {
        List<Map<String, Object>> result = new ArrayList<>();

        int totalCount = getTotalRecordCount(fileKey);
        int category1Count = skuPieMapper.countCategory1(fileKey);
        int category2Count = skuPieMapper.countCategory2(fileKey);
        int category3Count = skuPieMapper.countCategory3(fileKey);
        int category4Count = skuPieMapper.countCategory4(fileKey);
        int category5Count = skuPieMapper.countCategory5(fileKey);
        int category6Count = skuPieMapper.countCategory6(fileKey);

        int category1Percentage = calculatePercentage(category1Count, totalCount);
        int category2Percentage = calculatePercentage(category2Count, totalCount);
        int category3Percentage = calculatePercentage(category3Count, totalCount);
        int category4Percentage = calculatePercentage(category4Count, totalCount);
        int category5Percentage = calculatePercentage(category5Count, totalCount);
        int category6Percentage = calculatePercentage(category6Count, totalCount);

        result.add(createCategoryMap("数码家电", category1Percentage));
        result.add(createCategoryMap("生活用品", category2Percentage));
        result.add(createCategoryMap("虚拟物品", category3Percentage));
        result.add(createCategoryMap("服装鞋帽", category4Percentage));
        result.add(createCategoryMap("家居用品", category5Percentage));
        result.add(createCategoryMap("办公用品", category6Percentage));

        return result;
    }

    private int getTotalRecordCount(String fileKey) {
       return skuPieMapper.getTotalRecordCount(fileKey);
    }

    private int calculatePercentage(int count, int totalCount) {
        return (int) ((count / (double) totalCount) * 100);
    }

    private Map<String, Object> createCategoryMap(String categoryName, int percentage) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("name", categoryName);
        resultMap.put("value", percentage);
        return resultMap;
    }
}
