package br.com.finalcraft.unesp.compiladores.atividades.application;


import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.Grammar;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Derivation;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.GrammarError;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.NaoTerminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Terminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.history.HistoryLog;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaTypeEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AnalisadorSintatico {

    public static List<Lexema> todosLexemas = new ArrayList<Lexema>();
    private static int tamanhoLexemas = 0;
    private static int index = 0;
    private static List<HistoryLog> errorTrackerLogs = new ArrayList<HistoryLog>();

    public static void restoreMachineState(HistoryLog historyLog){
        index = historyLog.getStateIndex();
    }

    public static HistoryLog analiseSintatica(List<Lexema> lexemas){

        todosLexemas = lexemas;
        tamanhoLexemas = todosLexemas.size();
        index = 0;
        errorTrackerLogs.clear();

        System.out.println("\n\nIniciando analise sintática (" + lexemas.size() + " lexemas encontrados)\n\n");

        Grammar GRAMMAR_PROGRAM = Grammar.getOrCreateGrammar(new NaoTerminal("<programa>"));

      //  checkGrammmar(GRAMMAR_PROGRAM);


        HistoryLog currentHistoryLog = checkGrammmarMark2(new HistoryLog().createNewLogsFor(GRAMMAR_PROGRAM)[0]);

        System.out.println("RETURNED HISTORY LOG");
        printHistoryLog(currentHistoryLog);
        System.out.println("FullyMatch????:  " + currentHistoryLog.isFullyMach());
        System.out.println("FullyMatch????:  " + currentHistoryLog.isFullyMach());
        System.out.println("FullyMatch????:  " + currentHistoryLog.isFullyMach());
        System.out.println("FullyMatch????:  " + currentHistoryLog.isFullyMach());

        HistoryLog topErrorHistoryLog = getTopError();
        if (topErrorHistoryLog != null){
            System.out.println("TopErroredLog");
            System.out.println("TopErroredLog");
            System.out.println("TopErroredLog");
            System.out.println("TopErroredLog");
            System.out.println("TopErroredLog");
            System.out.println("TopErroredLog");
            System.out.println("TopErroredLog");
            System.out.println("TopErroredLog");
            System.out.println("TopErroredLog");
            System.out.println("TopErroredLog");
           printHistoryLog(topErrorHistoryLog);
        }else {
            System.out.println("Top Error is null!!!!");
        }

        return currentHistoryLog.isFullyMach() ? currentHistoryLog : topErrorHistoryLog;
    }

    private static void printHistoryLog(HistoryLog historyLog){
        System.out.println(historyLog);

        List<HistoryLog> historyLogs = new ArrayList<HistoryLog>();
        historyLogs.addAll(historyLog.getPreviousLogs());

        int j = 1;
        //HistoryLog previousLog = null;
        for (HistoryLog log : historyLogs) {
            System.out.println("HistoryLog [" + (j++) + "]");
            System.out.println(log);
          //  if (log.isErrored() && log.getError().getErrorType() != GrammarError.ErrorType.NO_UNTERNIMAL_MATCH) System.out.println(log.getError());
           // if (previousLog != null && previousLog.getNeededDerivations().size() > 0) System.out.println("Consumed " + todosLexemas.get(log.getStateIndex() - 1) + " with " + previousLog.getNeededDerivations().get(0) );
           // previousLog = log;
        }
    }


    public static HistoryLog getTopError(){
        Collections.sort(errorTrackerLogs);//Ordena do menor para o Maior
        Collections.reverse(errorTrackerLogs);//Inverte a ordem, pois quero do maior para o menor.
        return errorTrackerLogs.size() > 0 ? errorTrackerLogs.get(0) : null;
    }


    private static boolean debug = false;
    private static void debug(String msg){
        if (debug) System.out.println(msg);
    }

    private static HistoryLog checkGrammmarMark2(HistoryLog previousLog){

        debug(previousLog.toString());
       // errorTrackerLogs.add(previousLog);

        //Futuro tratamento de error vem aqui.... eu acho, vamo ve no que vai dar o resto...
        if (previousLog.isErrored()){
            debug("<-- Previous HistoryLog was Errored! + " + previousLog.getError() +  " \n");
            errorTrackerLogs.add(previousLog);
            return previousLog;
        }

        HistoryLog currentHistoryLog = previousLog.clone();
        currentHistoryLog.restoreMachineState();
        Lexema currentLexema = getCurrentLexema();

        if (!currentHistoryLog.hasNextDerivation()){
            if (currentLexema == null){
                currentHistoryLog.setFullyMach();
                debug("\n\n<-------- FullyMatch!\n\n");
                return currentHistoryLog;
            }else {
                currentHistoryLog.setError(new GrammarError(currentLexema,(LexemaTypeEnum) null, GrammarError.ErrorType.UNEXPECTED_END_OF_NON_TERMINALS));
                return checkGrammmarMark2(currentHistoryLog);
            }
        }

        Derivation derivation = currentHistoryLog.getNextDerivation();
        //Lógica para caso a derivação seja TERMINAL
        if (derivation instanceof Terminal){
            HistoryLog newHistoryLog = currentHistoryLog.consume(currentLexema);
            debug("Consuming " + currentLexema);
            return checkGrammmarMark2(newHistoryLog);
        }

        //Lógica para caso a derivação seja NÃO_TERMINAL
        NaoTerminal naoTerminal = (NaoTerminal)derivation;
        currentHistoryLog.consumeNonTerminalDerivation(currentLexema);
        Grammar derivationGrammar = Grammar.getGrammar(naoTerminal);
        HistoryLog[] derivativeHistoryLogs = currentHistoryLog.createNewLogsFor(derivationGrammar);

        for (HistoryLog derivativeHistoryLog : derivativeHistoryLogs) {

            HistoryLog resultantHistoryLog = checkGrammmarMark2(derivativeHistoryLog);

            if (resultantHistoryLog.isErrored()){
                continue;
            }

            if (resultantHistoryLog.isFullyMach()){
                return checkGrammmarMark2(resultantHistoryLog);
            }
        }

        currentHistoryLog.setError(new GrammarError(currentLexema,derivationGrammar, GrammarError.ErrorType.NO_UNTERNIMAL_MATCH));
        return checkGrammmarMark2(currentHistoryLog);
    }

    private static void ignoreAllLexemasUntilNextSemiColum(){
        for (int i = index; i < todosLexemas.size(); i++) {
            if (todosLexemas.get(i).getLexemaType() == LexemaType.PONTO_E_VIRGULA){
                index = i + 1;
                return;
            }
        }
    }

    private static Lexema getCurrentLexema(){
        if (index >= tamanhoLexemas) return null;
        return todosLexemas.get(index);
    }

    private static HistoryLog startingGrammar(){
        return new HistoryLog();
    }

    //OLD VERSION
    /*
    private static List<GrammarError> checkGrammmar(Grammar grammar){

        HistoryLog historyLog = startingGrammar();          // Seta o grammarStartIndex para caso de errado alguma derivação

        List<GrammarError> grammarErrorList = new ArrayList<GrammarError>();

        System.out.println("\n\nCheking Grammar [" + grammar.getOrigem() + "]");

        int iteration = 0;
        boolean checkNextList = true;
        for (List<Derivation> derivationList : grammar.getDerivacao2DList()) {
            if (checkNextList == false){
                break;
            }
            grammarErrorList.clear();
            iteration++;
            if (iteration > 1){//Restaura a historyLog, caso esteja na segunda iteração já vai ser necessário
                historyLog.restoreMachineState();
            }

            String derivacaoListString = derivationList.stream().map(Object::toString)
                    .collect(Collectors.joining("\'\n        - \'"));

            System.out.println("\n    DerivationList [" + iteration +"]: \n        - \'" + derivacaoListString + "\'");

            boolean forceNextCheck = false;
            for (Derivation derivation : derivationList) {

                System.out.println("\n        CurrentDerivation [" + grammar.getOrigem() + "] \'" + derivation + "\'");

                if (derivation instanceof Terminal){
                    final Lexema currentLexema = getCurrentLexema();
                    if (currentLexema == null) {
                        forceNextCheck = true;
                        System.out.println("            CurrenteLexema is The End of File, backtracking");
                        break;
                    }
                    final Terminal terminalDerivation = (Terminal) derivation;
                    System.out.println("            CurrenteLexema is Terminal: " + currentLexema);
                    if (currentLexema.getLexemaType() != terminalDerivation.getLexemaType()){
                        System.out.println("                ✖✖✖✖ Fail to match, backtracking!");
                        //  grammarErrorList.add(new GrammarError(currentLexema,terminalDerivation.getLexemaType()));
                        forceNextCheck = true;
                        break;
                    }
                    System.out.println("                ✔✔✔✔ Succes match, consuming it!");
                }else { //Não Terminal
                    final NaoTerminal nonTerminalDerivation = (NaoTerminal) derivation;
                    System.out.println("            NonTherminalDerivation [ " + nonTerminalDerivation + " ], here we go again!");
                    List<GrammarError> innerErrors = checkGrammmar(nonTerminalDerivation.getOwnGrammar());
                    if (innerErrors.size() > 0){
                        forceNextCheck = true;
                        break;
                    }
                }
            }
            checkNextList = forceNextCheck;
        }

        if (grammarErrorList.size() > 0){
            System.out.println("\n");
            for (GrammarError grammarError : grammarErrorList) {
                System.out.println(grammarError);
            }
        }else {
            System.out.println("Nenhum erro encontrado na gramatica: " + grammar.getOrigem());
        }


        return grammarErrorList;
    }
    */
}
