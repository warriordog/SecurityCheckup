package net.acomputerdog.securitycheckup.test.step.compare;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.comparison.Comparison;
import net.acomputerdog.securitycheckup.test.step.Step;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MatchAnyStep<T1, T2> extends Step<Boolean> {
    private final Step<? extends Iterator<T1>> source;
    private final Step<T2> match;
    private final Comparison<T1, T2> comparison;

    public MatchAnyStep(Step<? extends Iterator<T1>> source, Step<T2> match, Comparison<T1, T2> comparison) {
        this.source = source;
        this.match = match;
        this.comparison = comparison;
    }

    public Step<? extends Iterator<T1>> getSource() {
        return source;
    }

    public Comparison<T1, T2> getComparison() {
        return comparison;
    }

    public Step<T2> getMatch() {
        return match;
    }

    @Override
    public Boolean run(TestEnvironment environment) {
        Iterator<T1> it = source.run(environment);
        while (it.hasNext()) {
            if (comparison.compare(it.next(), match.run(environment))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Step> getSubsteps() {
        return Arrays.asList(new Step[]{source, match});
    }
}
