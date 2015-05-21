package cz.dpp.ukol2.demo;

import cz.dpp.ukol2.argparse.ArgParse;
import cz.dpp.ukol2.argparse.ArgParseException;

/**
 * Main class for assignment demo.
 */
public class DemoMain {

    public static void main (String[] args)
    {
        try {
            DemoArgs demoArgs = new DemoArgs();
            ArgParse.parse(args, demoArgs);
            System.out.println(ArgParse.help(demoArgs));

            System.out.println("verbose = " + demoArgs.verbose);
            System.out.println("size = " + demoArgs.size);

            System.out.print("args = ");
            for (String s : demoArgs.args) System.out.print(s + " ");
            System.out.println();

        } catch (ArgParseException ex) {
            ex.printStackTrace();
        }
    }
}
