package com.example.project.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Mapper
public interface CommentMapper {
    class CreateTable{
        String url = "jdbc:mysql://localhost:3306/mydatabase";

        public void DB(String fileKey){
            try (Connection connection = DriverManager.getConnection(url, database.username, database.password);
                 Statement statement = connection.createStatement()) {

                String tableName = "comment_"+fileKey;

                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        " user_id INT NOT NULL," +
                        " o_id INT NOT NULL," +
                        " score INT NOT NULL," +
                        " PRIMARY KEY (user_id, o_id,score) "+
                        ")" ;

                statement.executeUpdate(createTableSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Insert("INSERT INTO ${tableName} (user_id, o_id, score) " +
            "VALUES (#{user_id}, #{o_id}, #{score})")
    void addComment(@Param("tableName")String tableName,@Param("user_id") Integer user_id, @Param("o_id")Integer o_id, @Param("score")Integer score);
}
