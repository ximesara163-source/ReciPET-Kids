package com.utl.recipetkids.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.utl.recipetkids.db.ConexionMySQL;
import com.utl.recipetkids.model.Maestro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class ControllerRegistro {

    Gson gson = new Gson();

    // ================= LISTAR =================
  public String listar(String fecha, String grado, String grupo) throws Exception {

    StringBuilder sql = new StringBuilder(
        "SELECT r.idRegistro, r.fechaPesaje, r.kilogramos, r.observaciones, " +
        "g.idGrupo, g.idGrado, " +
        "CONCAT(g.idGrado, '°', g.nombre) AS grupoCompleto, " +
        "IFNULL(m.nombre,'') AS maestro " +
        "FROM registro_pesaje r " +
        "INNER JOIN grupo g ON r.idGrupo = g.idGrupo " +
        "LEFT JOIN maestro m ON g.idMaestro = m.idMaestro " +
        "WHERE r.estatus = 1 "
    );

    if (fecha != null && !fecha.isEmpty()) {
        sql.append("AND r.fechaPesaje = ? ");
    }

    if (grado != null && !grado.isEmpty()) {
        sql.append("AND g.idGrado = ? ");
    }

    if (grupo != null && !grupo.isEmpty()) {
        sql.append("AND g.idGrupo = ? ");
    }

    sql.append("ORDER BY r.fechaPesaje DESC");

    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.open();
    PreparedStatement pstmt = conn.prepareStatement(sql.toString());

    int index = 1;

    if (fecha != null && !fecha.isEmpty()) {
        pstmt.setString(index++, fecha);
    }

    if (grado != null && !grado.isEmpty()) {
        pstmt.setInt(index++, Integer.parseInt(grado));
    }

    if (grupo != null && !grupo.isEmpty()) {
        pstmt.setInt(index++, Integer.parseInt(grupo));
    }

    ResultSet rs = pstmt.executeQuery();

    JsonArray lista = new JsonArray();

    while (rs.next()) {
        JsonObject obj = new JsonObject();
        obj.addProperty("idRegistro", rs.getInt("idRegistro"));
        obj.addProperty("fecha", rs.getString("fechaPesaje"));
        obj.addProperty("kilogramos", rs.getDouble("kilogramos"));
        obj.addProperty("observaciones", rs.getString("observaciones"));
        obj.addProperty("idGrupo", rs.getInt("idGrupo"));
        obj.addProperty("grado", rs.getInt("idGrado"));
        obj.addProperty("grupo", rs.getString("grupoCompleto"));
        obj.addProperty("maestro", rs.getString("maestro"));
        lista.add(obj);
    }

    rs.close();
    pstmt.close();
    conn.close();

    return gson.toJson(lista);
}

    // ================= ESTADISTICAS =================
   public String estadisticas() throws Exception {

    String sqlTotal =
        "SELECT IFNULL(SUM(kilogramos),0) AS total " +
        "FROM registro_pesaje WHERE estatus = 1";

    String sqlGrupos =
        "SELECT COUNT(DISTINCT idGrupo) AS grupos " +
        "FROM registro_pesaje WHERE estatus = 1";

    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.open();

    // TOTAL KG
    PreparedStatement ps1 = conn.prepareStatement(sqlTotal);
    ResultSet rs1 = ps1.executeQuery();
    rs1.next();
    double total = rs1.getDouble("total");

    // GRUPOS PARTICIPANTES (SOLO LOS QUE TIENEN REGISTROS)
    PreparedStatement ps2 = conn.prepareStatement(sqlGrupos);
    ResultSet rs2 = ps2.executeQuery();
    rs2.next();
    int grupos = rs2.getInt("grupos");

    JsonObject obj = new JsonObject();
    obj.addProperty("total", total);
    obj.addProperty("grupos", grupos);

    rs1.close();
    ps1.close();
    rs2.close();
    ps2.close();
    conn.close();

    return gson.toJson(obj);
}

    // ================= LISTAR GRUPOS =================
    public String listarGrupos() throws Exception {

    String sql =
        "SELECT g.idGrupo, g.idGrado, g.nombre, m.nombre AS maestro " +
        "FROM grupo g " +
        "INNER JOIN maestro m ON g.idMaestro = m.idMaestro " +
        "WHERE g.estatus = 1 " +
        "ORDER BY g.idGrado, g.nombre";

    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.open();
    PreparedStatement pstmt = conn.prepareStatement(sql);
    ResultSet rs = pstmt.executeQuery();

    JsonArray lista = new JsonArray();

    while (rs.next()) {

        JsonObject obj = new JsonObject();

        obj.addProperty("idGrupo", rs.getInt("idGrupo"));
        obj.addProperty("idGrado", rs.getInt("idGrado"));
        obj.addProperty("nombre", rs.getString("nombre"));
        obj.addProperty("maestro", rs.getString("maestro"));
        obj.addProperty("estatus", 1);

        lista.add(obj);
    }

    rs.close();
    pstmt.close();
    conn.close();

    return gson.toJson(lista);
}
    

    // ================= AGREGAR REGISTRO =================
    public String agregar(String body) throws Exception {

    JsonObject datos = gson.fromJson(body, JsonObject.class);

    String sql = "INSERT INTO registro_pesaje(fechaPesaje, kilogramos, idGrupo, observaciones, estatus) VALUES(?,?,?,?,1)";

    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.open();
    PreparedStatement pstmt = conn.prepareStatement(sql);

    pstmt.setString(1, datos.get("fechaPesaje").getAsString());
    pstmt.setDouble(2, datos.get("kilogramos").getAsDouble());
    pstmt.setInt(3, datos.get("idGrupo").getAsInt());
    pstmt.setString(4, datos.get("observaciones").getAsString());

    pstmt.executeUpdate();

    pstmt.close();
    conn.close();

    return "{\"result\":\"Registro agregado correctamente\"}";
}

    // ================= MODIFICAR =================
    public String modificar(String body) throws Exception {

    JsonObject datos = gson.fromJson(body, JsonObject.class);

    String sql = "UPDATE registro_pesaje SET fechaPesaje=?, kilogramos=?, idGrupo=?, observaciones=? WHERE idRegistro=?";

    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.open();
    PreparedStatement pstmt = conn.prepareStatement(sql);

    pstmt.setString(1, datos.get("fechaPesaje").getAsString());
    pstmt.setDouble(2, datos.get("kilogramos").getAsDouble());
    pstmt.setInt(3, datos.get("idGrupo").getAsInt());
    pstmt.setString(4, datos.get("observaciones").getAsString());
    pstmt.setInt(5, datos.get("idRegistro").getAsInt());

    pstmt.executeUpdate();

    pstmt.close();
    conn.close();

    return "{\"result\":\"Registro modificado correctamente\"}";
}

    // ================= ELIMINAR =================
    public String eliminar(String body) throws Exception {

    JsonObject datos = gson.fromJson(body, JsonObject.class);

    String sql = "UPDATE registro_pesaje SET estatus=0 WHERE idRegistro=?";

    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.open();
    PreparedStatement pstmt = conn.prepareStatement(sql);

    pstmt.setInt(1, datos.get("idRegistro").getAsInt());
    pstmt.executeUpdate();

    pstmt.close();
    conn.close();

    return "{\"result\":\"Registro eliminado correctamente\"}";
}

    // ================= AGREGAR GRUPO =================
   public String agregarGrupo(String body) throws Exception {

    JsonObject datos = gson.fromJson(body, JsonObject.class);

    int idGrado = datos.get("grado").getAsInt();
    String nombre = datos.get("nombre").getAsString();
    int idMaestro = datos.get("idMaestro").getAsInt();

    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.open();

    // 🔎 Verificar si ya existe
    String checkSql = "SELECT idGrupo FROM grupo WHERE idGrado=? AND nombre=? AND estatus=1";
    PreparedStatement checkStmt = conn.prepareStatement(checkSql);
    checkStmt.setInt(1, idGrado);
    checkStmt.setString(2, nombre);
    ResultSet rs = checkStmt.executeQuery();

    if (rs.next()) {
        int idExistente = rs.getInt("idGrupo");
        conn.close();
        return "{\"idGrupo\":" + idExistente + "}";
    }

    // Insertar si no existe
    String insertSql = "INSERT INTO grupo(idGrado, nombre, idMaestro, estatus) VALUES(?,?,?,1)";
    PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
    insertStmt.setInt(1, idGrado);
    insertStmt.setString(2, nombre);
    insertStmt.setInt(3, idMaestro);

    insertStmt.executeUpdate();

    ResultSet generatedKeys = insertStmt.getGeneratedKeys();
    generatedKeys.next();
    int idNuevo = generatedKeys.getInt(1);

    conn.close();

    return "{\"idGrupo\":" + idNuevo + "}";
}
   
public List<Maestro> listarMaestros() throws Exception {

    String sql = "SELECT * FROM maestro WHERE estatus = 1";

    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.open();

    PreparedStatement pstmt = conn.prepareStatement(sql);
    ResultSet rs = pstmt.executeQuery();

    List<Maestro> lista = new ArrayList<>();

    while (rs.next()) {

        Maestro m = new Maestro();
        m.setIdMaestro(rs.getInt("idMaestro"));
        m.setNombre(rs.getString("nombre"));
        m.setApellidoPat(rs.getString("apellidoPat"));
        m.setApellidoMat(rs.getString("apellidoMat"));

        lista.add(m);
    }

    rs.close();
    pstmt.close();
    conn.close();

    return lista;
}

    // ================= UTIL =================
    private List<Map<String,Object>> rsToList(ResultSet rs) throws Exception {

        List<Map<String,Object>> list = new ArrayList<>();
        int columns = rs.getMetaData().getColumnCount();

        while (rs.next()) {
            Map<String,Object> row = new HashMap<>();
            for (int i = 1; i <= columns; i++) {
                row.put(rs.getMetaData().getColumnLabel(i), rs.getObject(i));
            }
            list.add(row);
        }

        return list;
    }
}