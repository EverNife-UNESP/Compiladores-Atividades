package br.com.finalcraft.unesp.compiladores.atividades.application.atividade1;


import br.com.finalcraft.unesp.compiladores.atividades.logical.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.logical.lexema.LexemaType;

import java.util.ArrayList;
import java.util.List;

public class CalculadoraExecutor {

    public static List<Lexema> analiseLexical(String theValue){
        List<Lexema> foundLexemas = new ArrayList<>();

        char[] charArray = (theValue + ' ').toCharArray();  //Fix for not checking the last word
        boolean foundWord = false;
        int wordStart = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < charArray.length; index++) {
            if (String.valueOf(charArray[index]).matches(LexemaType.BRANCO.getRegex())){
                if (foundWord == true){
                    foundWord = false;
                    foundLexemas.add(new Lexema(stringBuilder.toString(), wordStart, index - 1));
                }
                continue;
            }
            if (foundWord == false){
                wordStart = index;
                foundWord = true;
                stringBuilder = new StringBuilder();
            }
            stringBuilder.append(charArray[index]);
        }

        foundLexemas.forEach(System.out::println);

        return foundLexemas;
    }
}
