package com.example.project.mapper;

import com.example.project.entity.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SkuMapper {

    @Insert("INSERT INTO sku (fileKey, sku_id, price, cate) " +
            "VALUES (#{fileKey}, #{sku_id}, #{price}, #{cate})")
    void addSku(File.Sku sku);

}
