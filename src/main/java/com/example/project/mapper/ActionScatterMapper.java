package com.example.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ActionScatterMapper {
    @Select("SELECT user_id, COUNT(*) as number FROM action_${fileKey} GROUP BY user_id")
    List<Map<String, Object>> countUserViews(@Param("fileKey") String fileKey);
}

