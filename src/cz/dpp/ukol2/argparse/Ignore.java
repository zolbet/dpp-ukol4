package cz.dpp.ukol2.argparse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Ignore this member variable when parsing
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {
}
