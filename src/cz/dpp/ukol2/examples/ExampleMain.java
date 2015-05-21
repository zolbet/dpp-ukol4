package cz.dpp.ukol2.examples;

import cz.dpp.ukol2.argparse.ArgParse;
import cz.dpp.ukol2.argparse.ArgParseException;

/**
 * Example usage of the <tt>ArgParse</tt> class
 */
public class ExampleMain {

    public static void main (String[] args) {
        try {
            ExampleArgs parseargs = new ExampleArgs();
            ArgParse.parse(args, parseargs);
            System.out.println(ArgParse.help(parseargs));
        } catch (ArgParseException ex) {
            /* ... */
        }
    }
}
