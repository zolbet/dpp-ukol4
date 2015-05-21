package cz.dpp.ukol2.argparse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Alias(es) for this argument
 *
 * <p>By default, the name of the argument is taken from the name of the member property.
 * By supplying a string to the <tt>@Alias</tt> annotation, you can specify one or more
 * short or long names to use as aliases. This overrides auto-generated names.
 *
 * <p>Example: <tt>"--short -s --othershort"</tt>
 *
 * <p>The annotated option will be triggered by space-separated variants as specified in the string.
 *
 * <p>Note that it is not allowed to specify long option with single comma (<tt>-short</tt>)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Alias {
    String value();
}
