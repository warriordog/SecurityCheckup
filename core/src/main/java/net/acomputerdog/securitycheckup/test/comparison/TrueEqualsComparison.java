package net.acomputerdog.securitycheckup.test.comparison;

public class TrueEqualsComparison<T> extends Comparison<T, T> {
    private boolean inverted = false;

    public boolean isInverted() {
        return inverted;
    }

    public TrueEqualsComparison<T> setInverted(boolean inverted) {
        this.inverted = inverted;

        return this;
    }

    @Override
    public boolean compare(T a, T b) {
        return inverted ? a != b : a == b;
    }
}
