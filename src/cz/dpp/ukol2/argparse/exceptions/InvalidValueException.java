package cz.dpp.ukol2.argparse.exceptions;

import cz.dpp.ukol2.argparse.ArgParseException;

/**
 * Exception thrown when the user supplies a value that doesn't conform to argument requirements.
 */
public class InvalidValueException extends ArgParseException {

    public InvalidValueException(String message, String argument, String value)
    {
        super(message, argument, value);
    }
}
