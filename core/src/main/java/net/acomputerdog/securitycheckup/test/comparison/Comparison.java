package net.acomputerdog.securitycheckup.test.comparison;

public abstract class Comparison<T1, T2> {
    public abstract boolean compare(T1 a, T2 b);
}
