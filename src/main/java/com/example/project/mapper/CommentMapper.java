package com.example.project.mapper;

import com.example.project.entity.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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

                String tableName =fileKey+ "_comment";

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
            "VALUES (#{comment.user_id}, #{comment.o_id}, #{comment.score})")
    void addComment(String tableName, File.Comment comment);
}
