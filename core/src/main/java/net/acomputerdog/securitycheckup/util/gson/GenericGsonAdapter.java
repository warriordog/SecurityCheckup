package net.acomputerdog.securitycheckup.util.gson;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Gson utility class to serialize generic types.  Can ONLY be used if the generic supertype is abstract or an interface.
 * It is best to use an interface so that there is no chance of a class potentially becoming concrete and breaking here.
 *
 * @param <T> The type of class to serialize.
 */
public class GenericGsonAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
    private static final String PROPERTY_CLASS = "class";
    private static final String PROPERTY_CONTENTS = "contents";

    private final Class<T> tClass;

    public GenericGsonAdapter(Class<T> tClass) {
        if (tClass == null) {
            throw new IllegalArgumentException("Type class cannot be null");
        }

        this.tClass = tClass;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        if (object.has(PROPERTY_CLASS)) {
            try {
                Class cls = Class.forName(object.get(PROPERTY_CLASS).getAsString(), false, Thread.currentThread().getContextClassLoader());
                if (tClass.isAssignableFrom(cls)) {
                    if (object.has(PROPERTY_CONTENTS)) {
                        return context.deserialize(object.get(PROPERTY_CONTENTS), cls);
                    } else {
                        throw new JsonParseException("Missing required property " + PROPERTY_CONTENTS);
                    }
                } else {
                    throw new JsonParseException("Specified class is not a subclass of T");
                }
            } catch (ClassNotFoundException e) {
                throw new JsonParseException("Unable to find class of element", e);
            }
        } else {
            throw new JsonParseException("Missing required property " + PROPERTY_CLASS);
        }
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = new JsonObject();
        root.addProperty(PROPERTY_CLASS, src.getClass().getName());
        root.add(PROPERTY_CONTENTS, context.serialize(src, src.getClass()));
        return root;
    }
}
