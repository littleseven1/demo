package com.example.project.mapper;

import com.example.project.entity.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Mapper
public interface ActionMapper {

    class CreateTable{
        String url = "jdbc:mysql://localhost:3306/mydatabase";

        public void DB(String fileKey){
            try (Connection connection = DriverManager.getConnection(url, database.username, database.password);
                 Statement statement = connection.createStatement()) {

                String tableName = fileKey + "_action";

                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "user_id INT NOT NULL," +
                        "sku_id INT NOT NULL," +
                        "date DATE NOT NULL," +
                        "num INT NOT NULL," +
                        "PRIMARY KEY (user_id, sku_id,date,num)"+
                        " )";

                statement.executeUpdate(createTableSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Insert("INSERT INTO ${tableName} (user_id, sku_id, date, num)"+" VALUES (#{action.user_id}, #{action.sku_id}, #{action.date}, #{action.num})")
    void addAction(String tableName, File.Action action);
}
