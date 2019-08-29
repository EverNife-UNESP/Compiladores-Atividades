package br.com.finalcraft.unesp.compiladores.atividades.javafx.view.imported;

import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PascalKeywordsAsync {

    public static PascalKeywordsAsync instance = new PascalKeywordsAsync();

    public static void setUp(){
        instance.initialize();
    }

    private static final String[] KEYWORDS = new String[] {
            "program", "procedure", "begin", "end", "read",
            "write",



            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    private static final String sampleCode = String.join("\n", new String[] {
            "program correto;\n" +
                    "int a, b, c;\n" +
                    "boolean d, e, f;\n" +
                    "\n" +
                    "procedure proc(var a1 : int);\n" +
                    "int a, b, c;\n" +
                    "boolean d, e, f;\n" +
                    "begin\n" +
                    "\ta:=1;\n" +
                    "\tif (a<1)\n" +
                    "\t\ta:=12\n" +
                    "end;\n" +
                    "\n" +
                    "begin\n" +
                    "\ta:=2;\n" +
                    "\tb:=10;\n" +
                    "\tc:=11;\n" +
                    "\ta:=b+c;\n" +
                    "\td:=true;\n" +
                    "\te:=false;\n" +
                    "\tf:=true;\n" +
                    "\tread(a);\n" +
                    "\twrite(b);\n" +
                    "\tif (d)\n" +
                    "\tbegin\n" +
                    "\t\ta:=20;\n" +
                    "\t\tb:=10*c;\n" +
                    "\t\tc:=a div b\n" +
                    "\tend;\n" +
                    "\twhile (a>1)\n" +
                    "\tbegin\n" +
                    "\t\tif (b>10)\n" +
                    "\t\t\tb:=2;\n" +
                    "\t\ta:=a-1\n" +
                    "\tend\n" +
                    "end."
    });

    public static CodeArea getCodeArea(){
        return instance.codeArea;
    }

    public CodeArea codeArea;
    private ExecutorService executor;

    public Node node;
    public String styleSheet = JavaKeywordsAsyncDemo.class.getResource("/assets/java-keywords.css").toString();

    private Scene scene;

    public void initialize() {
        executor = Executors.newSingleThreadExecutor();
        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        Subscription cleanupWhenDone = codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(codeArea.multiPlainChanges())
                .filterMap(t -> {
                    if(t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);

        // call when no longer need it: `cleanupWhenFinished.unsubscribe();`

        codeArea.replaceText(0, 0, sampleCode);

        node = new StackPane(new VirtualizedScrollPane<>(codeArea));
        ((StackPane) node).getStylesheets().add(styleSheet);
        //scene = new Scene(new StackPane(new VirtualizedScrollPane<>(codeArea)), 600, 400);
        //scene.getStylesheets().add();
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = codeArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeArea.setStyleSpans(0, highlighting);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}