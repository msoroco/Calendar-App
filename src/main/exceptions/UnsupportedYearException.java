package exceptions;

public class UnsupportedYearException extends Exception {

    public UnsupportedYearException(int year) {
        super("The year" + year + " is not supported by this application");
    }
}
