package br.com.finalcraft.unesp.compiladores.atividades.application.semantic;

public class SemanticError {

    private final String error;

    public SemanticError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
