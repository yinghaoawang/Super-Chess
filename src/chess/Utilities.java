package chess;
import java.io.*;
public class Utilities {
    public static void printException(Exception e) {
        /* printing entire stack, it was too long
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        printError(sw.toString());
        */

        /* this seems useful for later
        String exceptionName = e.getClass().getSimpleName();
        */

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

    // self explanatory function
    public static void printError(String message) {
        System.err.println(message);
    }
    public static void printErrorAndExit(String message) {
        printError(message);
        System.exit(1);
    }
    public static char charToLowerCase(char c) {
        if (c >= 'A' && c <= 'Z') return (char)(c - ('A' - 'a'));
        return c;
    }
    public static char charToUpperCase(char c) {
        if (c >= 'a' && c <= 'z') return (char)(c + ('A' - 'a'));
        return c;
    }
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
