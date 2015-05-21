package cz.dpp.ukol4.examples;

import java.util.ArrayList;
import java.util.List;

import cz.dpp.ukol2.argparse.PlainArgs;
import cz.dpp.ukol2.argparse.Subcommand;

public class ExampleArgs {

	@Subcommand
    public ListSubcmdArgs list;
    
	@Subcommand
    public UnpackSubcmdArgs unpack;
    
	@Subcommand
    public PackSubcmdArgs pack;
	
    public boolean version;    

    /* all arguments that are not -s(hort) or --long options will be stored here */
    @PlainArgs
    public List<String> plainArguments = new ArrayList<>();
}
