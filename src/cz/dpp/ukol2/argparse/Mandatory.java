package cz.dpp.ukol2.argparse;

import cz.dpp.ukol2.argparse.exceptions.MandatoryNotPresentException;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** This argument is mandatory
 *
 * <p>By default, all member variables are converted to optional arguments. This annotation
 * marks the argument as mandatory, a {@link MandatoryNotPresentException} is thrown if it is not present.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Mandatory {
}
