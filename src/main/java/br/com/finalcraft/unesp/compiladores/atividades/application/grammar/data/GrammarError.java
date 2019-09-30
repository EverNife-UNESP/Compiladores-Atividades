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
        this.found = lexema != null ? lexema.getLexemaType() : null;
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
            case UNEXPECTED_END_OF_NON_TERMINALS:
                return "Error at lexema " + lexema + ", nothing was expected, but found " + found;
            default:
                return "Error at lexema " + lexema + ", was expectiong " + expected + "!";
        }
    }

    public Lexema getLexema() {
        return lexema;
    }

    public LexemaTypeEnum getFound() {
        return found;
    }

    public LexemaTypeEnum getExpected() {
        return expected;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public enum ErrorType{
        NO_TERMINAL_MATCH,
        NO_UNTERNIMAL_MATCH,
        UNEXPECTED_END_OF_FILE,
        UNEXPECTED_END_OF_NON_TERMINALS,
        UNKNOWN_ERROR;
    }
}
