package br.com.finalcraft.unesp.compiladores.atividades.application;


import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.imported.PascalKeywordsAsync;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorLexico {

    public static List<Lexema> analiseLexica(String theValue){

        //Aplica um replace usando a regex de comentário para limpar o texto.
        theValue = theValue.replaceAll(PascalKeywordsAsync.COMMENT_PATTERN," ");


        List<Lexema> foundLexemas = new ArrayList<>();

        char[] charArray = (theValue + ' ' + ' ' ).toCharArray();  //Fix for not checking the last word
        boolean foundWord = false;
        int wordStart = 0;
        StringBuilder stringBuilder = new StringBuilder();
        int contadorLinha = 1;
        int contadorColuna = 0;
        for (int index = 0; index < charArray.length - 1; index++, contadorColuna++) {
            String charAtIndex = String.valueOf(charArray[index]);
            boolean isUnicharacterSymbol = LexemaType.isUnichicharacterSymbol(charAtIndex);

            String charAtNextIndex = String.valueOf(charArray[index + 1]);
            String possibelSymbol = charAtIndex + charAtNextIndex;

            //Tratamento do ponto entre duas sequencias numéricas!
            if (isUnicharacterSymbol
                    && foundWord == true
                    && LexemaType.getOf(charAtIndex) == LexemaType.PONTO
                    && LexemaType.getOf(charAtNextIndex).getLexemaName().startsWith("INTEIRO")
                    && LexemaType.getOf(stringBuilder.toString()).getLexemaName().startsWith("INTEIRO")){
                stringBuilder.append(charAtIndex);
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
                if (charArray[index] == '\n'){
                    contadorLinha++;
                    contadorColuna = 0;
                }
                if (foundWord == true){
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

        //foundLexemas.forEach(System.out::println);

        return foundLexemas;
    }
}
