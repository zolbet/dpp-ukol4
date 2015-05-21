package cz.dpp.ukol2.examples;

import cz.dpp.ukol2.argparse.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Example annotation of various possible command line arguments
 */
public class ExampleArgs {

    /* boolean type checks presence of argument */
    /* you can specify help texts */
    @Help("shows program version")
    public boolean version;

    /* default argument names will not be generated automatically */
    @Alias("-n --noShort --somethingelseentirely")
    public boolean noauto;

    /* "-v" is already taken by "version, so we specify "-V" instead;
       unlike with @Alias, "--verbose" remains */
    @ShortOption('V')
    public boolean verbose;

    /* if not @Mandatory, the argument is optional */
    @Mandatory
    public boolean mustBePresent;

    /* this will not be filled out by the arg parser */
    @Ignore
    public boolean thisIsNotArgument;

    /* if an optional argument is not specified, it will keep its default value */
    public int size = 42;

    /* which can be null */
    public Integer size2;

    /* --size3=1 --size3=2 --size3=4
      the different values will be stored in the list.
      Otherwise, only the last value is stored.
      Collection must be initialized by default. */
    public List<Integer> size3 = new ArrayList<>();

    /* this is properly mandatory, and also only allows numbers from a range */
    @Mandatory
    @Range(min=0,max=100)
    public int limited;

    /* enum that specifies allowed values for an argument, these are taken as literal strings */
    public enum FruitValues {
        orange, apple, pear, banana
    }

    /* checks if value is from the enum */
    public FruitValues fruit;

    /* all arguments that are not -s(hort) or --long options will be stored here */
    @PlainArgs
    public List<String> plainArguments = new ArrayList<>();
}
