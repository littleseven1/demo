package com.example.project.mapper;

import com.example.project.entity.OrderData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

@Mapper
public interface OrderMapper {
    class CreateTable{
        String url = "jdbc:mysql://localhost:3306/mydatabase";

        public void DB(String fileKey){
            try (Connection connection = DriverManager.getConnection(url, database.username, database.password);
                 Statement statement = connection.createStatement()) {

                String tableName ="order_"+fileKey;

                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "iindex INT PRIMARY KEY," +
                        "user_id INT NOT NULL," +
                        "userName VARCHAR(255) NOT NULL," +
                        "sku_id INT NOT NULL," +
                        "o_id INT NOT NULL," +
                        "date DATE NOT NULL," +
                        "area INT NOT NULL," +
                        "areaName VARCHAR(255) NOT NULL," +
                        "num INT NOT NULL," +
                        "payType VARCHAR(255) NOT NULL" +
                        ")" ;

                statement.executeUpdate(createTableSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Insert({
            "<script>",
            "INSERT INTO ${tableName} (iindex, user_id, userName, sku_id, o_id, date, area, areaName, num, payType) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.iindex}, #{item.user_id}, #{item.userName}, #{item.sku_id}, #{item.o_id}, #{item.date}, #{item.area}, #{item.areaName}, #{item.num}, #{item.payType})",
            "</foreach>",
            "</script>"
    })
    void batchInsertOrders(@Param("tableName") String tableName, @Param("list") List<OrderData> orderDataList);

}
