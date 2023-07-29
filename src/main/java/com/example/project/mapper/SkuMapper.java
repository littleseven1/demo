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
                        "iindex INT PRIMARY KEY," +
                        "sku_id INT NOT NULL," +
                        "price float NOT NULL," +
                        "cate INT NOT NULL," +
                        "cateName VARCHAR(255) NOT NULL" +
                        ")" ;

                statement.executeUpdate(createTableSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Insert("INSERT INTO ${tableName} (iindex,sku_id, price, cate,cateName) VALUES (#{iindex},#{sku_id}, #{price}, #{cate},#{cateName})")
    void addSku(@Param("tableName")String tableName,@Param("iindex") Integer iindex,@Param("sku_id") Integer sku_id, @Param("price")double price, @Param("cate")Integer cate,@Param("cateName")String cateName);

}
