package br.edu.ifrs.fintrack.dao;

import br.edu.ifrs.fintrack.exception.DatabaseConnectionException;
import br.edu.ifrs.fintrack.util.ConfigProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = ConfigProperties.getProperty("db.url");
    private static final String USER = ConfigProperties.getProperty("db.user");
    private static final String PASSWORD = ConfigProperties.getProperty("db.password");

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Erro ao estabelecer conexão com o banco de dados.", e);
        }
    }
}
