package com.example.project.mapper;

import com.example.project.entity.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Mapper
public interface SkuMapper {
    class CreateTable{
        String url = "jdbc:mysql://localhost:3306/mydatabase";

        public void DB(String fileKey){
            try (Connection connection = DriverManager.getConnection(url, database.username, database.password);
                 Statement statement = connection.createStatement()) {

                String tableName =fileKey+ "_sku";

                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                        "sku_id INT NOT NULL," +
                        "price INT NOT NULL," +
                        "cate INT NOT NULL," +
                        "PRIMARY KEY (sku_id,price,cate) " +
                        ")" ;

                statement.executeUpdate(createTableSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Insert("INSERT INTO ${tableName} (sku_id, price, cate) VALUES (#{sku.sku_id}, #{sku.price}, #{sku.cate})")
    void addSku(String tableName, File.Sku sku);

}
