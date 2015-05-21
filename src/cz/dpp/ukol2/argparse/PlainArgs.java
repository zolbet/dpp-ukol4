package cz.dpp.ukol2.argparse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** List of "plain" arguments
 *
 * <p>Annotate a <tt>Collection&lt;String&gt;</tt> array as <tt>@PlainArgs</tt> to indicate that
 * you want this collection to hold all the "plain" arguments (i.e. all that are not options or their values).
 *
 * <p>At most one <tt>@PlainArgs</tt> argument is allowed.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PlainArgs {
}
