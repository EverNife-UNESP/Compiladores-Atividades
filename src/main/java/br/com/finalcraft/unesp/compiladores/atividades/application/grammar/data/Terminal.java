package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data;

import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaTypeEnum;

public class Terminal implements Derivation {

    private final String terminal;
    private final LexemaTypeEnum lexemaTypeEnum;

    public Terminal(String terminal) {
        this.terminal = terminal;
        lexemaTypeEnum = LexemaType.getOf(terminal);
    }

    public String getTerminal() {
        return terminal;
    }

    public LexemaTypeEnum getLexemaType() {
        return lexemaTypeEnum;
    }

    @Override
    public String toString() {
        return terminal;
    }
}
