package com.example.project.mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

@Mapper
public interface ActionMapper {

    class CreateTable{
        String url = "jdbc:mysql://localhost:3306/mydatabase";

        public void DB(String fileKey){
            try (Connection connection = DriverManager.getConnection(url, database.username, database.password);
                 Statement statement = connection.createStatement()) {

                String tableName ="action_" +fileKey  ;

                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "iindex INT PRIMARY KEY," +
                        "user_id INT NOT NULL," +
                        "userName VARCHAR(255) NOT NULL," +
                        "sku_id INT NOT NULL," +
                        "date DATE NOT NULL," +
                        "num INT NOT NULL" +
                        " )";

                statement.executeUpdate(createTableSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Insert("INSERT INTO ${tableName} (iindex,user_id, userName,sku_id, date, num)"+" VALUES (#{iindex},#{user_id}, #{userName},#{sku_id}, #{date}, #{num})")
    void addAction(@Param("tableName") String tableName,@Param("iindex") Integer iindex,@Param("user_id") Integer user_id,@Param("userName") String userName,@Param("sku_id") Integer sku_id, @Param("date") Date date,@Param("num") Integer num);
}
