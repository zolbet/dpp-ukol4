package cz.dpp.ukol2.argparse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Specify different short option
 *
 * <p>By default, arguments get single-letter short options from first letter of their names,
 * on a first-come-first-served basis. I.e., when both <tt>version</tt> and <tt>verbose</tt>
 * options are present, the first one gets a shorthand <tt>-v</tt> and the other only gets
 * the long option name. When annotated with <tt>@ShortOption('V')</tt>, the <tt>verbose</tt>
 * field would get the shorthand <tt>-V</tt>.
 *
 * <p>Unlike @{@link Alias}, <tt>@ShortOption</tt> doesn't override the long option name,
 * so in our example, <tt>verbose</tt> would still be available as <tt>--verbose</tt>.
 *
 * <p>If both <tt>@ShortOption</tt> and <tt>@Alias</tt> are present, <tt>@Alias</tt> takes
 * precedence and <tt>@ShortOption</tt> is ignored.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ShortOption {
    char value();
}
