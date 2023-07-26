package com.example.project.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

                String tableName ="sku_"+fileKey ;

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
    @Insert("INSERT INTO ${tableName} (sku_id, price, cate) VALUES (#{sku_id}, #{price}, #{cate})")
    void addSku(@Param("tableName")String tableName,@Param("sku_id") Integer sku_id, @Param("price")Integer price, @Param("cate")Integer cate);

}
