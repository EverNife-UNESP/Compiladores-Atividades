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

    public static final NaoTerminal PARTE_DE_DECLARACAO_DE_VARIAVEIS = new NaoTerminal("<parte de declarações de variáveis>");
    public static final NaoTerminal PARTE_DE_DECLARACAO_DE_SUB_ROTINAS = new NaoTerminal("<parte de declarações de sub-rotinas>");


    public static final NaoTerminal DECLARACAO_DE_PROCEDIMENTOS = new NaoTerminal("<declaração de procedimento>");

    public static final NaoTerminal DECLARACAO_DE_VARIAVEIS = new NaoTerminal("<declaração de variáveis>");


    public static final NaoTerminal COMANDO_COMPOSTO = new NaoTerminal("<comando composto>");


    public static final NaoTerminal TIPO = new NaoTerminal("<tipo>");
    public static final NaoTerminal LISTA_DE_IDENTIFICADORES = new NaoTerminal("<lista de identificadores>");


    @Override
    public String toString() {
        return naoTerminal;
    }
}
