package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.history;

import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Derivation;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.NaoTerminal;

public class HistoryMove {
    public final Derivation derivation;


    public HistoryMove(NaoTerminal naoTerminal, Derivation derivation) {
        this.derivation = derivation;
    }
}
