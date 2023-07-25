package com.example.project.mapper;

import com.example.project.entity.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO comment (fileKey, user_id, o_id, score) " +
            "VALUES (#{fileKey}, #{user_id}, #{o_id}, #{score})")
    void addComment(File.Comment comment);
}
