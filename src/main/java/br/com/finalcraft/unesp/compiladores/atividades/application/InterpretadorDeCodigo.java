package br.com.finalcraft.unesp.compiladores.atividades.application;


import br.com.finalcraft.unesp.compiladores.atividades.application.code.Instruction;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.analisadores.InterpretadorDeCodigoController;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

public class InterpretadorDeCodigo {

    public static List<Instruction> allInstructions;
    public static int INDEX = 0;
    public static List<Double> variables = new ArrayList<>();
    public static Stack<Double> pilhaDados = new Stack<>();

    public static synchronized void interpretar(List<Instruction> instructionList){


        System.out.println("Interpretando código: ");
        instructionList.forEach(System.out::println);

        allInstructions = instructionList;
        variables.clear();
        pilhaDados.clear();

        for (INDEX = 0; INDEX < allInstructions.size();) {

            Instruction currentInstruction = allInstructions.get(INDEX);

            System.out.println("Reading " + currentInstruction);
            InterpretadorDeCodigoController.instance.appendConsole("\nReading " + currentInstruction);
            switch (currentInstruction.getType()){

                case "CRCT":
                    pilhaDados.push(currentInstruction.getValue());
                    break;
                case "CRVL":
                    pilhaDados.push(variables.get(currentInstruction.getValueAsInt()));
                    break;
                case "ARMZ":
                    variables.set(currentInstruction.getValueAsInt(),pilhaDados.pop());
                    break;
                case "SOMA":
                    applyBIFunction((top, subTop) -> subTop + top);
                    break;
                case "SUBT":
                    applyBIFunction((top, subTop) -> subTop - top);
                    break;
                case "MULT":
                    applyBIFunction((top, subTop) -> subTop * top);
                    break;
                case "DIVI":
                    applyBIFunction((top, subTop) -> subTop / top);
                    break;
                case "MODI":
                    applyFunction(top -> Math.abs(top));
                    break;
                case "INVR":
                    applyFunction(top -> top*-1);
                    break;
                case "CONJ":
                    applyBIFunction((top, subTop) -> {
                        if (top == 1 && subTop == 1) {
                            return 1D;
                        }
                        return 0D;
                    });
                    break;
                case "DISJ":
                    applyBIFunction((top, subTop) -> {
                        if (top == 1 || subTop == 1) {
                            return 1D;
                        }
                        return 0D;
                    });
                    break;
                case "NEGA":
                    applyFunction(top -> 1-top);
                    break;
                case "CMME":
                    applyBIFunction((top, subTop) -> {
                        if (subTop < top) {
                            return 1D;
                        }
                        return 0D;
                    });
                    break;
                case "CMMA":
                    applyBIFunction((top, subTop) -> {
                        if (subTop > top) {
                            return 1D;
                        }
                        return 0D;
                    });
                    break;
                case "CMIG":
                    applyBIFunction((top, subTop) -> {
                        if (subTop == top) {
                            return 1D;
                        }
                        return 0D;
                    });
                    break;
                case "CMDG":
                    applyBIFunction((top, subTop) -> {
                        if (subTop != top) {
                            return 1D;
                        }
                        return 0D;
                    });
                    break;
                case "CMAG":
                    applyBIFunction((top, subTop) -> {
                        if (subTop >= top) {
                            return 1D;
                        }
                        return 0D;
                    });
                    break;
                case "CMEG":
                    applyBIFunction((top, subTop) -> {
                        if (subTop >= top) {
                            return 1D;
                        }
                        return 0D;
                    });
                    break;
                case "DSVS":
                    INDEX = currentInstruction.getValueAsInt();
                    continue;
                case "DSVF":
                    if (pilhaDados.pop() == 0){
                        INDEX = currentInstruction.getValueAsInt();
                        continue;
                    }
                    break;
                case "LEIT":
                    TextInputDialog dialog = new TextInputDialog("0");
                    dialog.setTitle("Leitura de Dados");
                    dialog.setHeaderText("Insira um valor Float!");
                    dialog.setContentText("Insira um número válido por favor!");

                    Double value = null;
                    while (value == null){
                        try {
                            Optional<String> result = dialog.showAndWait();
                            value = Double.parseDouble(result.get());
                        }catch (Exception e){
                            value = null;
                        }
                    }
                    InterpretadorDeCodigoController.instance.appendConsole("  Valor Lido ---:> " + value);
                    System.out.println("Valor Lido ---:> " + value);
                    pilhaDados.push(value);
                    break;
                case "IMPR":
                    InterpretadorDeCodigoController.instance.appendOutput(pilhaDados.pop().toString());
                    break;
                case "IMPE":
                    InterpretadorDeCodigoController.instance.appendOutput("\n");
                    break;
                case "ALME":
                case "AMEM":
                    for (int i = 0; i < currentInstruction.getValueAsInt(); i++) {
                        variables.add(0D);
                    }
                    break;
                case "PARA":
                    InterpretadorDeCodigoController.instance.appendConsole("\n\n!!!Processo Finalizado!!!");
                    return;
                case "INPP":
                case "NADA":
                    break;
                default:
                    InterpretadorDeCodigoController.instance.appendConsole("\n\nIgnorando comando: " + currentInstruction + "\n");
                    break;
            }
            INDEX++;
        }
    }


    private static void applyFunction(Function<Double,Double> function){
        Double varA = pilhaDados.pop();
        pilhaDados.push(function.apply(varA));
    }

    private static void applyBIFunction(BiFunction<Double,Double,Double> function){
        Double varA = pilhaDados.pop();
        Double varB = pilhaDados.pop();
        pilhaDados.push(function.apply(varA,varB));
    }
}
