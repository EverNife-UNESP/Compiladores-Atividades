package br.com.finalcraft.unesp.compiladores.atividades.javafx.controller.analisadores;

import br.com.finalcraft.unesp.compiladores.atividades.JavaFXMain;
import br.com.finalcraft.unesp.compiladores.atividades.application.AnalisadorLexico;
import br.com.finalcraft.unesp.compiladores.atividades.application.AnalisadorSintatico;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

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
            TableRow<Lexema> row = new TableRow<>();
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


    @FXML
    void onAnaliseSintatica() {

        CodeArea codeArea = PascalKeywordsAsync.getCodeArea();
        if (!codeArea.getText().isEmpty()){
            List<Lexema> lexemaList = AnalisadorLexico.analiseLexica(codeArea.getText());
            lexemaObservableList = FXCollections.observableList(lexemaList);
            tabela.setItems(lexemaObservableList);
            HistoryLog historyLog = AnalisadorSintatico.analiseSintatica(lexemaList);
            checkForErroredLexemas();
            if (historyLog.isFullyMach()){
                this.errosSintaticos.setText("Código 100% Correto!" +
                        "\nCódigo 100% Correto!" +
                        "\nCódigo 100% Correto!" +
                        "\nCódigo 100% Correto!");
            }else {
                this.errosSintaticos.setText("[" + historyLog.getError().getLexema().getId() + "]Erro:" + historyLog.getError().toString());
            }
        }

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
            String fileConcent = new String(encoded, Charset.defaultCharset());
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
}
