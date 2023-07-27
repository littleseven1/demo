package com.example.project.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface DeleteMapper {

    @Delete("DELETE FROM overview WHERE fileKey = #{fileKey}")
    void deleteFromOverview(String fileKey);

    @Delete("DROP TABLE IF EXISTS ${tableName}")
    void dropActionTable(String tableName);

    @Delete("DROP TABLE IF EXISTS ${tableName}")
    void dropCommentTable(String tableName);

    @Delete("DROP TABLE IF EXISTS ${tableName}")
    void dropOrderTable(String tableName);

    @Delete("DROP TABLE IF EXISTS ${tableName}")
    void dropSkuTable(String tableName);
}

