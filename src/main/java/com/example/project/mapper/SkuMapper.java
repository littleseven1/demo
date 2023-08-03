package com.example.project.mapper;

import com.example.project.entity.SkuData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Mapper
public interface SkuMapper {
    class CreateTable{
        String url = "jdbc:mysql://localhost:3306/mydatabase";

        public void DB(String fileKey){
            try (Connection connection = DriverManager.getConnection(url, database.username, database.password);
                 Statement statement = connection.createStatement()) {

                String tableName ="sku_"+fileKey ;

                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                        "iindex INT PRIMARY KEY," +
                        "sku_id INT NOT NULL," +
                        "price double NOT NULL," +
                        "cate INT NOT NULL," +
                        "cateName VARCHAR(255) NOT NULL" +
                        ")" ;

                statement.executeUpdate(createTableSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Insert({
            "<script>",
            "INSERT INTO ${tableName} (iindex, sku_id, price, cate, cateName) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.iindex}, #{item.sku_id}, #{item.price}, #{item.cate}, #{item.cateName})",
            "</foreach>",
            "</script>"
    })
    void batchInsertSkus(@Param("tableName") String tableName, @Param("list") List<SkuData> skuDataList);

}
