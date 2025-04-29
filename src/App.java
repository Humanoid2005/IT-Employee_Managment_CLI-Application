import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import management_systems.Parser;

public class App {
    private final Parser parser;
    public Scanner scanner;
    
    public App(InputStream in,PrintStream out){
        this.parser = new Parser(out);
        this.scanner = new Scanner(in);
    }

    public static void main(String[] args) {
        App EmployeeManager = new App(System.in,System.out);
        EmployeeManager.parser.processCommands(EmployeeManager.scanner);
    }
}
