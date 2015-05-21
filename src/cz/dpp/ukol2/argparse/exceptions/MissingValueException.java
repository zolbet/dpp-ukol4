package cz.dpp.ukol2.argparse.exceptions;

import cz.dpp.ukol2.argparse.ArgParseException;

/**
 * Exception thrown when the user doesn't specify a value for an argument that requires one.
 */
public class MissingValueException extends ArgParseException {

    public MissingValueException(String message, String argument)
    {
        super(message, argument, null);
    }
}
