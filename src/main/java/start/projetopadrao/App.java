package start.projetopadrao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import static javafx.application.Application.launch;
import model.Usuario;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static String caminho = "/view/";
    
    private static Usuario usuarioAutenticado;

    public static Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    public static void setUsuarioAutenticado(Usuario usuario) {
        usuarioAutenticado = usuario;
    }
    
    public static void clearUsuarioAutenticado() {
        usuarioAutenticado = null;
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("LoginUser"), 640, 640);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(caminho + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}