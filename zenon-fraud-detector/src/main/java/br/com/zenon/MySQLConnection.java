package br.com.zenon;


import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLConnection {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String URL = dotenv.get("MYSQL_URL");
    private static final String USERNAME = dotenv.get("MYSQL_USER");
    private static final String PASSWORD = dotenv.get("MYSQL_ROOT_PASSWORD");

    public static Connection getConnection() throws SQLException{
        if (URL == null || USERNAME == null || PASSWORD == null){
            throw new IllegalStateException(
                    "As variáveis de ambiente do MySQL não foram configuradas.");
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }


}
