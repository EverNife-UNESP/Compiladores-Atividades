package br.com.finalcraft.unesp.compiladores.atividades.javafx.controller;

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

}
