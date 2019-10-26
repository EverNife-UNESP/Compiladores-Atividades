package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data;

import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.Grammar;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaTypeEnum;

public class Tratamento implements Derivation {

    public final String actionString;
    public Action action;
    public String target;
    public LexemaTypeEnum targetLexema;
    public Grammar grammar = null;

    public Tratamento(Terminal terminal){
        this.actionString = "{replace_single_terminal_" + terminal.toString() +  "}";
        this.action = Action.REPLACE_SINGLE_TERMINAL;
        this.target = terminal.toString();
        this.targetLexema = terminal.getLexemaType();
    }

    public Tratamento(String actionString, Grammar grammar) {
        this.actionString = actionString;
        this.grammar = grammar;
        if (actionString.startsWith("{jump_to_")){
            actionString = actionString.replace("{jump_to_", "");
            action = Action.JUMP_TO;
            target = actionString.substring(0,actionString.length() - 1);
            targetLexema = LexemaType.getOf(target);
        }else if (actionString.startsWith("{fallback_to_")){
            actionString = actionString.replace("{fallback_to_", "");
            action = Action.FALLBACK_TO;
            target = actionString.substring(0,actionString.length() - 1);
            targetLexema = LexemaType.getOf(target);
        }else if (actionString.startsWith("{inplace_replace_")){
            actionString = actionString.replace("{inplace_replace_", "");
            action = Action.INPLACE_REPLACE;
            target = actionString.substring(0,actionString.length() - 1);
            targetLexema = LexemaType.getOf(target);
        }
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public Action getAction() {
        return action;
    }

    public String getTarget() {
        return target;
    }

    public LexemaTypeEnum getTargetLexema() {
        return targetLexema;
    }

    @Override
    public String toString() {
        return actionString;
    }


    public static enum Action{
        JUMP_TO,
        FALLBACK_TO,
        INPLACE_REPLACE,
        PLACE_AFTER,
        REPLACE_SINGLE_TERMINAL,
        BUGED_ACTION;
    }
}
