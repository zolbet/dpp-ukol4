package cz.dpp.ukol2.argparse.exceptions;

import cz.dpp.ukol2.argparse.ArgParseException;

/**
 * Exception thrown when the user doesn't specify an argument marked mandatory.
 */
public class MandatoryNotPresentException extends ArgParseException {

    public MandatoryNotPresentException(String argument)
    {
        super("Mandatory argument is missing", argument, null);
    }
}
