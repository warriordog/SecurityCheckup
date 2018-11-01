package net.acomputerdog.securitycheckup.test.comparison;

public class ComparableComparison<T extends Comparable<T>> implements Comparison<T, T> {
    private final CompareMode mode;

    public ComparableComparison(CompareMode mode) {
        this.mode = mode;
    }

    public CompareMode getMode() {
        return mode;
    }

    @Override
    public boolean compare(T a, T b) {
        int result = a.compareTo(b);
        switch (mode) {
            case LESS_THAN: return result < 0;
            case EQUALS: return result == 0;
            case GREATER_THAN: return result > 0;
            default: throw new IllegalArgumentException("CompareMode is invalid");
        }
    }

    public enum CompareMode {
        LESS_THAN,
        EQUALS,
        GREATER_THAN
    }
}
