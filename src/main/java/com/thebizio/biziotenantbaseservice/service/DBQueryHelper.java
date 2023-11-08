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

    public boolean createDataBaseAndUser(String dbName, String username, String password) {
        try{
            createUser(username, password);
            createDB(dbName, username);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }

    public void createDB(String dbName, String username) throws SQLException {
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement ps2 = connection.prepareStatement(getCreateDBQuery(dbName, username));
        ) {
            ps2.executeUpdate();
        }
    }

    public void createUser(String username, String password) throws SQLException {
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(getCreateDBUserQuery(username, password));
        ){
            ps.executeUpdate();
        }
    }

    public String getCreateDBQuery(String dbName, String username) {
        // To Do - prevent SQL injection
        return "create database \"" + dbName + "\"  with owner = \"" + username + "\"";
    }

    public String getCreateDBUserQuery(String username, String password) {
        return "CREATE USER \""+ username +"\" WITH PASSWORD '"+ password +"'";
    }
}
