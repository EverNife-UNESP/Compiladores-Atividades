package br.com.finalcraft.unesp.compiladores.atividades.application;


import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.history.HistoryLog;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.application.semantic.SemanticElement;
import br.com.finalcraft.unesp.compiladores.atividades.application.semantic.SemanticError;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSemantico {

    private static int INDEX = 0;
    private static List<HistoryLog> allLogs;
    private static MachineState machineState;

    private static List<SemanticElement> semanticElements = new ArrayList<>();
    private static List<SemanticError> semanticErrors = new ArrayList<>();

    private static int escopo = -1;

    public static List<SemanticElement> analiseSemantica(HistoryLog mainHistoryLog){
        INDEX = 0;
        machineState = MachineState.createNew();
        escopo = -1;

        allLogs = mainHistoryLog.clone().getPreviousLogs();
        allLogs.add(mainHistoryLog);

        semanticElements.clear();
        semanticErrors.clear();


        while (jumpUntil("<bloco>")){
            escopo++;

            System.out.println("Bloco " + escopo);
            declaracaoDeVariaveis();
            //jumpUntil("end");
        }

        return semanticElements;
    }

    private static int getCurrentEscopo(){
        return escopo;
    }

    public static void declaracaoDeVariaveis(){

        while (jumpUntil("<declaração_de_variáveis>", "<parte_de_declarações_de_subrotinas>")){

            if (getCurrentMove().equalsIgnoreCase("<parte_de_declarações_de_subrotinas>")){
                break;
            }

            jumpUntil("<dv_tipo>");
            System.out.println("Declarando variáveis do Tipo: " + getCurrentHystoryLog().getCurrentMove().lexema.getLexemaType());
            System.out.println("Escopo: " + getCurrentEscopo());

            while (jumpUntil("identificador",";")){
                if (getCurrentMove().equalsIgnoreCase(";")){
                    System.out.println("Fim Declaração.\n");
                    break;
                }
                String newVarName = getCurrentHystoryLog().getCurrentMove().lexema.getTheExpression();
                SemanticElement semanticElement = getVariable(newVarName,getCurrentEscopo(), true);
                if (semanticElement != null){
                    addError("Variável " + newVarName + " já foi declarada em : " + semanticElement.toString());
                    System.out.println("Variável " + newVarName + " já foi declarada em : " + semanticElement.toString());
                }else {
                    declarateVariable(getCurrentHystoryLog().getCurrentMove().lexema);
                }
                System.out.println(" - Var: " + newVarName);
            }

        }

    }

    public static boolean jumpUntil(String... rule){
        while (true){
            INDEX++;
            boolean equalsToAnyone = false;
            for (String aRule : rule) {
                if (getCurrentMove().equalsIgnoreCase(aRule)){
                    equalsToAnyone = true;
                    break;
                }
            }
            if (equalsToAnyone == true) return true;
            if (INDEX > allLogs.size()){
                return false;
            }
        }
    }

    public static String getCurrentNeededDerivations(){
        return INDEX < allLogs.size() && allLogs.get(INDEX).getNeededDerivations().size() > 1 ? allLogs.get(INDEX).getNeededDerivations().get(0).toString() : "";
    }

    public static String getCurrentMove(){
        return INDEX < allLogs.size() ? allLogs.get(INDEX).getCurrentMove().derivation.toString() : "";
    }

    public static HistoryLog getCurrentHystoryLog(){
        return INDEX < allLogs.size() ? allLogs.get(INDEX) : null;
    }


    private static class MachineState{
        final int currentIndex;

        private MachineState() {
            this.currentIndex = INDEX;
        }

        private static MachineState createNew(){
            return new MachineState();
        }

        private void restoreMachineState(){
            INDEX = currentIndex;
        }
    }

    private static SemanticElement getVariable(String variable, int escopo, boolean exact){
        for (SemanticElement semanticElement : semanticElements) {
            if (semanticElement.getEscopo() == escopo || (exact == false && semanticElement.getEscopo() == 0) ){
                if (semanticElement.getTheExpression().equalsIgnoreCase(variable)){
                    return semanticElement;
                }
            }
        }
        return null;
    }

    private static void declarateVariable(Lexema lexema){
        System.out.println("Declaring Variable [" + lexema.getTheExpression() + ", " + getCurrentEscopo() + "]" );
        SemanticElement semanticElement = new SemanticElement(lexema);
        semanticElement.setEscopo(getCurrentEscopo());
        semanticElements.add(semanticElement);
    }

    private static void addError(String errorMSG){
        semanticErrors.add(new SemanticError(errorMSG));
    }
}
