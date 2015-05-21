package cz.dpp.ukol2.argparse.parsers;

import java.lang.reflect.Field;

/** Converter interface for different types of values
 *
 * <p>Value parsers must know how to convert a string representation into a concrete type.
 * They can also return expected values, and configure themselves based on the supplied
 * type or field annotations.
 */
public interface ValueParser {
    /**
     * Convert string representation to an object
     * @param value string passed by user
     * @return Object of an appropriate type representing the value
     */
    Object parseStringValue(String value);

    /**
     * Description of allowed values
     * @return text representation of allowed values
     */
    String allowedValuesHelpText();

    /**
     * Configure self based on properties of the field, usually annotations,
     * or properties of the actual type for conversion.
     * @param field field in the argument class
     * @param actualType actual type -- either type of the field, or inner type if the field is a collection
     */
    void configureFromField(Field field, Class<?> actualType);
}
