package net.acomputerdog.securitycheckup.test.types.wmi;

import net.acomputerdog.jwmi.WMIException;
import net.acomputerdog.jwmi.wbem.EnumWbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemServices;
import net.acomputerdog.securitycheckup.test.BasicTest;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.TestResult;

public abstract class WMITest extends BasicTest {
    private final String namespace;
    private final String query;

    protected WMITest(String id, String name, String description, String namespace, String query) {
        super(id, name, description);
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
    protected TestResult runTestSafe(TestEnvironment environment) {
        try(EnumWbemClassObject results = getOrCreateWbemServices(environment, namespace).execQuery(query)) {
            return finishTest(results);
        } catch (WMIException e) {
            this.setState(State.ERROR);
            return new TestResult(this, TestResult.SCORE_FAIL).setException(e).setMessage("WMI error occurred: 0x" + Integer.toHexString(e.getHresult().intValue()));
        }
    }

    public abstract TestResult finishTest(EnumWbemClassObject results);

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

    public static String createResourceKey(String namespace) {
        return "WMI_NAMESPACE=" + namespace;
    }
}
