package com.example.project.mapper;

import com.example.project.entity.File.Action;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActionMapper {

    @Insert("INSERT INTO action (user_id, sku_id, date, num) " +
            "VALUES (#{user_id}, #{sku_id}, #{date}, #{num})")
    void insertAction(Action action);
}
