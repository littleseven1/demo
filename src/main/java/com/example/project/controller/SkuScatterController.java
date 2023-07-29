package com.example.project.controller;

import com.example.project.service.SkuScatterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class SkuScatterController {
    private SkuScatterService skuScatterService;

    public SkuScatterController(SkuScatterService skuScatterService) {
        this.skuScatterService = skuScatterService;
    }

    @GetMapping("/skuScatter")
    public List<Map<String, Object>> skuScatter(@RequestParam("key") String fileKey) {
        return skuScatterService.getSkuDetails(fileKey);
    }
}
