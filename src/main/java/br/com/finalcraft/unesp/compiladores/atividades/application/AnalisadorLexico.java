package br.com.finalcraft.unesp.compiladores.atividades.application;


import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorLexico {

    public static List<Lexema> analiseLexica(String theValue){
        List<Lexema> foundLexemas = new ArrayList<>();

        char[] charArray = (theValue + ' ' + ' ').toCharArray();  //Fix for not checking the last word
        boolean foundWord = false;
        int wordStart = 0;
        StringBuilder stringBuilder = new StringBuilder();
        int contadorLinha = 0;
        int contadorColuna = 0;
        for (int index = 0; index < charArray.length - 1; index++, contadorColuna++) {
            String charAtIndex = String.valueOf(charArray[index]);
            boolean isUnicharacterSymbol = LexemaType.isUnichicharacterSymbol(charAtIndex);

            String charAtNextIndex = String.valueOf(charArray[index + 1]);
            String possibelSymbol = charAtIndex + charAtNextIndex;

            if (isUnicharacterSymbol
                    && foundWord == true
                    && LexemaType.getOf(charAtIndex) == LexemaType.PONTO
                    && LexemaType.getOf(charAtNextIndex) == LexemaType.INTEIRO
                    && LexemaType.getOf(stringBuilder.toString()) == LexemaType.INTEIRO){
                stringBuilder.append(charAtIndex);
                System.out.println("GetOut");
                continue;
            }


            if (isUnicharacterSymbol && foundWord == false){
                if (LexemaType.isDoubleCharacterSymbol(possibelSymbol)){
                    foundLexemas.add(new Lexema(possibelSymbol, contadorLinha, contadorColuna, contadorColuna + 1));
                    index++;
                    contadorColuna++;
                    continue;
                }
                foundLexemas.add(new Lexema(charAtIndex, contadorLinha, contadorColuna, contadorColuna));
                continue;
            }

            if (charAtIndex.matches(LexemaType.BRANCO.getRegex()) || isUnicharacterSymbol ){
                if (foundWord == true){
                    if (charArray[index] == '\n'){
                        contadorLinha++;
                        contadorColuna = 0;
                    }
                    foundWord = false;
                    foundLexemas.add(new Lexema(stringBuilder.toString(), contadorLinha, wordStart, contadorColuna - 1));
                    if (isUnicharacterSymbol){
                        if (index+1 < charArray.length){//Se nao for o final do arquivo!
                            if (LexemaType.isDoubleCharacterSymbol(possibelSymbol)){
                                foundLexemas.add(new Lexema(possibelSymbol, contadorLinha, contadorColuna, contadorColuna + 1));
                                index++;
                                contadorColuna++;
                                continue;
                            }
                        }
                        foundLexemas.add(new Lexema(charAtIndex, contadorLinha, contadorColuna, contadorColuna));
                    }
                }
                continue;
            }
            if (foundWord == false){
                wordStart = contadorColuna;
                foundWord = true;
                stringBuilder = new StringBuilder();
            }
            stringBuilder.append(charAtIndex);
        }

        foundLexemas.forEach(System.out::println);

        return foundLexemas;
    }
}
