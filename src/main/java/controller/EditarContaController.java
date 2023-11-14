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
public class EditarContaController implements Initializable {

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private Button btnRegistrar;
    @FXML
    private PasswordField txtSenha1;
    @FXML
    private CheckBox ckbView;
    @FXML
    private CheckBox ckbView1;
    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void editarConta(ActionEvent event) throws Exception {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String senha = txtSenha.getText();
        String senha1 = txtSenha1.getText();

        // verificar se o email é válido
        if (!isEmailValido(email)) {
            exibirMensagemDeErro("Email Inválido", "Por favor, insira um email válido.");
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

        // verificar se o email já existe no banco de dados (exceto se for o email atual do usuário)
        Usuario usuarioAtual = App.getUsuarioAutenticado();
        if (!usuarioAtual.getEmail().equals(email)) {
            UsuarioDaoJdbc usuarioDao = new UsuarioDaoJdbc();
            boolean emailExistente = usuarioDao.verificarExistenciaEmail(email);
            if (emailExistente) {
                exibirMensagemDeErro("Email já Existente", "O email inserido já está em uso. Por favor, escolha outro email.");
                return;
            }
        }

        // verificar se houve alguma alteração
        if (usuarioAtual.getNome().equals(nome) && usuarioAtual.getEmail().equals(email) && usuarioAtual.getSenha().equals(senha)) {
            // não houve alteração, voltar para a tela principal
            try {
                App.setRoot("Principal");
            } catch (IOException e) {
                exibirMensagemDeErro("Erro", "Erro ao retornar para a tela principal: " + e.getMessage());
            }
            return;
        }

        // realizar a atualização no banco de dados
        usuarioAtual.setNome(nome);
        usuarioAtual.setEmail(email);
        usuarioAtual.setSenha(senha);

        try {
            UsuarioDaoJdbc usuarioDao = new UsuarioDaoJdbc();
            usuarioDao.editar(usuarioAtual);
            exibirMensagem("Sucesso", "Alterações feitas com sucesso.");

            App.setRoot("Principal");

        } catch (SQLException e) {
            exibirMensagemDeErro("Erro", "Erro ao editar conta: " + e.getMessage());
        }
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

    @FXML
    private void irParaPrincipal(ActionEvent event) throws IOException {
        App.setRoot("Principal");
    }

    private boolean isEmailValido(String email) {
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

    private void exibirMensagem(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
