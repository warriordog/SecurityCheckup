package net.acomputerdog.securitycheckup.main.gui.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.comparison.Comparison;
import net.acomputerdog.securitycheckup.test.registry.Bundle;
import net.acomputerdog.securitycheckup.test.step.Step;
import net.acomputerdog.securitycheckup.util.gson.GenericGsonAdapter;
import net.acomputerdog.securitycheckup.util.gson.GenericWrapped;

import java.io.Reader;
import java.io.Writer;

public class JsonUtils {
    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Step .class, new GenericGsonAdapter<>(Step.class))
            .registerTypeAdapter(Comparison .class, new GenericGsonAdapter<>(Comparison.class))
            .registerTypeAdapter(GenericWrapped .class, new GenericWrapped.GenericWrappedGsonAdapter())
            .create();

    public static Test readTest(Reader reader) {
        return gson.fromJson(reader, Test.class);
    }

    public static void writeTest(Test test, Writer writer) {
        gson.toJson(test, writer);
    }

    public static Profile readProfile(Reader reader) {
        return gson.fromJson(reader, Profile.class);
    }

    public static void writeProfile(Profile profile, Writer writer) {
        gson.toJson(profile, writer);
    }

    public static Bundle readBundle(Reader reader) {
        return gson.fromJson(reader, Bundle.class);
    }

    public static void writeBundle(Bundle bundle, Writer writer) {
        gson.toJson(bundle, writer);
    }

    public static JarIndex readJarIndex(Reader reader) {
        return gson.fromJson(reader, JarIndex.class);
    }

    public static Gson getGson() {
        return gson;
    }
}
