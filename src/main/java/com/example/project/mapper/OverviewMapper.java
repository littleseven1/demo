package com.example.project.mapper;

import com.example.project.entity.Overview;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Mapper
public interface OverviewMapper {

    class CreateTable{
        String url = "jdbc:mysql://localhost:3306/mydatabase";

        public void DB(String fileKey){
            try (Connection connection = DriverManager.getConnection(url, database.username, database.password);
                 Statement statement = connection.createStatement()) {

                String tableName =fileKey+ "_overview";
                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "fileKey VARCHAR(255) PRIMARY KEY,  " +
                        "description VARCHAR(255) NOT NULL," +
                        "date datetime NOT NULL," +
                        "model VARCHAR(255) DEFAULT NULL" +
                        ")" ;

                statement.executeUpdate(createTableSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Insert("INSERT INTO ${tableName} (fileKey, description,date) VALUES (#{overview.fileKey}, #{overview.description},#{overview.date})")
    void addOverview(String tableName, Overview overview);
}
