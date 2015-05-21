package cz.dpp.ukol2.argparse.exceptions;

import cz.dpp.ukol2.argparse.ArgParseException;

/**
 * Exception thrown when the user specifies a value for an argument that doesn't take one.
 */
public class SuperfluousValueException extends ArgParseException {

    public SuperfluousValueException(String argument, String value)
    {
        super("Superfluous value on argument", argument, value);
    }
}
