package com.example.project.controller;

import com.example.project.service.FileProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class FileProcessController {

    private FileProcessService fileProcessService;
    @Autowired
    public FileProcessController(FileProcessService fileProcessService) {
        this.fileProcessService = fileProcessService;

    }
    @GetMapping("/getDetail")
    public List<Map<String, Object>> getDetail(@RequestParam("key") String fileKey) {
        return fileProcessService.processAndCheckFile(fileKey);
    }
}
