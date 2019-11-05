package br.com.finalcraft.unesp.compiladores.atividades.application.grammar;

import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Derivation;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.NaoTerminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Terminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grammar {

    private final NaoTerminal origem;
    private final List<List<Derivation>> derivacaoList = new ArrayList<List<Derivation>>();
    private static final Map<String,Derivation> mapOfDerivation = new HashMap<String, Derivation>();

            //Uma lista de listas de derivações possívels

            // S -> aAa | bAb | cDc             Ou seja, varias derivações diferentes, com sequencies de terminais e nao terminais

    public Grammar(NaoTerminal origem) {
        this.origem = origem;
        mapOfgrammars.put(origem.toString(),this);

        mapOfDerivation.put(origem.toString(),origem);
    }

    public List<List<Derivation>> getDerivacao2DList() {
        return derivacaoList;
    }

    public int getDerivationAmount(){
        return derivacaoList.size();
    }

    public Grammar addDerivacao(List<Derivation> derivacoes){
        derivacaoList.add(derivacoes);

        derivacoes.forEach(derivation -> {
            mapOfDerivation.put(derivation.toString(),derivation);
        });
        return this;
    }

    public NaoTerminal getOrigem() {
        return origem;
    }

    public Terminal getFollow(){
        return null;
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

    public static Grammar getGrammar(NaoTerminal naoTerminal){
        return mapOfgrammars.get(naoTerminal.toString());
    }

    public static Derivation getDerivation(String derivation){
        return mapOfDerivation.get(derivation);
    }

    static {
        Grammar GRAMAR_NUMERO = new Grammar(new NaoTerminal("<número>"))
                .addDerivacao(Derivation.valueOf(new Terminal("123")))   //Inteiro
                .addDerivacao(Derivation.valueOf(new Terminal("1.1")));  //Double

        mapOfgrammars.put(new NaoTerminal("<número>").toString(), GRAMAR_NUMERO);

      /*  Grammar IDENTIFICADOR = new Grammar(new NaoTerminal("<identificador>"))
               .addDerivacao(Derivation.valueOf(new Terminal("qualquerCoisa")));   //Identificador Qualquer
        mapOfgrammars.put(new NaoTerminal("<identificador>").toString(), IDENTIFICADOR);
          */
    }

}
