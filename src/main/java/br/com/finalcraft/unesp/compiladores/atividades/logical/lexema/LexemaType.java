package br.com.finalcraft.unesp.compiladores.atividades.logical.lexema;

import java.util.regex.Pattern;

public enum LexemaType {
    BRANCO("[\n| |\t|\r]"),
    IDENTIFICADOR("[_|a-z|A-Z][a-z|A-Z|0-9|_]*"),
    INTEIRO("0|[1-9][0-9]{0,19}"),
    DOUBLE("(0|([1-9][0-9]{0,19}))(\\.[0-9]{1,20})?$"),

    SOMA(Pattern.quote("+")),
    SUBTRACAO(Pattern.quote("-")),
    MULTIPLICACAO(Pattern.quote("*")),
    DIVISAO(Pattern.quote("/")),

    ABRE_PARENTESES(Pattern.quote("(")),
    FECHA_PARENTESES(Pattern.quote(")")),
    VIRGULA(Pattern.quote(",")),
    PONT_E_VIRGULA(Pattern.quote(";")),
    DOIS_PONTOS(Pattern.quote(":")),

    DESCONHECIDO(Pattern.quote(""));

    public String regex;

    LexemaType(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public static LexemaType getOf(String stringToCheck){
        for (LexemaType lexemaType : LexemaType.values()){
            if (stringToCheck.matches(lexemaType.getRegex())){
                return lexemaType;
            }
        }
        return LexemaType.DESCONHECIDO;
    }

    public static boolean isUnichicharacterSymbol(String theEpression){
        if (theEpression.matches(SOMA.getRegex())) return true;
        if (theEpression.matches(SUBTRACAO.getRegex())) return true;
        if (theEpression.matches(MULTIPLICACAO.getRegex())) return true;
        if (theEpression.matches(DIVISAO.getRegex())) return true;

        return false;
    }
}
