package br.com.finalcraft.unesp.compiladores.atividades;

import br.com.finalcraft.unesp.compiladores.atividades.application.atividade1.CalculadoraExecutor;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.atividade1.CalculadoraController;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.filemanager.LoaderController;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.compiladores.atividades.logical.lexema.Lexema;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;

public class JavaFXMain extends Application {

    public static Stage thePrimaryStage;
    public static BorderPane rootLayout;

    public static BorderPane getBP(){
        return rootLayout;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        MyFXMLs.loadUpFiles();

        thePrimaryStage = primaryStage;
        thePrimaryStage.setTitle("Compiladores - 2019");

        initRootLayout();

        //Iniciando todos as abas para deixar no grau!
        CalculadoraController.setUp();
        LoaderController.setUp();
    }

    public void initRootLayout()  throws Exception{
        //Carrega o root layout do arquivo fxml.
        rootLayout = (BorderPane) MyFXMLs.main_screen;

        // Mostra a scene (cena) contendo oroot layout.
        Scene scene = new Scene(rootLayout);
        thePrimaryStage.setScene(scene);

        //Ter certeza de que as janelas serão fechadas corretamente!
        thePrimaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        thePrimaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
