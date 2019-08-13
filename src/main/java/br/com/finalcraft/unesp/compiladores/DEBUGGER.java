package br.com.finalcraft.unesp.compiladores;

import java.util.logging.Logger;

public class DEBUGGER {


    private static Logger logger = Logger.getLogger("FinalCraft-LFA");
    public static boolean debug = false;

    public static void info(String message){
        System.out.println(message);
        //logger.log(Level.INFO, message);
    }

    public static void debug(String message){
        if (debug) System.out.println(message);
        //if (debug) logger.log(Level.INFO, message);
    }

}
