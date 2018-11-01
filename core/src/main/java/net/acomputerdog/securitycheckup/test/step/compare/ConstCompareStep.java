package net.acomputerdog.securitycheckup.test.step.compare;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.comparison.Comparison;
import net.acomputerdog.securitycheckup.test.step.Step;

public class ConstCompareStep<T extends Comparable<T>> implements Step<Boolean> {
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

}
