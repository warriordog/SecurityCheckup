package net.acomputerdog.securitycheckup.test.step.compare;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.comparison.Comparison;
import net.acomputerdog.securitycheckup.test.step.Step;

import java.util.Arrays;
import java.util.List;

public class CompareStep<T> extends Step<Boolean> {
    private final Comparison<T, T> comparison;
    private final Step<T> step1;
    private final Step<T> step2;

    public CompareStep(Comparison<T, T> comparison, Step<T> step1, Step<T> step2) {
        this.comparison = comparison;
        this.step1 = step1;
        this.step2 = step2;
    }

    public Step<T> getStep1() {
        return step1;
    }

    public Step<T> getStep2() {
        return step2;
    }

    @Override
    public Boolean run(TestEnvironment environment) {
        T result1 = step1.run(environment);
        T result2 = step2.run(environment);

        return comparison.compare(result1, result2);
    }

    @Override
    public List<Step> getSubsteps() {
        return Arrays.asList(new Step[]{step1, step2});
    }
}
