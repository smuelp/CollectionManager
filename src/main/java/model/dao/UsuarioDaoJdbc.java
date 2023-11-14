/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Miniatura;
import model.Usuario;
import start.projetopadrao.App;

/**
 *
 * @author samue
 */
public class UsuarioDaoJdbc {

    private Connection conn;

    public UsuarioDaoJdbc() throws Exception {
        this.conn = ConnFactory.getConnection();
    }

    public void incluir(Usuario entidade) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO usuario (nome, email, senha) VALUES(?, ?, ?)");
            ps.setString(1, entidade.getNome());
            ps.setString(2, entidade.getEmail());
            ps.setString(3, entidade.getSenha());
            ps.execute();

        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void editar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE id = ?";

        try (Connection conn = ConnFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            ps.setInt(4, usuario.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection conn = ConnFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Usuario verificarCredenciais(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
        Usuario usuario = null;

        try (Connection conn = ConnFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // se encontrou um usuário com as credenciais fornecidas
                    // cria um objeto Usuario com os dados do ResultSet
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha"));
                }
            }
        }

        return usuario;
    }

    public boolean verificarExistenciaEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE email = ?";
        try (Connection conn = ConnFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    // significa que o email já existe na tabela
                    return count > 0;
                }
            }
        }
        // retornar false para indicar que o email não existe
        return false;
    }
}
