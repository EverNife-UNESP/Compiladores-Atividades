package br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.analisadores;

import br.com.finalcraft.unesp.compiladores.atividades.JavaFXMain;
import br.com.finalcraft.unesp.compiladores.atividades.application.AnalisadorLexico;
import br.com.finalcraft.unesp.compiladores.atividades.application.AnalisadorSintatico;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.GrammarErrorFixed;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.data.Tratamento;
import br.com.finalcraft.unesp.compiladores.atividades.application.grammar.history.HistoryLog;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.Lexema;
import br.com.finalcraft.unesp.compiladores.atividades.application.lexema.LexemaType;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.filemanager.FileLoaderHandler;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.compiladores.atividades.javafx.view.imported.PascalKeywordsAsync;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnalisadorSintaticoController implements FileLoaderHandler{
    private static AnalisadorSintaticoController instance;
    private static Stage dialog;

    public static void setUp(){
        dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        // Defines a modal window that blocks events from being
        // delivered to any other application window.
        dialog.initOwner(JavaFXMain.thePrimaryStage);

        Scene newSceneWindow1 = new Scene(MyFXMLs.analisador_sintatico);
        newSceneWindow1.getStylesheets().add(PascalKeywordsAsync.instance.styleSheet);
        dialog.setScene(newSceneWindow1);
        dialog.setOnShowing(event -> {
            instance.startHearingForFiles();
        });
        dialog.setOnCloseRequest(event -> {
            instance.stopHearingForFiles();
        });

        PascalKeywordsAsync.instance.node.prefHeight(instance.codeBorderPanel.getHeight());
        PascalKeywordsAsync.instance.node.prefWidth(instance.codeBorderPanel.getWidth());
    }

    public static void show(){
        dialog.show();
        instance.codeBorderPanel.setCenter(PascalKeywordsAsync.instance.node);
    }

    @FXML
    private TableView<Lexema> tabela;

    @FXML
    private TableColumn<Lexema, String> columnID;


    @FXML
    private TableColumn<Lexema, String> columnExpressao;

    @FXML
    private TableColumn<Lexema, String> columnTipoLexema;

    @FXML
    private TableColumn<Lexema, Integer> columnLinha;

    @FXML
    private TableColumn<Lexema, Integer> columnInicio;

    @FXML
    private TableColumn<Lexema, Integer> columnFim;

    @FXML
    private BorderPane codeBorderPanel;

    @FXML
    private TextArea errosLexicos;

    @FXML
    private TextArea errosSintaticos;

    private ObservableList<Lexema> lexemaObservableList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        instance = this;

        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnExpressao.setCellValueFactory(new PropertyValueFactory<>("theExpression"));
        columnTipoLexema.setCellValueFactory(new PropertyValueFactory<>("lexemaType"));
        columnLinha.setCellValueFactory(new PropertyValueFactory<>("linha"));
        columnInicio.setCellValueFactory(new PropertyValueFactory<>("start"));
        columnFim.setCellValueFactory(new PropertyValueFactory<>("end"));

        tabela.setItems(lexemaObservableList);

        tabela.setRowFactory( tv -> {
            TableRow<Lexema> row = new TableRow<Lexema>(){
                @Override
                protected void updateItem(Lexema item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setStyle("");
                    } else if (item.getLexemaType() instanceof LexemaType.Error) {
                        setStyle("-fx-background-color: tomato;");
                    } else {
                        setStyle("");
                    }
                }
            };
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Lexema rowData = row.getItem();
                    PascalKeywordsAsync.instance.codeArea.requestFocus();
                    PascalKeywordsAsync.instance.codeArea.selectRange(rowData.getLinha() - 1,rowData.getStart(),rowData.getLinha() - 1,rowData.getEnd() + 1);
                }
            });
            return row ;
        });
    }


    private void appendErroSintatico(String text){
        this.errosSintaticos.setText(errosSintaticos.getText() + text);
    }

    @FXML
    void onAnaliseSintatica() {

        CodeArea codeArea = PascalKeywordsAsync.getCodeArea();
        if (!codeArea.getText().isEmpty()){
            try {
                this.errosSintaticos.setText("");
                List<Lexema> lexemaList = AnalisadorLexico.analiseLexica(codeArea.getText());
                lexemaObservableList = FXCollections.observableList(lexemaList);
                tabela.setItems(lexemaObservableList);
                HistoryLog theHistoryLog = AnalisadorSintatico.analiseSintatica(lexemaList);

                List<HistoryLog> allHistoryLogs = new ArrayList<HistoryLog>();
                allHistoryLogs.addAll(theHistoryLog.getPreviousLogs());
                allHistoryLogs.add(theHistoryLog);

                checkForErroredLexemas();
                for (HistoryLog historyLog : allHistoryLogs) {
                    if (historyLog.isFixed()) {
                        GrammarErrorFixed grammarErrorFixed = (GrammarErrorFixed) historyLog.getError();
                        Tratamento tratamento = grammarErrorFixed.getTratamento();
                        appendErroSintatico("\n[" + grammarErrorFixed.getLexema().getId() + "]Erro:" + (tratamento.getGrammar() != null ? tratamento.getGrammar().getOrigem() : ""));
                    }
                }

                if (theHistoryLog.isFullyMach()) {
                    appendErroSintatico("\n\nCódigo 100% Analisado!");
                } else {
                    this.errosSintaticos.setText("[" + theHistoryLog.getError().getLexema().getId() + "]Erro:" + theHistoryLog.getError().toString());
                }

            }catch(Throwable throwable) {
                showCrashMSG(throwable);
            }
        }
    }

    private void showCrashMSG(Throwable throwable){
        this.errosSintaticos.setText("FatalError meu chapa!");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("O programa crashou meu chapa!");
        alert.setHeaderText("Infelizmente aconteceu um: " + throwable.getClass().getSimpleName());
        alert.setContentText(frasesMotivacionais[(new Random().nextInt(frasesMotivacionais.length))]);
        alert.showAndWait();
    }

    public void checkForErroredLexemas(){
        StringBuilder stringBuilder = new StringBuilder();
        for (Lexema lexema : AnalisadorSintatico.todosLexemas) {
            if (lexema.getLexemaType() instanceof LexemaType.Error){
                stringBuilder.append("[" + lexema.getId() + "] Errored lexema: " + lexema.getTheExpression() );
            }
        }
        errosLexicos.setText(stringBuilder.toString());
    }

    @Override
    public void onFileLoaded(File file) {
        try {
            byte[] encoded = Files.readAllBytes(file.toPath());
            String fileConcent = new String(encoded, Charset.forName("UTF-8"));
            System.out.println("Setting CodeText");
            PascalKeywordsAsync.getCodeArea().clear();
            PascalKeywordsAsync.getCodeArea().replaceText(0, 0, fileConcent);
        }catch (Exception e){
            e.printStackTrace();
            PascalKeywordsAsync.getCodeArea().replaceText(0, 0, "Arquivo lido corrompido :/");
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
