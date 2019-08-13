package br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.filemanager;

import br.com.finalcraft.unesp.compiladores.atividades.JavaFXMain;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.compiladores.myown.Sleeper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class LoaderController {

    private static LoaderController instance;
    private static Stage dialog;
    public static File lastLoadedFile;

    @FXML
    void initialize() {
        instance = this;
    }

    public static void setUp(){
        dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        // Defines a modal window that blocks events from being
        // delivered to any other application window.
        dialog.initOwner(JavaFXMain.thePrimaryStage);

        Scene newSceneWindow1 = new Scene(MyFXMLs.load_from_file);
        dialog.setScene(newSceneWindow1);
    }

    public static void show(){
        dialog.show();
    }

    @FXML
    private TextField fileName;


    @FXML
    private Label resultLabel;


    @FXML
    void onLoadFile() {
        if (!fileName.getText().isEmpty()){
            try {
                File file = new File(fileName.getText());
                if (!file.exists()) {
                    throw new IOException("File not Found");
                }
                resultLabel.setText("Arquivo carregado com sucesso!");
                new Sleeper(){
                    @Override
                    public void andDo() {
                        resultLabel.setText("...");
                        dialog.close();
                    }
                }.runAfter(0050L);
                FileLoaderHandler.deliver(file);
            }catch (Exception e){
                e.printStackTrace();
                resultLabel.setText("Erro ao carregar arquivo T.T");
                new Sleeper(){
                    @Override
                    public void andDo() {
                        resultLabel.setText("....");
                    }
                }.runAfter(3000L);
            }
        }
    }

    @FXML
    void onCancel(ActionEvent event) {
        dialog.close();
    }
}
