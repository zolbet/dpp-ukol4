package cz.dpp.ukol2.argparse.parsers;

import java.lang.reflect.Field;

/**
 * Do-nothing ValueParser for string values
 */
public class StringParser implements ValueParser {

    @Override
    public Object parseStringValue(String value) {
        return value;
    }

    @Override
    public String allowedValuesHelpText() {
        return "<text>";
    }

    @Override
    public void configureFromField(Field field, Class<?> actualType) {

    }
}
