package br.com.finalcraft.unesp.compiladores.atividades.application;


import br.com.finalcraft.unesp.compiladores.atividades.application.gambiarra.MathCalculator;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.history.HistoryLog;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;
import br.com.finalcraft.unesp.compiladores.atividades.application.semantic.SemanticElement;
import br.com.finalcraft.unesp.compiladores.atividades.application.semantic.SemanticError;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSemantico {

    private static int INDEX = 0;
    private static List<HistoryLog> allLogs;
    private static MachineState machineState;

    private static List<SemanticElement> semanticElements = new ArrayList<>();
    public static List<SemanticError> semanticErrors = new ArrayList<>();

    private static boolean debug = true;

    private static void debug(String msg){
        if (debug) System.out.println(msg);
    }

    public static List<SemanticElement> analiseSemantica(HistoryLog mainHistoryLog){
        INDEX = 0;
        machineState = MachineState.createNew();

        allLogs = mainHistoryLog.clone().getPreviousLogs();
        allLogs.add(mainHistoryLog);

        semanticElements.clear();
        semanticErrors.clear();

        LexemaType.Reservada varType = LexemaType.Reservada.PROCEDURE;
        SemanticElement read = declarateVariable(AnalisadorSintatico.todosLexemas.get(3),varType,0);
        read.setTheExpression("read");
        SemanticElement write = declarateVariable(AnalisadorSintatico.todosLexemas.get(3),varType,0);
        write.setTheExpression("write");

        jumpUntil("<bloco>");
        handleBloco(0);


        for (SemanticElement semanticElement : semanticElements) {
            if (!semanticElement.isUtilizada()){
                addError(semanticElement.getOriginalLexema(),"A " + (semanticElement.getVarType()== SemanticElement.VarType.PROCEDIMENTO ? "procedure" : "variável") + " " + semanticElement.getTheExpression() + " não é utilizada nunca!");
            }
        }
        return semanticElements;
    }

    public static void handleBloco(int context){
        debug("\n\n---> Start Bloco " + context);
        declaracaoDeVariaveis(context);
        declaracaoDeSubRotinas(context);
        comandoComposto(context);
        debug("---> End Bloco " + context + "\n\n");
    }


    public static void declaracaoDeVariaveis(int context){

        while (jumpUntil("<declaração_de_variáveis>", "<parte_de_declarações_de_subrotinas>")){

            if (getCurrentDerivation().equalsIgnoreCase("<parte_de_declarações_de_subrotinas>")){
                break;
            }

            jumpUntil("<dv_tipo>");
            LexemaType.Reservada varType = (LexemaType.Reservada) getCurrentHystoryLog().getCurrentMove().lexema.getLexemaType();
            debug("Declarando variáveis do Tipo: " + varType);
            debug("Escopo: " + context);

            while (jumpUntil("identificador",";")){
                if (getCurrentDerivation().equalsIgnoreCase(";")){
                    debug("Fim Declaração.\n");
                    break;
                }
                Lexema currentLexema = getCurrentLexema();
                String newVarName = currentLexema.getTheExpression();
                SemanticElement semanticElement = getVariable(newVarName,context, true);
                if (semanticElement != null){
                    addError(currentLexema, "Variável " + newVarName + " já foi declarada em : " + semanticElement.toString());
                }else {
                    declarateVariable(getCurrentHystoryLog().getCurrentMove().lexema,varType,context);
                }
                debug(" - Var: " + newVarName);
            }

        }

    }

    public static void declaracaoDeSubRotinas(int context) {
        while (jumpUntil("procedure", "<comando_composto>")){

            if (getCurrentDerivation().equalsIgnoreCase("<comando_composto>")){
                break;
            }

            jumpUntil("identificador");

            LexemaType.Reservada varType = LexemaType.Reservada.PROCEDURE;
            declarateVariable(getCurrentHystoryLog().getCurrentMove().lexema,varType,context);

            debug("Declaring procedure: " + getCurrenExpression());
            debug("Escopo: " + context+1);

            while (jumpUntil("<possible_var>","<bloco>")){

                int newContext = context + 1;

                if (getCurrentDerivation().equalsIgnoreCase("<bloco>")){
                    handleBloco(newContext);
                    break;
                }

                List<Lexema> varsToAdd = new ArrayList<>();

                while (jumpUntil("identificador","<spf_tipo>")){
                    if (getCurrentDerivation().equalsIgnoreCase("<spf_tipo>")){
                        break;
                    }
                    varsToAdd.add(getCurrentHystoryLog().getCurrentMove().lexema);
                }

                varType = (LexemaType.Reservada) getCurrentHystoryLog().getCurrentMove().lexema.getLexemaType();

                for (Lexema newVarLexema : varsToAdd) {
                    SemanticElement semanticElement = getVariable(newVarLexema.getTheExpression(),newContext, true);
                    if (semanticElement != null){
                        addError(newVarLexema,"Variável " + newVarLexema.getTheExpression() + " já foi declarada em : " + semanticElement.toString());
                    }else {
                        declarateVariable(newVarLexema,varType,newContext);
                    }
                }

            }
        }
    }

    public static void comandoComposto(int context) {
        debug("Iniciando busca por ComandoComposto {Escopo: " + context + "}");
        int beginLevel = 0;
        while (true){
            jumpUntil("<atribuição>","<chamada_de_procedimento>","begin","end");

            switch (getCurrentDerivation().toLowerCase()){
                case "begin":
                    beginLevel++;
                    break;
                case "end":
                    beginLevel--;
                    if (beginLevel == 0) {
                        debug("Finalizado ComandoComposto");
                        return;
                    }
                    break;

                case "<atribuição>": {
                    jumpUntil("<variável>");
                    Lexema currentLexema = getCurrentLexema();
                    String leftVarName = getCurrenExpression();
                    SemanticElement leftSemanticElement = getVariable(leftVarName, context, false);
                    if (leftSemanticElement == null) {
                        addError(currentLexema, "A variável " + leftVarName + " foi usada más não foi declarada!");
                    } else {
                        leftSemanticElement.setUtilizada();
                    }
                    jumpUntil(":=");
                    int lexemaIndexStart = getCurrentHystoryLog().getStateIndex() + 1;
                    jumpUntil(";","end");
                    int lexemaIndexEnd = getCurrentHystoryLog().getStateIndex() - 1;
                    if (getCurrentDerivation().equalsIgnoreCase("end")){
                        INDEX--;
                    }

                    Boolean boleanValue = null;
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = lexemaIndexStart; i <= lexemaIndexEnd; i++) {
                        Lexema rightSideLexema = AnalisadorSintatico.todosLexemas.get(i);

                        if (rightSideLexema.getTheExpression().equalsIgnoreCase("true")){
                            //TODO Do a propper System respectinc || &&
                            boleanValue = Boolean.TRUE;
                        } else if (rightSideLexema.getTheExpression().equalsIgnoreCase("false")){
                            //TODO Do a propper System respectinc || &&
                            boleanValue = Boolean.FALSE;
                        } else if (rightSideLexema.getLexemaType() != LexemaType.IDENTIFICADOR) {
                            //Parte de Atribuição é NÚMERO ou Simbolo

                            if (rightSideLexema.getLexemaType() instanceof LexemaType.Reservada){
                                LexemaType.Reservada reservada = (LexemaType.Reservada) rightSideLexema.getLexemaType();
                                switch (reservada){
                                    case MOD:
                                        stringBuilder.append("%");
                                        break;
                                    case DIV:
                                        stringBuilder.append("/");
                                        break;
                                    case NOT:
                                        stringBuilder.append("-");
                                        break;
                                    default:
                                        stringBuilder.append(rightSideLexema.getTheExpression());
                                }
                            }else {
                                stringBuilder.append(rightSideLexema.getTheExpression());
                            }


                        } else {
                            //Parte de Atribuição é VARIAVEL
                            SemanticElement rightSideVar = getVariable(rightSideLexema.getTheExpression(), context, false);
                            if (rightSideVar == null) {
                                stringBuilder.append('0');
                                if (leftSemanticElement != null && leftSemanticElement.getVarType() == SemanticElement.VarType.BOOLEAN) boleanValue = false; //Default bolean == False
                                addError(rightSideLexema, "Váriavel " + rightSideLexema.getTheExpression() + " utilizada más não declarada!");
                                continue;
                            } else if (rightSideVar.getVarType() == SemanticElement.VarType.BOOLEAN) {
                                //TODO Do a propper System respectinc || &&
                                boleanValue = rightSideVar.getValor() != null ? (Boolean) rightSideVar.getValor() : false;
                            } else {
                                stringBuilder.append(rightSideVar.getValor());
                            }
                            leftSemanticElement.setUtilizada();
                        }
                    }

                    if (leftSemanticElement != null) {
                        if (leftSemanticElement.getVarType() == SemanticElement.VarType.BOOLEAN) {
                            leftSemanticElement.setValor(boleanValue);
                            stringBuilder = new StringBuilder(boleanValue == null ? Boolean.FALSE.toString() : boleanValue.toString());
                        } else {
                            String mathResult = MathCalculator.calculate(stringBuilder.toString());
                            if (!mathResult.isEmpty()) {
                                try {
                                    Double result = Double.parseDouble(mathResult);
                                    if (result != result.intValue()) {
                                        //Is not integer
                                        addError(leftSemanticElement.getOriginalLexema(), "A variável " + leftSemanticElement.getTheExpression() + " é inteira mas recebeu double(" + result + ")");
                                    }
                                    leftSemanticElement.setValor(result);
                                }catch (Exception ignored){
                                    addError(leftSemanticElement.getOriginalLexema(), "" + ignored.getMessage());
                                }
                            }else {
                                addError(leftSemanticElement.getOriginalLexema(), "A variável " + leftSemanticElement.getTheExpression() + " recebeu um valor NaN");
                            }
                        }
                    }
                    debug("Atribuindo " + leftVarName + " := " + stringBuilder);
                    break;
                }

                case "<chamada_de_procedimento>":{
                    jumpUntil("identificador");
                    SemanticElement semanticElement = getProcedimento(getCurrenExpression());
                    if (semanticElement == null){
                        addError(getCurrentLexema(),"Tentando utilizar procedimento " + getCurrenExpression() + " não declarado!");
                    }else {
                        semanticElement.setUtilizada();
                    }
                    debug("Chamada de Procedimento: " + getCurrenExpression());
                }
            }
        }
    }

    public static boolean jumpUntil(String... rule){
        while (true){
            INDEX++;
            boolean equalsToAnyone = false;
            for (String aRule : rule) {
                if (getCurrentDerivation().equalsIgnoreCase(aRule)){
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

    public static String getCurrentDerivation(){
        return INDEX < allLogs.size() ? allLogs.get(INDEX).getCurrentMove().derivation.toString() : "";
    }

    public static HistoryLog getCurrentHystoryLog(){
        return INDEX < allLogs.size() ? allLogs.get(INDEX) : null;
    }

    public static String getCurrenExpression(){
        return INDEX < allLogs.size() ? allLogs.get(INDEX).getCurrentMove().lexema.getTheExpression() : "";
    }

    public static Lexema getCurrentLexema(){
        return INDEX < allLogs.size() ? allLogs.get(INDEX).getCurrentMove().lexema : null;

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

    private static SemanticElement getVariable(String variable, int escopo, boolean onlyInCurrentContext){
        while (true){
            for (SemanticElement semanticElement : semanticElements) {
                if (semanticElement.getEscopo() == escopo && semanticElement.getTheExpression().equalsIgnoreCase(variable)){
                    return semanticElement;
                }
            }
            if (onlyInCurrentContext == true || escopo == 0){
                break;
            }
            escopo = 0;
        }
        return null;
    }

    private static SemanticElement getProcedimento(String variable){
        for (SemanticElement semanticElement : semanticElements) {
            if (semanticElement.getVarType() == SemanticElement.VarType.PROCEDIMENTO){
                if (semanticElement.getTheExpression().equalsIgnoreCase(variable)){
                    return semanticElement;
                }
            }
        }
        return null;
    }

    private static SemanticElement declarateVariable(Lexema lexema, LexemaType.Reservada varType, int context){
        SemanticElement semanticElement = new SemanticElement(lexema);
        semanticElement.setEscopo(context);
        semanticElement.setVarType(varType);
        semanticElements.add(semanticElement);
        return semanticElement;
    }

    private static void addError(Lexema lexema, String errorMSG){
        semanticErrors.add(new SemanticError(errorMSG,lexema));
    }
}
