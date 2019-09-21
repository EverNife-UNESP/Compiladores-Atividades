package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data;

import java.util.Arrays;
import java.util.List;

public interface Derivation {

    public static List<Derivation> valueOf(Derivation... derivation){
        return Arrays.asList(derivation);
    }
}
