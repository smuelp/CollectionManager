/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
public class RegistroController implements Initializable {

    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private TextField txtNome;
    @FXML
    private PasswordField txtSenha1;
    @FXML
    private CheckBox ckbView;
    @FXML
    private CheckBox ckbView1;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    private void registrarConta(ActionEvent event) throws IOException, SQLException, Exception {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String senha = txtSenha.getText();
        String senha1 = txtSenha1.getText();

        // valida campos de entrada
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            exibirMensagemDeErro("Cadastro incompleto", "Por favor, preencha todos os campos.");
        } else {
            // verifica se o email já existe no banco de dados
            UsuarioDaoJdbc usuarioDao = new UsuarioDaoJdbc();
            boolean emailExistente = usuarioDao.verificarExistenciaEmail(email);

            // verificar se o e-mail é válido
            if (!isEmailValido(email)) {
                exibirMensagemDeErro("E-mail Inválido", "Por favor, insira um e-mail válido.");
                return;
            }

            // verificar se a senha tem pelo menos 5 caracteres
            if (senha.length() < 5) {
                exibirMensagemDeErro("Senha Inválida", "A senha deve ter pelo menos 5 caracteres.");
                return;
            }

            if (senha == null ? senha1 != null : !senha.equals(senha1)) {
                exibirMensagemDeErro("Erro", "As senhas não coincidem.");
                return;
            }

            if (!emailExistente) {
                // se o email não existe, registrar o usuário no banco de dados
                try {
                    Usuario usuario = new Usuario(nome, email, senha);

                    usuarioDao.incluir(usuario); // inclua o usuário no banco de dados
                    // exibir uma mensagem de sucesso ao usuário
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setHeaderText("Registro bem-sucedido");
                    alert.setContentText("Sua conta foi registrada com sucesso!");
                    alert.showAndWait();
                    App.setUsuarioAutenticado(usuario); // define user autenticado
                    App.setRoot("LoginUser");

                } catch (Exception e) {
                    // trata exceções de inclusão no banco de dados, se necessário
                    e.printStackTrace();
                    exibirMensagemDeErro("Erro", "Erro ao registrar a conta. Por favor, tente novamente mais tarde.");
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Email já existe");
                alert.setContentText("O email inserido já está em uso. Por favor, escolha outro email.");
                alert.showAndWait();
            }
        }
    }

    public boolean isEmailValido(String email) {
        // regex para validar o formato do email
        String regex = "^[a-z0-9_]+(\\.[a-z0-9_]+)*@[a-z0-9]+(\\.[a-z0-9]+)*(\\.[a-z]{2,})$";

        // verifica se o email corresponde ao padrão regex e não contém letras maiúsculas
        return email.toLowerCase().matches(regex);
    }

    private void exibirMensagemDeErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void irParaLogin(ActionEvent event) throws IOException {
        App.setRoot("LoginUser");
    }

    @FXML
    private void verSenha(ActionEvent event) {
        if (ckbView.isSelected()) {
            txtSenha.setPromptText(txtSenha.getText());
            txtSenha.setText("");
        } else {
            txtSenha.setText(txtSenha.getPromptText());
        }
    }

    @FXML
    private void verConfirmeSenha(ActionEvent event) {
        if (ckbView1.isSelected()) {
            txtSenha1.setPromptText(txtSenha1.getText());
            txtSenha1.setText("");
        } else {
            txtSenha1.setText(txtSenha1.getPromptText());
        }
    }

}
