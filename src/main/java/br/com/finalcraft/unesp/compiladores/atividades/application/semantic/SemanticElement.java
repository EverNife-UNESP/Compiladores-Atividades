package br.com.finalcraft.unesp.compiladores.atividades.application.semantic;

import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;

public class SemanticElement extends Lexema{

    private int escopo = 0;
    private boolean utiliza = false;
    private Object valor = 0;
    private LexemaType.Reservada varType;

    private boolean isErrored;

    public boolean isErrored() {
        return isErrored;
    }

    public SemanticElement(Lexema lexema){
        super(lexema);
    }

    public int getEscopo() {
        return escopo;
    }

    public void setEscopo(int escopo) {
        this.escopo = escopo;
    }

    public void setUtiliza(boolean utiliza) {
        this.utiliza = utiliza;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public LexemaType.Reservada getVarType() {
        return varType;
    }

    public void setVarType(LexemaType.Reservada varType) {
        this.varType = varType;
    }

    public boolean isUtiliza() {
        return utiliza;
    }

    public Object getValor() {
        return valor;
    }

}
