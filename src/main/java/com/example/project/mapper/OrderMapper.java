package com.example.project.mapper;

import com.example.project.entity.File.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO order_table (user_id, sku_id, o_id, date, area, num) " +
            "VALUES (#{user_id}, #{sku_id}, #{o_id}, #{date}, #{area}, #{num})")
    void insertOrder(Order order);
}
