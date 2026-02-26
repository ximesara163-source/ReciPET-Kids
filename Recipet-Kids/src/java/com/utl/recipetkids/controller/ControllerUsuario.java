package com.utl.recipetkids.controller;

import com.utl.recipetkids.db.ConexionMySQL;
import com.utl.recipetkids.model.Maestro;
import com.utl.recipetkids.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ControllerUsuario {

    public Usuario validate(String nombreUsuario, String contrasenia) throws Exception {

        String sql = """
            SELECT * 
            FROM v_usuario 
            WHERE nombreUsuario = ? 
              AND contrasenia = ?
              AND rol = 'ADMIN'
        """;

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, nombreUsuario);
        pstmt.setString(2, contrasenia);

        ResultSet rs = pstmt.executeQuery();

        Usuario u = null;

        if (rs.next()) {
            u = fill(rs);
        }

        rs.close();
        pstmt.close();
        conn.close();

        return u;
    }

    private Usuario fill(ResultSet rs) throws Exception {

        Maestro m = new Maestro();
        m.setIdMaestro(rs.getInt("idMaestro"));
        m.setNombre(rs.getString("nombre"));
        m.setApellidoPat(rs.getString("apellidoPat"));
        m.setApellidoMat(rs.getString("apellidoMat"));
        m.setFechaNac(rs.getString("fechaNac"));
        m.setGenero(rs.getString("genero"));
        m.setTelefono(rs.getString("telefono"));
        m.setEmail(rs.getString("email"));
        m.setFechaAlta(rs.getString("fechaAlta"));
        m.setEstatus(rs.getInt("estatus"));

        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("idUsuario"));
        u.setNombreUsuario(rs.getString("nombreUsuario"));
        u.setContrasenia(rs.getString("contrasenia"));
        u.setRol(rs.getString("rol"));
        u.setMaestro(m);

        return u;
    }
}