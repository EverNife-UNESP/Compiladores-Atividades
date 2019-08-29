package br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.filemanager;

import br.com.finalcraft.unesp.compiladores.atividades.JavaFXMain;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface FileLoaderHandler {

    public static List<FileLoaderHandler> fileHandlers = new ArrayList<FileLoaderHandler>();

    public static void openFilerLoader(FileLoaderHandler fileLoaderHandler){
            //File Loader não existe ou já está escutando?
        boolean deliveryOnly = fileLoaderHandler == null ? true : !fileHandlers.contains(fileLoaderHandler);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("").getAbsoluteFile());
        File loadedFile = fileChooser.showOpenDialog(JavaFXMain.thePrimaryStage);

        if (loadedFile == null) return; //tab closed

        if (!deliveryOnly) fileLoaderHandler.startHearingForFiles();
        fileHandlers.forEach(fLoader -> fLoader.onFileLoaded(loadedFile));
        if (!deliveryOnly) fileLoaderHandler.stopHearingForFiles();
    }

    public default void startHearingForFiles(){
        fileHandlers.add(this);
    }

    public default void stopHearingForFiles(){
        fileHandlers.remove(this);
    }

    public void onFileLoaded(File file);

}
