package com.example.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;
@Mapper
public interface SkuScatterMapper {
    @Select("SELECT sku_id, price, cateName FROM ${tableName}")
    List<Map<String, Object>> findSkuDetails(@Param("tableName") String tableName);
}

