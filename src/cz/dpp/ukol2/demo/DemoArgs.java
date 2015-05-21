package cz.dpp.ukol2.demo;

import cz.dpp.ukol2.argparse.Help;
import cz.dpp.ukol2.argparse.PlainArgs;

import java.util.ArrayList;
import java.util.List;

/**
 * Assignment demo
 */
public class DemoArgs {

    /* works as -v or --verbose */
    @Help("be verbose")
    public boolean verbose;

    /* optional, works as -s 42 or --size=42 */
    @Help("size of your shoes")
    public int size = 42;


    /* plain args = everything else */
    @PlainArgs
    public List<String> args = new ArrayList<>();
}
