package com.example.project.mapper;


import com.example.project.entity.CommentData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Mapper
public interface CommentMapper {
    class CreateTable{
        String url = "jdbc:mysql://localhost:3306/mydatabase";

        public void DB(String fileKey){
            try (Connection connection = DriverManager.getConnection(url, database.username, database.password);
                 Statement statement = connection.createStatement()) {

                String tableName = "comment_"+fileKey;

                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "iindex INT PRIMARY KEY," +
                        " user_id INT NOT NULL," +
                        " o_id INT NOT NULL," +
                        " score INT NOT NULL" +
                        ")" ;

                statement.executeUpdate(createTableSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Insert({
            "<script>",
            "INSERT INTO ${tableName} (iindex, user_id, o_id, score) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.iindex}, #{item.user_id}, #{item.o_id}, #{item.score})",
            "</foreach>",
            "</script>"
    })
    void batchInsertComments(@Param("tableName") String tableName, @Param("list") List<CommentData> commentDataList);
}
