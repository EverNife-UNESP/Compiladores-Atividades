package br.com.finalcraft.unesp.compiladores.atividades.application.grammar.history;

import br.com.finalcraft.unesp.compiladores.atividades.application.AnalisadorSintatico;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.Grammar;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Derivation;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.GrammarError;
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

    private HistoryLog createWithNewDerivations(List<Derivation> derivationListToADD){
        HistoryLog newHistoryLog = this.clone();

        newHistoryLog.previousLogs.add(this);
        //For inverso, adicionado as novas derivações do fim para o inicio
        for (int i = derivationListToADD.size() - 1; i >= 0; i--) {
            newHistoryLog.neededDerivations.add(0,derivationListToADD.get(i));
        }
        return newHistoryLog;
    }

    public boolean hasNextDerivation(){
        return this.neededDerivations.size() > 0;
    }

    public Derivation getNextDerivation(){
        return this.neededDerivations.get(0);
    }

    public Derivation consumeNonTerminalDerivation(){
        return this.neededDerivations.remove(0);
    }

    public GrammarError consume(Lexema currentLexema){
        Terminal terminalDerivation = (Terminal) consumeNonTerminalDerivation();

        if (currentLexema == null) {
            return new GrammarError(currentLexema,terminalDerivation.getLexemaType(), GrammarError.ErrorType.UNEXPECTED_END_OF_FILE);
        }

        if (currentLexema.getLexemaType() != terminalDerivation.getLexemaType()){
            return new GrammarError(currentLexema,terminalDerivation.getLexemaType(), GrammarError.ErrorType.NO_TERMINAL_MATCH);
        }

        return null;
    }


    @Override
    public HistoryLog clone(){
        HistoryLog newHistoryLog = new HistoryLog();

        newHistoryLog.grammarError = this.grammarError;
        newHistoryLog.stateIndex = this.stateIndex;

        newHistoryLog.previousLogs.addAll(this.previousLogs);
        newHistoryLog.neededDerivations.addAll(this.neededDerivations);

        return newHistoryLog;
    }

    public void setFullyMach(){
        this.fullyMach = true;
    }

    public boolean isFullyMach(){
        return this.fullyMach;
    }

    public boolean isErrored(){
        return this.grammarError != null;
    }

    public void setError(GrammarError grammarError){
        this.grammarError = grammarError;
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

        return stringBuilder.toString();
    }
}