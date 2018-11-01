package net.acomputerdog.securitycheckup.test.step.reg;

public abstract class RegKeyStep<T> extends RegStep<T> {
    /**
     * Registry key to read
     */
    private final String key;

    public RegKeyStep(String hive, String key) {
        super(hive);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
