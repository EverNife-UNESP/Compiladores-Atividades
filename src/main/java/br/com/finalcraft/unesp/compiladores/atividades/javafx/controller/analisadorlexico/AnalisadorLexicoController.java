package br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.analisadorlexico;

import br.com.finalcraft.unesp.compiladores.atividades.JavaFXMain;
import br.com.finalcraft.unesp.compiladores.atividades.application.AnalisadorLexico;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.filemanager.FileLoaderHandler;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.imported.PascalKeywordsAsync;
import br.com.finalcraft.unesp.compiladores.atividades.logical.lexema.Lexema;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class AnalisadorLexicoController implements FileLoaderHandler{
    private static AnalisadorLexicoController instance;
    private static Stage dialog;

    public static void setUp(){
        dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        // Defines a modal window that blocks events from being
        // delivered to any other application window.
        dialog.initOwner(JavaFXMain.thePrimaryStage);

        Scene newSceneWindow1 = new Scene(MyFXMLs.analisador_lexico);
        newSceneWindow1.getStylesheets().add(PascalKeywordsAsync.instance.styleSheet);
        dialog.setScene(newSceneWindow1);
        dialog.setOnShowing(event -> {
            instance.startHearingForFiles();
        });
        dialog.setOnCloseRequest(event -> {
            instance.stopHearingForFiles();
        });

        PascalKeywordsAsync.instance.node.prefHeight(instance.codeBorderPanel.getHeight());
        PascalKeywordsAsync.instance.node.prefWidth(instance.codeBorderPanel.getWidth());
        instance.codeBorderPanel.setCenter(PascalKeywordsAsync.instance.node);
    }

    public static void show(){
        dialog.show();
    }

    @FXML
    private TableView<Lexema> tabela;

    @FXML
    private TableColumn<Lexema, String> columnExpressao;

    @FXML
    private TableColumn<Lexema, String> columnTipoLexema;

    @FXML
    private TableColumn<Lexema, Integer> columnLinha;

    @FXML
    private TableColumn<Lexema, Integer> columnInicio;

    @FXML
    private TableColumn<Lexema, Integer> columnFim;

    @FXML
    private BorderPane codeBorderPanel;

    private ObservableList<Lexema> lexemaObservableList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        instance = this;

        columnExpressao.setCellValueFactory(new PropertyValueFactory<>("theExpression"));
        columnTipoLexema.setCellValueFactory(new PropertyValueFactory<>("lexemaType"));
        columnLinha.setCellValueFactory(new PropertyValueFactory<>("linha"));
        columnInicio.setCellValueFactory(new PropertyValueFactory<>("start"));
        columnFim.setCellValueFactory(new PropertyValueFactory<>("end"));

        tabela.setItems(lexemaObservableList);
    }


    @FXML
    void onAnaliseLexica() {

        CodeArea codeArea = PascalKeywordsAsync.getCodeArea();
        System.out.println(codeArea.getText());
        if (!codeArea.getText().isEmpty()){
            lexemaObservableList = FXCollections.observableList(AnalisadorLexico.analiseLexica(codeArea.getText()));
            tabela.setItems(lexemaObservableList);
        }

    }

    @Override
    public void onFileLoaded(File file) {
        try {
            byte[] encoded = Files.readAllBytes(file.toPath());
            String fileConcent = new String(encoded, Charset.defaultCharset());
            System.out.println("Setting CodeText");
            PascalKeywordsAsync.getCodeArea().clear();
            PascalKeywordsAsync.getCodeArea().replaceText(0, 0, fileConcent);
        }catch (Exception e){
            e.printStackTrace();
            PascalKeywordsAsync.getCodeArea().replaceText(0, 0, "Arquivo lido corrompido :/");
        }
    }

    @FXML
    void onLerArquivo() {
        FileLoaderHandler.openFilerLoader(this);
    }
}
