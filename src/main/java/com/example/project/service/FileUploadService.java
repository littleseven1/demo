package com.example.project.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Service
public class FileUploadService {
    @Autowired(required = false)
    public String uploadFile(MultipartFile file) {
        String fileKey = generateFileKey();
        String storageLocation = "E:/la/"; // 修改为实际存储文件的路径

        try {
            // 将文件保存在服务器上
            File targetFile = new File(storageLocation + fileKey);
            file.transferTo(targetFile);

        } catch (IOException e) {
            // 处理文件上传失败的异常
            e.printStackTrace();
            return null;
        }

        return fileKey;
    }

    private String generateFileKey() {
        // 在这里生成一个唯一的文件key，可以使用UUID或其他方式生成
        return UUID.randomUUID().toString();
    }

}
