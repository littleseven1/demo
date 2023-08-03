package com.example.project.service;

import com.example.project.entity.*;
import com.example.project.filepath;
import com.example.project.mapper.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
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
    OverviewMapper.CreateTable createTable_overview = new OverviewMapper.CreateTable();
    OrderMapper.CreateTable createTable_order = new OrderMapper.CreateTable();
    SkuMapper.CreateTable createTable_sku = new SkuMapper.CreateTable();
    CommentMapper.CreateTable createTable_comment = new CommentMapper.CreateTable();

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
        Overview overview = new Overview(fileKey, fileDescription, dateTime);
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
                    String entryName = entry.getName();
                    InputStream inputStream = new ByteArrayInputStream(zipInputStream.readAllBytes());

                    Workbook workbook = null;

                    if (entryName.endsWith(".xlsx") || entryName.endsWith(".xls")) {
                        workbook = new XSSFWorkbook(inputStream);
                    }
                    if (workbook != null) {
                        switch (entryName) {
                            case "action.xlsx", "action.xls" -> processActionSheet(workbook, fileKey);
                            case "order.xlsx", "order.xls" -> processOrderSheet(workbook, fileKey);
                            case "sku.xlsx", "sku.xls" -> processSkuSheet(workbook, fileKey);
                            case "comment.xlsx", "comment.xls" -> processCommentSheet(workbook, fileKey);
                        }
                    } else if (entryName.endsWith(".csv")) {
                        switch (entryName) {
                            case "action.csv" -> processActionSheetCSV(inputStream, fileKey);
                            case "order.csv" -> processOrderSheetCSV(inputStream, fileKey);
                            case "sku.csv" -> processSkuSheetCSV(inputStream, fileKey);
                            case "comment.csv" -> processCommentSheetCSV(inputStream, fileKey);
                        }
                    }
                }
            }
            zipInputStream.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error while processing files.");
        }
    }

    private void processActionSheet(Workbook workbook, String fileKey) {
        createTable_action.DB(fileKey);
        Sheet sheet = workbook.getSheetAt(0);
        int USER_ID_INDEX = 999;
        int USERNAME_INDEX = 999;
        int SKU_ID_INDEX = 999;
        int DATE_INDEX = 999;
        int NUM_INDEX = 999;
        Row headerRow = sheet.getRow(0);

        List<ActionData> actionDataList = new ArrayList<>();
        int batchSize = 50000;
        int count = 0;

        for (int i = 1; i <= 5; i++) {
            String str = getCellValueAsString(headerRow.getCell(i));
            if (str != null && str.equals("user_id")) {
                USER_ID_INDEX = i;
            } else if (str != null && str.equals("userName")) {
                USERNAME_INDEX = i;
            } else if (str != null && str.equals("sku_id")) {
                SKU_ID_INDEX = i;
            } else if (str != null && str.equals("date")) {
                DATE_INDEX = i;
            } else if (str != null && str.equals("num")) {
                NUM_INDEX = i;
            }
        }

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            Integer iindex = getCellValueAsInteger(row.getCell(0));
            Integer userId = getCellValueAsInteger(row.getCell(USER_ID_INDEX));
            String userName = getCellValueAsString(row.getCell(USERNAME_INDEX));
            Integer skuId = getCellValueAsInteger(row.getCell(SKU_ID_INDEX));
            Date date = getCellValueAsDate(row.getCell(DATE_INDEX));
            Integer num = getCellValueAsInteger(row.getCell(NUM_INDEX));

            ActionData actionData = new ActionData();
            actionData.setIindex(iindex);
            actionData.setUser_id(userId);
            actionData.setUserName(userName);
            actionData.setSku_id(skuId);
            actionData.setDate(date);
            actionData.setNum(num);

            actionDataList.add(actionData);
            count++;

            if (count % batchSize == 0) {
                String tableName = "action_" + fileKey;
                actionMapper.batchInsertActions(tableName, actionDataList);
                actionDataList.clear();
            }
        }

        // 插入最后一批未满批次的数据
        if (!actionDataList.isEmpty()) {
            String tableName = "action_" + fileKey;
            actionMapper.batchInsertActions(tableName, actionDataList);
        }
    }

    private void processOrderSheet(Workbook workbook, String fileKey) {
        createTable_order.DB(fileKey);
        Sheet sheet = workbook.getSheetAt(0);
        int USER_ID_INDEX = 999;
        int USERNAME_INDEX = 999;
        int SKU_ID_INDEX = 999;
        int ORDER_ID_INDEX = 999;
        int DATE_INDEX = 999;
        int AREA_INDEX = 999;
        int AREANAME_INDEX = 999;
        int NUM_INDEX = 999;
        int PAYTYPE_INDEX = 999;
        Row headerRow = sheet.getRow(0);
        List<OrderData> orderDataList = new ArrayList<>();
        int batchSize = 50000;
        int count = 0;

        for (int i = 1; i <= 9; i++) {
            String str = getCellValueAsString(headerRow.getCell(i));
            if (str != null && str.equals("user_id")) {
                USER_ID_INDEX = i;
            } else if (str != null && str.equals("userName")) {
                USERNAME_INDEX = i;
            } else if (str != null && str.equals("sku_id")) {
                SKU_ID_INDEX = i;
            } else if (str != null && str.equals("o_id")) {
                ORDER_ID_INDEX = i;
            } else if (str != null && str.equals("date")) {
                DATE_INDEX = i;
            } else if (str != null && str.equals("area")) {
                AREA_INDEX = i;
            } else if (str != null && str.equals("areaName")) {
                AREANAME_INDEX = i;
            } else if (str != null && str.equals("num")) {
                NUM_INDEX = i;
            } else if (str != null && str.equals("payType")) {
                PAYTYPE_INDEX = i;
            }
        }

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            Integer iindex = getCellValueAsInteger(row.getCell(0));
            Integer userId = getCellValueAsInteger(row.getCell(USER_ID_INDEX));
            String userName = getCellValueAsString(row.getCell(USERNAME_INDEX));
            Integer skuId = getCellValueAsInteger(row.getCell(SKU_ID_INDEX));
            Integer orderId = getCellValueAsInteger(row.getCell(ORDER_ID_INDEX));
            Date date = getCellValueAsDate(row.getCell(DATE_INDEX));
            Integer area = getCellValueAsInteger(row.getCell(AREA_INDEX));
            String areaName = getCellValueAsString(row.getCell(AREANAME_INDEX));
            Integer num = getCellValueAsInteger(row.getCell(NUM_INDEX));
            String payType = getCellValueAsString(row.getCell(PAYTYPE_INDEX));

            OrderData orderData = new OrderData();
            orderData.setIindex(iindex);
            orderData.setUser_id(userId);
            orderData.setUserName(userName);
            orderData.setSku_id(skuId);
            orderData.setO_id(orderId);
            orderData.setDate(date);
            orderData.setArea(area);
            orderData.setAreaName(areaName);
            orderData.setNum(num);
            orderData.setPayType(payType);

            orderDataList.add(orderData);
            count++;

            if (count % batchSize == 0) {
                String tableName = "order_" + fileKey;
                orderMapper.batchInsertOrders(tableName, orderDataList);
                orderDataList.clear();
            }
        }
        if (!orderDataList.isEmpty()) {
            String tableName = "order_" + fileKey;
            orderMapper.batchInsertOrders(tableName, orderDataList);
        }
    }

    private void processSkuSheet(Workbook workbook, String fileKey) {
        createTable_sku.DB(fileKey);
        Sheet sheet = workbook.getSheetAt(0);
        int SKU_ID_INDEX = 999;
        int PRICE_INDEX = 999;
        int CATE_INDEX = 999;
        int CATENAME_INDEX = 999;
        Row headerRow = sheet.getRow(0);
        List<SkuData> skuDataList = new ArrayList<>();
        int batchSize = 50000;
        int count = 0;

        for (int i = 1; i <= 4; i++) {
            String str = getCellValueAsString(headerRow.getCell(i));
            if (str != null && str.equals("sku_id")) {
                SKU_ID_INDEX = i;
            } else if (str != null && str.equals("price")) {
                PRICE_INDEX = i;
            } else if (str != null && str.equals("cate")) {
                CATE_INDEX = i;
            } else if (str != null && str.equals("cateName")) {
                CATENAME_INDEX = i;
            }
        }

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            Integer iindex = getCellValueAsInteger(row.getCell(0));
            Integer skuId = getCellValueAsInteger(row.getCell(SKU_ID_INDEX));
            double price = getCellValueAsDouble(row.getCell(PRICE_INDEX));
            Integer category = getCellValueAsInteger(row.getCell(CATE_INDEX));
            String cateName = getCellValueAsString(row.getCell(CATENAME_INDEX));

            SkuData skuData = new SkuData();
            skuData.setIindex(iindex);
            skuData.setSku_id(skuId);
            skuData.setPrice((float) price);
            skuData.setCate(category);
            skuData.setCateName(cateName);

            skuDataList.add(skuData);
            count++;

            if (count % batchSize == 0) {
                String tableName = "sku_" + fileKey;
                skuMapper.batchInsertSkus(tableName, skuDataList);
                skuDataList.clear();
            }
        }

        if (!skuDataList.isEmpty()) {
            String tableName = "sku_" + fileKey;
            skuMapper.batchInsertSkus(tableName, skuDataList);
        }
    }

    private void processCommentSheet(Workbook workbook, String fileKey) {
        createTable_comment.DB(fileKey);
        Sheet sheet = workbook.getSheetAt(0);
        int USER_ID_INDEX = 999;
        int ORDER_ID_INDEX = 999;
        int SCORE_INDEX = 999;
        Row headerRow = sheet.getRow(0);
        List<CommentData> commentDataList = new ArrayList<>();
        int batchSize = 50000;
        int count = 0;

        for (int i = 1; i <= 3; i++) {
            String str = getCellValueAsString(headerRow.getCell(i));
            if (str != null && str.equals("user_id")) {
                USER_ID_INDEX = i;
            } else if (str != null && str.equals("o_id")) {
                ORDER_ID_INDEX = i;
            } else if (str != null && str.equals("score")) {
                SCORE_INDEX = i;
            }
        }

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            Integer iindex = getCellValueAsInteger(row.getCell(0));
            Integer userId = getCellValueAsInteger(row.getCell(USER_ID_INDEX));
            Integer orderId = getCellValueAsInteger(row.getCell(ORDER_ID_INDEX));
            Integer score = getCellValueAsInteger(row.getCell(SCORE_INDEX));

            CommentData commentData = new CommentData();
            commentData.setIindex(iindex);
            commentData.setUser_id(userId);
            commentData.setO_id(orderId);
            commentData.setScore(score);

            commentDataList.add(commentData);
            count++;

            if (count % batchSize == 0) {
                String tableName = "comment_" + fileKey;
                commentMapper.batchInsertComments(tableName, commentDataList);
                commentDataList.clear();
            }
        }
        if (!commentDataList.isEmpty()) {
            String tableName = "comment_" + fileKey;
            commentMapper.batchInsertComments(tableName, commentDataList);
        }
    }


    private void processActionSheetCSV(InputStream inputStream, String fileKey) {
        createTable_action.DB(fileKey);
        try {
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            int USER_ID_INDEX = 1;
            int USERNAME_INDEX = 2;
            int SKU_ID_INDEX = 3;
            int DATE_INDEX = 4;
            int NUM_INDEX = 5;

            int batchSize = 50000; // 设置每批次的大小
            List<ActionData> batchDataList = new ArrayList<>();
            int count = 0;
            CSVRecord headerRow = records.iterator().next();
            for (CSVRecord record : records) {
                Integer iindex = Integer.parseInt(record.get(0));
                Integer userId = USER_ID_INDEX != -1 ? Integer.parseInt(record.get(USER_ID_INDEX)) : null;
                String userName = USERNAME_INDEX != -1 ? record.get(USERNAME_INDEX) : null;
                double skuI = SKU_ID_INDEX != -1 ? Double.parseDouble(record.get(SKU_ID_INDEX)) : null;
                Integer skuId = (int) skuI;
                Date date = DATE_INDEX != -1 ? isDate(record.get(DATE_INDEX)) : null;
                double numi = NUM_INDEX != -1 ? Double.parseDouble(record.get(NUM_INDEX)) : null;
                Integer num = (int) numi;

                ActionData actionData = new ActionData();
                actionData.setIindex(iindex);
                actionData.setUser_id(userId);
                actionData.setUserName(userName);
                actionData.setSku_id(skuId);
                actionData.setDate(date);
                actionData.setNum(num);

                batchDataList.add(actionData);
                count++;

                if (count % batchSize == 0) {
                    String tableName = "action_" + fileKey;
                    actionMapper.batchInsertActions(tableName, batchDataList);
                    batchDataList.clear();
                }
            }

            if (!batchDataList.isEmpty()) {
                String tableName = "action_" + fileKey;
                actionMapper.batchInsertActions(tableName, batchDataList);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processOrderSheetCSV(InputStream inputStream, String fileKey) {
        createTable_order.DB(fileKey);
        try {
            Reader reader = new InputStreamReader(inputStream);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

            int USER_ID_INDEX = 1;
            int USERNAME_INDEX = 2;
            int SKU_ID_INDEX = 3;
            int ORDER_ID_INDEX = 4;
            int DATE_INDEX = 5;
            int AREA_INDEX = 6;
            int AREANAME_INDEX = 7;
            int NUM_INDEX = 8;
            int PAYTYPE_INDEX = 9;

            int batchSize = 50000;
            List<OrderData> batchDataList = new ArrayList<>();
            int count = 0;
            CSVRecord headerRow = records.iterator().next();
            for (CSVRecord record : records) {
                Integer iidex = Integer.parseInt(record.get(0));
                Integer userId = USER_ID_INDEX != -1 ? Integer.parseInt(record.get(USER_ID_INDEX)) : null;
                String userName = USERNAME_INDEX != -1 ? record.get(USERNAME_INDEX) : null;
                Integer skuId = SKU_ID_INDEX != -1 ? Integer.parseInt(record.get(SKU_ID_INDEX)) : null;
                Integer orderId = ORDER_ID_INDEX != -1 ? Integer.parseInt(record.get(ORDER_ID_INDEX)) : null;
                Date date = DATE_INDEX != -1 ? isDate(record.get(DATE_INDEX)) : null;
                Integer area = AREA_INDEX != -1 ? Integer.parseInt(record.get(AREA_INDEX)) : null;
                String areaName = AREANAME_INDEX != -1 ? record.get(AREANAME_INDEX) : null;
                Integer num = NUM_INDEX != -1 ? Integer.parseInt(record.get(NUM_INDEX)) : null;
                String payType = PAYTYPE_INDEX != -1 ? record.get(PAYTYPE_INDEX) : null;

                OrderData orderData = new OrderData();
                orderData.setIindex(iidex);
                orderData.setUser_id(userId);
                orderData.setUserName(userName);
                orderData.setSku_id(skuId);
                orderData.setO_id(orderId);
                orderData.setDate(date);
                orderData.setArea(area);
                orderData.setAreaName(areaName);
                orderData.setNum(num);
                orderData.setPayType(payType);

                batchDataList.add(orderData);
                count++;

                if (count % batchSize == 0) {
                    String tableName = "order_" + fileKey;
                    orderMapper.batchInsertOrders(tableName, batchDataList);
                    batchDataList.clear();
                }
            }

            if (!batchDataList.isEmpty()) {
                String tableName = "order_" + fileKey;
                orderMapper.batchInsertOrders(tableName, batchDataList);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processSkuSheetCSV(InputStream inputStream, String fileKey) {
        createTable_sku.DB(fileKey);
        try {
            Reader reader = new InputStreamReader(inputStream);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            int SKU_ID_INDEX = 1;
            int PRICE_INDEX = 2;
            int CATE_INDEX = 3;
            int CATENAME_INDEX = 4;

            int batchSize = 50000;
            List<SkuData> batchDataList = new ArrayList<>();
            int count = 0;
            CSVRecord headerRow = records.iterator().next();
            for (CSVRecord record : records) {
                Integer iidex = Integer.parseInt(record.get(0));
                Integer skuId = SKU_ID_INDEX != -1 ? Integer.parseInt(record.get(SKU_ID_INDEX)) : null;
                double price = PRICE_INDEX != -1 ? Double.parseDouble(record.get(PRICE_INDEX)) : null;
                Integer category = CATE_INDEX != -1 ? Integer.parseInt(record.get(CATE_INDEX)) : null;
                String cateName = CATENAME_INDEX != -1 ? record.get(CATENAME_INDEX) : null;

                SkuData skuData = new SkuData();
                skuData.setIindex(iidex);
                skuData.setSku_id(skuId);
                skuData.setPrice((float) price);
                skuData.setCate(category);
                skuData.setCateName(cateName);

                batchDataList.add(skuData);
                count++;

                if (count % batchSize == 0) {
                    String tableName = "sku_" + fileKey;
                    skuMapper.batchInsertSkus(tableName, batchDataList);
                    batchDataList.clear();
                }
            }

            if (!batchDataList.isEmpty()) {
                String tableName = "sku_" + fileKey;
                skuMapper.batchInsertSkus(tableName, batchDataList);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void processCommentSheetCSV(InputStream inputStream, String fileKey) {
        createTable_comment.DB(fileKey);
        try {
            Reader reader = new InputStreamReader(inputStream);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            int USER_ID_INDEX = 1;
            int ORDER_ID_INDEX = 2;
            int SCORE_INDEX = 3;

            int batchSize = 50000;
            List<CommentData> batchDataList = new ArrayList<>();
            int count = 0;
            CSVRecord headerRow = records.iterator().next();
            for (CSVRecord record : records) {
                Integer iidex = Integer.parseInt(record.get(0));
                Integer userId = USER_ID_INDEX != -1 ? Integer.parseInt(record.get(USER_ID_INDEX)) : null;
                Integer orderId = ORDER_ID_INDEX != -1 ? Integer.parseInt(record.get(ORDER_ID_INDEX)) : null;
                Integer score = SCORE_INDEX != -1 ? Integer.parseInt(record.get(SCORE_INDEX)) : null;

                CommentData commentData = new CommentData();
                commentData.setIindex(iidex);
                commentData.setUser_id(userId);
                commentData.setO_id(orderId);
                commentData.setScore(score);

                batchDataList.add(commentData);
                count++;

                if (count % batchSize == 0) {
                    String tableName = "comment_" + fileKey;
                    commentMapper.batchInsertComments(tableName, batchDataList);
                    batchDataList.clear();
                }
            }

            if (!batchDataList.isEmpty()) {
                String tableName = "comment_" + fileKey;
                commentMapper.batchInsertComments(tableName, batchDataList);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
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

    private Double getCellValueAsDouble(Cell cell) {
        if (cell == null) {
            return null;
        }

        CellType cellType = cell.getCellType();
        if (cellType == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else {
            return null;
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        CellType cellType = cell.getCellType();
        if (cellType == CellType.STRING) {
            return cell.getStringCellValue();
        } else {
            return null;
        }
    }

    public static Date isDate(String dateString) throws ParseException {
        List<String> dateFormats = Arrays.asList("yyyy/MM/dd", "yyyy-MM-dd");
        Date date = null;
        for (String dateFormat : dateFormats) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            sdf.setLenient(false);
            try {
                date = sdf.parse(dateString);
                return date; // 成功解析，直接返回Date对象
            } catch (ParseException e) {
                // 解析失败，继续尝试下一个日期格式
            }
        }
        return date; // 返回null，表示日期解析失败
    }

}