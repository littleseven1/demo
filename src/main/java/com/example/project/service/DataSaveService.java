package com.example.project.service;

import com.example.project.entity.Overview;
import com.example.project.mapper.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class DataSaveService {
    @Autowired
    private final OverviewMapper overviewMapper;
    private final ActionMapper actionMapper;
    private final OrderMapper orderMapper;
    private final SkuMapper skuMapper;
    private final CommentMapper commentMapper;
    ActionMapper.CreateTable createTable_action = new ActionMapper.CreateTable();
    OverviewMapper.CreateTable createTable_overview=new OverviewMapper.CreateTable();
    OrderMapper.CreateTable createTable_order=new OrderMapper.CreateTable();
    SkuMapper.CreateTable createTable_sku=new SkuMapper.CreateTable();
    CommentMapper.CreateTable createTable_comment=new CommentMapper.CreateTable();
    public DataSaveService(OverviewMapper overviewMapper, ActionMapper actionMapper,
                           OrderMapper orderMapper, SkuMapper skuMapper, CommentMapper commentMapper) {
        this.overviewMapper = overviewMapper;
        this.actionMapper = actionMapper;
        this.orderMapper = orderMapper;
        this.skuMapper = skuMapper;
        this.commentMapper = commentMapper;
    }

    public void SaveData(String fileKey, String fileDescription) {
        LocalDateTime dateTime = LocalDateTime.now();
        Overview overview = new Overview(fileKey,fileDescription,dateTime);
        createTable_overview.DB();
        overviewMapper.addOverview(overview);
        String filePath = filepath.path + fileKey;
        if (filePath == null) {
            throw new IllegalArgumentException("Error: File not found.");
        }
        try (FileInputStream fis = new FileInputStream(filePath);
             ZipInputStream zipInputStream = new ZipInputStream(fis)) {

            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String fileName = entry.getName();
                    Workbook workbook = WorkbookFactory.create(zipInputStream);
                    if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                        switch (fileName) {
                            case "action.xlsx":
                                processActionSheet(workbook,fileKey);
                                break;
                            case "order.xlsx":
                                processOrderSheet(workbook,fileKey);
                                break;
                            case "sku.xlsx":
                                processSkuSheet(workbook,fileKey);
                                break;
                            case "comment.xlsx":
                                processCommentSheet(workbook,fileKey);
                                break;
                        }
                    }
                }
                zipInputStream.closeEntry();
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error while processing files.");
        }
    }
    private void processActionSheet(Workbook workbook,String fileKey) {

        Sheet sheet = workbook.getSheetAt(0);
        final int USER_ID_INDEX = 0;
        final int SKU_ID_INDEX = 1;
        final int DATE_INDEX = 2;
        final int NUM_INDEX = 3;
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;

            Integer userId = getCellValueAsInteger(row.getCell(USER_ID_INDEX));
            Integer skuId = getCellValueAsInteger(row.getCell(SKU_ID_INDEX));
            Date date = getCellValueAsDate(row.getCell(DATE_INDEX));
            Integer num = getCellValueAsInteger(row.getCell(NUM_INDEX));
            createTable_action.DB(fileKey);
            String tableName="action_"+fileKey;
            actionMapper.addAction(tableName,userId, skuId,date,num);
        }
    }
    private void processOrderSheet(Workbook workbook,String fileKey) {
        Sheet sheet = workbook.getSheetAt(0);
        final int USER_ID_INDEX = 0;
        final int SKU_ID_INDEX = 1;
        final int ORDER_ID_INDEX = 2;
        final int DATE_INDEX = 3;
        final int AREA_INDEX = 4;
        final int NUM_INDEX = 5;

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            Integer userId = getCellValueAsInteger(row.getCell(USER_ID_INDEX));
            Integer skuId = getCellValueAsInteger(row.getCell(SKU_ID_INDEX));
            Integer orderId = getCellValueAsInteger(row.getCell(ORDER_ID_INDEX));
            Date date = getCellValueAsDate(row.getCell(DATE_INDEX));
            Integer area = getCellValueAsInteger(row.getCell(AREA_INDEX));
            Integer num = getCellValueAsInteger(row.getCell(NUM_INDEX));
            createTable_order.DB(fileKey);
            String tableName="order_"+fileKey;
            orderMapper.addOrder(tableName,userId, skuId, orderId, date, area, num);
        }
    }
    private void processSkuSheet(Workbook workbook,String fileKey) {
        Sheet sheet = workbook.getSheetAt(0);
        final int SKU_ID_INDEX = 0;
        final int PRICE_INDEX = 1;
        final int CATE_INDEX = 2;

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            Integer skuId = getCellValueAsInteger(row.getCell(SKU_ID_INDEX));
            Integer price = getCellValueAsInteger(row.getCell(PRICE_INDEX));
            Integer category = getCellValueAsInteger(row.getCell(CATE_INDEX));
            createTable_sku.DB(fileKey);
            String tableName="sku_"+fileKey;
            skuMapper.addSku(tableName,skuId, price, category);
        }
    }

    private void processCommentSheet(Workbook workbook,String fileKey) {
        Sheet sheet = workbook.getSheetAt(0);
        final int USER_ID_INDEX = 0;
        final int ORDER_ID_INDEX = 1;
        final int SCORE_INDEX = 2;

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;

            Integer userId = getCellValueAsInteger(row.getCell(USER_ID_INDEX));
            Integer orderId = getCellValueAsInteger(row.getCell(ORDER_ID_INDEX));
            Integer score = getCellValueAsInteger(row.getCell(SCORE_INDEX));
            createTable_comment.DB(fileKey);
            String tableName="comment_"+fileKey;
            commentMapper.addComment(tableName,userId, orderId, score);
        }
    }

    private Integer getCellValueAsInteger(Cell cell) {
        if (cell == null) {
            return null;
        }

        CellType cellType = cell.getCellType();
        if (cellType == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else {
            return null;
        }
    }

    private Date getCellValueAsDate(Cell cell) {
        if (cell == null) {
            return null;
        }

        CellType cellType = cell.getCellType();
        if (cellType == CellType.NUMERIC) {
            return cell.getDateCellValue();
        } else {
            return null;
        }
    }

}

