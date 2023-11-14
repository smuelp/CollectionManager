/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import model.Miniatura;
import model.Usuario;

/**
 *
 * @author samue
 */
public class MiniaturaDaoJdbc implements InterfaceDao<Miniatura> {

    private Connection conn;

    public MiniaturaDaoJdbc() throws Exception {
        this.conn = ConnFactory.getConnection();
    }

    @Override
    public void incluir(Miniatura entidade) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO miniatura (lugar, estado, pais, data, status, foto, idUser) VALUES(?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, entidade.getLugar());
            ps.setString(2, entidade.getEstado());
            ps.setString(3, entidade.getPais());

            // convertendo a data de java.util.Date para java.sql.Date
            java.sql.Date dataSql = new java.sql.Date(entidade.getData().getTime());
            ps.setDate(4, dataSql);

            ps.setString(5, entidade.getStatus());
            ps.setString(6, entidade.getFoto());
            ps.setInt(7, entidade.getUser().getId());
            ps.execute();

        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public void editar(Miniatura entidade) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE miniatura SET lugar=?, estado=?, pais=?, data=?, status=?, foto=? WHERE id=?");
            ps.setString(1, entidade.getLugar());
            ps.setString(2, entidade.getEstado());
            ps.setString(3, entidade.getPais());

            // convertendo a data de java.util.Date para java.sql.Date
            java.sql.Date dataSql = new java.sql.Date(entidade.getData().getTime());
            ps.setDate(4, dataSql);

            ps.setString(5, entidade.getStatus());
            ps.setString(6, entidade.getFoto());
            ps.setInt(7, entidade.getId());
            ps.execute();

        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public void excluir(Miniatura entidade) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM miniatura WHERE id=?");
            ps.setInt(1, entidade.getId());
            ps.execute();

        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public Miniatura pesquisarPorId(int id) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM miniatura WHERE id=?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Miniatura mini = new Miniatura();
                mini.setId(rs.getInt("id"));
                mini.setLugar(rs.getString("lugar"));
                mini.setEstado(rs.getString("estado"));
                mini.setPais(rs.getString("pais"));

                // obtendo a data do ResultSet como um objeto java.sql.Date
                java.sql.Date dataSql = rs.getDate("data");
                // convertendo java.sql.Date para java.util.Date
                java.util.Date dataUtil = new java.util.Date(dataSql.getTime());

                mini.setData(dataUtil);

                mini.setStatus(rs.getString("status"));
                mini.setFoto(rs.getString("foto"));

                Usuario user = new Usuario();
                user.setId(rs.getInt("idUser"));
                mini.setUser(user); // associa usuario com miniatura

                return mini;

            } else {
                return null;
            }

        } finally {
            ps.close();
            rs.close();
        }
    }

    @Override
    public List<Miniatura> listar(String param, int idUser) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (param.equals(" ")) {
                ps = conn.prepareStatement("SELECT * FROM miniatura WHERE idUser = " + idUser);
            } else {
                ps = conn.prepareStatement(
                        "SELECT * FROM miniatura WHERE lugar like '%" + param + "%' AND idUser = " + idUser);
            }
            rs = ps.executeQuery();
            List<Miniatura> lista = new ArrayList();
            while (rs.next()) { // enquanho houver mais registro no rs...
                Miniatura mini = new Miniatura();
                mini.setId(rs.getInt("id"));
                mini.setLugar(rs.getString("lugar"));
                mini.setEstado(rs.getString("estado"));
                mini.setPais(rs.getString("pais"));

                // obtendo a data do ResultSet como um objeto java.sql.Date
                java.sql.Date dataSql = rs.getDate("data");
                
                // convertendo java.sql.Date para java.util.Date
                java.util.Date dataUtil = new java.util.Date(dataSql.getTime());
                
                mini.setData(dataUtil);

                mini.setStatus(rs.getString("status"));
                mini.setFoto(rs.getString("foto"));

                // linhas adicionadas para a correção
                Usuario user = new Usuario();
                user.setId(rs.getInt("idUser"));
                mini.setUser(user);
                // ---------------------------------

                lista.add(mini);
            }
            return lista;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /* ------------------------------------------------------------------------------------- */
    public int contarMiniaturasPorUsuarioEStatus(int idUser, String status) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int cont = 0;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/miniatura", "root", "mysql");
            ps = conn.prepareStatement("SELECT COUNT(*) FROM miniatura WHERE idUser=? AND status=?");
            ps.setInt(1, idUser);
            ps.setString(2, status);

            rs = ps.executeQuery();

            if (rs.next()) {
                cont = rs.getInt(1); // resultado da contagem
            }
        } finally {
            // fecha conexões e resultados
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return cont;
    }

    public int contarMiniaturasPorUsuario(int idUser) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int cont = 0;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/miniatura", "root", "mysql");
            ps = conn.prepareStatement("SELECT COUNT(*) FROM miniatura WHERE idUser=?");
            ps.setInt(1, idUser);

            rs = ps.executeQuery();

            if (rs.next()) {
                cont = rs.getInt(1); // resultado da contagem
            }
        } finally {
            // fecha conexões e resultados
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return cont;

    }
}
