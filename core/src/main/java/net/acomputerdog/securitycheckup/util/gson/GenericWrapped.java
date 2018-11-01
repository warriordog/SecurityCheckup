package net.acomputerdog.securitycheckup.util.gson;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GenericWrapped<T> {
    private String type;
    private T value;

    // constructor for deserialization
    private GenericWrapped() {}

    public GenericWrapped(T value) {
        this.value = value;

        if (value != null) {
            this.type = value.getClass().getName();
        } else {
            this.type = null;
        }
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public static class GenericWrappedGsonAdapter implements JsonSerializer<GenericWrapped>, JsonDeserializer<GenericWrapped> {
        private static final String PROPERTY_TYPE = "class";
        private static final String PROPERTY_VALUE = "value";

        @Override
        public GenericWrapped deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            if (object.has(PROPERTY_TYPE)) {
                if (object.has(PROPERTY_VALUE)) {
                    try {
                        String className = object.get(PROPERTY_TYPE).getAsString();
                        Class cls = Class.forName(className, false, Thread.currentThread().getContextClassLoader());

                        return new GenericWrapped(context.deserialize(object.get(PROPERTY_VALUE), cls));
                    } catch (ClassNotFoundException e) {
                        throw new JsonParseException("Unable to find class of element", e);
                    }
                } else {
                    throw new JsonParseException("Missing required property " + PROPERTY_VALUE);
                }
            } else {
                throw new JsonParseException("Missing required property " + PROPERTY_TYPE);
            }
        }

        @Override
        public JsonElement serialize(GenericWrapped src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject root = new JsonObject();
            root.addProperty(PROPERTY_TYPE, src.type);
            root.add(PROPERTY_VALUE, context.serialize(src.value));
            return root;
        }
    }
}
