package com.example.project.controller;

import com.example.project.entity.UserViewData;
import com.example.project.service.ActionScatterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ActionScatterController {
    @Autowired
    private ActionScatterService actionScatterService;

    @GetMapping("/actionScatter")
    public List<Map<String, Object>> actionScatter(@RequestParam("key") String fileKey) {
        return actionScatterService.getUserViewsAndCategory(fileKey);
    }
}

