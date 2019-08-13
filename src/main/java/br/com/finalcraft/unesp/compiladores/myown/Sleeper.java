package br.com.finalcraft.unesp.compiladores.myown;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public abstract class Sleeper {

    //Dorme por um tempo e executa uma função :V

    private static Sleeper currentSleeping = null;

    private Task<Void> sleeper;

    //Evita que mais do que um Sleeper esteja sendo executado ao mesmo tempo
    public Sleeper() {
        if (currentSleeping != null){
            currentSleeping.sleeper.cancel();
        }
        currentSleeping = this;
    }

    //Executa a função andDo() após esperar um determinado tempo!
    public void runAfter(final long millis){
        sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException ignored) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                andDo();
            }
        });
        new Thread(sleeper).start();
    }

    public abstract void andDo();

}
