package com.example.project.mapper;

import com.example.project.entity.File.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO comment (user_id, o_id, score) " +
            "VALUES (#{user_id}, #{o_id}, #{score})")
    void insertComment(Comment comment);
}
