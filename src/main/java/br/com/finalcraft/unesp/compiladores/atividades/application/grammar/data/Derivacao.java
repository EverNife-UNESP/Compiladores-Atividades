package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data;

import java.util.Arrays;
import java.util.List;

public interface Derivacao {

    public static List<Derivacao> valueOf(Derivacao... derivacao){
        return Arrays.asList(derivacao);
    }
}
