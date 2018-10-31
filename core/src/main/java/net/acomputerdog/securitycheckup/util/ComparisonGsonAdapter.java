package net.acomputerdog.securitycheckup.util;

import com.google.gson.*;
import net.acomputerdog.securitycheckup.test.comparison.Comparison;

import java.lang.reflect.Type;

public class ComparisonGsonAdapter implements JsonSerializer<Comparison>, JsonDeserializer<Comparison> {
    @Override
    public Comparison deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        if (object.has("class")) {
            try {
                Class cls = Class.forName(object.get("class").getAsString(), false, Thread.currentThread().getContextClassLoader());
                if (Comparison.class.isAssignableFrom(cls)) {
                    if (object.has("args")) {
                        return context.deserialize(object.get("args"), cls);
                    } else {
                        throw new JsonParseException("Missing required property 'args'");
                    }
                } else {
                    throw new JsonParseException("Specified class is not a subclass of Step");
                }
            } catch (ClassNotFoundException e) {
                throw new JsonParseException("Unable to find class of step", e);
            }
        } else {
            throw new JsonParseException("Missing required property 'class'");
        }
    }

    @Override
    public JsonElement serialize(Comparison src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = new JsonObject();
        root.addProperty("class", src.getClass().getName());
        root.add("args", context.serialize(src, src.getClass()));
        return root;
    }
}
