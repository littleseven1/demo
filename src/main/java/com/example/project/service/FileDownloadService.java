//package com.example.project.service;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.*;
//import java.nio.file.Files;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;
//
//@Service
//public class FileDownloadService {
//@Autowired(required = false)
//    public MultipartFile getfile(String fileKey) throws IOException {
////        String sourcePath =filepath.path+fileKey; // 替换成你的压缩文件路径
////        String targetFileName = "dataSet";
////        File targetFile = new File(targetFileName);
////        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(sourcePath))) {
////            ZipEntry zipEntry = zipInputStream.getNextEntry();
////            while (zipEntry != null) {
////                if (!zipEntry.isDirectory() && zipEntry.getName().equals(targetFileName)) {
////                    // 将zipEntry的内容写入目标文件
////                    try (FileOutputStream fileOutputStream = new FileOutputStream(targetFile)) {
////                        byte[] buffer = new byte[1024];
////                        int length;
////                        while ((length = zipInputStream.read(buffer)) > 0) {
////                            fileOutputStream.write(buffer, 0, length);
////                        }
////                    }
////                    break;
////                }
////                zipEntry = zipInputStream.getNextEntry();
////            }
////        }
////
////        return createMultipartFile(targetFile);
//    }
//
////    private MultipartFile createMultipartFile(File file) throws IOException {
////        FileInputStream fileInputStream = new FileInputStream(file);
////        return new CommonsMultipartFile(
////                new org.springframework.web.multipart.commons.CommonsMultipartFile(fileInputStream));
////    }
//}
