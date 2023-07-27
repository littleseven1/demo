package com.example.project.service;

import com.example.project.filepath;
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
    public List<Map<String, Object>> processAndCheckFile(String fileKey) {
        String filePath = filepath.path + fileKey;
        List<String> fileNames = Arrays.asList("action.xlsx", "comment.xlsx", "order.xlsx", "sku.xlsx");

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (String fileName : fileNames) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("name", fileName.substring(0, fileName.lastIndexOf(".")));

            Map<String, Object> errorMap = setErrorMap(false, true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            try (FileInputStream fis = new FileInputStream(filePath);
                 ZipInputStream zipInputStream = new ZipInputStream(fis)) {

                ZipEntry entry;
                boolean isFileFound = false;

                while ((entry = zipInputStream.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        String entryName = entry.getName();
                        if (entryName.equals(fileName)) {
                            isFileFound = true;
                            Workbook workbook = WorkbookFactory.create(zipInputStream);

                            switch (entryName) {
                                case "action.xlsx" -> processActionSheet(workbook, errorMap);
                                case "order.xlsx" -> processOrderSheet(workbook, errorMap);
                                case "sku.xlsx" -> processSkuSheet(workbook, errorMap);
                                case "comment.xlsx" -> processCommentSheet(workbook, errorMap);
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
                throw new IllegalArgumentException("处理文件时发生错误。");
            }

            result.put("error", errorMap);
            resultList.add(result);
        }

        return resultList;
    }

    private void processActionSheet(Workbook workbook, Map<String, Object> errorMap) {
        errorMap.put("isExist", true);
        Sheet sheet = workbook.getSheetAt(0);
        final int USER_ID_INDEX = 0;
        final int SKU_ID_INDEX = 1;
        final int DATE_INDEX = 2;
        final int NUM_INDEX = 3;

        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            errorMap.put("isEmpty", true);
        }

        List<String> columns = new ArrayList<>();
        List<String> columnsNan = new ArrayList<>();
        List<String> dataType = new ArrayList<>();

        if (headerRow.getCell(USER_ID_INDEX) == null) {
            columns.add("user_id");
        }
        if (headerRow.getCell(SKU_ID_INDEX) == null) {
            columns.add("sku_id");
        }
        if (headerRow.getCell(DATE_INDEX) == null) {
            columns.add("date");
        }
        if (headerRow.getCell(NUM_INDEX) == null) {
            columns.add("num");
        }
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            errorMap.put("isEmpty", false);
            Cell userId = row.getCell(USER_ID_INDEX);
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
        final int USER_ID_INDEX = 0;
        final int SKU_ID_INDEX = 1;
        final int ORDER_ID_INDEX = 2;
        final int DATE_INDEX = 3;
        final int AREA_INDEX = 4;
        final int NUM_INDEX = 5;

        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            errorMap.put("isEmpty", true);
        }

        List<String> columns = new ArrayList<>();
        List<String> columnsNan = new ArrayList<>();
        List<String> dataType = new ArrayList<>();

        if (headerRow.getCell(USER_ID_INDEX) == null) {
            columns.add("user_id");
        }
        if (headerRow.getCell(SKU_ID_INDEX) == null) {
            columns.add("sku_id");
        }
        if (headerRow.getCell(ORDER_ID_INDEX) == null) {
            columns.add("o_id");
        }
        if (headerRow.getCell(DATE_INDEX) == null) {
            columns.add("date");
        }
        if (headerRow.getCell(AREA_INDEX) == null) {
            columns.add("area");
        }
        if (headerRow.getCell(NUM_INDEX) == null) {
            columns.add("num");
        }

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            errorMap.put("isEmpty", false);
            Cell userId = row.getCell(USER_ID_INDEX);
            Cell skuId = row.getCell(SKU_ID_INDEX);
            Cell orderId = row.getCell(ORDER_ID_INDEX);
            Cell date =row.getCell(DATE_INDEX);
            Cell area =row.getCell(AREA_INDEX);
            Cell num = row.getCell(NUM_INDEX);

            if (headerRow.getCell(USER_ID_INDEX) != null) {
                if (userId == null&&!columnsNan.contains("user_id")) {
                    columnsNan.add("user_id");
                } else {
                    if (userId.getCellType()!=CellType.NUMERIC&&!dataType.contains("user_id")) {
                        dataType.add("user_id");
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
            if (headerRow.getCell(NUM_INDEX) != null) {
                if (num == null&&!columnsNan.contains("num")) {
                    columnsNan.add("num");
                } else {
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

    private void processSkuSheet(Workbook workbook, Map<String, Object> errorMap) {
        errorMap.put("isExist", true);
        Sheet sheet = workbook.getSheetAt(0);
        final int SKU_ID_INDEX = 0;
        final int PRICE_INDEX = 1;
        final int CATE_INDEX = 2;

        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            errorMap.put("isEmpty", true);
        }

        List<String> columns = new ArrayList<>();
        List<String> columnsNan = new ArrayList<>();
        List<String> dataType = new ArrayList<>();

        if (headerRow.getCell(SKU_ID_INDEX) == null) {
            columns.add("sku_id");
        }
        if (headerRow.getCell(PRICE_INDEX) == null) {
            columns.add("price");
        }
        if (headerRow.getCell(CATE_INDEX) == null) {
            columns.add("cate");
        }

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            errorMap.put("isEmpty", false);
            Cell skuId = row.getCell(SKU_ID_INDEX);
            Cell price = row.getCell(PRICE_INDEX);
            Cell category = row.getCell(CATE_INDEX);

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

        }

        errorMap.put("columns", columns);
        errorMap.put("columnsNan", columnsNan);
        errorMap.put("dataType", dataType);
    }

    private void processCommentSheet(Workbook workbook, Map<String, Object> errorMap) {
        errorMap.put("isExist", true);
        Sheet sheet = workbook.getSheetAt(0);
        final int USER_ID_INDEX = 0;
        final int ORDER_ID_INDEX = 1;
        final int SCORE_INDEX = 2;

        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            errorMap.put("isEmpty", true);
        }

        List<String> columns = new ArrayList<>();
        List<String> columnsNan = new ArrayList<>();
        List<String> dataType = new ArrayList<>();

        if (headerRow.getCell(USER_ID_INDEX) == null) {
            columns.add("user_id");
        }
        if (headerRow.getCell(ORDER_ID_INDEX) == null) {
            columns.add("o_id");
        }
        if (headerRow.getCell(SCORE_INDEX) == null) {
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

    private Map<String, Object> setErrorMap(boolean isExist, boolean isEmpty, List<String> columns, List<String> columnsNan, List<String> dataType) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("isExist", isExist);
        errorMap.put("isEmpty", isEmpty);
        errorMap.put("columns", columns);
        errorMap.put("columnsNan", columnsNan);
        errorMap.put("dataType", dataType);
        return errorMap;
    }
}
