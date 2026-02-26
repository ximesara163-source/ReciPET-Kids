package com.utl.recipetkids.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionMySQL
{
    Connection conn;

    public Connection open() throws Exception
    {
        // Ruta de conexión a MySQL:
        String url      = "jdbc:mysql://127.0.0.1:3306/recipet_kids";
        String usuario  = "root";
        String password = "root";

        // Registramos el Driver de MySQL:
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Abrimos la conexión:
        conn = DriverManager.getConnection(url, usuario, password);

        return conn;
    }

    public void close() throws Exception
    {
        if (conn != null)
            conn.close();
    }
}