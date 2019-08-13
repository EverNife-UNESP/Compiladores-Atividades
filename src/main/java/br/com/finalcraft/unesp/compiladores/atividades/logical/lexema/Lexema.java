package br.com.finalcraft.unesp.compiladores.atividades.logical.lexema;

public class Lexema {

    private String theExpression;
    private LexemaType lexemaType;
    private int start;
    private int end;

    public Lexema(String theExpression, int start, int end) {
        this.theExpression = theExpression;
        this.lexemaType = LexemaType.getOf(theExpression);
        this.start = start;
        this.end = end;
    }

    public String getTheExpression() {
        return theExpression;
    }

    public LexemaType getLexemaType() {
        return lexemaType;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "[Start:" + start + "][End:" + end + "]     " + theExpression + "  ⇨ " + lexemaType;
    }
}
