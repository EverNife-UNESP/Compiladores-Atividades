package br.com.finalcraft.unesp.compiladores.atividades;

import br.com.finalcraft.unesp.compiladores.atividades.application.gambiarra.MathCalculator;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.GramaticalImporter;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.analisadores.AnalisadorLexicoController;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.analisadores.AnalisadorSemanticoController;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.analisadores.AnalisadorSintaticoController;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.analisadores.InterpretadorDeCodigoController;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.atividade1.CalculadoraController;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.imported.CodeInterpreterKeywordsAsync;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.imported.PascalKeywordsAsync;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
        PascalKeywordsAsync.setUp();    //Code TextArea
        CodeInterpreterKeywordsAsync.setUp();    //Code TextArea


        CalculadoraController.setUp();
        AnalisadorLexicoController.setUp();
        AnalisadorSintaticoController.setUp();
        AnalisadorSemanticoController.setUp();
        InterpretadorDeCodigoController.setUp();
        MathCalculator.assyncStart();

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
        GramaticalImporter.importGrammar();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
