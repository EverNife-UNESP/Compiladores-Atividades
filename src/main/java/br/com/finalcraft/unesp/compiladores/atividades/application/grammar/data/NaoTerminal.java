package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data;

import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.Grammar;

public class NaoTerminal implements Derivacao{

    final String naoTerminal;
    public NaoTerminal(String naoTerminal) {
        this.naoTerminal = naoTerminal;
    }

    public Grammar getOwnGrammar() {
        return Grammar.getOrCreateGrammar(this);
    }
    @Override
    public String toString() {
        return naoTerminal;
    }
}
