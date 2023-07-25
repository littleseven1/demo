package com.example.project.service;

import com.example.project.entity.File;
import com.example.project.entity.Overview;
import com.example.project.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class DataSaveService {

    private final ActionMapper actionMapper;
    private final OrderMapper orderMapper;
    private final SkuMapper skuMapper;
    private final CommentMapper commentMapper;
    private final OverviewMapper overviewMapper;

    @Autowired
    public DataSaveService(ActionMapper actionMapper, OrderMapper orderMapper, SkuMapper skuMapper, CommentMapper commentMapper, OverviewMapper overviewMapper) {
        this.actionMapper = actionMapper;
        this.orderMapper = orderMapper;
        this.skuMapper = skuMapper;
        this.commentMapper = commentMapper;
        this.overviewMapper = overviewMapper;
    }

    public void SaveData(String fileKey, String fileDescription) {

        Overview overview = new Overview(fileKey, fileDescription);
        overviewMapper.addOverview(overview);

        String zipFilePath = "E:/la/" + fileKey;
        String unzipFolderPath = "E:/la/unzipped/" + fileKey + "/";
        unzipFiles(zipFilePath, unzipFolderPath);

        // Process each excel file
        processActionExcel(unzipFolderPath + fileKey + "_action.xlsx");
        processOrderExcel(unzipFolderPath + fileKey + "_order.xlsx");
        processSkuExcel(unzipFolderPath + fileKey + "_sku.xlsx");
        processCommentExcel(unzipFolderPath + fileKey + "_comment.xlsx");
    }

    private void unzipFiles(String zipFilePath, String unzipFolderPath) {
        try {
            byte[] buffer = new byte[1024];
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = new File(unzipFolderPath + fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, len);
                }
                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processActionExcel(String filePath) {
        // TODO: Implement the logic to read the excel file and save data to the action table
        // You can use Apache POI or other libraries to read Excel files
    }

    private void processOrderExcel(String filePath) {
        // TODO: Implement the logic to read the excel file and save data to the order table
        // You can use Apache POI or other libraries to read Excel files
    }

    private void processSkuExcel(String filePath) {
        // TODO: Implement the logic to read the excel file and save data to the sku table
        // You can use Apache POI or other libraries to read Excel files
    }

    private void processCommentExcel(String filePath) {
        // TODO: Implement the logic to read the excel file and save data to the comment table
        // You can use Apache POI or other libraries to read Excel files
    }
}
