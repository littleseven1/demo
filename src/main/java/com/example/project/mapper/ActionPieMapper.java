package com.example.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ActionPieMapper {
    @Select("SELECT COUNT(*) as count FROM (SELECT user_id, SUM(num) as total_num FROM action_${fileKey} GROUP BY user_id) AS total_records")
    int getTotalRecordCount(@Param("fileKey") String fileKey);

    @Select("SELECT COUNT(*) as count FROM (SELECT user_id, SUM(num) as total_num FROM action_${fileKey} GROUP BY user_id HAVING total_num > 1000) AS category1")
    int countCategory1(@Param("fileKey") String fileKey);

    @Select("SELECT COUNT(*) as count FROM (SELECT user_id, SUM(num) as total_num FROM action_${fileKey} GROUP BY user_id HAVING total_num BETWEEN 700 AND 1000) AS category2")
    int countCategory2(@Param("fileKey") String fileKey);

    @Select("SELECT COUNT(*) as count FROM (SELECT user_id, SUM(num) as total_num FROM action_${fileKey} GROUP BY user_id HAVING total_num BETWEEN 500 AND 700) AS category3")
    int countCategory3(@Param("fileKey") String fileKey);

    @Select("SELECT COUNT(*) as count FROM (SELECT user_id, SUM(num) as total_num FROM action_${fileKey} GROUP BY user_id HAVING total_num BETWEEN 300 AND 500) AS category4")
    int countCategory4(@Param("fileKey") String fileKey);

    @Select("SELECT COUNT(*) as count FROM (SELECT user_id, SUM(num) as total_num FROM action_${fileKey} GROUP BY user_id HAVING total_num BETWEEN 200 AND 300) AS category5")
    int countCategory5(@Param("fileKey") String fileKey);

    @Select("SELECT COUNT(*) as count FROM (SELECT user_id, SUM(num) as total_num FROM action_${fileKey} GROUP BY user_id HAVING total_num BETWEEN 100 AND 200) AS category6")
    int countCategory6(@Param("fileKey") String fileKey);

    @Select("SELECT COUNT(*) as count FROM (SELECT user_id, SUM(num) as total_num FROM action_${fileKey} GROUP BY user_id HAVING total_num < 100) AS category7")
    int countCategory7(@Param("fileKey") String fileKey);

}
