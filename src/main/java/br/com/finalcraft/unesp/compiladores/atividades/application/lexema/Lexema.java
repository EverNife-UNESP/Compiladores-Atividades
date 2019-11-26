package br.com.finalcraft.unesp.compiladores.atividades.application.lexema;

import br.com.finalcraft.unesp.compiladores.atividades.application.AnalisadorLexico;

public class Lexema {

    protected String theExpression;
    protected LexemaTypeEnum lexemaType;
    protected int linha;
    protected int start;
    protected int end;
    protected int id = -1;

    public Lexema(Lexema otherLexema) {
        this.theExpression = otherLexema.theExpression;
        this.lexemaType = otherLexema.lexemaType;
        this.linha = otherLexema.linha;
        this.start = otherLexema.start;
        this.end = otherLexema.end;
        this.id = otherLexema.id;
    }

    public Lexema(String theExpression, int linha, int start, int end) {
        this.theExpression = theExpression;
        this.lexemaType = LexemaType.getOf(theExpression);
        this.linha = linha;
        this.start = start + (linha > 1 ? -1 : 0);
        this.end = end + (linha > 1 ? -1 : 0);
        this.id = ++AnalisadorLexico.currentID;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "[line: " + linha + ", start:" + start + ", end:" + end + ", expression:\'" + theExpression + "\',  lexemaType:" + lexemaType + "]";
    }

    public String simpleToString(){
        return "[expression:\'" + theExpression + "\',  lexemaType:" + lexemaType + "]";
    }
}
