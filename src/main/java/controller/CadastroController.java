/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Miniatura;
import model.StatusLugar;
import model.Usuario;
import model.dao.MiniaturaDaoJdbc;
import model.dao.DaoFactory;
import start.projetopadrao.App;

/**
 * FXML Controller class
 *
 * @author samue
 */
public class CadastroController implements Initializable {

    @FXML
    private TextField txtLugar;
    @FXML
    private TextField txtEstado;
    @FXML
    private TextField txtPais;
    private TextField txtData;
    @FXML
    private TextField txtStatus;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGravar;

    private static Miniatura miniaturaSelecionada;
    @FXML
    private ImageView imgFoto;
    private String caminhoFoto;
    @FXML
    private DatePicker dtData;
    @FXML
    private ComboBox<StatusLugar> comboStatus;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboStatus.getItems().setAll(StatusLugar.values());
        comboStatus.valueProperty().addListener((obs, oldStatus, newStatus) -> {
            // atualiza o campo status da miniatura quando o ComboBox for alterado
            txtStatus.setText(newStatus.toString());
        });
        if (miniaturaSelecionada != null) {
            txtLugar.setText(miniaturaSelecionada.getLugar());
            txtEstado.setText(miniaturaSelecionada.getEstado());
            txtPais.setText(miniaturaSelecionada.getPais());
            dtData.setValue(miniaturaSelecionada.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            txtStatus.setText(miniaturaSelecionada.getStatus());
            String path = null;
            try {
                path = new File(".").getCanonicalPath();
                String caminho = path + miniaturaSelecionada.getFoto();
                imgFoto.setImage(new Image("file:///" + caminho));
                System.out.println("=============> " + path);

            } catch (Exception e) {
                System.out.println("=============> " + e.getMessage());
            }

        } else {
            // mostra data atual caso não tenha miniatura selecionada
            dtData.setValue(LocalDate.now());
        }
    }

    @FXML
    private void btnCancelarOnAction(ActionEvent event) throws IOException {
        miniaturaSelecionada = null;
        App.setRoot("Principal");

    }

    @FXML
    private void btnGravarOnAction(ActionEvent event) throws IOException, Exception {

        String lugar = txtLugar.getText();
        String estado = txtEstado.getText();
        String pais = txtPais.getText();
        LocalDate data = dtData.getValue();

        if (lugar.isEmpty() || estado.isEmpty() || pais.isEmpty() || data == null) {
            exibirMensagemDeErro("Informações inválidas", "Preencha todos os campos");
        } else {
            Miniatura miniatura = new Miniatura();
            miniatura.setLugar(txtLugar.getText());
            miniatura.setEstado(txtEstado.getText());
            miniatura.setPais(txtPais.getText());

            // linha para disponibilizar data local
            Date dataSelecionada = Date.from(dtData.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            miniatura.setData(dataSelecionada);

            miniatura.setStatus(txtStatus.getText());
            miniatura.setFoto(caminhoFoto);

            //* ------- MODIFICAÇÃO PARA USUARIO AUTENTICADO ------- */
            // linha que obtém o usuário autenticado
            Usuario usuarioAutenticado = App.getUsuarioAutenticado();

            // associa o usuário à miniatura
            miniatura.setUser(usuarioAutenticado);

            // chama o método incluir no MiniaturaDaoJdbc
            // MiniaturaDaoJdbc miniaturaDao = new MiniaturaDaoJdbc();

            // miniaturaDao.incluir(miniatura); // estava duplicando as miniaturas
            MiniaturaDaoJdbc dao = DaoFactory.novaMiniaturaDao();

            if (miniaturaSelecionada == null) {
                dao.incluir(miniatura);
            } else {
                miniatura.setId(miniaturaSelecionada.getId());
                dao.editar(miniatura);
                miniaturaSelecionada = null;
            }
            App.setRoot("Principal");
        }
    }

    public static void setMiniaturaSelecionada(Miniatura miniaturaSelecionada) {
        CadastroController.miniaturaSelecionada = miniaturaSelecionada;
    }

    public void selecionaFoto() {
        FileChooser f = new FileChooser();
        f.getExtensionFilters().add(new ExtensionFilter("Imagens", "*.jpg", "*.jpeg", "*.png", "*.webp"));
        File file = f.showOpenDialog(new Stage());
        if (file != null) {
            imgFoto.setImage(new Image("file:///" + file.getAbsolutePath()));
            caminhoFoto = "/imagens/" + file.getName();
        }
    }

    @FXML
    private void imgFotoOnMouseClicked(MouseEvent event) {
        selecionaFoto();
    }

    private void exibirMensagemDeErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

}
