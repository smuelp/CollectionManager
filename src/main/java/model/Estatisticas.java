/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.SQLException;
import model.dao.MiniaturaDaoJdbc;

/**
 *
 * @author samue
 */
public class Estatisticas {

    private MiniaturaDaoJdbc miniaturaDao;

    public Estatisticas() throws Exception {
        // instância pra acessar o BD
        miniaturaDao = new MiniaturaDaoJdbc();
    }

    public double calcularPorcentagemPorStatus(int idUser, String status) {
        try {
            int totalMiniaturasUsuario = miniaturaDao.contarMiniaturasPorUsuario(idUser);

            // verifica pra evitar divisão por 0
            if (totalMiniaturasUsuario > 0) {
                int totalMiniaturasComStatus = miniaturaDao.contarMiniaturasPorUsuarioEStatus(idUser, status);
                // cast para divisao exata
                return ((double) totalMiniaturasComStatus / totalMiniaturasUsuario) * 100;
            } else {
                // retorna 0.0 se o usuário não tiver miniaturas
                return 0.0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
