import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JavaTokenizer {
    private ArrayList<Token> tokens = new ArrayList<Token>();
    private String code;
    public JavaTokenizer(String code){
        this.code = code;
    }
    public ArrayList<Token> tokenize() throws Exception {
        tokens = new ArrayList<Token>();
        InputStream stream = new ByteArrayInputStream(this.code.getBytes(StandardCharsets.UTF_8));
        JavaLexer lexer = new JavaLexer(CharStreams.fromStream(stream, StandardCharsets.UTF_8));
        List<? extends Token> tokenList = lexer.getAllTokens();
        for (Token token : tokenList) {
            String symbolicName = JavaLexer.VOCABULARY.getSymbolicName(token.getType());
            // normalize the token except white space (skip)
            if (!symbolicName.equals("WS")
                    && !symbolicName.equals("COMMENT")
                    && !symbolicName.equals("LINE_COMMENT")) {
              tokens.add(token);
            }
        }

        return tokens;
    }

}
