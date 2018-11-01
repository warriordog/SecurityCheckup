package net.acomputerdog.securitycheckup.test.comparison;

public interface Comparison<T1, T2> {
    boolean compare(T1 a, T2 b);
}
