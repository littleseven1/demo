package com.example.project.service;

import com.example.project.entity.File;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class FileProcessService {
    @Autowired(required = false)
    public String processAndCheckFile(String fileKey) {
        String filePath ="E:/la/"+fileKey;

        if (filePath == null) {
            return "Error: File not found.";
        }

        try (FileInputStream fis = new FileInputStream(filePath);
             ZipInputStream zipInputStream = new ZipInputStream(fis)) {

            ZipEntry entry;
            StringBuilder errors = new StringBuilder();

            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String fileName = entry.getName();
                    if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                        Workbook workbook = WorkbookFactory.create(zipInputStream);

                        switch (fileName) {
                            case "action.xlsx" -> {
                                String result = processActionSheet(workbook);

                                if (!result.isEmpty()) {
                                    errors.append("Error in file: ").append(fileName).append("\n").append(result);
                                }
                            }
                            case "order.xlsx" -> {
                                String result = processOrderSheet(workbook);
                                if (!result.isEmpty()) {
                                    errors.append("Error in file: ").append(fileName).append("\n").append(result);
                                }
                            }
                            case "sku.xlsx" -> {
                                String result = processSkuSheet(workbook);
                                if (!result.isEmpty()) {
                                    errors.append("Error in file: ").append(fileName).append("\n").append(result);
                                }
                            }
                            case "comment.xlsx" -> {
                                String result = processCommentSheet(workbook);
                                if (!result.isEmpty()) {
                                    errors.append("Error in file: ").append(fileName).append("\n").append(result);
                                }
                            }
                        }
                    }
                }
                zipInputStream.closeEntry();
            }

            if (errors.length() > 0) {
                return errors.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "Error while processing files.";
        }

        return "File processing completed successfully.";
    }

    private String processActionSheet(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        List<File.Action> actions = new ArrayList<>();
        StringBuilder errors = new StringBuilder();
        // 定义表格中的属性列索引
        final int USER_ID_INDEX = 0;
        final int SKU_ID_INDEX = 1;
        final int DATE_INDEX = 2;
        final int NUM_INDEX = 3;

        // 检查表格属性是否都存在
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            errors.append("Error in action: Header row is missing.\n");
            return errors.toString();
        }

        if (headerRow.getCell(USER_ID_INDEX) == null) {
            errors.append("Error in action: 'user_id' attribute is missing.\n");
        }
        if (headerRow.getCell(SKU_ID_INDEX) == null) {
            errors.append("Error in action: 'sku_id' attribute is missing.\n");
        }
        if (headerRow.getCell(DATE_INDEX) == null) {
            errors.append("Error in action: 'date' attribute is missing.\n");
        }
        if (headerRow.getCell(NUM_INDEX) == null) {
            errors.append("Error in action: 'num' attribute is missing.\n");
        }

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;

            Integer userId = getCellValueAsInteger(row.getCell(0));
            Integer skuId = getCellValueAsInteger(row.getCell(1));
            Date date = getCellValueAsDate(row.getCell(2));
            Integer num = getCellValueAsInteger(row.getCell(3));

            // 检查属性是否为空
            if (userId == null || skuId == null || date == null || num == null) {
                errors.append("Error in action: Row ").append(row.getRowNum() + 1).append(" - Missing attributes.\n");
            }

            // 检查数据类型是否正确
            if (!isValidDataType(userId, Integer.class) || !isValidDataType(skuId, Integer.class) ||
                    !isValidDataType(date, Date.class) || !isValidDataType(num, Integer.class)) {
                errors.append("Error in action: Row ").append(row.getRowNum() + 1).append(" - Incorrect data type.\n");
            }

            File.Action action = new File.Action(userId, skuId, date, num);
            actions.add(action);
        }

        return errors.toString();
    }

    private String processOrderSheet(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        List<File.Order> orders = new ArrayList<>();
        StringBuilder errors = new StringBuilder();
// 定义表格中的属性列索引
        final int USER_ID_INDEX = 0;
        final int SKU_ID_INDEX = 1;
        final int ORDER_ID_INDEX = 2;
        final int DATE_INDEX = 3;
        final int AREA_INDEX = 4;
        final int NUM_INDEX = 5;

        // 检查表格属性是否都存在
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            errors.append("Error in order: Header row is missing.\n");
            return errors.toString();
        }

        if (headerRow.getCell(USER_ID_INDEX) == null) {
            errors.append("Error in order: 'user_id' attribute is missing.\n");
        }
        if (headerRow.getCell(SKU_ID_INDEX) == null) {
            errors.append("Error in order: 'sku_id' attribute is missing.\n");
        }
        if (headerRow.getCell(ORDER_ID_INDEX) == null) {
            errors.append("Error in order: 'o_id' attribute is missing.\n");
        }
        if (headerRow.getCell(DATE_INDEX) == null) {
            errors.append("Error in order: 'date' attribute is missing.\n");
        }
        if (headerRow.getCell(AREA_INDEX) == null) {
            errors.append("Error in order: 'area' attribute is missing.\n");
        }
        if (headerRow.getCell(NUM_INDEX) == null) {
            errors.append("Error in order: 'num' attribute is missing.\n");
        }

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;

            Integer userId = getCellValueAsInteger(row.getCell(0));
            Integer skuId = getCellValueAsInteger(row.getCell(1));
            Integer orderId = getCellValueAsInteger(row.getCell(2));
            Date date = getCellValueAsDate(row.getCell(3));
            Integer area = getCellValueAsInteger(row.getCell(4));
            Integer num = getCellValueAsInteger(row.getCell(5));

            // 检查属性是否为空
            if (userId == null || skuId == null || orderId == null || date == null || area == null || num == null) {
                errors.append("Error in order: Row ").append(row.getRowNum() + 1).append(" - Missing attributes.\n");
            }

            // 检查数据类型是否正确
            if (!isValidDataType(userId, Integer.class) || !isValidDataType(skuId, Integer.class) ||
                    !isValidDataType(orderId, Integer.class) || !isValidDataType(date, Date.class) ||
                    !isValidDataType(area, Integer.class) || !isValidDataType(num, Integer.class)) {
                errors.append("Error in order: Row ").append(row.getRowNum() + 1).append(" - Incorrect data type.\n");
            }

            File.Order order = new File.Order(userId, skuId, orderId, date, area, num);
            orders.add(order);
        }

        return errors.toString();
    }

    private String processSkuSheet(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        List<File.Sku> skus = new ArrayList<>();
        StringBuilder errors = new StringBuilder();
        // 定义表格中的属性列索引
        final int SKU_ID_INDEX = 0;
        final int PRICE_INDEX = 1;
        final int CATE_INDEX = 2;

        // 检查表格属性是否都存在
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            errors.append("Error in sku: Header row is missing.\n");
            return errors.toString();
        }

        if (headerRow.getCell(SKU_ID_INDEX) == null) {
            errors.append("Error in sku: 'sku_id' attribute is missing.\n");
        }
        if (headerRow.getCell(PRICE_INDEX) == null) {
            errors.append("Error in sku: 'price' attribute is missing.\n");
        }
        if (headerRow.getCell(CATE_INDEX) == null) {
            errors.append("Error in sku: 'cate' attribute is missing.\n");
        }
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row

            Integer skuId = getCellValueAsInteger(row.getCell(0));
            Integer price = getCellValueAsInteger(row.getCell(1));
            Integer category = getCellValueAsInteger(row.getCell(2));

            // 检查属性是否为空
            if (skuId == null || price == null || category == null) {
                errors.append("Error in sku: Row ").append(row.getRowNum() + 1).append(" - Missing attributes.\n");
            }

            // 检查数据类型是否正确
            if (!isValidDataType(skuId, Integer.class) || !isValidDataType(price, Integer.class) ||
                    !isValidDataType(category, Integer.class)) {
                errors.append("Error in sku: Row ").append(row.getRowNum() + 1).append(" - Incorrect data type.\n");
            }

            File.Sku sku = new File.Sku(skuId, price, category);
            skus.add(sku);
        }

        return errors.toString();
    }
    private String processCommentSheet(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        List<File.Comment> comments = new ArrayList<>();
        StringBuilder errors = new StringBuilder();
// 定义表格中的属性列索引
        final int USER_ID_INDEX = 0;
        final int ORDER_ID_INDEX = 1;
        final int SCORE_INDEX = 2;

        // 检查表格属性是否都存在
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            errors.append("Error in comment: Header row is missing.\n");
            return errors.toString();
        }

        if (headerRow.getCell(USER_ID_INDEX) == null) {
            errors.append("Error in comment: 'user_id' attribute is missing.\n");
        }
        if (headerRow.getCell(ORDER_ID_INDEX) == null) {
            errors.append("Error in comment: 'o_id' attribute is missing.\n");
        }
        if (headerRow.getCell(SCORE_INDEX) == null) {
            errors.append("Error in comment: 'score' attribute is missing.\n");
        }
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row

            Integer userId = getCellValueAsInteger(row.getCell(0));
            Integer orderId = getCellValueAsInteger(row.getCell(1));
            Integer score = getCellValueAsInteger(row.getCell(2));

            // 检查属性是否为空
            if (userId == null || orderId == null || score == null) {
                errors.append("Error in comment: Row ").append(row.getRowNum() + 1).append(" - Missing attributes.\n");
            }

            // 检查数据类型是否正确
            if (!isValidDataType(userId, Integer.class) || !isValidDataType(orderId, Integer.class) ||
                    !isValidDataType(score, Integer.class)) {
                errors.append("Error in comment: Row ").append(row.getRowNum() + 1).append(" - Incorrect data type.\n");
            }

            File.Comment comment = new File.Comment(userId, orderId, score);
            comments.add(comment);
        }

        return errors.toString();
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

    private boolean isValidDataType(Object value, Class<?> dataType) {
        return dataType.isInstance(value);
    }

    // 静态内部类，用于存储文件key与文件路径的映射关系
    private static class FileKeyStorage {
        private static final Map<String, String> fileKeyMap = new HashMap<>();

        public static void addFileKey(String fileKey, String filePath) {
            fileKeyMap.put(fileKey, filePath);
        }

        public static String getFilePath(String fileKey) {
            return fileKeyMap.get(fileKey);
        }
    }
}
