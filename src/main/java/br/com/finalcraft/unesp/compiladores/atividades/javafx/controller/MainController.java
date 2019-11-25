package br.com.finalcraft.unesp.compiladores.atividades.javafx.controller;

import br.com.finalcraft.unesp.compiladores.atividades.application.AnalisadorSemantico;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.analisadores.AnalisadorLexicoController;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.analisadores.AnalisadorSemanticoController;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.analisadores.AnalisadorSintaticoController;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.atividade1.CalculadoraController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainController {

    private static MainController instance;

    @FXML
    void initialize() {
        instance = this;
    }

    @FXML
    void onCalculadoraAnaliseLexica(ActionEvent event) {
        CalculadoraController.show();
    }

    @FXML
    void onAnalisadorLexico(){
        AnalisadorLexicoController.show();
    }

    @FXML
    void onAnalisadorSintatico(){
        AnalisadorSintaticoController.show();
    }

    @FXML
    void onAnalisadorSemantico(){
        AnalisadorSemanticoController.show();
    }

    @FXML
    void onLerCodigo(){
        AnalisadorSemanticoController.show();
    }
}
