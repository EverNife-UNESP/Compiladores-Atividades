package br.com.finalcraft.unesp.compiladores.myown;

import java.io.File;

public class FileHelper {

    public static File loadFile(String path){
        try {
            File file = new File(path);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
