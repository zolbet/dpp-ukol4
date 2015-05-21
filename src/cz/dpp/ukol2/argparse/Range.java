package cz.dpp.ukol2.argparse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Specify range of numeric values
 *
 * <p>Only valid on numeric-type arguments. You can specify minimum, maximum or both ranges.
 *
 * <p>Range is inclusive, <tt>min</tt> is the minimum allowed value and <tt>max</tt> is the maximum.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Range {
    long min() default Long.MIN_VALUE;
    long max() default Long.MAX_VALUE;
}
