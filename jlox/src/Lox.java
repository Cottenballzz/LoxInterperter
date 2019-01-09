import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/***********************************************************
 * Name:
 * Use:
 *
 **********************************************************/

public class Lox{
    static boolean hadError = false;
    public static void main (String[] args) throws IOException {
        if(args.length > 1)
        {
            System.out.println("Usage: jlox [script");
            System.exit(64);
        }else if (args.length == 1)
        {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    /*************************************************************************
     * Name: runFile()
     * Use: When Lox is run from the command line, it takes a single argument,
     * the path, and reads the file and executes
     *************************************************************************/
    private static void runFile(String path) throws IOException {
        //creates an array of bytes and reads file into it 1 at a time
        byte[] bytes = Files.readAllBytes(Paths.get(path));

        //calls the run function with the bytes arguement, turned into a string using default Charset
        run(new String(bytes, Charset.defaultCharset()));

        //Indicate an error in the exit code
        //Error code is removed from the scanner, because it is good practice to
        //separate the code the generates the error and reports the error
        if(hadError) System.exit(65);
    }
    /*************************************************************
     * Name: runPrompt()
     * Use: If Lox is called without any arguments, it prompts the user
     * where the user can write code one line at a time which it executes
     ************************************************************/
    private static void runPrompt() throws IOException {
        //
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;){
            System.out.print("> ");
            run(reader.readLine());
            //Error code is removed from the scanner, because it is good practice to
            //separate the code the generates the error and reports the error
            hadError = false;
        }

    }

    /***********************************************************
     * Name: run();
     * Use: takes the bytes sent from both runFile() and runPrompt()
     * and turns them into tokens, currently it writes those tokens
     * to the console;
     *
     **********************************************************/
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        //only prints tokens for now
        for(Token token : tokens) {
            System.out.println(token);
        }
    }

    /***********************************************************
     * Name: error()
     * Use: Basic error handler that calls report() with the
     * the line number and error handler
     *
     **********************************************************/
    static void error(int line, String message) {
        report(line, "", message);
    }

    /***********************************************************
     * Name: report()
     * Use: prints out the error with the line number, the error,
     * where the error occurred in the line and changes a bool
     * to flag an error occurred
     *
     **********************************************************/
    private static void report(int line, String where, String message){
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }


}