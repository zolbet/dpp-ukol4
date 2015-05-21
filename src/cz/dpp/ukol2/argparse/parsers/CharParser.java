package cz.dpp.ukol2.argparse.parsers;

import java.lang.reflect.Field;

/**
 * ValueParser for <tt>char</tt> and <tt>Character</tt> classes
 */
public class CharParser implements ValueParser {

    @Override
    public Object parseStringValue(String value) {
        if (value.length() != 1) {
            throw new IllegalArgumentException("Exactly one character allowed for argument");
        }
        return value.charAt(0);
    }

    @Override
    public String allowedValuesHelpText() {
        return "<character>";
    }

    @Override
    public void configureFromField(Field field, Class<?> actualType) { }
}
