package net.acomputerdog.securitycheckup.test.step.wmi;

import net.acomputerdog.jwmi.wbem.EnumWbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemServices;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

public class WMIStep extends Step<EnumWbemClassObject> {
    private final String namespace;
    private final String query;

    public WMIStep(String namespace, String query) {
        this.namespace = namespace;
        this.query = query;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public EnumWbemClassObject run(TestEnvironment environment) {
        return getOrCreateWbemServices(environment, namespace).execQuery(query);
    }

    /**
     * Gets or creates a shared instance of WbemServices for a specified WMI namespace.
     *
     * This method first looks in the Test Environment for an already-existing instance,
     * and returns that if found.  If an instance does not exist yet, then one is created,
     * stored in the Test Environment, and returned.
     *
     * @param environment   Current test environment
     * @param namespace     WMI namespace to open
     * @return  return WbemServices for the namespace
     */
    public static WbemServices getOrCreateWbemServices(TestEnvironment environment, String namespace) {
        String resource = createResourceKey(namespace);
        WbemServices services;
        if (environment.hasSharedResource(resource)) {
            services = environment.getSharedResource(resource);
        } else {
            services = environment.getWbemLocator().connectServer(namespace);
            environment.setSharedResource(resource, services);
        }

        return services;
    }

    /**
     * Generates the string used as a key to store a WbemServices instance in the test environment.
     * This method only generates the key; there may or may not actually be an instance in the
     * test environment at any time.
     *
     * @param namespace The namespace match
     * @return Return the content key for the specified namespace
     */
    public static String createResourceKey(String namespace) {
        return "WMI_NAMESPACE=" + namespace;
    }
}
