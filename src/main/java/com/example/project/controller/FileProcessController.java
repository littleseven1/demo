package com.example.project.controller;

import com.example.project.service.FileProcessService;
import com.example.project.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileProcessController {

    private FileProcessService fileProcessService;
    private FileUploadService fileUploadService;
    @Autowired
    public FileProcessController(FileProcessService fileProcessService) {
        this.fileProcessService = fileProcessService;
    }

    @GetMapping("/getDetail")
    public String getDetail(@RequestParam("fileKey") String fileKey) {
        return fileProcessService.processAndCheckFile(fileKey);
    }
}
