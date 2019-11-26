package br.com.finalcraft.unesp.compiladores.atividades.application.code;

import java.util.ArrayList;
import java.util.List;

public class Instruction {

    private final String type;
    private final double value;
    private final int INDEX;

    public Instruction(String type, double value, int INDEX) {
        this.type = type;
        this.value = value;
        this.INDEX = INDEX;
    }

    public String getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    public int getValueAsInt() {
        return (int)value;
    }


    public static List<Instruction> fromText(String fullText){
        List<Instruction> instructionList = new ArrayList<>();
        String[] splitedText = fullText.split("\n");
        for (int index = 0; index < fullText.split("\n").length; index++) {
            String aLine = splitedText[index];

            if (aLine.isEmpty()){
                continue;
            }

            String type;
            double value = 0;

            if (aLine.contains(" ")){
                type = aLine.split("\\s+")[0];
                value = Double.parseDouble(aLine.split("\\s+")[1]);
            }else {
                type = aLine;
            }


            instructionList.add(new Instruction(type,value,index));
        }
        return instructionList;
    }

    @Override
    public String toString() {
        return "[" + INDEX + "] " + type + " - " + value;
    }
}
