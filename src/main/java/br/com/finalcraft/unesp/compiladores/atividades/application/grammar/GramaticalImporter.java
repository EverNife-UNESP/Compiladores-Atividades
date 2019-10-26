package br.com.finalcraft.unesp.compiladores.atividades.application.grammar;

import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Derivation;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.NaoTerminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Terminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Tratamento;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.imported.PascalKeywordsAsync;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GramaticalImporter {

    public static void importGrammar(){

        try {
            InputStream in = GramaticalImporter.class.getResourceAsStream("/assets/gramar-rulles-myown.txt");
            InputStreamReader inReader = new InputStreamReader(in,"UTF-8");
            BufferedReader reader = new BufferedReader(inReader);

            ArrayList<String> lines = reader.lines().collect(Collectors.toCollection(ArrayList::new));


            String allLines = "";

            for (String line : lines) {
                allLines += "\n" + line;
            }

            allLines = allLines.replaceAll(PascalKeywordsAsync.COMMENT_PATTERN,""); //Removendo comentários das regras gramáticais;

            String allRules[] = allLines.split(Pattern.quote("☢")); //Split em cada uma das regras

            for (String aRule : allRules) {

                String[] splitedRule = aRule.split("\\s+");

                if (splitedRule.length == 0) {
                    continue;
                }

                System.out.println("\n\n\nAdicionando regra: " + splitedRule[0]);

                NaoTerminal naoTerminal = new NaoTerminal(splitedRule[1]);

                System.out.println("\tGramatica do simbolo NãoTerminal encontrada: " + naoTerminal);

                Grammar grammar = Grammar.getOrCreateGrammar(naoTerminal);

                List<Derivation> derivationList = new ArrayList<Derivation>();


                int j = 0;
                for (int i = 3; i < splitedRule.length; i++) {

                    j++;

                    String derivationElement = splitedRule[i];
                    Derivation derivation;

                    if (derivationElement.equals("VAZIO")) continue;

                    if (derivationElement.startsWith("{") && derivationElement.endsWith("}")){
                        derivation = new Tratamento(derivationElement,grammar);
                        System.out.println("\t\tDerivation["+ j + "] Tratamento(" + derivationElement + ")");
                    }else if (derivationElement.startsWith("<") && derivationElement.endsWith(">") && !derivationElement.contentEquals("<>")){
                        derivation = new NaoTerminal(derivationElement);
                        Grammar.getOrCreateGrammar((NaoTerminal) derivation);    //Garantir que esse simbolo terminal fique salvo na lista de gramáticas para futuras checagens e validações
                        System.out.println("\t\tDerivation["+ j + "] NonTerminal(" + derivationElement + ")");
                    }else if (derivationElement.startsWith("[") && derivationElement.endsWith("]")){
                        String originGrammar = grammar.getOrigem().toString();
                        originGrammar = originGrammar.substring(0,originGrammar.length() - 1);
                        Terminal terminal = new Terminal(derivationElement.substring(1,derivationElement.length() - 1));
                        Grammar internalTerminalGrammar = Grammar.getOrCreateGrammar(new NaoTerminal(originGrammar + "_terminal_" + terminal + ">"));

                        internalTerminalGrammar.addDerivacao(Arrays.asList(terminal));
                        internalTerminalGrammar.addDerivacao(Arrays.asList(new Tratamento("{inplace_replace_" + terminal + "}",internalTerminalGrammar)));

                        derivation = internalTerminalGrammar.getOrigem();
                        System.out.println("\t\tDerivation["+ j + "] Terminal(" + derivationElement + ")");
                    }else {
                        derivation = new Terminal(derivationElement);
                        System.out.println("\t\tDerivation["+ j + "] Terminal(" + derivationElement + ")");
                    }

                    derivationList.add(derivation);
                }

                String regraGerada = "";
                if (derivationList.size() == 0){
                    regraGerada = "VAZIO";
                }else {
                    for (Derivation derivation : derivationList) {
                        regraGerada +=  " " + derivation;
                    }
                }

                grammar.addDerivacao(derivationList);
                System.out.println("\t\t\tRegra gramatical gerada: " + regraGerada);
            }

            System.out.println(" \nUm total de " + Grammar.mapOfgrammars.values().size() + " regras gramáticais foram geradas!");

            Grammar.mapOfgrammars.values().forEach(grammar -> {
                if (grammar.getDerivacao2DList().isEmpty()){
                    System.out.println("Grammar: " + grammar.getOrigem() + " does not have a derivationList");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
