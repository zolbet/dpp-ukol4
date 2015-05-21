package cz.dpp.ukol4.examples;

import cz.dpp.ukol2.argparse.ArgParse;
import cz.dpp.ukol2.argparse.ArgParseException;
import cz.dpp.ukol4.examples.ExampleArgs;

public class ExampleMain {

    public static void main (String[] args) {

        try {
            ExampleArgs demoArgs = new ExampleArgs();
            ArgParse.parse(args, demoArgs);
            System.out.println(ArgParse.help(demoArgs));
            
            if (demoArgs.version) {
            	System.out.println("Ultimate File Archiver v007");            	
            }
            
            if (demoArgs.list != null) {            	
                System.out.print("Will print contents of ");
                
                if (demoArgs.list != null) {
                	for (String s : demoArgs.list.args) System.out.print(s + " ");
                }
                
            	if (demoArgs.list.timestamps) {
            		System.out.println(" with timestamps.");
            	}
            }            
            
        } catch (ArgParseException ex) {
            ex.printStackTrace();
        }

    }
}
