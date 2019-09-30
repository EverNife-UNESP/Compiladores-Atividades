package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.history;

import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Derivation;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.NaoTerminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Terminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;

public class HistoryMove {

    public final Lexema lexema;
    public final Derivation derivation;

    public HistoryMove(Lexema lexema, Derivation derivation) {
        this.lexema = lexema;
        this.derivation = derivation;
    }


    @Override
    public String toString() {
        if (derivation instanceof Terminal){
            return "Consumed " + lexema + " with derivation " + derivation;
        }else {
            return "Satayed with " + lexema + " but passed to " + derivation;
        }
    }
}
