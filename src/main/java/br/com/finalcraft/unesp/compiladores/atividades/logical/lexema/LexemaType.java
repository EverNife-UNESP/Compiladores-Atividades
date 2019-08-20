package br.com.finalcraft.unesp.compiladores.atividades.logical.lexema;

import java.util.regex.Pattern;

public enum LexemaType {
    BRANCO("[\n| |\t|\r]"),

    IDENTIFICADOR("[_|a-z|A-Z][a-z|A-Z|0-9|_]{1,20}"), //20
    INTEIRO("0|[1-9][0-9]{0,5}"),  //6
    DOUBLE("(0|([1-9][0-9]{0,5}))(\\.[0-9]{1,5})?$"), //6.6 Double

    SOMA(Pattern.quote("+")),
    SUBTRACAO(Pattern.quote("-")),
    MULTIPLICACAO(Pattern.quote("*")),
    DIVISAO(Pattern.quote("/")),

    ABRE_PARENTESES(Pattern.quote("(")),
    FECHA_PARENTESES(Pattern.quote(")")),
    VIRGULA(Pattern.quote(",")),
    PONTO_E_VIRGULA(Pattern.quote(";")),
    DOIS_PONTOS(Pattern.quote(":")),
    IGUAL(Pattern.quote("=")),
    MAIOR(Pattern.quote(">")),
    MENOR(Pattern.quote("<")),

    MENOR_IGUAL(Pattern.quote("<=")),
    MAIOR_IGUAL(Pattern.quote(">=")),
    ATRIBUICAO(Pattern.quote(":=")),
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

    public static boolean isUnichicharacterSymbol(String theExpression){
        if (theExpression.matches(SOMA.getRegex())) return true;
        if (theExpression.matches(SUBTRACAO.getRegex())) return true;
        if (theExpression.matches(MULTIPLICACAO.getRegex())) return true;
        if (theExpression.matches(DIVISAO.getRegex())) return true;
        if (theExpression.matches(ABRE_PARENTESES.getRegex())) return true;
        if (theExpression.matches(FECHA_PARENTESES.getRegex())) return true;
        if (theExpression.matches(VIRGULA.getRegex())) return true;
        if (theExpression.matches(PONTO_E_VIRGULA.getRegex())) return true;
        if (theExpression.matches(DOIS_PONTOS.getRegex())) return true;
        if (theExpression.matches(IGUAL.getRegex())) return true;
        if (theExpression.matches(MAIOR.getRegex())) return true;
        if (theExpression.matches(MENOR.getRegex())) return true;
        return false;
    }

    public static boolean isDoubleCharacterSymbol(String theExpression){
        if (theExpression.matches(MENOR_IGUAL.getRegex())) return true;
        if (theExpression.matches(MAIOR_IGUAL.getRegex())) return true;
        if (theExpression.matches(ATRIBUICAO.getRegex())) return true;
        return false;
    }
}
