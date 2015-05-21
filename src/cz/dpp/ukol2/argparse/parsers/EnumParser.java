package cz.dpp.ukol2.argparse.parsers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * ValueParser for enum types
 */
public class EnumParser implements ValueParser {

    private final Map<String, Object> options = new HashMap<>();

    @Override
    public Object parseStringValue(String value) {
        Object result = options.get(value);
        if (result != null) {
            return result;
        } else {
            throw new IllegalArgumentException(String.format("Value '%s' not allowed; must be one of: %s", value, allowedValuesHelpText()));
        }
    }

    @Override
    public String allowedValuesHelpText() {
        StringBuilder sb = new StringBuilder();
        for (String key : options.keySet()) {
            sb.append(key);
            sb.append("|");
        }
        // throw away trailing space
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /**
     * Assign an enum class. This happens implicitly in <tt>configureFromField</tt>, but it is exposed for testing purposes.
     * @param enumClass a class object representing an enum
     */
    public void setEnumClass(Class<?> enumClass) {
        assert enumClass.isEnum();
        if (enumClass.getEnumConstants().length == 0) {
            throw new RuntimeException("ArgParse: please don't use empty enums as allowed values...");
        }
        for (Object item : enumClass.getEnumConstants()) {
            options.put(item.toString(), item);
        }
    }

    @Override
    public void configureFromField(Field field, Class<?> actualType) {
        setEnumClass(actualType);
    }
}
