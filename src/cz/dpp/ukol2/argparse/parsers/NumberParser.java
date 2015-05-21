package cz.dpp.ukol2.argparse.parsers;

import cz.dpp.ukol2.argparse.Range;

import java.lang.reflect.Field;

/**
 * Common ValueParser for all integer types
 */
abstract public class NumberParser implements ValueParser {

    /** Minimum allowed value */
    private long min = Long.MIN_VALUE;
    /** Maximum allowed value */
    private long max = Long.MAX_VALUE;

    /** Set minimum and maximum allowed values.
     *
     * <p>This is derived from the @{@link cz.dpp.ukol2.argparse.Range} annotation in <tt>configureFromField</tt>, but
     * the method is exposed for testing purposes.
     * @param min minimum allowed value
     * @param max maximum allowed value
     */
    public void setRange (long min, long max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void configureFromField (Field field, Class<?> actualType) {
        Range range = field.getAnnotation(Range.class);
        if (range != null) {
            setRange(range.min(), range.max());
        }
        if (min > max) throw new RuntimeException(String.format("ArgParse: Invalid range on field '%s'", field.getName()));
    }

    @Override
    public String allowedValuesHelpText() {
        if (min == Long.MIN_VALUE && max == Long.MAX_VALUE) {
            return "<number>";
        } else if (min == Long.MIN_VALUE) {
            return String.format("<at most %d>", max);
        } else if (max == Long.MAX_VALUE) {
            return String.format("<at least %d>", min);
        } else {
            return String.format("%d..%d", min, max);
        }
    }

    /**
     * Perform number conversion, range checking, and return a Long
     * @param value string representation of number
     * @return Long object, for downcasting in subclasses
     * @throws IllegalArgumentException when <tt>value</tt> is not a number, or when it exceeds specified range
     */
    protected Long parseValueWithChecks(String value) {
        // attempt to convert to number, throws NumberFormatException if that fails
        long number = Long.parseLong(value);

        // check if result fits in range
        if (number < min || number > max) {
            String message;
            if (min > Long.MIN_VALUE && max < Long.MAX_VALUE) {
                message = String.format("Value must be between %d and %d", min, max);
            } else if (min > Long.MIN_VALUE) {
                message = String.format("Value must be at least %d", min);
            } else if (max < Long.MAX_VALUE) {
                message = String.format("Value must be at most %d", max);
            } else {
                message = "Value somehow exceeded its own range";
            }
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * NumberParser for <tt>Long</tt> and <tt>long</tt>
     */
    public static class LongNum extends NumberParser {
        @Override
        public Object parseStringValue (String value)
        {
            return parseValueWithChecks(value).longValue();
        }
    }

    /**
     * NumberParser for <tt>Integer</tt> and <tt>int</tt>
     */
    public static class IntNum extends NumberParser {
        @Override
        public Object parseStringValue (String value)
        {
            return parseValueWithChecks(value).intValue();
        }
    }

    /**
     * NumberParser for <tt>Short</tt> and <tt>short</tt>
     */
    public static class ShortNum extends NumberParser {
        @Override
        public Object parseStringValue (String value)
        {
            return parseValueWithChecks(value).shortValue();
        }
    }

    /**
     * NumberParser for <tt>Byte</tt> and <tt>byte</tt>
     */
    public static class ByteNum extends NumberParser {
        @Override
        public Object parseStringValue (String value)
        {
            return parseValueWithChecks(value).byteValue();
        }
    }
}
