package br.com.finalcraft.unesp.compiladores.atividades.application.lexema;

public class Lexema {

    private String theExpression;
    private LexemaTypeEnum lexemaType;
    private int linha;
    private int start;
    private int end;

    public Lexema(String theExpression, int linha, int start, int end) {
        this.theExpression = theExpression;
        this.lexemaType = LexemaType.getOf(theExpression);
        this.linha = linha;
        this.start = start;
        this.end = end;
    }

    public String getTheExpression() {
        return theExpression;
    }

    public LexemaTypeEnum getLexemaType() {
        return lexemaType;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getLinha() {
        return linha;
    }

    @Override
    public String toString() {
        return "[Start:" + start + "][End:" + end + "]     " + theExpression + "  ⇨ " + lexemaType;
    }
}
