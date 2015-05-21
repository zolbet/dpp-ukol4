package cz.dpp.ukol2.argparse;

/**
 * Exception for errors in command line arguments
 */
public class ArgParseException extends Exception {

    private final String argumentName;
    private final String argumentValue;

    public ArgParseException (String message, String argumentName, String argumentValue)
    {
        super(message);
        this.argumentName = argumentName;
        this.argumentValue = argumentValue;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public String getArgumentValue() {
        return argumentValue;
    }
}
