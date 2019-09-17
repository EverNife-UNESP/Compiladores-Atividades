package br.com.finalcraft.unesp.compiladores.atividades.application;


import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.Grammar;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Derivacao;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.GrammarError;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.NaoTerminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Terminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalisadorSintatico {

    private static List<Lexema> todosLexemas = new ArrayList<Lexema>();
    private static int tamanhoLexemas = 0;
    private static int index = 0;

    public static void analiseSintatica(List<Lexema> lexemas){

        todosLexemas = lexemas;
        tamanhoLexemas = todosLexemas.size();
        index = 0;

        System.out.println("\n\nIniciando analise sintática (" + lexemas.size() + " lexemas encontrados)\n\n");
        checkGrammmar(Grammar.PROGRAM);
    }


    private static List<GrammarError> checkGrammmar(Grammar grammar){

        MachineState machineState = startingGrammar();          // Seta o grammarStartIndex para caso de errado alguma derivação

        List<GrammarError> grammarErrorList = new ArrayList<GrammarError>();

        System.out.println("\n\nCheking Grammar [" + grammar.getOrigem() + "]");

        int iteration = 0;
        boolean checkNextList = true;
        for (List<Derivacao> derivacaoList : grammar.getDerivacao2DList()) {
            if (checkNextList == false){
                break;
            }
            grammarErrorList.clear();
            iteration++;
            if (iteration > 1){//Restaura a machineState, caso esteja na segunda iteração já vai ser necessário
                machineState.restoreMachineState();
            }

            String derivacaoListString = derivacaoList.stream().map(Object::toString)
                    .collect(Collectors.joining("\'\n        - \'"));

            System.out.println("\n    DerivationList [" + iteration +"]: \n        - \'" + derivacaoListString + "\'");

            boolean forceNextCheck = false;
            for (Derivacao derivation : derivacaoList) {

                System.out.println("\n        CurrentDerivation \'" + derivation + "\'");

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
                        grammarErrorList.add(new GrammarError(currentLexema,currentLexema.getLexemaType()));
                        forceNextCheck = true;
                        break;
                    }
                    System.out.println("                ✔✔✔✔ Succes match, consuming it!");
                }else { //Não Terminal
                    final NaoTerminal nonTerminalDerivation = (NaoTerminal) derivation;
                    System.out.println("            NonTherminalDerivation [ " + nonTerminalDerivation + " ], here we go again!");
                    List<GrammarError> innerErrors = checkGrammmar(nonTerminalDerivation.getOwnGrammar());
                    if (innerErrors.size() > 0){
                        grammarErrorList = innerErrors;
                        forceNextCheck = true;
                        break;
                    }
                }
            }
            checkNextList = forceNextCheck;
        }

        System.out.println("\n");
        for (GrammarError grammarError : grammarErrorList) {
            System.out.println(grammarError);
        }

        return grammarErrorList;
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
        if (index == tamanhoLexemas) return null;
        index++;
        return todosLexemas.get(index - 1);
    }

    private static MachineState startingGrammar(){
        return new MachineState(index);
    }

    private static class MachineState{
        int stateIndex;

        public MachineState(int stateIndex) {
            this.stateIndex = stateIndex;
        }

        private void restoreMachineState(){
            AnalisadorSintatico.index = stateIndex;
        }
    }
}
