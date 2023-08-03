package com.example.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ActionPieMapper {
    @Select("SELECT user_id, COUNT(*) as count FROM action_${fileKey} GROUP BY user_id")
    int getTotalRecordCount(@Param("fileKey") String fileKey);

    @Select("SELECT user_id, COUNT(*) as count FROM action_${fileKey} WHERE num > 1000 GROUP BY user_id")
    int countCategory1(@Param("fileKey") String fileKey);

    @Select("SELECT user_id, COUNT(*) as count FROM action_${fileKey} WHERE num BETWEEN 700 AND 1000 GROUP BY user_id ")
    int countCategory2(@Param("fileKey") String fileKey);

    @Select("SELECT user_id, COUNT(*) as count FROM action_${fileKey} WHERE num BETWEEN 500 AND 700 GROUP BY user_id")
    int countCategory3(@Param("fileKey") String fileKey);

    @Select("SELECT user_id, COUNT(*) as count FROM action_${fileKey} WHERE num BETWEEN 300 AND 500 GROUP BY user_id")
    int countCategory4(@Param("fileKey") String fileKey);

    @Select("SELECT user_id, COUNT(*) as count FROM action_${fileKey} WHERE num BETWEEN 200 AND 300 GROUP BY user_id")
    int countCategory5(@Param("fileKey") String fileKey);

    @Select("SELECT user_id, COUNT(*) as count FROM action_${fileKey} WHERE num BETWEEN 100 AND 200 GROUP BY user_id")
    int countCategory6(@Param("fileKey") String fileKey);

    @Select("SELECT user_id, COUNT(*) as count FROM action_${fileKey} WHERE num < 100 GROUP BY user_id")
    int countCategory7(@Param("fileKey") String fileKey);
}
