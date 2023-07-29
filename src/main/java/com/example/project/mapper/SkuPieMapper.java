package com.example.project.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SkuPieMapper {
    @Select("SELECT COUNT(*) FROM sku_${fileKey}")
    int getTotalRecordCount(@Param("fileKey") String fileKey);

    @Select("SELECT COUNT(*) as count FROM sku_${fileKey} WHERE cateName = '数码家电'")
    int countCategory1(@Param("fileKey") String fileKey);

    @Select("SELECT COUNT(*) as count FROM sku_${fileKey} WHERE cateName = '生活用品'")
    int countCategory2(@Param("fileKey") String fileKey);

    @Select("SELECT COUNT(*) as count FROM sku_${fileKey} WHERE cateName = '虚拟物品'")
    int countCategory3(@Param("fileKey") String fileKey);

    @Select("SELECT COUNT(*) as count FROM sku_${fileKey} WHERE cateName = '服装鞋帽'")
    int countCategory4(@Param("fileKey") String fileKey);

    @Select("SELECT COUNT(*) as count FROM sku_${fileKey} WHERE cateName = '家居用品'")
    int countCategory5(@Param("fileKey") String fileKey);

    @Select("SELECT COUNT(*) as count FROM sku_${fileKey} WHERE cateName = '办公用品'")
    int countCategory6(@Param("fileKey") String fileKey);
}
