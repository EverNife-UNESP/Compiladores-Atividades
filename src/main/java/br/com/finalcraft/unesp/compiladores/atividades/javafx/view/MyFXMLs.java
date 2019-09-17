package br.com.finalcraft.unesp.compiladores.atividades.javafx.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class MyFXMLs {

    public static Parent main_screen;

    //Atividades
    public static Parent calculadora;
    public static Parent analisador_lexico;
    public static Parent analisador_sintatico;


    //Arquivos
    public static Parent save_to_file;
    public static Parent load_from_file;

    public static void loadUpFiles() throws IOException {
        main_screen = FXMLLoader.load(MyFXMLs.class.getResource("/assets/main_screen.fxml"));

        calculadora = FXMLLoader.load(MyFXMLs.class.getResource("/assets/atividade1/calculadora.fxml"));
        analisador_lexico = FXMLLoader.load(MyFXMLs.class.getResource("/assets/atividade2/analisador_lexico.fxml"));
        analisador_sintatico = FXMLLoader.load(MyFXMLs.class.getResource("/assets/atividade2/analisador_sintatico.fxml"));

        //save_to_file = FXMLLoader.load(MyFXMLs.class.getResource("/assets/file/save_to_file.fxml"));
    }
}
