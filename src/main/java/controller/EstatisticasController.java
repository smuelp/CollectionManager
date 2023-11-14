/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Estatisticas;
import model.Usuario;
import start.projetopadrao.App;

/**
 * FXML Controller class
 *
 * @author samue
 */
public class EstatisticasController {

    @FXML
    private Button btnGerarGrafico;
    @FXML
    private Button btnVoltarTela;
    @FXML
    private PieChart pieChart;


    @FXML
    private void gerarGrafico(ActionEvent event) throws Exception {

        Estatisticas estatisticas = new Estatisticas();
        
        // obtem usuario autenticado
        Usuario usuarioAutenticado = App.getUsuarioAutenticado();
        int idUser = usuarioAutenticado.getId();

        double porcentagemVisitado = estatisticas.calcularPorcentagemPorStatus(idUser, "VISITADO");
        double porcentagemDesejado = estatisticas.calcularPorcentagemPorStatus(idUser, "DESEJADO");
        double porcentagemMarcado = estatisticas.calcularPorcentagemPorStatus(idUser, "MARCADO");
      
        // formatação pra exibição
        String formatoPorcentagem = "%.2f%%";
        String porcentagemVisitadoFormatada = String.format(formatoPorcentagem, porcentagemVisitado);
        String porcentagemDesejadoFormatada = String.format(formatoPorcentagem, porcentagemDesejado);
        String porcentagemMarcadoFormatada = String.format(formatoPorcentagem, porcentagemMarcado);

        // cria o gráfico
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("VISITADO: " + porcentagemVisitadoFormatada, porcentagemVisitado),
                new PieChart.Data("DESEJADO: " + porcentagemDesejadoFormatada, porcentagemDesejado),
                new PieChart.Data("MARCADO: " + porcentagemMarcadoFormatada, porcentagemMarcado)
        );

        // define os dados no PieChart
        pieChart.setData(pieChartData);
    }

    @FXML
    private void voltarTela(ActionEvent event) throws IOException {
        App.setRoot("Principal");
    }
}
