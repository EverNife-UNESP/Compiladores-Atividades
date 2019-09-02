package br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.atividade1;

import br.com.finalcraft.unesp.compiladores.atividades.JavaFXMain;
import br.com.finalcraft.unesp.compiladores.atividades.application.AnalisadorLexico;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.filemanager.FileLoaderHandler;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class CalculadoraController implements FileLoaderHandler {

    private static CalculadoraController instance;
    private static Stage dialog;

    public static void setUp(){
        dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        // Defines a modal window that blocks events from being
        // delivered to any other application window.
        dialog.initOwner(JavaFXMain.thePrimaryStage);

        Scene newSceneWindow1 = new Scene(MyFXMLs.calculadora);
        dialog.setScene(newSceneWindow1);
        dialog.setOnShowing(event -> {
            instance.startHearingForFiles();
        });
        dialog.setOnCloseRequest(event -> {
            instance.stopHearingForFiles();
        });
    }

    public static void show(){
        dialog.show();
    }

    @FXML
    private TextField analiseLexicaInput;

    @FXML
    private TableView<Lexema> tabela;

    @FXML
    private TableColumn<Lexema, String> columnExpressao;

    @FXML
    private TableColumn<Lexema, String> columnTipoLexema;

    @FXML
    private TableColumn<Lexema, Integer> columnInicio;

    @FXML
    private TableColumn<Lexema, Integer> columnFim;

    private ObservableList<Lexema> lexemaObservableList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        instance = this;

        columnExpressao.setCellValueFactory(new PropertyValueFactory<>("theExpression"));
        columnTipoLexema.setCellValueFactory(new PropertyValueFactory<>("lexemaType"));
        columnInicio.setCellValueFactory(new PropertyValueFactory<>("start"));
        columnFim.setCellValueFactory(new PropertyValueFactory<>("end"));

        tabela.setItems(lexemaObservableList);
    }


    @FXML
    void onAnaliseLexica() {

        if (!analiseLexicaInput.getText().isEmpty()){
            lexemaObservableList = FXCollections.observableList(AnalisadorLexico.analiseLexica(analiseLexicaInput.getText()));
            tabela.setItems(lexemaObservableList);
        }
    }

    @Override
    public void onFileLoaded(File file) {
        try {
            byte[] encoded = Files.readAllBytes(file.toPath());
            String fileConcent = new String(encoded, Charset.defaultCharset());
            this.analiseLexicaInput.setText(fileConcent);
        }catch (Exception e){
            e.printStackTrace();
            this.analiseLexicaInput.setText("Arquivo lido corrompido :/");
        }
    }

    @FXML
    void onLerArquivo() {
        FileLoaderHandler.openFilerLoader(this);
    }
}
