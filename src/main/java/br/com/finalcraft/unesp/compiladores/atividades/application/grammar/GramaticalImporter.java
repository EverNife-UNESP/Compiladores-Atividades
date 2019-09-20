package br.com.finalcraft.unesp.compiladores.atividades.application.grammar;

import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Derivacao;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.NaoTerminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Terminal;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.imported.PascalKeywordsAsync;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GramaticalImporter {

    public static void importGrammar(){

        InputStream in = GramaticalImporter.class.getResourceAsStream("/assets/gramar-rulles-myown.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

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

            List<Derivacao> derivacaoList = new ArrayList<Derivacao>();


            int j = 0;
            for (int i = 3; i < splitedRule.length; i++) {

                j++;

                String derivationElement = splitedRule[i];
                Derivacao derivacao;

                if (derivationElement.equals("VAZIO")) continue;

                if (derivationElement.startsWith("<") && derivationElement.endsWith(">") && !derivationElement.contentEquals("<>")){
                    derivacao = new NaoTerminal(derivationElement);
                    System.out.println("\t\tDerivation["+ j + "] NonTerminal(" + derivationElement + ")");
                }else {
                    derivacao = new Terminal(derivationElement);
                    System.out.println("\t\tDerivation["+ j + "] Terminal(" + derivationElement + ")");
                }

                derivacaoList.add(derivacao);
            }

            String regraGerada = "";
            if (derivacaoList.size() == 0){
                regraGerada = "VAZIO";
            }else {
                for (Derivacao derivacao : derivacaoList) {
                    regraGerada +=  " " + derivacao;
                }
            }

            grammar.addDerivacao(derivacaoList);
            System.out.println("\t\t\tRegra gramatical gerada: " + regraGerada);
        }


        Grammar.mapOfgrammars.values().forEach(grammar -> {
            if (grammar.getDerivacao2DList().isEmpty()){
                System.out.println("Grammar: " + grammar + " does not have a derivationList");
                System.out.println("Grammar: " + grammar + " does not have a derivationList");
                System.out.println("Grammar: " + grammar + " does not have a derivationList");
                System.out.println("Grammar: " + grammar + " does not have a derivationList");
                System.out.println("Grammar: " + grammar + " does not have a derivationList");
                System.out.println("Grammar: " + grammar + " does not have a derivationList");
                System.out.println("Grammar: " + grammar + " does not have a derivationList");
            }
        });
    }
}
