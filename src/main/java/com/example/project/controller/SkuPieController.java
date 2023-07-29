package com.example.project.controller;

import com.example.project.service.SkuPieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SkuPieController {
    @Autowired
    private SkuPieService skuPieService;

    public SkuPieController(SkuPieService skuPieService) {
        this.skuPieService = skuPieService;
    }

    @GetMapping("/skuPie")
    public List<Map<String, Object>> skuPie(@RequestParam("key") String fileKey) {
        return skuPieService.getCategoryPercentages(fileKey);
    }
}
