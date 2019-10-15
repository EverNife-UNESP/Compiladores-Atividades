package br.com.finalcraft.unesp.compiladores.atividades.application.lexema;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public enum LexemaType implements LexemaTypeEnum{
    BRANCO("[\n| |\t|\r]"),

    IDENTIFICADOR("[_|a-z|A-Z][a-z|A-Z|0-9|_]*"),
    INTEIRO("0|[1-9][0-9]*"),
    DOUBLE("(0|([1-9][0-9]*))(\\.[0-9]*)?$"),

    SOMA(Pattern.quote("+")),
    SUBTRACAO(Pattern.quote("-")),
    MULTIPLICACAO(Pattern.quote("*")),
    DIVISAO(Pattern.quote("/")),

    ABRE_PARENTESES(Pattern.quote("(")),
    FECHA_PARENTESES(Pattern.quote(")")),
    VIRGULA(Pattern.quote(",")),
    PONTO(Pattern.quote(".")),
    PONTO_E_VIRGULA(Pattern.quote(";")),
    DOIS_PONTOS(Pattern.quote(":")),
    IGUAL(Pattern.quote("=")),
    MAIOR(Pattern.quote(">")),
    MENOR(Pattern.quote("<")),
    MENOR_IGUAL(Pattern.quote("<=")),
    MAIOR_IGUAL(Pattern.quote(">=")),
    ATRIBUICAO(Pattern.quote(":="));

    public String regex;

    LexemaType(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public static LexemaTypeEnum getOf(String stringToCheck){
        LexemaTypeEnum theLexema = Error.DESCONHECIDO;
        for (LexemaType lexemaType : LexemaType.values()){
            if (stringToCheck.matches(lexemaType.getRegex())){
                theLexema = lexemaType;
                break;
            }
        }

        if (theLexema == LexemaType.IDENTIFICADOR){ //Caso seja um identificado, verificar se ele não é uma palavra reservada
            theLexema = Reservada.getOf(stringToCheck);
            if (theLexema == null) theLexema = LexemaType.IDENTIFICADOR;
        }

        theLexema = Error.checkForErrors(stringToCheck,theLexema);

        return theLexema;
    }

    public static boolean isUnichicharacterSymbol(String theExpression){
        if (theExpression.matches(SOMA.getRegex())) return true;
        if (theExpression.matches(SUBTRACAO.getRegex())) return true;
        if (theExpression.matches(MULTIPLICACAO.getRegex())) return true;
        if (theExpression.matches(DIVISAO.getRegex())) return true;
        if (theExpression.matches(ABRE_PARENTESES.getRegex())) return true;
        if (theExpression.matches(FECHA_PARENTESES.getRegex())) return true;
        if (theExpression.matches(VIRGULA.getRegex())) return true;
        if (theExpression.matches(PONTO.getRegex())) return true;
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

    @Override
    public String getLexemaName() {
        return name();
    }

    public static enum Error implements LexemaTypeEnum{
        INTEIRO_OVERSIZE(""),  //Inteiro com mais de 6 digitos
        DOUBLE_OVERSIZE(""), //6.6 Double (mais de 6 digitos antes ou depois do .
        IDENTIFICADOR_OVERSIZE(""), //Mais de 20 caracteres
        DESCONHECIDO(Pattern.quote(""));


        ;
        String regex;

        Error(String regex) {
            this.regex = regex;
        }

        public String getRegex() {
            return regex;
        }

        public static LexemaTypeEnum checkForErrors(String stringToCheck, LexemaTypeEnum currentLexema){
            if (currentLexema == LexemaType.INTEIRO){
                return  stringToCheck.length() > 6 ? Error.INTEIRO_OVERSIZE : currentLexema;
            }
            if (currentLexema == LexemaType.DOUBLE){
                String[] parts = stringToCheck.split(Pattern.quote("."));
                boolean oversize = (parts[0].length() > 6 || parts[1].length() > 6);
                return  oversize ? Error.DOUBLE_OVERSIZE : currentLexema;
            }
            if (currentLexema == LexemaType.IDENTIFICADOR){
                return  stringToCheck.length() > 20 ? IDENTIFICADOR_OVERSIZE : currentLexema;
            }
            return currentLexema;
        }

        public static LexemaTypeEnum getOf(String stringToCheck){
            for (LexemaType.Error lexemaError : LexemaType.Error.values()){
                if (stringToCheck.matches(lexemaError.getRegex())){
                    return lexemaError;
                }
            }
            return null;
        }

        @Override
        public String getLexemaName() {
            return name();
        }
    }

    public static enum Reservada implements LexemaTypeEnum{
        IF,
        THEN,
        ELSE,
        BEGIN,
        END,
        WHILE,
        DO,
        PROGRAM,
        PROCEDURE,
        TRUE,
        FALSE,
        CHAR,
        INTEGER,
        INT,
        BOOLEAN,
        VAR,
        CONST,
        AND,
        DIV,
        OR,
        NOT,
        MOD,
        IN;
        public static Map<String, Reservada> mapOfReservado = new HashMap<>();

        public static LexemaTypeEnum getOf(String stringToCheck){
            return mapOfReservado.get(stringToCheck.toLowerCase());
        }

        static {
            Arrays.stream(Reservada.values()).forEach(reservada -> mapOfReservado.put(reservada.name().toLowerCase(), reservada));
        }

        @Override
        public String toString() {
            return "RESERVADA_"+super.toString();
        }

        @Override
        public String getLexemaName() {
            return name();
        }
    }
}
