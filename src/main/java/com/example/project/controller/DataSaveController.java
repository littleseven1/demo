package com.example.project.controller;
import com.example.project.service.DataSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
public class DataSaveController {

    @Autowired
    private DataSaveService dataSaveService;

    @PostMapping("/saveData")
    public void uploadFile(@RequestParam String fileKey, @RequestParam String fileDescription) {
        dataSaveService.SaveData(fileKey, fileDescription);
    }
}
