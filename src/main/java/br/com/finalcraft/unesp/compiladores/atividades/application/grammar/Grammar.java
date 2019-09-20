package br.com.finalcraft.unesp.compiladores.atividades.application.grammar;

import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Derivacao;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.NaoTerminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Terminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grammar {

    private final NaoTerminal origem;
    private final List<List<Derivacao>> derivacaoList = new ArrayList<List<Derivacao>>();
            //Uma lista de listas de derivações possívels

            // S -> aAa | bAb | cDc             Ou seja, varias derivações diferentes, com sequencies de terminais e nao terminais

    public Grammar(NaoTerminal origem) {
        this.origem = origem;
        mapOfgrammars.put(origem.toString(),this);
    }

    public List<List<Derivacao>> getDerivacao2DList() {
        return derivacaoList;
    }

    public Grammar addDerivacao(List<Derivacao> derivacoes){
        derivacaoList.add(derivacoes);
        return this;
    }

    public NaoTerminal getOrigem() {
        return origem;
    }


    // -----------------------------------------------------------------------------------------------------------------
    // Controller
    // -----------------------------------------------------------------------------------------------------------------
    public static Map<String, Grammar> mapOfgrammars = new HashMap<String, Grammar>();

    public static Grammar getOrCreateGrammar(NaoTerminal naoTerminal){
        Grammar grammar = mapOfgrammars.get(naoTerminal.toString());
        if (grammar == null){
            grammar = new Grammar(naoTerminal);
        }
        return grammar;
    }

    static {
        Grammar GRAMAR_NUMERO = new Grammar(new NaoTerminal("<número>"))
                .addDerivacao(Derivacao.valueOf(new Terminal("123")))   //Inteiro
                .addDerivacao(Derivacao.valueOf(new Terminal("1.1")));  //Double

        Grammar IDENTIFICADOR = new Grammar(new NaoTerminal("<identificador>"))
                .addDerivacao(Derivacao.valueOf(new Terminal("qualquerCoisa")));   //Identificador Qualquer

        mapOfgrammars.put(new NaoTerminal("<número>").toString(), GRAMAR_NUMERO);
        mapOfgrammars.put(new NaoTerminal("<identificador>").toString(), IDENTIFICADOR);
    }
}
