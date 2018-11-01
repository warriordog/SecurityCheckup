package net.acomputerdog.securitycheckup.test.comparison;

public class EqualsComparison<T> implements Comparison<T, T> {
    private boolean inverted = false;

    public boolean isInverted() {
        return inverted;
    }

    public EqualsComparison<T> setInverted(boolean inverted) {
        this.inverted = inverted;

        return this;
    }

    @Override
    public boolean compare(T a, T b) {
        if (inverted) {
            return !a.equals(b);
        } else {
            return a.equals(b);
        }
    }
}
