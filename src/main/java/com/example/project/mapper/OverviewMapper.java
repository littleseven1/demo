package com.example.project.mapper;

import com.example.project.entity.Overview;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OverviewMapper {

    @Insert("INSERT INTO overview (fileKey, description) VALUES (#{fileKey}, #{description})")
    void addOverview(Overview overview);
}
