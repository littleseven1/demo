package com.example.project.service;
import com.example.project.mapper.ActionScatterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActionScatterService {
    @Autowired
    private ActionScatterMapper actionScatterMapper;
    public ActionScatterService(ActionScatterMapper actionScatterMapper) {
        this.actionScatterMapper = actionScatterMapper;
    }
    public List<Map<String, Object>> getUserViewsAndCategory(String fileKey) {
        List<Map<String, Object>> userViews = actionScatterMapper.countUserViews(fileKey);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> userView : userViews) {
            int userId = (int) userView.get("user_id");
            int viewCount = ((Long) userView.get("number")).intValue();

            int category = categorizeUser(viewCount);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("user_id", userId);
            resultMap.put("number", viewCount);
            resultMap.put("category", category);
            result.add(resultMap);
        }

        return result;
    }
    private int categorizeUser(int viewCount) {
        if (viewCount > 1000) {

            return 1;
        } else if (viewCount > 700) {
            return 2;
        } else if (viewCount > 500) {
            return 3;
        } else if (viewCount > 300) {
            return 4;
        } else if (viewCount > 200) {
            return 5;
        } else if (viewCount > 100) {
            return 6;
        } else {
            return 7;
        }
    }
}
