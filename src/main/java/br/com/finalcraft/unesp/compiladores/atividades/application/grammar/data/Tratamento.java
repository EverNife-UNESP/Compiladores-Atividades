package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data;

import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaTypeEnum;

public class Tratamento implements Derivation {

    public final String actionString;
    public Action action;
    public String target;
    public LexemaTypeEnum targetLexema;

    public Tratamento(String actionString) {
        this.actionString = actionString;
        if (actionString.startsWith("{jump_to_")){
            actionString = actionString.replace("{jump_to_", "");
            action = Action.JUMP_TO;
            target = actionString.substring(0,actionString.length() - 1);
            targetLexema = LexemaType.getOf(target);
        }
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
        BUGED_ACTION;
    }
}
