package chess;
import java.io.*;

// A helper class, it is composed of static functions
public class Utilities {
    // custom controlled printing of stack trace
    public static void printException(Exception e) {
        StackTraceElement ste = null;

        // this loop is important to find the proper stack trace element
        // without this, there may be null fileName and -1 lineNumber
        int i = 0;
        while (i < e.getStackTrace().length && (ste = e.getStackTrace()[i++]).getLineNumber() == -1);
        if (ste == null) {
            printError("Utilities.printException error: StackTraceElement ste is null");
            return;
        }

        // set the respective variables
        String className = ste.getClassName();
        String methodName = ste.getMethodName();
        String fileName = ste.getFileName();
        int lineNumber = ste.getLineNumber();
        // print error message and location
        printError(e.toString());
        printError("\tat " + className + "." + methodName + "(" + fileName + ":" + lineNumber + ")");
    }

    // prints errors
    public static void printError(String message) {
        System.err.println(message);
    }
    // prints error and exist
    public static void printErrorAndExit(String message) {
        printError(message);
        System.exit(1);
    }

    // return case equivalent of letter. returns same char otherwise
    public static char charToLowerCase(char c) {
        if (c >= 'A' && c <= 'Z') return (char)(c - ('A' - 'a'));
        return c;
    }
    public static char charToUpperCase(char c) {
        if (c >= 'a' && c <= 'z') return (char)(c + ('A' - 'a'));
        return c;
    }

    // return row/col's respective chess character. i.e 00 -> a1, 13 -> c2
    public static char rowToChar(int row) {
        try {
            return (char)(row + '1');
        } catch (Exception e) { Utilities.printException(e); }
        return '\0';
    }
    public static char colToChar(int col) {
        try {
            return (char)(col + 'a');
        } catch (Exception e) { Utilities.printException(e); }
        return '\0';
    }
}
