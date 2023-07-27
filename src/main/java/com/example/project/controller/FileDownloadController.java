//package com.example.project.controller;
//
//import com.example.project.service.FileDownloadService;
//import com.example.project.service.FileUploadService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//public class FileDownloadController {
//    @Autowired
//    private FileDownloadService fileDownloadService;
//
//    @GetMapping("/downloadData")
//    public MultipartFile handleFileUpload(@RequestParam("key") String fileKey) {
//        return fileDownloadService.getfile(fileKey);
//    }
//}
