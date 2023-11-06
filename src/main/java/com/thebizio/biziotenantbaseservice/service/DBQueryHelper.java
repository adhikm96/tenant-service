package com.thebizio.biziotenantbaseservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
@Slf4j
public class DBQueryHelper {
    @Autowired
    DataSource dataSource;

    public boolean createDataBase(String dbName) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(getCreateDBQuery(dbName));
        ) {
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }

    public String getCreateDBQuery(String dbName) {
        // To Do - prevent SQL injection
        return "create database " + dbName + "  with owner default";
    }
}
