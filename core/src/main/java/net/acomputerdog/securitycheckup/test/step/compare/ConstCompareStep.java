package net.acomputerdog.securitycheckup.test.step.compare;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.comparison.Comparison;
import net.acomputerdog.securitycheckup.test.step.Step;

import java.util.Arrays;
import java.util.List;

public class ConstCompareStep<T extends Comparable<T>> extends Step<Boolean> {
    private final Comparison<T, T> comparison;
    private final Step<T> child;
    private final T value;

    public ConstCompareStep(Comparison<T, T> comparison, Step<T> child, T value) {
        this.comparison = comparison;
        this.child = child;
        this.value = value;
    }

    public Step<T> getChild() {
        return child;
    }

    public T getValue() {
        return value;
    }

    public Comparison<T, T> getComparison() {
        return comparison;
    }

    @Override
    public Boolean run(TestEnvironment environment) {
        return comparison.compare(child.run(environment), value);
    }

    @Override
    public List<Step> getSubsteps() {
        return Arrays.asList(new Step[]{child});
    }
}
