package net.acomputerdog.securitycheckup.test.step.reg;

public abstract class RegEntryStep<T> extends RegKeyStep<T> {
    /**
     * Registry key value to access
     */
    private final String value;

    public RegEntryStep(String hive, String key, String value) {
        super(hive, key);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
