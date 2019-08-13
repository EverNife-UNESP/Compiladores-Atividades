package br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.filemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface FileLoaderHandler {

    public static List<FileLoaderHandler> fileHandlers = new ArrayList<FileLoaderHandler>();

    public static void deliver(File file){
        fileHandlers.forEach(fileLoaderHandler -> fileLoaderHandler.onFileLoaded(file));
    }

    public default void startHearingForFiles(){
        fileHandlers.add(this);
    }

    public default void stopHearingForFiles(){
        fileHandlers.remove(this);
    }

    public void onFileLoaded(File file);

}
