package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data;

import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.Grammar;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaTypeEnum;

public class GrammarError{

    private final Lexema lexema;
    private final LexemaTypeEnum found;
    private final LexemaTypeEnum expected;
    private final ErrorType errorType;

    private final Grammar grammar;

    public GrammarError(Lexema found, LexemaTypeEnum expected, ErrorType errorType) {
        this.lexema = found;
        this.found = lexema.getLexemaType();
        this.expected = expected;
        this.errorType = errorType;
        this.grammar = null;
    }

    public GrammarError(Lexema found, Grammar expectedGrammar, ErrorType errorType) {
        this.lexema = found;
        this.found = lexema.getLexemaType();
        this.grammar = expectedGrammar;
        this.expected = null;//expectedGrammar.getFollow().getLexemaType();
        this.errorType = errorType;
    }


    @Override
    public String toString() {
        switch (this.errorType){
            case NO_TERMINAL_MATCH:
                return "Error at lexema " + lexema + ", found " + found + " but was expectiong " + expected;
            case UNEXPECTED_END_OF_FILE:
                return "Error at lexema " + lexema + ", was expectiong " + expected + " but found the end of File!";
            case NO_UNTERNIMAL_MATCH:
                return "Error at lexema " + lexema + ", found " + found + " but needed the FOLLOW_OF(" + grammar.getOrigem() + ")" ;
            default:
                return "Error at lexema " + lexema + ", was expectiong " + expected + "!";
        }
    }


    public enum ErrorType{
        NO_TERMINAL_MATCH,
        NO_UNTERNIMAL_MATCH,
        UNEXPECTED_END_OF_FILE,
        UNKNOWN_ERROR;
    }
}
