package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.history;

import br.com.finalcraft.unesp.compiladores.atividades.application.AnalisadorSintatico;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.Grammar;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Derivation;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.GrammarError;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.GrammarErrorFixed;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Terminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;

import java.util.ArrayList;
import java.util.List;

public class HistoryLog implements Comparable<HistoryLog>{

    private boolean fullyMach = false;
    private int stateIndex;
    private List<HistoryLog> previousLogs;
    private List<Derivation> neededDerivations;
    private GrammarError grammarError = null;
    private HistoryMove currentMove = null;

    public HistoryLog() {
        this.stateIndex = 0;
        this.previousLogs = new ArrayList<>();
        this.neededDerivations = new ArrayList<>();
    }

    public HistoryLog[] createNewLogsFor(Grammar grammar){
        HistoryLog[] newHistoryLogs = new HistoryLog[grammar.getDerivationAmount()];

        for (int i = 0; i < grammar.getDerivacao2DList().size(); i++) {
            newHistoryLogs[i] = this.createWithNewDerivations(grammar.getDerivacao2DList().get(i));
        }

        return newHistoryLogs;
    }

    public HistoryLog createPerfectNewDerivation(){
        HistoryLog newHistoryLog = this.clone();
        newHistoryLog.setError(this.getError());
        newHistoryLog.getPreviousLogs().add(this);
        return newHistoryLog;
    }

    public HistoryLog createNewDerivation(){
        HistoryLog newHistoryLog = this.clone();
        newHistoryLog.setError(null);
        newHistoryLog.getPreviousLogs().add(this);
        return newHistoryLog;
    }

    public HistoryLog createNewDerivationWithoutCurrentMove(){
        HistoryLog newHistoryLog = this.clone();
        newHistoryLog.setError(null);
        newHistoryLog.setCurrentMove(null);
        newHistoryLog.getPreviousLogs().add(this);
        return newHistoryLog;
    }


    private HistoryLog createWithNewDerivations(List<Derivation> derivationListToADD){
        HistoryLog newThis = this.clone();

        //Null check needed for First PROGRAM check....
        if (newThis.currentMove != null) newThis.currentMove.setNonTerminalDerivations(derivationListToADD);

        HistoryLog newHistoryLog = this.clone();

        //Null check needed for First PROGRAM check....
        if (newHistoryLog.neededDerivations.size() > 0) newHistoryLog.neededDerivations.remove(0);


        newHistoryLog.previousLogs.add(newThis);
        //For inverso, adicionado as novas derivações do fim para o inicio
        for (int i = derivationListToADD.size() - 1; i >= 0; i--) {
            newHistoryLog.neededDerivations.add(0,derivationListToADD.get(i));
        }
        return newHistoryLog;
    }

    public HistoryLog getFather(int degre){
        if (degre >= this.getPreviousLogs().size()){
            return null;
        }
        return this.getPreviousLogs().get(this.getPreviousLogs().size() - degre);
    }

    public boolean hasNextDerivation(){
        return this.neededDerivations.size() > 0;
    }

    public Derivation getNextDerivation(){
        return this.neededDerivations.get(0);
    }

    public Derivation consumeTerminalDerivation(){
        return this.neededDerivations.remove(0);
    }

    public Derivation consumeNonTerminalDerivation(Lexema lexema){
        this.currentMove = new HistoryMove(lexema,this.neededDerivations.get(0));
        return this.neededDerivations.get(0);
    }

    public HistoryLog consume(Lexema currentLexema){
        this.currentMove = new HistoryMove(currentLexema,this.neededDerivations.get(0));

        HistoryLog newHistoryLog = this.clone();
        newHistoryLog.updateIndex(this.stateIndex + 1);

        Terminal terminalDerivation = (Terminal) newHistoryLog.consumeTerminalDerivation();

        newHistoryLog.previousLogs.add(this);


        if (currentLexema == null) {
            newHistoryLog.grammarError =  new GrammarError(currentLexema,terminalDerivation.getLexemaType(), GrammarError.ErrorType.UNEXPECTED_END_OF_FILE);
            return newHistoryLog;
        }

        if (terminalDerivation == null
                || currentLexema.getLexemaType() != terminalDerivation.getLexemaType()){

            newHistoryLog.grammarError =  new GrammarError(currentLexema,terminalDerivation.getLexemaType(), GrammarError.ErrorType.NO_TERMINAL_MATCH);
            return newHistoryLog;
        }

        return newHistoryLog;
    }

    public void setCurrentMove(HistoryMove historyMove){
        this.currentMove = historyMove;
    }


    @Override
    public HistoryLog clone(){
        HistoryLog newHistoryLog = new HistoryLog();

        newHistoryLog.grammarError = this.grammarError;
        newHistoryLog.stateIndex = this.stateIndex;

        newHistoryLog.previousLogs.addAll(this.previousLogs);
        newHistoryLog.neededDerivations.addAll(this.neededDerivations);

        newHistoryLog.currentMove = this.currentMove != null ? this.currentMove.clone() : null;
        return newHistoryLog;
    }

    public void setFullyMach(){
        this.fullyMach = true;
    }

    public boolean isFullyMach(){
        return this.fullyMach;
    }

    public boolean isErrored(){
        return this.grammarError != null && !(this.grammarError instanceof GrammarErrorFixed);
    }

    public boolean isFixed(){
        return this.grammarError != null && this.grammarError instanceof GrammarErrorFixed;
    }

    public HistoryLog setError(GrammarError grammarError){
        this.grammarError = grammarError;
        return this;
    }

    public GrammarError getError() {
        return grammarError;
    }

    public int getStateIndex() {
        return stateIndex;
    }

    public List<HistoryLog> getPreviousLogs() {
        return previousLogs;
    }

    public List<Derivation> getNeededDerivations() {
        return neededDerivations;
    }

    public void restoreMachineState(){
        AnalisadorSintatico.restoreMachineState(this);
    }

    public void updateIndex(int newIndex){
        this.stateIndex = newIndex;
    }



    @Override
    public int compareTo(HistoryLog o) {
        return Integer.compare(this.stateIndex,o.stateIndex);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n---------- [ HistoryLog ] ----------");
        stringBuilder.append("\n");
        stringBuilder.append("\nCurrentIndex: " + stateIndex);
        stringBuilder.append("\nPreviousLogsSize: " + previousLogs.size());
        stringBuilder.append("\nNeeded Derivations: ");
        stringBuilder.append("\n               ");

        for (int i = neededDerivations.size() - 1; i >= 0; i--) {
            stringBuilder.append("\n               - " + neededDerivations.get(i).toString());
        }

        if (currentMove != null) stringBuilder.append("\n\n    CurrentMove: " + currentMove);

        return stringBuilder.toString();
    }
}