package com.example.project.controller;

import com.example.project.service.ActionPieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ActionPieController {
    @Autowired
    private ActionPieService actionPieService;

    public ActionPieController(ActionPieService actionPieService) {
        this.actionPieService = actionPieService;
    }

    @GetMapping("/actionPie")
    public List<Map<String, Object>> actionPie(@RequestParam("key") String fileKey) {
        return actionPieService.getCategoryPercentages(fileKey);
    }
}
