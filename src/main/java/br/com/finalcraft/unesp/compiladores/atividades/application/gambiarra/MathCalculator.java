package br.com.finalcraft.unesp.compiladores.atividades.application.gambiarra;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class MathCalculator {

    private static ScriptEngineManager manager;
    private static ScriptEngine engine;

    public static void assyncStart(){
        manager = new ScriptEngineManager();
        engine = manager.getEngineByName("js");
        calculate("(2 + 2) * (2 + 2)");
    }

    public static String calculate(String mathString){
        try {
            return engine.eval(mathString).toString();
        }catch (Exception ignored){
        }
        return "";
    }
}
