package com.example.project.mapper;

import com.example.project.entity.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Mapper
public interface OrderMapper {
    class CreateTable{
        String url = "jdbc:mysql://localhost:3306/mydatabase";

        public void DB(String fileKey){
            try (Connection connection = DriverManager.getConnection(url, database.username, database.password);
                 Statement statement = connection.createStatement()) {

                String tableName =fileKey+ "_order";

                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "user_id INT NOT NULL," +
                        "sku_id INT NOT NULL," +
                        "o_id INT NOT NULL," +
                        "date DATE NOT NULL," +
                        "area INT NOT NULL," +
                        "num INT NOT NULL," +
                        "PRIMARY KEY (user_id, sku_id, o_id,date,area,num)"+
                        ")" ;

                statement.executeUpdate(createTableSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Insert("INSERT INTO ${tableName} (user_id, sku_id, o_id, date, area, num) " +
            "VALUES (#{order.user_id}, #{order.sku_id}, #{order.o_id}, #{order.date}, #{order.area}, #{order.num})")
    void addOrder(String tableName, File.Order order);

}
