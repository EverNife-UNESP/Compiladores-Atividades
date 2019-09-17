package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data;

import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaTypeEnum;

public class GrammarError{

    private final Lexema lexema;
    private final LexemaTypeEnum found;
    private final LexemaTypeEnum expected;

    public GrammarError(Lexema found, LexemaTypeEnum expected) {
        this.lexema = found;
        this.found = lexema.getLexemaType();
        this.expected = expected;
    }

    @Override
    public String toString() {
        return "Error at lexema " + lexema + ", found " + found + " but was expectiong " + expected;
    }
}
