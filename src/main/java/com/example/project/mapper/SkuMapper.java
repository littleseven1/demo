package com.example.project.mapper;

import com.example.project.entity.File.Sku;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SkuMapper {

    @Insert("INSERT INTO sku (sku_id, price, cate) " +
            "VALUES (#{sku_id}, #{price}, #{cate})")
    void insertSku(Sku sku);
}
