package br.com.finalcraft.unesp.compiladores.atividades.logical.lexema;

import java.util.regex.Pattern;

public enum LexemaType {
    BRANCO("[\n| |\t|\r]"),
    IDENTIFICADOR("[_|a-z|A-Z][a-z|A-Z|0-9|_]*"),
    INTEIRO("0|[1-9][0-9]*"),
    DOUBLE("(0|([1-9][0-9]*))(\\.[0-9]+)?$"),

    SOMA(Pattern.quote("+")),
    SUBTRACAO(Pattern.quote("-")),
    MULTIPLICACAO(Pattern.quote("*")),
    DIVISAO(Pattern.quote("/")),

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
}
