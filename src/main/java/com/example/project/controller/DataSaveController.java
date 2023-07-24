package com.example.project.controller;/*package com.example.demo.controller;

import com.example.demo.service.DataSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataSaveController {

    private DataSaveService dataSaveService;

    @Autowired
    public DataSaveController(DataSaveService dataSaveService) {
        this.dataSaveService = dataSaveService;
    }

    @PostMapping("/saveData")
    public String saveDataToDatabase(@RequestBody FileDescriptionRequest request) {
        try {
           String fileKey = request.getFileKey();
            String fileDescription = request.getFileDescription();

            dataSaveService.saveDataToDatabase(fileKey, fileDescription);
            return "Data saved successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while saving data.";
        }
    }

    // 创建一个请求类用于接收前端上传的文件描述
    public static class FileDescriptionRequest {
        private String fileKey;
        private String fileDescription;

        // Constructors, getters, and setters (可以使用IDE自动生成)

        // Constructors, getters, and setters (可以使用IDE自动生成)
    }
}
*/