class Token {
    /***********************************************************
     * Name:Token
     * Use: Creates a token object we can use this to keep track
     *  of the raw lexeme we used, and other things the scanner
     *  learns
     *
     **********************************************************/

    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;

    Token(TokenType type, String lexeme, Object literal, int line) {
        //
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line =  line;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
