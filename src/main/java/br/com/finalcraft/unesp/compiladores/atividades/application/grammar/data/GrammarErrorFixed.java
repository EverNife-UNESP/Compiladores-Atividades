package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data;

import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.Grammar;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaTypeEnum;

public class GrammarErrorFixed extends GrammarError{

    public final Tratamento tratamento;

    public GrammarErrorFixed(Lexema found, LexemaTypeEnum expected, ErrorType errorType, Tratamento tratamento) {
        super(found, expected, errorType);
        this.tratamento = tratamento;
    }

    public GrammarErrorFixed(Lexema found, Grammar expectedGrammar, ErrorType errorType, Tratamento tratamento) {
        super(found, expectedGrammar, errorType);
        this.tratamento = tratamento;
    }

    public Tratamento getTratamento() {
        return tratamento;
    }
}
