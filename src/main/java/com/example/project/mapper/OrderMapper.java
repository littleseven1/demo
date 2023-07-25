package com.example.project.mapper;

import com.example.project.entity.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orderr (fileKey, user_id, sku_id, o_id, date, area, num) " +
            "VALUES (#{fileKey}, #{user_id}, #{sku_id}, #{o_id}, #{date}, #{area}, #{num})")
    void addOrder(File.Order order);

}
