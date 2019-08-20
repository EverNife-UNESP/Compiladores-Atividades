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
        int contadorLinha = 0;
        int contadorColuna = 0;
        for (int index = 0; index < charArray.length; index++, contadorColuna++) {
            String charAtIndex = String.valueOf(charArray[index]);
            boolean isUnicharacterSymbol = LexemaType.isUnichicharacterSymbol(charAtIndex);

            if (isUnicharacterSymbol && foundWord == false){
                foundLexemas.add(new Lexema(charAtIndex, contadorLinha, contadorColuna, contadorColuna));
                continue;
            }

            if (foundWord == true && (charAtIndex.matches(LexemaType.BRANCO.getRegex()) || isUnicharacterSymbol) ){
                if (charArray[index] == '\n'){
                    contadorLinha++;
                    contadorColuna = 0;
                }
                foundWord = false;
                foundLexemas.add(new Lexema(stringBuilder.toString(), contadorLinha, wordStart, contadorColuna - 1));
                if (isUnicharacterSymbol){
                    if (index+1 < charArray.length){//Se nao for o final do arquivo!
                        String nextChar = String.valueOf(charArray[index + 1]);
                        String possibelSymbol = charAtIndex + nextChar;
                        //System.out.println("Next Double Possible: " + possibelSymbol);
                        if (LexemaType.isDoubleCharacterSymbol(possibelSymbol)){
                            foundLexemas.add(new Lexema(possibelSymbol, contadorLinha, contadorColuna, contadorColuna + 1));
                            index++;
                            contadorColuna++;
                            continue;
                        }
                    }
                    foundLexemas.add(new Lexema(charAtIndex, contadorLinha, contadorColuna, contadorColuna));
                }
                continue;
            }
            if (foundWord == false){
                wordStart = contadorColuna;
                foundWord = true;
                stringBuilder = new StringBuilder();
            }
            stringBuilder.append(charArray[index]);
        }

        foundLexemas.forEach(System.out::println);

        return foundLexemas;
    }
}
