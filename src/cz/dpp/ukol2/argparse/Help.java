package cz.dpp.ukol2.argparse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Help text for this option
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Help {
    String value();
}

