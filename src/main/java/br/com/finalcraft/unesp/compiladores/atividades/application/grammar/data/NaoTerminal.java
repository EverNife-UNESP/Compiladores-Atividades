package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data;

import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.Grammar;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaTypeEnum;

public class NaoTerminal implements Derivacao{

    final String naoTerminal;
    private Grammar ownGrammar;

    public NaoTerminal(String naoTerminal) {
        this.naoTerminal = naoTerminal;
    }

    public void setGrammar(Grammar grammar){
        ownGrammar = grammar;
    }

    public Grammar getOwnGrammar() {
        return ownGrammar;
    }

    public static final NaoTerminal PROGRAM = new NaoTerminal("<program>");
    public static final NaoTerminal BLOCO = new NaoTerminal("<bloco>");


    @Override
    public String toString() {
        return naoTerminal;
    }
}
