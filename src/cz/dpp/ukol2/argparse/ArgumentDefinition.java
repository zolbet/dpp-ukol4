package cz.dpp.ukol2.argparse;

import cz.dpp.ukol2.argparse.exceptions.*;
import cz.dpp.ukol2.argparse.parsers.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Representation of a concrete command-line argument
 */
class ArgumentDefinition {

    /** the argument is mandatory */
    private final boolean mandatory;
    /** the argument has auto-generated short name */
    private final boolean defaultShortName;
    /** the field for this argument is a collection */
    private final boolean isCollection;

    /** help text from annotation */
    private final String helpText;

    /** recognized long option names */
    private final Set<String> longNames = new HashSet<>();
    /** recognized short option names */
    private final Set<String> shortNames = new HashSet<>();

    /** field for this argument */
    private final Field field;
    /** value parser for this argument */
    private final ValueParser valueParser;
    /** Collection.add method if the field is a collection */
    private final Method collectionAddMethod;

    /** collection of types for which we have parsers */
    private static final Map<Class<?>, Class<? extends ValueParser>> availableParsers = new HashMap<>();

    static {
        // text parsers
        availableParsers.put(String.class, StringParser.class);
        availableParsers.put(Character.class, CharParser.class);
        availableParsers.put(char.class, CharParser.class);

        // numeric parsers
        availableParsers.put(Long.class, NumberParser.LongNum.class);
        availableParsers.put(long.class, NumberParser.LongNum.class);
        availableParsers.put(Integer.class, NumberParser.IntNum.class);
        availableParsers.put(int.class, NumberParser.IntNum.class);
        availableParsers.put(Short.class, NumberParser.ShortNum.class);
        availableParsers.put(short.class, NumberParser.ShortNum.class);
        availableParsers.put(Byte.class, NumberParser.ByteNum.class);
        availableParsers.put(byte.class, NumberParser.ByteNum.class);

        // enum parser
        availableParsers.put(Enum.class, EnumParser.class);
    }

