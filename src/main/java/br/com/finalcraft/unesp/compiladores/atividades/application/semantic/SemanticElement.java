package br.com.finalcraft.unesp.compiladores.atividades.application.semantic;

import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;

public class SemanticElement extends Lexema{

    private int escopo = 0;
    private boolean utilizada = false;
    private Object valor = 0;
    private VarType varType;

    private boolean isErrored;

    private final Lexema originalLexema;

    public boolean isErrored() {
        return isErrored;
    }

    public SemanticElement(Lexema lexema){
        super(lexema);
        originalLexema = lexema;
    }

    public int getEscopo() {
        return escopo;
    }

    public void setEscopo(int escopo) {
        this.escopo = escopo;
    }

    public void setUtilizada() {
        if (this.utilizada == false) this.utilizada = true;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public VarType getVarType() {
        return varType;
    }

    public String getVarTypeString() {
        return varType.toString().substring(10);
    }

    public void setVarType(VarType varType) {
        this.varType = varType;
    }

    public void setVarType(LexemaType.Reservada reservada){
        switch (reservada){
            case BOOLEAN:
                this.varType = VarType.BOOLEAN;
                break;
            case INT:
            case INTEGER:
                this.varType = VarType.INT;
                break;
            case PROCEDURE:
                this.varType = VarType.PROCEDIMENTO;
                break;
        }
    }

    public boolean isUtilizada() {
        return utilizada;
    }

    public Object getValor() {
        return valor;
    }

    public Lexema getOriginalLexema() {
        return originalLexema;
    }

    public void setTheExpression(String newExpression){
        this.theExpression = newExpression;
    }

    public static enum VarType{
        BOOLEAN,
        INT,
        PROCEDIMENTO;
       // FLOAT;
    }


}
