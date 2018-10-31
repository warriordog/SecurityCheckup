package net.acomputerdog.securitycheckup.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.acomputerdog.securitycheckup.test.comparison.Comparison;
import net.acomputerdog.securitycheckup.test.step.Step;
import net.acomputerdog.securitycheckup.util.GenericGsonAdapter;

import java.io.Reader;
import java.io.Writer;
import java.util.Objects;

public class Test {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Comparison.class, new GenericGsonAdapter<>(Comparison.class))
            .registerTypeAdapter(Step.class, new GenericGsonAdapter<>(Step.class))
            .create();

    private final TestInfo info;
    private final Step<TestResult> rootStep;

    // constructor for deserializing
    private Test() {
        this (null, null);
    }

    public Test(TestInfo info, Step<TestResult> rootStep) {
        this.info = info;
        this.rootStep = rootStep;
    }

    public TestInfo getInfo() {
        return info;
    }

    public Step<TestResult> getRootStep() {
        return rootStep;
    }

    public void toJson(Writer writer) {
        gson.toJson(this, writer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Test)) return false;
        Test test = (Test) o;
        return Objects.equals(info.getID(), test.info.getID());
    }

    @Override
    public int hashCode() {
        return info.getID().hashCode();
    }

    @Override
    public String toString() {
        return info.toString();
    }

    public static Test fromJson(Reader reader) {
        return gson.fromJson(reader, Test.class);
    }
}
