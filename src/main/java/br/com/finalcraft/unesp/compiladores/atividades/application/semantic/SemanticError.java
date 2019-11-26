package br.com.finalcraft.unesp.compiladores.atividades.application.semantic;

import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;

public class SemanticError {

    private final String error;
    private final Lexema lexema;

    public SemanticError(String error, Lexema lexema) {
        this.error = error;
        this.lexema = lexema;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "[" + lexema.getId() + "]" + error;
    }
}
