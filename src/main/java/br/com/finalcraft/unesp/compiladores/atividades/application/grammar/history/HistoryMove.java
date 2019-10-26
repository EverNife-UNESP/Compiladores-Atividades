package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.history;

import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.Grammar;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Derivation;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.NaoTerminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Terminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryMove {

    public final Lexema lexema;
    public final Derivation derivation;
    public final HType hType;
    public final List<Derivation> nonTerminalDerivations = new ArrayList<Derivation>();

    public HistoryMove(Lexema lexema, Derivation derivation) {
        this.lexema = lexema;
        this.derivation = derivation;
        this.hType = HType.CONSUME;
    }

    public HistoryMove(Lexema lexema, Derivation derivation, HType hType) {
        this.lexema = lexema;
        this.derivation = derivation;
        this.hType = hType;
    }

    public void setNonTerminalDerivations(List<Derivation> derivations){
        nonTerminalDerivations.clear();
        nonTerminalDerivations.addAll(derivations);
    }

    @Override
    public String toString() {
        if (derivation instanceof Terminal){
            return "Consumed " + lexema.simpleToString() + " with derivation '" + derivation + "'";
        }else {
            NaoTerminal naoTerminal = (NaoTerminal) derivation;
            List<String> nonTerminalStringDerivations = new ArrayList<String>();
            for (Derivation nonTerminalDerivation : nonTerminalDerivations) {
                nonTerminalStringDerivations.add(nonTerminalDerivation.toString());
            }
            return "Derivating " + naoTerminal + " into : [" + String.join(" ",nonTerminalStringDerivations) + "]";
        }
    }

    public HistoryMove clone(){
        return new HistoryMove(this.lexema,this.derivation);
    }

    private enum HType {
        CONSUME,
        DERIVATE;
    }
}
