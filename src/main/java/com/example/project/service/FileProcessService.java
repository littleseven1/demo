package com.example.project.service;

import com.example.project.filepath;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class FileProcessService {
    @Autowired(required = false)
    public List<Map<String, Object>> processAndCheckFile(String fileKey){
        String filePath = filepath.path + fileKey;
        List<String> fileNames = Arrays.asList("action", "comment", "order", "sku");

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (String fileName : fileNames) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("name", fileName);
            Map<String, Object> errorMap = setErrorMap(false, true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

            try (FileInputStream fis = new FileInputStream(filePath);
                 ZipInputStream zipInputStream = new ZipInputStream(fis)) {

                ZipEntry entry;
                boolean isFileFound = false;

                while ((entry = zipInputStream.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        String entryName = entry.getName();
                        String fileNameWithoutExtension = entryName;
                        int dotIndex = entryName.lastIndexOf(".");
                        if (dotIndex > 0) {
                            fileNameWithoutExtension = entryName.substring(0, dotIndex);
                        }
                        if (fileNameWithoutExtension.equals(fileName)) {
                            isFileFound = true;

                            InputStream inputStream = new ByteArrayInputStream(zipInputStream.readAllBytes());

                            Workbook workbook = null;

                            if (entryName.endsWith(".xlsx")||entryName.endsWith(".xls")) {
                                workbook = new XSSFWorkbook(inputStream);
                            }
                            if (workbook != null) {
                                switch (entryName) {
                                    case "action.xlsx", "action.xls" -> processActionSheet(workbook, errorMap);
                                    case "order.xlsx", "order.xls" -> processOrderSheet(workbook, errorMap);
                                    case "sku.xlsx", "sku.xls" -> processSkuSheet(workbook, errorMap);
                                    case "comment.xlsx", "comment.xls" -> processCommentSheet(workbook, errorMap);
                                }
                            } else if (entryName.endsWith(".csv")) {
                                switch (entryName) {
                                    case "action.csv" -> processActionSheetCSV(inputStream, errorMap);
                                    case "order.csv" -> processOrderSheetCSV(inputStream, errorMap);
                                    case "sku.csv" -> processSkuSheetCSV(inputStream, errorMap);
                                    case "comment.csv" -> processCommentSheetCSV(inputStream, errorMap);
                                }
                            }

                            break;
                        }
                    }
                    zipInputStream.closeEntry();
                }

                if (!isFileFound) {
                    errorMap = setErrorMap(false, true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                }

            } catch (IOException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("处理文件时发生错误: " + e.getMessage(), e);
            }

            result.put("error", errorMap);
            resultList.add(result);
        }

        return resultList;
    }

    private void processActionSheet(Workbook workbook, Map<String, Object> errorMap) {
        errorMap.put("isExist", true);
        Sheet sheet = workbook.getSheetAt(0);

        int USER_ID_INDEX=999 ;
        int USERNAME_INDEX=999;
        int SKU_ID_INDEX=999 ;
        int DATE_INDEX=999 ;
        int NUM_INDEX =999;

        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            errorMap.put("isEmpty", true);
        }
        for(int i=1;i<=5;i++){
            String str=getCellValueAsString(headerRow.getCell(i));
            if (str!=null&&str.equals("user_id")) {
                USER_ID_INDEX=i;
            }
            else if(str!=null&&str.equals("userName"))
            {
                USERNAME_INDEX=i;
            }
            else if(str!=null&&str.equals("sku_id"))
            {
                SKU_ID_INDEX=i;
            }
            else if(str!=null&&str.equals("date"))
            {
                DATE_INDEX=i;
            }
            else if(str!=null&&str.equals("num"))
            {
                NUM_INDEX=i;
            }
        }
        List<String> columns = new ArrayList<>();
        List<String> columnsNan = new ArrayList<>();
        List<String> dataType = new ArrayList<>();

        if (USER_ID_INDEX==999) {
            columns.add("user_id");
        }
        if (USERNAME_INDEX == 999) {
            columns.add("userName");
        }
        if (SKU_ID_INDEX== 999) {
            columns.add("sku_id");
        }
        if (DATE_INDEX== 999) {
            columns.add("date");
        }
        if (NUM_INDEX == 999) {
            columns.add("num");
        }
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            errorMap.put("isEmpty", false);
            Cell userId = row.getCell(USER_ID_INDEX);
            Cell userName=row.getCell(USERNAME_INDEX);
            Cell skuId = row.getCell(SKU_ID_INDEX);
            Cell date = row.getCell(DATE_INDEX);
            Cell num =row.getCell(NUM_INDEX);
            if (headerRow.getCell(USER_ID_INDEX) != null) {
                if (userId == null&&!columnsNan.contains("user_id"))
                {
                    columnsNan.add("user_id");
                }
                else
                {
                    if (userId.getCellType()!=CellType.NUMERIC&&!dataType.contains("user_id"))
                    {
                        dataType.add("user_id");
                    }
                }
            }
            if (headerRow.getCell(USERNAME_INDEX) != null) {
                if (userName == null&&!columnsNan.contains("userName"))
                {
                    columnsNan.add("userName");
                }
                else
                {
                    if (userName.getCellType()!=CellType.STRING&&!dataType.contains("userName"))
                    {
                        dataType.add("userName");
                    }
                }
            }
            if (headerRow.getCell(SKU_ID_INDEX) != null) {
                if (skuId == null&&!columnsNan.contains("sku_id"))
                {
                    columnsNan.add("sku_id");
                }
                else
                {
                    if(skuId.getCellType()!=CellType.NUMERIC&&!dataType.contains("sku_id"))
                    {
                        dataType.add("sku_id");
                    }
                }
            }
            if (headerRow.getCell(DATE_INDEX) != null) {
                if (date == null&&!columnsNan.contains("date"))
                {
                    columnsNan.add("date");
                }
                else
                {
                    if (!(date.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(date))&&!dataType.contains("date")) {
                        dataType.add("date");
                    }
                }
            }
            if (headerRow.getCell(NUM_INDEX) != null) {

                if (num == null&&!columnsNan.contains("num"))
                {
                    columnsNan.add("num");
                }
                else
                {
                    if (num.getCellType()!=CellType.NUMERIC&&!dataType.contains("num")) {
                        dataType.add("num");
                    }
                }
            }

        }
        errorMap.put("columns", columns);
        errorMap.put("columnsNan", columnsNan);
        errorMap.put("dataType", dataType);
    }

    private void processOrderSheet(Workbook workbook, Map<String, Object> errorMap) {
        errorMap.put("isExist", true);
        Sheet sheet = workbook.getSheetAt(0);
        int USER_ID_INDEX = 999;
        int USERNAME_INDEX=999;
        int SKU_ID_INDEX =999;
        int ORDER_ID_INDEX = 999;
        int DATE_INDEX =999;
        int AREA_INDEX =999;
        int AREANAME_INDEX=999;
        int NUM_INDEX = 999;
        int PAYTYPE_INDEX=999;

        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            errorMap.put("isEmpty", true);
        }
        for (int i = 1; i <= 9; i++) {
           String str=getCellValueAsString(headerRow.getCell(i));
            if (str!=null&&str.equals("user_id")) {
                USER_ID_INDEX=i;
            }
            else if (str!=null&&str.equals("userName")) {
                USERNAME_INDEX=i;
            }
            else if (str!=null&&str.equals("sku_id")) {
                SKU_ID_INDEX=i;
            }
            else if (str!=null&&str.equals("o_id")) {
                ORDER_ID_INDEX=i;
            }
            else if (str!=null&&str.equals("date")) {
                DATE_INDEX=i;
            }
            else if (str!=null&&str.equals("area")) {
                AREA_INDEX=i;
            }
            else if (str!=null&&str.equals("areaName")) {
                AREANAME_INDEX=i;
            }
            else if (str!=null&&str.equals("num")) {
                NUM_INDEX=i;
            }
            else if (str!=null&&str.equals("payType")) {
                PAYTYPE_INDEX=i;
            }
        }
        List<String> columns = new ArrayList<>();
        List<String> columnsNan = new ArrayList<>();
        List<String> dataType = new ArrayList<>();

        if (USER_ID_INDEX == 999) {
            columns.add("user_id");
        }
        if (USERNAME_INDEX == 999) {
            columns.add("userName");
        }
        if (SKU_ID_INDEX == 999) {
            columns.add("sku_id");
        }
        if (ORDER_ID_INDEX== 999) {
            columns.add("o_id");
        }
        if (DATE_INDEX == 999) {
            columns.add("date");
        }
        if (AREA_INDEX== 999) {
            columns.add("area");
        }
        if (AREANAME_INDEX== 999) {
            columns.add("areaName");
        }
        if (NUM_INDEX== 999) {
            columns.add("num");
        }
        if (PAYTYPE_INDEX== 999) {
            columns.add("payType");
        }
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            errorMap.put("isEmpty", false);
            Cell userId = row.getCell(USER_ID_INDEX);
            Cell userName=row.getCell(USERNAME_INDEX);
            Cell skuId = row.getCell(SKU_ID_INDEX);
            Cell orderId = row.getCell(ORDER_ID_INDEX);
            Cell date =row.getCell(DATE_INDEX);
            Cell area =row.getCell(AREA_INDEX);
            Cell areaName=row.getCell(AREANAME_INDEX);
            Cell num = row.getCell(NUM_INDEX);
            Cell payType=row.getCell(PAYTYPE_INDEX);

            if (headerRow.getCell(USER_ID_INDEX) != null) {
                if (userId == null&&!columnsNan.contains("user_id")) {
                    columnsNan.add("user_id");
                } else {
                    if (userId.getCellType()!=CellType.NUMERIC&&!dataType.contains("user_id")) {
                        dataType.add("user_id");
                    }
                }
            }
            if (headerRow.getCell(USERNAME_INDEX) != null) {
                if (userName == null&&!columnsNan.contains("userName"))
                {
                    columnsNan.add("userName");
                }
                else
                {
                    if (userName.getCellType()!=CellType.STRING&&!dataType.contains("userName"))
                    {
                        dataType.add("userName");
                    }
                }
            }
            if (headerRow.getCell(SKU_ID_INDEX) != null) {
                if (skuId == null&&!columnsNan.contains("sku_id")) {
                    columnsNan.add("sku_id");
                } else {
                    if (skuId.getCellType()!=CellType.NUMERIC&&!dataType.contains("sku_id")) {
                        dataType.add("sku_id");
                    }
                }
            }
            if (headerRow.getCell(ORDER_ID_INDEX) != null) {
                if (orderId == null&&!columnsNan.contains("o_id")) {
                    columnsNan.add("o_id");
                } else {
                    if (orderId.getCellType()!=CellType.NUMERIC&&!dataType.contains("o_id")) {
                        dataType.add("o_id");
                    }
                }
            }
            if (headerRow.getCell(DATE_INDEX) != null) {
                if (date == null&&!columnsNan.contains("date")) {
                    columnsNan.add("date");
                } else {
                    if (!(date.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(date))&&!dataType.contains("date")) {
                        dataType.add("date");
                    }
                }
            }
            if (headerRow.getCell(AREA_INDEX) != null) {
                if (area == null&&!columnsNan.contains("area")) {
                    columnsNan.add("area");
                } else {
                    if (area.getCellType()!=CellType.NUMERIC&&!dataType.contains("area")) {
                        dataType.add("area");
                    }
                }
            }
            if (headerRow.getCell(AREANAME_INDEX) != null) {
                if (areaName == null&&!columnsNan.contains("areaName")) {
                    columnsNan.add("areaName");
                } else {
                    if (areaName.getCellType()!=CellType.STRING&&!dataType.contains("areaName")) {
                        dataType.add("areaName");
                    }
                }
            }
            if (headerRow.getCell(NUM_INDEX) != null) {
                if (num == null&&!columnsNan.contains("num")) {
                    columnsNan.add("num");
                } else {
                    if (num.getCellType()!=CellType.NUMERIC&&!dataType.contains("num")) {
                        dataType.add("num");
                    }
                }
            }
            if (headerRow.getCell(PAYTYPE_INDEX) != null) {
                if (payType == null&&!columnsNan.contains("payType")) {
                    columnsNan.add("payType");
                } else {
                    if (payType.getCellType()!=CellType.STRING&&!dataType.contains("payType")) {
                        dataType.add("payType");
                    }
                }
            }
        }
        errorMap.put("columns", columns);
        errorMap.put("columnsNan", columnsNan);
        errorMap.put("dataType", dataType);
    }

    private void processSkuSheet(Workbook workbook, Map<String, Object> errorMap) {
        errorMap.put("isExist", true);
        Sheet sheet = workbook.getSheetAt(0);
        int SKU_ID_INDEX = 999;
        int PRICE_INDEX = 999;
        int CATE_INDEX = 999;
        int CATENAME_INDEX=999;

        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            errorMap.put("isEmpty", true);
        }
        for(int i=1;i<=4;i++){
            String str=getCellValueAsString(headerRow.getCell(i));
            if (str != null && str.equals("sku_id")) {
                SKU_ID_INDEX=i;
            }
            else if (str != null &&str.equals("price")) {
                PRICE_INDEX=i;
            }
            else if (str != null &&str.equals("cate")) {
                CATE_INDEX=i;
            }
            else if (str != null &&str.equals("cateName")) {
                CATENAME_INDEX=i;
            }
        }
        List<String> columns = new ArrayList<>();
        List<String> columnsNan = new ArrayList<>();
        List<String> dataType = new ArrayList<>();

        if (SKU_ID_INDEX == 999) {
            columns.add("sku_id");
        }
        if (PRICE_INDEX == 999) {
            columns.add("price");
        }
        if (CATE_INDEX == 999) {
            columns.add("cate");
        }
        if (CATENAME_INDEX == 999) {
            columns.add("cateName");
        }
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            errorMap.put("isEmpty", false);
            Cell skuId = row.getCell(SKU_ID_INDEX);
            Cell price = row.getCell(PRICE_INDEX);
            Cell category = row.getCell(CATE_INDEX);
            Cell cateName=row.getCell(CATENAME_INDEX);

            if (headerRow.getCell(SKU_ID_INDEX) != null) {
                if (skuId == null&&!columnsNan.contains("sku_id")) {
                    columnsNan.add("sku_id");
                } else {
                    if (skuId.getCellType()!=CellType.NUMERIC&&!dataType.contains("sku_id")) {
                        dataType.add("sku_id");
                    }
                }
            }
            if (headerRow.getCell(PRICE_INDEX) != null) {
                if (price == null&&!columnsNan.contains("price")) {
                    columnsNan.add("price");
                } else {
                    if (price.getCellType()!=CellType.NUMERIC&&!dataType.contains("price")) {
                        dataType.add("price");
                    }
                }
            }
            if (headerRow.getCell(CATE_INDEX) != null) {
                if (category == null&&!columnsNan.contains("cate")) {
                    columnsNan.add("cate");
                } else {
                    if (category.getCellType()!=CellType.NUMERIC&&!dataType.contains("cate")) {
                        dataType.add("cate");
                    }
                }
            }
            if (headerRow.getCell(CATENAME_INDEX) != null) {
                if (cateName == null&&!columnsNan.contains("cateName")) {
                    columnsNan.add("cateName");
                } else {
                    if (cateName.getCellType()!=CellType.STRING&&!dataType.contains("cateName")) {
                        dataType.add("cateName");
                    }
                }
            }

        }

        errorMap.put("columns", columns);
        errorMap.put("columnsNan", columnsNan);
        errorMap.put("dataType", dataType);
    }

    private void processCommentSheet(Workbook workbook, Map<String, Object> errorMap) {
        errorMap.put("isExist", true);
        Sheet sheet = workbook.getSheetAt(0);
        int USER_ID_INDEX = 999;
        int ORDER_ID_INDEX = 999;
        int SCORE_INDEX = 999;

        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            errorMap.put("isEmpty", true);
        }
        for(int i=1;i<=3;i++){
            String str=getCellValueAsString(headerRow.getCell(i));
            if (str!=null&&str.equals("user_id")) {
                USER_ID_INDEX=i;
            }
            else if (str!=null&&str.equals("o_id")) {
                ORDER_ID_INDEX=i;
            }
            else if (str!=null&&str.equals("score")) {
                SCORE_INDEX=i;
            }
        }
        List<String> columns = new ArrayList<>();
        List<String> columnsNan = new ArrayList<>();
        List<String> dataType = new ArrayList<>();

        if (USER_ID_INDEX == 999) {
            columns.add("user_id");
        }
        if (ORDER_ID_INDEX== 999) {
            columns.add("o_id");
        }
        if (SCORE_INDEX == 999) {
            columns.add("score");
        }

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            errorMap.put("isEmpty", false);
            Cell userId = row.getCell(USER_ID_INDEX);
            Cell orderId = row.getCell(ORDER_ID_INDEX);
            Cell score = row.getCell(SCORE_INDEX);

            if (headerRow.getCell(USER_ID_INDEX) != null) {
                if (userId == null&&!columnsNan.contains("user_id")) {
                    columnsNan.add("user_id");
                } else {
                    if (userId.getCellType()!=CellType.NUMERIC&&!dataType.contains("user_id")) {
                        dataType.add("user_id");
                    }
                }
            }
            if (headerRow.getCell(ORDER_ID_INDEX) != null) {
                if (orderId == null&&!columnsNan.contains("o_id")) {
                    columnsNan.add("o_id");
                } else {
                    if (orderId.getCellType()!=CellType.NUMERIC&&!dataType.contains("o_id")) {
                        dataType.add("o_id");
                    }
                }
            }
            if (headerRow.getCell(SCORE_INDEX) != null) {
                if (score == null&&!columnsNan.contains("score")) {
                    columnsNan.add("score");
                } else {
                    if (score.getCellType()!=CellType.NUMERIC&&!dataType.contains("score")) {
                        dataType.add("score");
                    }
                }
            }
        }

        errorMap.put("columns", columns);
        errorMap.put("columnsNan", columnsNan);
        errorMap.put("dataType", dataType);
    }

    private void processActionSheetCSV(InputStream inputStream, Map<String, Object> errorMap) {
        errorMap.put("isExist", true);
        try {
            Reader reader = new InputStreamReader(inputStream);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

            int USER_ID_INDEX = -1;
            int USERNAME_INDEX = -1;
            int SKU_ID_INDEX = -1;
            int DATE_INDEX = -1;
            int NUM_INDEX = -1;

            CSVRecord headerRow = records.iterator().next();
            for (int i = 0; i < headerRow.size(); i++) {
                String header = headerRow.get(i);
                if (header.equalsIgnoreCase("user_id")) {
                    USER_ID_INDEX = i;
                } else if (header.equalsIgnoreCase("userName")) {
                    USERNAME_INDEX = i;
                } else if (header.equalsIgnoreCase("sku_id")) {
                    SKU_ID_INDEX = i;
                } else if (header.equalsIgnoreCase("date")) {
                    DATE_INDEX = i;
                } else if (header.equalsIgnoreCase("num")) {
                    NUM_INDEX = i;
                }
            }

            List<String> columns = new ArrayList<>();
            List<String> columnsNan = new ArrayList<>();
            List<String> dataType = new ArrayList<>();

            if (headerRow.size() == 0) {
                errorMap.put("isEmpty", true);
                reader.close();
                return;
            } else {
                errorMap.put("isEmpty", false);
            }

            if (USER_ID_INDEX == -1) {
                columns.add("user_id");
            }
            if (USERNAME_INDEX == -1) {
                columns.add("userName");
            }
            if (SKU_ID_INDEX == -1) {
                columns.add("sku_id");
            }
            if (DATE_INDEX == -1) {
                columns.add("date");
            }
            if (NUM_INDEX == -1) {
                columns.add("num");
            }
            for (CSVRecord record : records) {
                String userId = USER_ID_INDEX != -1 ? record.get(USER_ID_INDEX) : null;
                String userName = USERNAME_INDEX != -1 ? record.get(USERNAME_INDEX) : null;
                String skuId = SKU_ID_INDEX != -1 ? record.get(SKU_ID_INDEX) : null;
                String date = DATE_INDEX != -1 ? record.get(DATE_INDEX) : null;
                String num = NUM_INDEX != -1 ? record.get(NUM_INDEX) : null;
                if (userId == null && !columnsNan.contains("user_id")) {
                    columnsNan.add("user_id");
                } else if (userId != null && !userId.matches("\\d+") && !dataType.contains("user_id")) {
                    dataType.add("user_id");
                }

                if (userName == null && !columnsNan.contains("userName")) {
                    columnsNan.add("userName");
                } else if (userName != null && !userName.matches("^[\\u4e00-\\u9fa5a-zA-Z]+$") && !dataType.contains("userName")) {
                    dataType.add("userName");
                }

                if (skuId == null && !columnsNan.contains("sku_id")) {
                    columnsNan.add("sku_id");
                } else if (skuId != null && !skuId.matches("\\d+(\\.\\d+)?") && !dataType.contains("sku_id")) {
                    dataType.add("sku_id");
                }

                if (date == null && !columnsNan.contains("date")) {
                    columnsNan.add("date");
                } else if (date != null && !isDate(date) && !dataType.contains("date")) {
                    dataType.add("date");
                }

                if (num == null && !columnsNan.contains("num")) {
                    columnsNan.add("num");
                } else if (num != null && !num.matches("\\d+") && !dataType.contains("num")) {
                    dataType.add("num");
                }
            }

            reader.close();
            errorMap.put("columns", columns);
            errorMap.put("columnsNan", columnsNan);
            errorMap.put("dataType", dataType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void processOrderSheetCSV(InputStream inputStream, Map<String, Object> errorMap) {
        errorMap.put("isExist", true);
        try {
            // Read the CSV data and parse it using Apache Commons CSV
            Reader reader = new InputStreamReader(inputStream);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

            int USER_ID_INDEX = -1;
            int USERNAME_INDEX = -1;
            int SKU_ID_INDEX = -1;
            int ORDER_ID_INDEX = -1;
            int DATE_INDEX = -1;
            int AREA_INDEX = -1;
            int AREANAME_INDEX = -1;
            int NUM_INDEX = -1;
            int PAYTYPE_INDEX = -1;

            CSVRecord headerRow = records.iterator().next();
            for (int i = 0; i < headerRow.size(); i++) {
                String header = headerRow.get(i);
                if (header.equalsIgnoreCase("user_id")) {
                    USER_ID_INDEX = i;
                } else if (header.equalsIgnoreCase("userName")) {
                    USERNAME_INDEX = i;
                } else if (header.equalsIgnoreCase("sku_id")) {
                    SKU_ID_INDEX = i;
                } else if (header.equalsIgnoreCase("o_id")) {
                    ORDER_ID_INDEX = i;
                } else if (header.equalsIgnoreCase("date")) {
                    DATE_INDEX = i;
                } else if (header.equalsIgnoreCase("area")) {
                    AREA_INDEX = i;
                } else if (header.equalsIgnoreCase("areaName")) {
                    AREANAME_INDEX = i;
                } else if (header.equalsIgnoreCase("num")) {
                    NUM_INDEX = i;
                } else if (header.equalsIgnoreCase("payType")) {
                    PAYTYPE_INDEX = i;
                }
            }

            List<String> columns = new ArrayList<>();
            List<String> columnsNan = new ArrayList<>();
            List<String> dataType = new ArrayList<>();

            if (headerRow.size() == 0) {
                errorMap.put("isEmpty", true);
                reader.close();
                return;
            } else {
                errorMap.put("isEmpty", false);
            }

            if (USER_ID_INDEX == -1) {
                columns.add("user_id");
            }
            if (USERNAME_INDEX == -1) {
                columns.add("userName");
            }
            if (SKU_ID_INDEX == -1) {
                columns.add("sku_id");
            }
            if (ORDER_ID_INDEX == -1) {
                columns.add("o_id");
            }
            if (DATE_INDEX == -1) {
                columns.add("date");
            }
            if (AREA_INDEX == -1) {
                columns.add("area");
            }
            if (AREANAME_INDEX == -1) {
                columns.add("areaName");
            }
            if (NUM_INDEX == -1) {
                columns.add("num");
            }
            if (PAYTYPE_INDEX == -1) {
                columns.add("payType");
            }

            for (CSVRecord record : records) {
                String userId = USER_ID_INDEX != -1 ? record.get(USER_ID_INDEX) : null;
                String userName = USERNAME_INDEX != -1 ? record.get(USERNAME_INDEX) : null;
                String skuId = SKU_ID_INDEX != -1 ? record.get(SKU_ID_INDEX) : null;
                String orderId = ORDER_ID_INDEX != -1 ? record.get(ORDER_ID_INDEX) : null;
                String date = DATE_INDEX != -1 ? record.get(DATE_INDEX) : null;
                String area = AREA_INDEX != -1 ? record.get(AREA_INDEX) : null;
                String areaName = AREANAME_INDEX != -1 ? record.get(AREANAME_INDEX) : null;
                String num = NUM_INDEX != -1 ? record.get(NUM_INDEX) : null;
                String payType = PAYTYPE_INDEX != -1 ? record.get(PAYTYPE_INDEX) : null;

                if (userId == null && !columnsNan.contains("user_id")) {
                    columnsNan.add("user_id");
                } else if (userId != null && !userId.matches("\\d+") && !dataType.contains("user_id")) {
                    dataType.add("user_id");
                }

                if (userName == null && !columnsNan.contains("userName")) {
                    columnsNan.add("userName");
                } else if (userName != null && !userName.matches("^[\\u4e00-\\u9fa5a-zA-Z]+$") && !dataType.contains("userName")) {
                    dataType.add("userName");
                }

                if (skuId == null && !columnsNan.contains("sku_id")) {
                    columnsNan.add("sku_id");
                } else if (skuId != null && !skuId.matches("\\d+") && !dataType.contains("sku_id")) {
                    dataType.add("sku_id");
                }

                if (orderId == null && !columnsNan.contains("o_id")) {
                    columnsNan.add("o_id");
                } else if (orderId != null && !orderId.matches("\\d+") && !dataType.contains("o_id")) {
                    dataType.add("o_id");
                }

                if (date == null && !columnsNan.contains("date")) {
                    columnsNan.add("date");
                } else if (date != null && !isDate(date) && !dataType.contains("date")) {
                    dataType.add("date");
                }

                if (area == null && !columnsNan.contains("area")) {
                    columnsNan.add("area");
                } else if (area != null && !area.matches("\\d+") && !dataType.contains("area")) {
                    dataType.add("area");
                }

                if (areaName == null && !columnsNan.contains("areaName")) {
                    columnsNan.add("areaName");
                } else if (areaName != null && !areaName.matches("^[\\u4e00-\\u9fa5a-zA-Z]+$") && !dataType.contains("areaName")) {
                    dataType.add("areaName");
                }

                if (num == null && !columnsNan.contains("num")) {
                    columnsNan.add("num");
                } else if (num != null && !num.matches("\\d+") && !dataType.contains("num")) {
                    dataType.add("num");
                }

                if (payType == null && !columnsNan.contains("payType")) {
                    columnsNan.add("payType");
                } else if (payType != null && !payType.matches("^[\\u4e00-\\u9fa5a-zA-Z]+$") && !dataType.contains("payType")) {
                    dataType.add("payType");
                }
            }

            reader.close();
            errorMap.put("columns", columns);
            errorMap.put("columnsNan", columnsNan);
            errorMap.put("dataType", dataType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processSkuSheetCSV(InputStream inputStream, Map<String, Object> errorMap) {
        errorMap.put("isExist", true);
        try {
            // Read the CSV data and parse it using Apache Commons CSV
            Reader reader = new InputStreamReader(inputStream);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

            int SKU_ID_INDEX = -1;
            int PRICE_INDEX = -1;
            int CATE_INDEX = -1;
            int CATENAME_INDEX = -1;

            CSVRecord headerRow = records.iterator().next();
            for (int i = 0; i < headerRow.size(); i++) {
                String header = headerRow.get(i);
                if (header != null && header.equalsIgnoreCase("sku_id")) {
                    SKU_ID_INDEX = i;
                } else if (header != null && header.equalsIgnoreCase("price")) {
                    PRICE_INDEX = i;
                } else if (header != null && header.equalsIgnoreCase("cate")) {
                    CATE_INDEX = i;
                } else if (header != null && header.equalsIgnoreCase("cateName")) {
                    CATENAME_INDEX = i;
                }
            }

            List<String> columns = new ArrayList<>();
            List<String> columnsNan = new ArrayList<>();
            List<String> dataType = new ArrayList<>();

            if (headerRow.size() == 0) {
                errorMap.put("isEmpty", true);
                reader.close();
                return;
            } else {
                errorMap.put("isEmpty", false);
            }

            if (SKU_ID_INDEX == -1) {
                columns.add("sku_id");
            }
            if (PRICE_INDEX == -1) {
                columns.add("price");
            }
            if (CATE_INDEX == -1) {
                columns.add("cate");
            }
            if (CATENAME_INDEX == -1) {
                columns.add("cateName");
            }
            for (CSVRecord record : records) {
                String skuId = SKU_ID_INDEX != -1 ? record.get(SKU_ID_INDEX) : null;
                String price = PRICE_INDEX != -1 ? record.get(PRICE_INDEX) : null;
                String category = CATE_INDEX != -1 ? record.get(CATE_INDEX) : null;
                String cateName = CATENAME_INDEX != -1 ? record.get(CATENAME_INDEX) : null;

                if (skuId == null && !columnsNan.contains("sku_id")) {
                    columnsNan.add("sku_id");
                } else if (skuId != null && !skuId.matches("\\d+") && !dataType.contains("sku_id")) {
                    dataType.add("sku_id");
                }

                if (price == null && !columnsNan.contains("price")) {
                    columnsNan.add("price");
                } else if (price != null && !price.matches("\\d+(\\.\\d+)?") && !dataType.contains("price")) {
                    dataType.add("price");
                }

                if (category == null && !columnsNan.contains("cate")) {
                    columnsNan.add("cate");
                } else if (category != null && !category.matches("\\d+") && !dataType.contains("cate")) {
                    dataType.add("cate");
                }

                if (cateName == null && !columnsNan.contains("cateName")) {
                    columnsNan.add("cateName");
                }
                else if (cateName != null && !cateName.matches("^[\\u4e00-\\u9fa5a-zA-Z]+$") && !dataType.contains("cateName")) {
                    dataType.add("cateName");
                }
            }

            reader.close();
            errorMap.put("columns", columns);
            errorMap.put("columnsNan", columnsNan);
            errorMap.put("dataType", dataType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processCommentSheetCSV(InputStream inputStream, Map<String, Object> errorMap) {
        errorMap.put("isExist", true);
        try {
            // Read the CSV data and parse it using Apache Commons CSV
            Reader reader = new InputStreamReader(inputStream);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            int USER_ID_INDEX = -1;
            int ORDER_ID_INDEX = -1;
            int SCORE_INDEX = -1;

            CSVRecord headerRow = records.iterator().next();
            for (int i = 0; i < headerRow.size(); i++) {
                String header = headerRow.get(i);
                if (header != null && header.equalsIgnoreCase("user_id")) {
                    USER_ID_INDEX = i;
                } else if (header != null && header.equalsIgnoreCase("o_id")) {
                    ORDER_ID_INDEX = i;
                } else if (header != null && header.equalsIgnoreCase("score")) {
                    SCORE_INDEX = i;
                }
            }

            List<String> columns = new ArrayList<>();
            List<String> columnsNan = new ArrayList<>();
            List<String> dataType = new ArrayList<>();

            if (headerRow.size() == 0) {
                errorMap.put("isEmpty", true);
                reader.close();
                return;
            } else {
                errorMap.put("isEmpty", false);
            }

            if (USER_ID_INDEX == -1) {
                columns.add("user_id");
            }
            if (ORDER_ID_INDEX == -1) {
                columns.add("o_id");
            }
            if (SCORE_INDEX == -1) {
                columns.add("score");
            }

            for (CSVRecord record : records) {
                String userId = USER_ID_INDEX != -1 ? record.get(USER_ID_INDEX) : null;
                String orderId = ORDER_ID_INDEX != -1 ? record.get(ORDER_ID_INDEX) : null;
                String score = SCORE_INDEX != -1 ? record.get(SCORE_INDEX) : null;

                if (userId == null && !columnsNan.contains("user_id")) {
                    columnsNan.add("user_id");
                } else if (userId != null && !userId.matches("\\d+") && !dataType.contains("user_id")) {
                    dataType.add("user_id");
                }

                if (orderId == null && !columnsNan.contains("o_id")) {
                    columnsNan.add("o_id");
                } else if (orderId != null && !orderId.matches("\\d+") && !dataType.contains("o_id")) {
                    dataType.add("o_id");
                }

                if (score == null && !columnsNan.contains("score")) {
                    columnsNan.add("score");
                } else if (score != null && !score.matches("\\d+") && !dataType.contains("score")) {
                    dataType.add("score");
                }
            }

            reader.close();
            errorMap.put("columns", columns);
            errorMap.put("columnsNan", columnsNan);
            errorMap.put("dataType", dataType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> setErrorMap(boolean isExist, boolean isEmpty, List<String> columns, List<String> columnsNan, List<String> dataType) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("isExist", isExist);
        errorMap.put("isEmpty", isEmpty);
        errorMap.put("columns", columns);
        errorMap.put("columnsNan", columnsNan);
        errorMap.put("dataType", dataType);
        return errorMap;
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

    public static boolean isDate(String dateString) {
        List<String> dateFormats = Arrays.asList("yyyy/MM/dd", "yyyy-MM-dd");

        for (String dateFormat : dateFormats) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            sdf.setLenient(false);

            try {
                sdf.parse(dateString);
                return true;
            } catch (ParseException e) {
                // Dateformat does not match, continue to the next format
            }
        }
        return false;
    }
}
