package com.example.project.controller;

import com.example.project.service.DataDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataDeleteController {
    @Autowired
    private DataDeleteService dataDeleteService;

    @DeleteMapping("/deleteData")
    public void DeleteData(@RequestParam("key") String fileKey) {
       dataDeleteService.delete(fileKey);
    }
}
