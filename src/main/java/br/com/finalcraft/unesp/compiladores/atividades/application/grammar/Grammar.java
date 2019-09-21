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
            //Uma lista de listas de derivações possívels

            // S -> aAa | bAb | cDc             Ou seja, varias derivações diferentes, com sequencies de terminais e nao terminais

    public Grammar(NaoTerminal origem) {
        this.origem = origem;
        mapOfgrammars.put(origem.toString(),this);
    }

    public List<List<Derivation>> getDerivacao2DList() {
        return derivacaoList;
    }

    public int getDerivationAmount(){
        return derivacaoList.size();
    }

    public Grammar addDerivacao(List<Derivation> derivacoes){
        derivacaoList.add(derivacoes);
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

    static {
        Grammar GRAMAR_NUMERO = new Grammar(new NaoTerminal("<número>"))
                .addDerivacao(Derivation.valueOf(new Terminal("123")))   //Inteiro
                .addDerivacao(Derivation.valueOf(new Terminal("1.1")));  //Double

        Grammar IDENTIFICADOR = new Grammar(new NaoTerminal("<identificador>"))
                .addDerivacao(Derivation.valueOf(new Terminal("qualquerCoisa")));   //Identificador Qualquer

        Grammar VARIAVEL = new Grammar(new NaoTerminal("<variavel>"))
                .addDerivacao(Derivation.valueOf(new Terminal("umaVariavel")));      //Variavel Qualquer

        mapOfgrammars.put(new NaoTerminal("<número>").toString(), GRAMAR_NUMERO);
        mapOfgrammars.put(new NaoTerminal("<identificador>").toString(), IDENTIFICADOR);
        mapOfgrammars.put(new NaoTerminal("<variavel>").toString(), VARIAVEL);
    }




    /*

        //
        // Tudo daqui para baixo foi alterado e feito de forma automática por intermédio do GramaticalImporter.class
        //
        // Teoricamente eu teria que fazer todas as regras dessa forma, foi mais facil fazer a automção...
        //

    // -----------------------------------------------------------------------------------------------------------------
    // Program
    // -----------------------------------------------------------------------------------------------------------------
    public static final Grammar PROGRAM = new Grammar(NaoTerminal.PROGRAM)
            .addDerivacao(Derivation.valueOf(new Terminal("program"), new Terminal("identificador"), new Terminal(";"), NaoTerminal.BLOCO, new Terminal(".")));


    // -----------------------------------------------------------------------------------------------------------------
    // Bloco
    // -----------------------------------------------------------------------------------------------------------------
    public static final Grammar BLOCO = new Grammar(NaoTerminal.BLOCO)
            .addDerivacao(Derivation.valueOf(NaoTerminal.PARTE_DE_DECLARACAO_DE_VARIAVEIS,NaoTerminal.PARTE_DE_DECLARACAO_DE_SUB_ROTINAS,NaoTerminal.COMANDO_COMPOSTO));


    // -----------------------------------------------------------------------------------------------------------------	    // -----------------------------------------------------------------------------------------------------------------
    // Parte de Declaração de Variáveis	    // Controller
    // -----------------------------------------------------------------------------------------------------------------	    // -----------------------------------------------------------------------------------------------------------------
    public static final Grammar PARTE_DE_DECLARACAO_DE_VARIAVEIS = new Grammar(NaoTerminal.PARTE_DE_DECLARACAO_DE_VARIAVEIS)	    public static Map<String, Grammar> mapOfgrammars = new HashMap<String, Grammar>();
            .addDerivacao(Derivation.valueOf(NaoTerminal.DECLARACAO_DE_VARIAVEIS, new Terminal(";"),NaoTerminal.PARTE_DE_DECLARACAO_DE_VARIAVEIS))
            .addDerivacao(Derivation.valueOf());//Derivação vazia --> Optional


    // -----------------------------------------------------------------------------------------------------------------	    public static Grammar getOrCreateGrammar(NaoTerminal naoTerminal){
    // Declaração de Variáveis	        Grammar grammar = mapOfgrammars.get(naoTerminal.toString());
    // -----------------------------------------------------------------------------------------------------------------	        if (grammar == null){
    public static final Grammar DECLARACAO_DE_VARIAVEIS = new Grammar(NaoTerminal.DECLARACAO_DE_VARIAVEIS)	            grammar = new Grammar(naoTerminal);
            .addDerivacao(Derivation.valueOf(NaoTerminal.TIPO,NaoTerminal.TIPO, new Terminal(";"),NaoTerminal.PARTE_DE_DECLARACAO_DE_VARIAVEIS))	        }
            .addDerivacao(Derivation.valueOf());//Derivação vazia --> Optional	        return grammar;


    // -----------------------------------------------------------------------------------------------------------------
    // Parte de Declaração de SubRotinas
    // -----------------------------------------------------------------------------------------------------------------
    public static final Grammar PARTE_DE_DECLARACAO_DE_SUB_ROTINAS = new Grammar(NaoTerminal.PARTE_DE_DECLARACAO_DE_SUB_ROTINAS)
            .addDerivacao(Derivation.valueOf(NaoTerminal.DECLARACAO_DE_PROCEDIMENTOS,NaoTerminal.PARTE_DE_DECLARACAO_DE_SUB_ROTINAS))
            .addDerivacao(Derivation.valueOf());//Derivação vazia --> Optional
     */
}
