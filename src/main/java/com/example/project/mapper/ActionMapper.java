package com.example.project.mapper;

import com.example.project.entity.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActionMapper {

    @Insert("INSERT INTO action (fileKey, user_id, sku_id, date, num) " +
            "VALUES (#{fileKey}, #{user_id}, #{sku_id}, #{date}, #{num})")
    void addAction(File.Action action);
}
