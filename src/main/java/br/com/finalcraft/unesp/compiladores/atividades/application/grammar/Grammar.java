package br.com.finalcraft.unesp.compiladores.atividades.application.grammar;

import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Derivacao;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.NaoTerminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Terminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;

import java.util.ArrayList;
import java.util.List;

public class Grammar {

    private final NaoTerminal origem;
    private final List<List<Derivacao>> derivacaoList = new ArrayList<List<Derivacao>>();
            //Uma lista de listas de derivações possívels

            // S -> aAa | bAb | cDc             Ou seja, varias derivações diferentes, com sequencies de terminais e nao terminais

    public Grammar(NaoTerminal origem) {
        this.origem = origem;
        origem.setGrammar(this);
    }

    public List<List<Derivacao>> getDerivacao2DList() {
        return derivacaoList;
    }

    private Grammar addDerivacao(List<Derivacao> derivacoes){
        derivacaoList.add(derivacoes);
        return this;
    }

    public NaoTerminal getOrigem() {
        return origem;
    }

    public static final Grammar PROGRAM = new Grammar(NaoTerminal.PROGRAM)
            .addDerivacao(Derivacao.valueOf(new Terminal("program"), new Terminal("identificador"), new Terminal(";"), NaoTerminal.BLOCO, new Terminal(".")));

    public static final Grammar BLOCO = new Grammar(NaoTerminal.BLOCO)
            .addDerivacao(Derivacao.valueOf(new Terminal("if"), new Terminal("(")))
            .addDerivacao(Derivacao.valueOf(new Terminal("identificador"), new Terminal(";")));


}
