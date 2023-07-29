package com.example.project.service;

import com.example.project.mapper.ActionPieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ActionPieService {
    @Autowired
    private ActionPieMapper actionPieMapper;

    public ActionPieService(ActionPieMapper actionPieMapper) {
        this.actionPieMapper = actionPieMapper;
    }

    public List<Map<String, Object>> getCategoryPercentages(String fileKey) {
        List<Map<String, Object>> result = new ArrayList<>();

        int totalCount = getTotalRecordCount(fileKey);
        int category1Count = actionPieMapper.countCategory1(fileKey);
        int category2Count = actionPieMapper.countCategory2(fileKey);
        int category3Count = actionPieMapper.countCategory3(fileKey);
        int category4Count = actionPieMapper.countCategory4(fileKey);
        int category5Count = actionPieMapper.countCategory5(fileKey);
        int category6Count = actionPieMapper.countCategory6(fileKey);
        int category7Count = actionPieMapper.countCategory7(fileKey);

        int category1Percentage = calculatePercentage(category1Count, totalCount);
        int category2Percentage = calculatePercentage(category2Count, totalCount);
        int category3Percentage = calculatePercentage(category3Count, totalCount);
        int category4Percentage = calculatePercentage(category4Count, totalCount);
        int category5Percentage = calculatePercentage(category5Count, totalCount);
        int category6Percentage = calculatePercentage(category6Count, totalCount);
        int category7Percentage = calculatePercentage(category7Count, totalCount);

        result.add(createCategoryMap(1, category1Percentage));
        result.add(createCategoryMap(2, category2Percentage));
        result.add(createCategoryMap(3, category3Percentage));
        result.add(createCategoryMap(4, category4Percentage));
        result.add(createCategoryMap(5, category5Percentage));
        result.add(createCategoryMap(6, category6Percentage));
        result.add(createCategoryMap(7, category7Percentage));

        return result;
    }

    private int getTotalRecordCount(String fileKey) {
        return actionPieMapper.getTotalRecordCount(fileKey);
    }

    private int calculatePercentage(int count, int totalCount) {
        return (int) ((count / (double) totalCount) * 100);
    }

    private Map<String, Object> createCategoryMap(int category, int percentage) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("name", category);
        resultMap.put("value", percentage);
        return resultMap;
    }
}
