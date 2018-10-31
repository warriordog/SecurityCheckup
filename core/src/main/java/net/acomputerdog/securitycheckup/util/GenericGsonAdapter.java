package net.acomputerdog.securitycheckup.util;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Gson utility class to serialize generic types.  Can ONLY be used if the generic supertype is abstract or an interface.
 * @param <T> The type of class to serialize.
 */
public class GenericGsonAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
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
        if (object.has("class")) {
            try {
                Class cls = Class.forName(object.get("class").getAsString(), false, Thread.currentThread().getContextClassLoader());
                if (tClass.isAssignableFrom(cls)) {
                    if (object.has("args")) {
                        return context.deserialize(object.get("args"), cls);
                    } else {
                        throw new JsonParseException("Missing required property 'args'");
                    }
                } else {
                    throw new JsonParseException("Specified class is not a subclass of T");
                }
            } catch (ClassNotFoundException e) {
                throw new JsonParseException("Unable to find class of element", e);
            }
        } else {
            throw new JsonParseException("Missing required property 'class'");
        }
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = new JsonObject();
        root.addProperty("class", src.getClass().getName());
        root.add("args", context.serialize(src, src.getClass()));
        return root;
    }
}
