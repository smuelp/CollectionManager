/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Usuario;
import model.dao.UsuarioDaoJdbc;
import start.projetopadrao.App;

/**
 * FXML Controller class
 *
 * @author samue
 */
public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtSenha;
    @FXML
    private Button btnEntrar;
    @FXML
    private Hyperlink hypRegister;
    @FXML
    private Hyperlink hypEsqueciSenha;
    @FXML
    private CheckBox ckbView;

    private void onLoginButtonClicked(ActionEvent event) {
        //
    }

    // método para exibir uma mensagem de erro em um Alert
    private void exibirMensagemDeErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void btnEntrarOnAction(ActionEvent event) throws IOException, Exception {
        String email = txtEmail.getText();
        String senha = txtSenha.getText();

        // e-mail existe no banco de dados e se a senha está correta
        UsuarioDaoJdbc usuarioDao = new UsuarioDaoJdbc();
        boolean credenciaisValidas = usuarioDao.verificarExistenciaEmail(email);
        Usuario usuario = usuarioDao.verificarCredenciais(email, senha);

        if (credenciaisValidas && usuario != null) {
            App.setUsuarioAutenticado(usuario); // define o usuário autenticado na classe App
            App.setRoot("Principal");
        } else {
            exibirMensagemDeErro("Credenciais inválidas", "Por favor, verifique seu e-mail e senha.");
        }
    }

    @FXML
    private void irParaRegistro(ActionEvent event) throws IOException {
        App.setRoot("RegistroUser");
    }

    @FXML
    private void esqueciMinhaSenha(ActionEvent event) throws IOException {
        App.setRoot("EsqueceuSenha");
    }

    @FXML
    private void OnActionverSenha(ActionEvent event) {
        if (ckbView.isSelected()) {
            txtSenha.setPromptText(txtSenha.getText());
            txtSenha.setText("");
        } else {
            txtSenha.setText(txtSenha.getPromptText());
        }
    }

}
