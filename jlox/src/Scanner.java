import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("and",    TokenType.AND);
        keywords.put("class",  TokenType.CLASS);
        keywords.put("else",   TokenType.ELSE);
        keywords.put("false",  TokenType.FALSE);
        keywords.put("for",    TokenType.FOR);
        keywords.put("fun",    TokenType.FUN);
        keywords.put("if",     TokenType.IF);
        keywords.put("nil",    TokenType.NIL);
        keywords.put("or",     TokenType.OR);
        keywords.put("print",  TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super",  TokenType.SUPER);
        keywords.put("this",   TokenType.THIS);
        keywords.put("true",   TokenType.TRUE);
        keywords.put("var",    TokenType.VAR);
        keywords.put("while",  TokenType.WHILE);
    }

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while(!isAtEnd()) {
            //Start at beginning of the next Lexeme
            start = current;
            //starts a recursive call to scan token
            scanToken();
        }
        //Scanner works it way through the file until it runs out of tokens
        //When the scanner is finished it appends one last EOF token
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    /***********************************************************
     * Name: isAtEnd()
     * Use: returns if the current char is at the end of the file
     *
     **********************************************************/


    private boolean isAtEnd() {
        return current>= source.length();
    }
    /***********************************************************
     * Name: scanToken()
     * Use: uses a switch statement on current char and adds token
     *  to the proper output
     *
     **********************************************************/



        private boolean isDigit(char c){
            return c >= '0' && c <= '9';
        }

        private void number() {
            while(isDigit(peek())) advance();

            //look for a decimal point
            if(peek() == '.' && isDigit(peekNext())) {
                //consume the .
                advance();

                while(isDigit(peek())) advance();
            }
            //adds a token by substringing the number and then casts that substring into a double
            addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
        }
    /***********************************************************
     * Name: advance()
     * Use: Consumes the next character in the file and returns it
     *
     **********************************************************/

    private char advance()
    {
        current++;
        return source.charAt(current - 1);
    }
    /***********************************************************
     * Name: addToken(TokenType type)
     * Use: Calls the addToken(TokenType type, Object literal)
     *
     *
     **********************************************************/

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal)
    {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));

    }

    private boolean match(char expected)
    {
        if(isAtEnd()) return false;
        if(source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek() {
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private void string(){
        while(peek() != '"' && !isAtEnd())
        {
            if(peek() == '\n') line++;
            advance();
        }

        //Unterminated String
        if(isAtEnd()){
            Lox.error(line, "Unterminated String.");
            return;
        }
        //the close ".

        advance();

        //Trim surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }



    private char peekNext()
    {
        if(current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c)
    {
        return (c >= 'a' && c <= 'z' ||
                c >= 'A' && c <= 'Z' ||
                c == '_');
    }
    private boolean isAplhaNumeric(char c)
    {
        return isAlpha(c) || isDigit(c);
    }

    private void identifier() {
        while(isAplhaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if(type == null) type = TokenType.IDENTIFIER;
        addToken(type);
    }

    private void scanToken() {
        //call advance to move to next char
        char c = advance();
        switch(c)
        {
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(TokenType.MINUS); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '*': addToken(TokenType.STAR); break;
            case '!': addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG); break;
            case '=': addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL); break;
            case '<': addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS); break;
            case '>': addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER); break;
            case '/':
                if(match('/')) {
                    //A comment goes until the end of the line.
                    while(peek() != '\n' && !isAtEnd()) advance();
                }else{
                    addToken(TokenType.SLASH);
                }
                break;

            case ' ':
            case '\r':
            case '\t':
                //ignore whitespace
                break;

            case '\n':
                line++;
                break;

            case '"': string(); break;

            default:
                if(isDigit(c)) {
                    number();
                }else if(isAlpha(c)){
                identifier();
                }else{

                Lox.error(line, "Unexpected Character.");
            }
                break;
            }
        }
}
