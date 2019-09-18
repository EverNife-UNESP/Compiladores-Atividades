package br.com.finalcraft.unesp.compiladores.atividades.application.grammar;

import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Derivacao;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.NaoTerminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Terminal;

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

    // -----------------------------------------------------------------------------------------------------------------
    // Program
    // -----------------------------------------------------------------------------------------------------------------
    public static final Grammar PROGRAM = new Grammar(NaoTerminal.PROGRAM)
            .addDerivacao(Derivacao.valueOf(new Terminal("program"), new Terminal("identificador"), new Terminal(";"), NaoTerminal.BLOCO, new Terminal(".")));


    // -----------------------------------------------------------------------------------------------------------------
    // Bloco
    // -----------------------------------------------------------------------------------------------------------------
    public static final Grammar BLOCO = new Grammar(NaoTerminal.BLOCO)
            .addDerivacao(Derivacao.valueOf(NaoTerminal.PARTE_DE_DECLARACAO_DE_VARIAVEIS,NaoTerminal.PARTE_DE_DECLARACAO_DE_SUB_ROTINAS,NaoTerminal.COMANDO_COMPOSTO));


    // -----------------------------------------------------------------------------------------------------------------
    // Parte de Declaração de Variáveis
    // -----------------------------------------------------------------------------------------------------------------
    public static final Grammar PARTE_DE_DECLARACAO_DE_VARIAVEIS = new Grammar(NaoTerminal.PARTE_DE_DECLARACAO_DE_VARIAVEIS)
            .addDerivacao(Derivacao.valueOf(NaoTerminal.DECLARACAO_DE_VARIAVEIS, new Terminal(";"),NaoTerminal.PARTE_DE_DECLARACAO_DE_VARIAVEIS))
            .addDerivacao(Derivacao.valueOf());//Derivação vazia --> Optional


    // -----------------------------------------------------------------------------------------------------------------
    // Declaração de Variáveis
    // -----------------------------------------------------------------------------------------------------------------
    public static final Grammar DECLARACAO_DE_VARIAVEIS = new Grammar(NaoTerminal.DECLARACAO_DE_VARIAVEIS)
            .addDerivacao(Derivacao.valueOf(NaoTerminal.TIPO,NaoTerminal.TIPO, new Terminal(";"),NaoTerminal.PARTE_DE_DECLARACAO_DE_VARIAVEIS))
            .addDerivacao(Derivacao.valueOf());//Derivação vazia --> Optional

    // -----------------------------------------------------------------------------------------------------------------
    // Parte de Declaração de SubRotinas
    // -----------------------------------------------------------------------------------------------------------------
    public static final Grammar PARTE_DE_DECLARACAO_DE_SUB_ROTINAS = new Grammar(NaoTerminal.PARTE_DE_DECLARACAO_DE_SUB_ROTINAS)
            .addDerivacao(Derivacao.valueOf(NaoTerminal.DECLARACAO_DE_PROCEDIMENTOS,NaoTerminal.PARTE_DE_DECLARACAO_DE_SUB_ROTINAS))
            .addDerivacao(Derivacao.valueOf());//Derivação vazia --> Optional



}
