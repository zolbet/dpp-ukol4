package cz.dpp.ukol2.argparse.exceptions;

import cz.dpp.ukol2.argparse.ArgParseException;

/**
 * Exception thrown when the user passes an argument that is not part of the definition.
 */
public class UnrecognizedArgumentException extends ArgParseException {

    public UnrecognizedArgumentException(String argument)
    {
        super("Unrecognized argument", argument, null);
    }
}