    /**
     * Generate argument definition from a field and its annotations
     * @param field field of the argument class
     */
    public ArgumentDefinition(Field field) {
        this.field = field;

        mandatory = field.isAnnotationPresent(Mandatory.class);

        // handle aliases.
        if (field.isAnnotationPresent(Alias.class)) {
            // first, check whether @Alias is present, as that overrides everything
            defaultShortName = false;
            Alias aliasAnnotation = field.getAnnotation(Alias.class);
            String[] aliases = aliasAnnotation.value().split(" +");

            if (aliases.length == 0) {
                throw new RuntimeException(String.format("ArgParse: @Alias used on '%s' but no aliases specified", field.getName()));
            }

            for (String alias : aliases) {
                if (alias.startsWith("---")) {
                    throw new RuntimeException(String.format("ArgParse: alias '%s' on '%s' is invalid", alias, field.getName()));
                } else if (alias.startsWith("--")) {
                    longNames.add(alias.substring(2));
                } else if (alias.startsWith("-")) {
                    shortNames.add(alias.substring(1));
                } else {
                    throw new RuntimeException(String.format("ArgParse: alias '%s' on '%s' is invalid", alias, field.getName()));
                }
            }

        } else if (field.isAnnotationPresent(ShortOption.class)) {
            // if not, check if default short name is overriden by @ShortOption
            defaultShortName = false;
            ShortOption shortOption = field.getAnnotation(ShortOption.class);
            shortNames.add(Character.toString(shortOption.value()));
            longNames.add(field.getName());

        } else {
            // finally, generate both short and long option name from field name
            defaultShortName = true;
            shortNames.add(field.getName().substring(0,1));
            longNames.add(field.getName());
        }

        if (field.isAnnotationPresent(Help.class)) {
            Help helpAnnotation = field.getAnnotation(Help.class);
            helpText = helpAnnotation.value();
        } else {
            helpText = "";
        }

        // determine value type for the field
        Class<?> fieldType = field.getType();
        isCollection = Collection.class.isAssignableFrom(fieldType);
        if (isCollection) {
            // find inner type for the collection
            Type type = field.getGenericType();
            if (!(type instanceof ParameterizedType)) {
                throw new RuntimeException(String.format("ArgParse: unspecified Collection type of field '%s'", field.getName()));
            }
            ParameterizedType parType = (ParameterizedType) type;
            Type[] typeArguments = parType.getActualTypeArguments();
            assert typeArguments.length == 1;
            Class<?> innerType = (Class<?>) typeArguments[0];

            // find the "add" method -- we cannot invoke it directly on a collection of unspecified type
            try {
                collectionAddMethod = fieldType.getDeclaredMethod("add", Object.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("missing Collection.add; this should never happen");
            }
            fieldType = innerType;
        } else {
            collectionAddMethod = null;
        }

        // assign appropriate value parser
        valueParser = findValueParser(fieldType);
        if (valueParser == null && isCollection) {
            throw new RuntimeException(String.format("ArgParse: field '%s' is a collection of valueless arguments; this makes no sense.", field.getName()));
        }
    }

    /**
     * return a {@link ValueParser} instance appropriate for the specified type
     * @param type value type of the argument
     * @return <tt>ValueParser</tt> instance
     */
    private ValueParser findValueParser(Class<?> type) {
        if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {
            // boolean fields denote valueless arguments
            return null;

        } else if (type.isArray()) {
            throw new RuntimeException("ArgParse: arrays are not supported, please use an appropriate Collection");

        } else {
            // try to find the type directly
            Class<? extends ValueParser> parserClass = availableParsers.get(type);
            if (parserClass == null) {
                // try to find a supertype
                for (Class<?> klass : availableParsers.keySet()) {
                    if (klass.isAssignableFrom(type)) {
                        parserClass = availableParsers.get(klass);
                        break;
                    }
                }
                // could not find anything
                if (parserClass == null) {
                    throw new RuntimeException("ArgParse: unsupported argument type " + type);
                }
            }

            // instantiate parser
            ValueParser parser;
            try {
                parser = (ValueParser) parserClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("failed to instantiate ValueParser; this should never happen");
            }
            // allow the parser to configure itself based on annotations
            parser.configureFromField(field, type);
            return parser;
        }
    }

    /**
     * Assign value from command-line argument to a field on the argument object
     * @param target the argument object
     * @param name name under which the argument was specified
     * @param value supplied value, or null for valueless arguments
     * @throws ArgParseException
     */
    public void applyArgument (Object target, String name, String value)
    throws ArgParseException {
        if (needsValue() && value == null) {
            throw new MissingValueException("Value is missing", name);
        } else if (!needsValue() && value != null) {
            throw new SuperfluousValueException(name, value);
        }

        try {
            if (!needsValue()) {
                // valueless field is boolean
                field.setBoolean(target, true);
            } else {
                Object parsedValue = valueParser.parseStringValue(value);
                if (isCollection) addToCollection(target, parsedValue);
                else field.set(target, parsedValue);
            }
        } catch (IllegalArgumentException e) {
            // convert to ArgParse exception
            throw new InvalidValueException(e.getMessage(), name, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("ArgParse: not allowed to set field '%s'", field.getName()));
        }
    }

    /**
     * Add the parsed value to a collection field
     * @param target the argument object
     * @param value parsed value
     */
    private void addToCollection(Object target, Object value) {
        try {
            Collection<?> collection = (Collection<?>)field.get(target);
            if (collection == null) {
                throw new RuntimeException(String.format("ArgParse: collection '%s' is not initialized.", field.getName()));
            }
            collectionAddMethod.invoke(collection, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("ArgParse: not allowed to set field '%s'", field.getName()));
        } catch (InvocationTargetException e) {
            throw new RuntimeException("could not invoke Collection.add on a Collection object; this should never happen");
        }
    }

    /**
     * Is this a mandatory argument?
     * @return true if the argument is mandatory
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * Is this a collection field?
     * @return true if the field is a collection
     */
    public boolean isCollection() {
        return isCollection;
    }

    /**
     * Does this argument require a value?
     * @return true if the argument requires a value, false if it is valueless
     */
    public boolean needsValue() {
        return valueParser != null;
    }

    /**
     * Generate and return help text snippet, listing option names and allowed values
     * @return help snippet
     */
    public String getHelp() {
        StringBuilder sb = new StringBuilder();
        // find allowed values, if appropriate
        String allowedValues = "";
        if (valueParser != null) {
            allowedValues = valueParser.allowedValuesHelpText();
        }

        // list all short names
        for (String shortName : shortNames) {
            if (needsValue()) {
                sb.append(String.format("\t-%s %s\n", shortName, allowedValues));
            } else {
                sb.append(String.format("\t-%s\n", shortName));
            }
        }

        // list all long names
        for (String longName : longNames) {
            if (needsValue()) {
                sb.append(String.format("\t--%s=%s\n", longName, allowedValues));
            } else {
                sb.append(String.format("\t--%s\n", longName));
            }
        }

        // erase last newline, append help text
        sb.setLength(sb.length() - 1);
        sb.append('\t');
        sb.append(helpText);
        sb.append('\n');

        return sb.toString();
    }

    /**
     * Get a set of all long option names
     * @return set of long option names
     */
    public Set<String> getLongNames() {
        return longNames;
    }

    /**
     * Get a set of all short option names
     * @return set of short option names
     */
    public Set<String> getShortNames() {
        return shortNames;
    }

    /**
     * Does this argument have an auto-generated short name?
     * @return true if the argumen has an auto-generated short name
     */
    public boolean hasDefaultShortName() {
        return defaultShortName;
    }

    @Override
    public String toString() {
        return field.getName();
    }
}
