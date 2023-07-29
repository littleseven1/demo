package com.example.project.service;

import com.example.project.entity.Overview;
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

                    break;
                }
            }
            zipInputStream.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error while processing files.");
        }
    }

    private void processActionSheet(Workbook workbook, String fileKey) {
        Sheet sheet = workbook.getSheetAt(0);
        int USER_ID_INDEX = 999;
        int USERNAME_INDEX = 999;
        int SKU_ID_INDEX = 999;
        int DATE_INDEX = 999;
        int NUM_INDEX = 999;
        Row headerRow = sheet.getRow(0);
        for (int i = 1; i <=5; i++) {
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
            Integer iindex=getCellValueAsInteger(row.getCell(0));
            Integer userId = getCellValueAsInteger(row.getCell(USER_ID_INDEX));
            String userName = getCellValueAsString(row.getCell(USERNAME_INDEX));
            Integer skuId = getCellValueAsInteger(row.getCell(SKU_ID_INDEX));
            Date date = getCellValueAsDate(row.getCell(DATE_INDEX));
            Integer num = getCellValueAsInteger(row.getCell(NUM_INDEX));
            createTable_action.DB(fileKey);
            String tableName = "action_" + fileKey;
            actionMapper.addAction(tableName,iindex, userId, userName, skuId, date, num);
        }
    }

    private void processOrderSheet(Workbook workbook, String fileKey) {
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
            Integer iindex=getCellValueAsInteger(row.getCell(0));
            Integer userId = getCellValueAsInteger(row.getCell(USER_ID_INDEX));
            String userName = getCellValueAsString(row.getCell(USERNAME_INDEX));
            Integer skuId = getCellValueAsInteger(row.getCell(SKU_ID_INDEX));
            Integer orderId = getCellValueAsInteger(row.getCell(ORDER_ID_INDEX));
            Date date = getCellValueAsDate(row.getCell(DATE_INDEX));
            Integer area = getCellValueAsInteger(row.getCell(AREA_INDEX));
            String areaName = getCellValueAsString(row.getCell(AREANAME_INDEX));
            Integer num = getCellValueAsInteger(row.getCell(NUM_INDEX));
            String payType = getCellValueAsString(row.getCell(PAYTYPE_INDEX));
            createTable_order.DB(fileKey);
            String tableName = "order_" + fileKey;
            orderMapper.addOrder(tableName,iindex, userId, userName, skuId, orderId, date, area, areaName, num, payType);
        }
    }

    private void processSkuSheet(Workbook workbook, String fileKey) {
        Sheet sheet = workbook.getSheetAt(0);
        int SKU_ID_INDEX = 999;
        int PRICE_INDEX = 999;
        int CATE_INDEX = 999;
        int CATENAME_INDEX = 999;

        Row headerRow = sheet.getRow(0);
        for (int i =1; i <= 4; i++) {
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
            Integer iindex=getCellValueAsInteger(row.getCell(0));
            Integer skuId = getCellValueAsInteger(row.getCell(SKU_ID_INDEX));
            Integer price = getCellValueAsInteger(row.getCell(PRICE_INDEX));
            Integer category = getCellValueAsInteger(row.getCell(CATE_INDEX));
            String cateName = getCellValueAsString(row.getCell(CATENAME_INDEX));
            createTable_sku.DB(fileKey);
            String tableName = "sku_" + fileKey;
            skuMapper.addSku(tableName,iindex, skuId, price, category, cateName);
        }
    }

    private void processCommentSheet(Workbook workbook, String fileKey) {
        Sheet sheet = workbook.getSheetAt(0);
        int USER_ID_INDEX = 999;
        int ORDER_ID_INDEX = 999;
        int SCORE_INDEX = 999;

        Row headerRow = sheet.getRow(0);
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
            Integer iindex=getCellValueAsInteger(row.getCell(0));
            Integer userId = getCellValueAsInteger(row.getCell(USER_ID_INDEX));
            Integer orderId = getCellValueAsInteger(row.getCell(ORDER_ID_INDEX));
            Integer score = getCellValueAsInteger(row.getCell(SCORE_INDEX));
            createTable_comment.DB(fileKey);
            String tableName = "comment_" + fileKey;
            commentMapper.addComment(tableName,iindex,userId, orderId, score);
        }
    }

    private void processActionSheetCSV(InputStream inputStream, String fileKey) {
        try {
            Reader reader = new InputStreamReader(inputStream,"utf-8");
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            int USER_ID_INDEX = 1;
            int USERNAME_INDEX = 2;
            int SKU_ID_INDEX = 3;
            int DATE_INDEX = 4;
            int NUM_INDEX = 5;
           CSVRecord headerRow = records.iterator().next();
//            for (int i = 0; i < headerRow.size(); i++) {
//                String header = headerRow.get(i);
//                if (header.equalsIgnoreCase("user_id")) {
//                    USER_ID_INDEX = i;
//                } else if (header.equalsIgnoreCase("userName")) {
//                    USERNAME_INDEX = i;
//                } else if (header.equalsIgnoreCase("sku_id")) {
//                    SKU_ID_INDEX = i;
//                } else if (header.equalsIgnoreCase("date")) {
//                    DATE_INDEX = i;
//                } else if (header.equalsIgnoreCase("num")) {
//                    NUM_INDEX = i;
//                }
//            }
            for (CSVRecord record : records) {
                Integer iidex=Integer.parseInt(record.get(0));
                Integer userId = USER_ID_INDEX!=-1?Integer.parseInt(record.get(USER_ID_INDEX)):null;
                String userName = USERNAME_INDEX != -1 ? record.get(USERNAME_INDEX) : null;
                double skuI = SKU_ID_INDEX!=-1?Double.parseDouble(record.get(SKU_ID_INDEX)):null;
                Integer skuId=(int)skuI;
                Date date =DATE_INDEX!=-1?isDate(record.get(DATE_INDEX)):null;
                double numi =NUM_INDEX!=-1?Double.parseDouble(record.get(NUM_INDEX)):null;
                Integer num=(int)numi;
                createTable_action.DB(fileKey);
                String tableName = "action_" + fileKey;
                actionMapper.addAction(tableName,iidex,userId, userName, skuId, date, num);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processOrderSheetCSV(InputStream inputStream, String fileKey) {
        try {
            Reader reader = new InputStreamReader(inputStream);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

            int USER_ID_INDEX =1;
            int USERNAME_INDEX = 2;
            int SKU_ID_INDEX = 3;
            int ORDER_ID_INDEX = 4;
            int DATE_INDEX = 5;
            int AREA_INDEX = 6;
            int AREANAME_INDEX = 7;
            int NUM_INDEX = 8;
            int PAYTYPE_INDEX = 9;

       CSVRecord headerRow = records.iterator().next();
//            for (int i = 0; i < headerRow.size(); i++) {
//                String header = headerRow.get(i);
//                if (header.equalsIgnoreCase("user_id")) {
//                    USER_ID_INDEX = i;
//                } else if (header.equalsIgnoreCase("userName")) {
//                    USERNAME_INDEX = i;
//                } else if (header.equalsIgnoreCase("sku_id")) {
//                    SKU_ID_INDEX = i;
//                } else if (header.equalsIgnoreCase("o_id")) {
//                    ORDER_ID_INDEX = i;
//                } else if (header.equalsIgnoreCase("date")) {
//                    DATE_INDEX = i;
//                } else if (header.equalsIgnoreCase("area")) {
//                    AREA_INDEX = i;
//                } else if (header.equalsIgnoreCase("areaName")) {
//                    AREANAME_INDEX = i;
//                } else if (header.equalsIgnoreCase("num")) {
//                    NUM_INDEX = i;
//                } else if (header.equalsIgnoreCase("payType")) {
//                    PAYTYPE_INDEX = i;
//                }
//            }
            
            for (CSVRecord record : records) {
                Integer iidex=Integer.parseInt(record.get(0));
                Integer userId = USER_ID_INDEX!=-1?Integer.parseInt(record.get(USER_ID_INDEX)):null;
                String userName = USERNAME_INDEX != -1 ? record.get(USERNAME_INDEX) : null;
                Integer skuId = SKU_ID_INDEX!=-1?Integer.parseInt(record.get(SKU_ID_INDEX)):null;
                Integer orderId =ORDER_ID_INDEX!=-1?Integer.parseInt(record.get(ORDER_ID_INDEX)):null;
                Date date = DATE_INDEX!=-1?isDate(record.get(DATE_INDEX)):null;
                Integer area = AREA_INDEX!=-1?Integer.parseInt(record.get(AREA_INDEX)):null;
                String areaName = AREANAME_INDEX != -1 ? record.get(AREANAME_INDEX) : null;
                Integer num = NUM_INDEX!=-1?Integer.parseInt(record.get(NUM_INDEX)):null;
                String payType = PAYTYPE_INDEX != -1 ? record.get(PAYTYPE_INDEX) : null;
                createTable_order.DB(fileKey);
                String tableName = "order_" + fileKey;
                orderMapper.addOrder(tableName,iidex,userId, userName, skuId, orderId, date, area, areaName, num, payType);

            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processSkuSheetCSV(InputStream inputStream, String fileKey) {
        try {
            Reader reader = new InputStreamReader(inputStream);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            int SKU_ID_INDEX = 1;
            int PRICE_INDEX = 2;
            int CATE_INDEX = 3;
            int CATENAME_INDEX = 4;
          CSVRecord headerRow = records.iterator().next();
//            for (int i = 0; i < headerRow.size(); i++) {
//                String header = headerRow.get(i);
//                if (header != null && header.equalsIgnoreCase("sku_id")) {
//                    SKU_ID_INDEX = i;
//                } else if (header != null && header.equalsIgnoreCase("price")) {
//                    PRICE_INDEX = i;
//                } else if (header != null && header.equalsIgnoreCase("cate")) {
//                    CATE_INDEX = i;
//                } else if (header != null && header.equalsIgnoreCase("cateName")) {
//                    CATENAME_INDEX = i;
//                }
//            }
            
            for (CSVRecord record : records) {
                Integer iidex=Integer.parseInt(record.get(0));
                Integer skuId =SKU_ID_INDEX!=-1?Integer.parseInt(record.get(SKU_ID_INDEX)):null;
                double pricey =PRICE_INDEX!=-1?Double.parseDouble(record.get(PRICE_INDEX)):null;
                Integer price=(int)pricey;
                Integer category =CATE_INDEX!=-1?Integer.parseInt(record.get(CATE_INDEX)):null;
                String cateName = CATENAME_INDEX != -1 ? record.get(CATENAME_INDEX) : null;
                createTable_sku.DB(fileKey);
                String tableName = "sku_" + fileKey;
                skuMapper.addSku(tableName,iidex,skuId, price, category, cateName);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processCommentSheetCSV(InputStream inputStream, String fileKey) {
        try {
            Reader reader = new InputStreamReader(inputStream);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            int USER_ID_INDEX =1;
            int ORDER_ID_INDEX = 2;
            int SCORE_INDEX = 3;

           CSVRecord headerRow = records.iterator().next();
//            for (int i = 0; i < headerRow.size(); i++) {
//                String header = headerRow.get(i);
//                if (header != null && header.equalsIgnoreCase("user_id")) {
//                    USER_ID_INDEX = i;
//                } else if (header != null && header.equalsIgnoreCase("o_id")) {
//                    ORDER_ID_INDEX = i;
//                } else if (header != null && header.equalsIgnoreCase("score")) {
//                    SCORE_INDEX = i;
//                }
//            }
            
            for (CSVRecord record : records) {
                Integer iidex=Integer.parseInt(record.get(0));
                Integer userId =USER_ID_INDEX!=-1? Integer.parseInt(record.get(USER_ID_INDEX)):null;
                Integer orderId =ORDER_ID_INDEX!=-1?Integer.parseInt(record.get(ORDER_ID_INDEX)):null;
                Integer score = SCORE_INDEX!=-1?Integer.parseInt(record.get(SCORE_INDEX)):null;
                createTable_comment.DB(fileKey);
                String tableName = "comment_" + fileKey;
                commentMapper.addComment(tableName,iidex, userId, orderId, score);
            
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