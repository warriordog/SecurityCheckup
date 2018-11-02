package net.acomputerdog.securitycheckup.main.gui.json;

public class JarIndex {
    private final String[] bundles;
    private final String[] tests;
    private final String[] profiles;

    // constructor for deserialization
    private JarIndex() {
        this(null, null, null);
    }

    public JarIndex(String[] bundles, String[] tests, String[] profiles) {
        this.bundles = bundles;
        this.tests = tests;
        this.profiles = profiles;
    }

    public String[] getBundles() {
        return bundles;
    }

    public String[] getTests() {
        return tests;
    }

    public String[] getProfiles() {
        return profiles;
    }
}
