/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Miniatura;
import model.Usuario;
import model.dao.DaoFactory;
import model.dao.MiniaturaDaoJdbc;
import model.dao.UsuarioDaoJdbc;
import start.projetopadrao.App;

/**
 * FXML Controller class
 *
 * @author samue
 */
public class PrincipalController implements Initializable {

    @FXML
    private Button btnIncluir;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnExcluir;
    @FXML
    private TextField txtFiltro;
    @FXML
    private Button btnFiltrar;
    @FXML
    private Button btnLimpar;
    @FXML
    private TableView<Miniatura> tblMiniatura;
    @FXML
    private TableColumn<Miniatura, String> tblColLugar;
    @FXML
    private TableColumn<Miniatura, String> tblColEstado;
    @FXML
    private TableColumn<Miniatura, String> tblColPais;
    @FXML
    private TableColumn<Miniatura, Date> tblColData;
    @FXML
    private TableColumn<Miniatura, String> tblColStatus;
    private List<Miniatura> listaMiniaturas;
    private ObservableList<Miniatura> observableListMiniaturas;
    @FXML
    private TableColumn<Miniatura, String> tblColFoto;
    @FXML
    private Button btnSair;
    @FXML
    private Label lblOlaUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Usuario usuario = App.getUsuarioAutenticado();
        lblOlaUsuario.setText("Olá, " + usuario.getNome() + "!");
        carregarMiniaturas("");
    }

    @FXML
    private void btnIncluirOnAction(ActionEvent event) throws IOException {
        App.setRoot("CadastroMiniatura");
    }

    @FXML
    private void btnEditarOnAction(ActionEvent event) throws IOException {
        try {
            Miniatura miniaturaSelecionada = tblMiniatura.getSelectionModel().getSelectedItem();

            if (miniaturaSelecionada != null) {
                CadastroController.setMiniaturaSelecionada(miniaturaSelecionada);
                App.setRoot("CadastroMiniatura");
            }
        } catch (Exception e) {
            exibirMensagemDeErro("Erro ao editar miniatura", e.getMessage());
        }
    }

    @FXML
    private void btnExcluirOnAction(ActionEvent event) {
        try {
            Miniatura miniaturaSelecionada = tblMiniatura.getSelectionModel().getSelectedItem();

            if (miniaturaSelecionada != null) {
                MiniaturaDaoJdbc dao = DaoFactory.novaMiniaturaDao();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmação");
                alert.setContentText("Confirma exclusão de " + miniaturaSelecionada.getLugar() + "?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    dao.excluir(miniaturaSelecionada);
                    carregarMiniaturas("");
                }
            }
        } catch (SQLException e) {
            exibirMensagemDeErro("Erro ao excluir miniatura", e.getMessage());
        } catch (Exception e) {
            exibirMensagemDeErro("Erro desconhecido", e.getMessage());
        }
    }

    @FXML
    private void editarConta(ActionEvent event)  throws IOException{
        try {
            Usuario usuario = App.getUsuarioAutenticado();
            if (usuario != null) {
                UsuarioDaoJdbc usuarioDao = new UsuarioDaoJdbc();
                usuarioDao.editar(usuario);
                App.setRoot("EditarContaUser");
            } else {
                exibirMensagemDeErro("Erro", "Usuário não autenticado.");
            }
        } catch (SQLException e) {
            exibirMensagemDeErro("Erro ao editar conta", e.getMessage());
        } catch (IOException e) {
            exibirMensagemDeErro("Erro ao mudar de cena", e.getMessage());
        } catch (Exception e) {
            exibirMensagemDeErro("Erro desconhecido", e.getMessage());
        }
    }

    @FXML
    private void excluirConta(ActionEvent event)  throws IOException{
        try {
            Usuario usuario = App.getUsuarioAutenticado();

            if (usuario != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmação");
                alert.setContentText("Tem certeza que deseja excluir sua conta? Esta ação não pode ser desfeita.");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    UsuarioDaoJdbc usuarioDao = new UsuarioDaoJdbc();
                    usuarioDao.excluir(usuario.getId());

                    exibirMensagem("Excluir Conta", "Conta excluída com sucesso!");

                    // limpa as credenciais do usuário excluído
                    App.setUsuarioAutenticado(null);

                    App.setRoot("LoginUser");
                }
            } else {
                exibirMensagemDeErro("Erro", "Usuário não autenticado.");
            }
        } catch (SQLException e) {
            exibirMensagemDeErro("Erro ao excluir conta", e.getMessage());
        } catch (IOException e) {
            exibirMensagemDeErro("Erro ao mudar de cena", e.getMessage());
        } catch (Exception e) {
            exibirMensagemDeErro("Erro desconhecido", e.getMessage());
        }
    }

    /* ------------------------------------------------------------------- */

    private void exibirMensagem(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void btnFiltrarOnAction(ActionEvent event) {
        carregarMiniaturas(txtFiltro.getText());
    }

    @FXML
    private void btnLimparOnAction(ActionEvent event) {
        txtFiltro.clear();
        carregarMiniaturas("");
    }

    public void carregarMiniaturas(String param) {
        tblColLugar.setCellValueFactory(new PropertyValueFactory<>("Lugar"));
        tblColEstado.setCellValueFactory(new PropertyValueFactory<>("Estado"));
        tblColPais.setCellValueFactory(new PropertyValueFactory<>("Pais"));
        tblColData.setCellValueFactory(new PropertyValueFactory<>("Data"));
        tblColStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tblColFoto.setCellValueFactory(new PropertyValueFactory<>("Foto"));
        try {
            Usuario user = App.getUsuarioAutenticado();
            MiniaturaDaoJdbc dao = DaoFactory.novaMiniaturaDao();
            listaMiniaturas = dao.listar(param, user.getId());

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        observableListMiniaturas = FXCollections.observableArrayList(listaMiniaturas);
        tblMiniatura.setItems(observableListMiniaturas);
    }

    private void exibirMensagemDeErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void verGrafico(ActionEvent event) throws IOException {
        App.setRoot("Estatisticas");
    }

    @FXML
    private void OnActionSair(ActionEvent event) throws IOException {
        App.setUsuarioAutenticado(null); // clear nas informações de autenticação
        App.setRoot("LoginUser");
    }
}
