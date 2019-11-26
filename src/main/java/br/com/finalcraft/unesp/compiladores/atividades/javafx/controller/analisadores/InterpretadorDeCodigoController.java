package br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.analisadores;

import br.com.finalcraft.unesp.compiladores.atividades.JavaFXMain;
import br.com.finalcraft.unesp.compiladores.atividades.application.AnalisadorSintatico;
import br.com.finalcraft.unesp.compiladores.atividades.application.InterpretadorDeCodigo;
import br.com.finalcraft.unesp.compiladores.atividades.application.code.Instruction;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.filemanager.FileLoaderHandler;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.imported.CodeInterpreterKeywordsAsync;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

public class InterpretadorDeCodigoController implements FileLoaderHandler{
    public static InterpretadorDeCodigoController instance;
    private static Stage dialog;

    public static void setUp(){
        dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        // Defines a modal window that blocks events from being
        // delivered to any other application window.
        dialog.initOwner(JavaFXMain.thePrimaryStage);

        Scene newSceneWindow1 = new Scene(MyFXMLs.interpretador_codigo);
        newSceneWindow1.getStylesheets().add(CodeInterpreterKeywordsAsync.instance.styleSheet);
        dialog.setScene(newSceneWindow1);
        dialog.setOnShowing(event -> {
            instance.startHearingForFiles();
        });
        dialog.setOnCloseRequest(event -> {
            instance.stopHearingForFiles();
        });

        CodeInterpreterKeywordsAsync.instance.node.prefHeight(instance.codeBorderPanel.getHeight());
        CodeInterpreterKeywordsAsync.instance.node.prefWidth(instance.codeBorderPanel.getWidth());
    }

    public static void show(){
        dialog.show();
        instance.codeBorderPanel.setCenter(CodeInterpreterKeywordsAsync.instance.node);
    }

    @FXML
    private BorderPane codeBorderPanel;

    @FXML
    private TextArea console;

    @FXML
    private TextArea outPut;

    @FXML
    private TextArea vars;

    @FXML
    private TextArea stack;
    private ObservableList<Lexema> lexemaObservableList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        instance = this;
    }

    public void appendConsole(String text){
        this.console.setText(console.getText() + text);
    }

    public void appendOutput(String text){
        this.outPut.setText(outPut.getText() + text);
    }

    @FXML
    void onInterpretarCodigo() {

        CodeArea codeArea = CodeInterpreterKeywordsAsync.getCodeArea();

        if (!codeArea.getText().isEmpty()){
            try {
                this.console.setText("");
                this.outPut.setText("");
                this.vars.setText("Variáveis: \n");
                this.stack.setText("Pilha: \n");

                List<Instruction> instructionList = Instruction.fromText(codeArea.getText().replaceAll(CodeInterpreterKeywordsAsync.COMMENT_PATTERN,""));
                InterpretadorDeCodigo.interpretar(instructionList);

                for (int i = 0; i < InterpretadorDeCodigo.variables.size(); i++) {
                    this.vars.setText(this.vars.getText() + "\n[" + i + "] == " + InterpretadorDeCodigo.variables.get(i));
                }

                int i = InterpretadorDeCodigo.pilhaDados.size();
                while (!InterpretadorDeCodigo.pilhaDados.isEmpty()){
                    this.stack.setText(this.stack.getText() + "\n[" + i + "] == " + InterpretadorDeCodigo.pilhaDados.pop());
                    i--;
                }

            }catch(Throwable throwable) {
                showCrashMSG(throwable);
            }
        }
    }

    private void showCrashMSG(Throwable throwable){
        this.console.setText("FatalError meu chapa!");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("O programa crashou meu chapa!");
        alert.setHeaderText("Infelizmente aconteceu um: " + throwable.getClass().getSimpleName());
        alert.setContentText(frasesMotivacionais[(new Random().nextInt(frasesMotivacionais.length))]);
        throwable.printStackTrace();
        alert.showAndWait();
    }

    @Override
    public void onFileLoaded(File file) {
        try {
            byte[] encoded = Files.readAllBytes(file.toPath());
            String fileConcent = new String(encoded, Charset.forName("UTF-8"));
            System.out.println("Setting CodeText");
            CodeInterpreterKeywordsAsync.getCodeArea().clear();
            CodeInterpreterKeywordsAsync.getCodeArea().replaceText(0, 0, fileConcent);
        }catch (Exception e){
            e.printStackTrace();
            CodeInterpreterKeywordsAsync.getCodeArea().replaceText(0, 0, "Arquivo lido corrompido :/");
        }
    }

    @FXML
    void onLerArquivo() {
        FileLoaderHandler.openFilerLoader(this);
    }


    private static final String[] frasesMotivacionais = new String[]{
            "Se cair levante se deslizar se segure mas nunca pence em desistir por que o quanto mas amarga for a sua queda mas doce sera a sua vitoria.",
            "Eu odiava cada minuto dos treinos, mas dizia para mim mesmo: Não desista! Sofra agora e viva o resto de sua vida como um campeão.",
            "Não desista. Geralmente é a última chave no chaveiro que abre a porta.",
            "Então não desista, sorria. Você é mais forte do que pensa e será mais feliz do que imagina.",
            "Tomara que a gente não desista de ser quem é por nada nem ninguém deste mundo.",
            "Não desista de seu sonho",
            "Não desista jamais e saiba valorizar quem te ama, esses sim merecem seu respeito.",
            "Tomara que a gente não desista de ser quem é por nada nem ninguém deste mundo. Que a gente reconheça o poder do outro sem esquecer do nosso.",
            "Não desista, vá em frente, sempre há uma chance de você tropeçar em algo maravilhoso.",
            "Não desista só porque as coisas estão difíceis.",
            "Não desista do amor, não desista de amar, não se entregue à dor, porque ela um dia vai passar...",
            "-Desista\n" +
                    "-Não... desista você... de me tentar fazer desistir!!!!",
            "Mude sua rota, mas não desista no caminho"
    };
}
