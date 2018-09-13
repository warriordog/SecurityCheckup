package net.acomputerdog.securitycheckup.test.types.wmi;

import net.acomputerdog.jwmi.ex.NativeException;
import net.acomputerdog.jwmi.ex.NativeHresultException;
import net.acomputerdog.jwmi.ex.WMIException;
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
            return new TestResult(this, TestResult.SCORE_FAIL).setException(e).setMessage(
                    String.format("WMI error occurred in object at %s, hresult = 0x%s", e.getPointer().toString(), Integer.toHexString(e.getHresult().intValue()))
            );
        } catch (NativeHresultException e) {
            this.setState(State.ERROR);
            return new TestResult(this, TestResult.SCORE_FAIL).setException(e).setMessage(
                    String.format("Error in native function, hresult = 0x%s\n", Integer.toHexString(e.getHresult().intValue()))
            );
        } catch (NativeException e) {
            this.setState(State.ERROR);
            return new TestResult(this, TestResult.SCORE_FAIL).setException(e).setMessage("Unknown error in native code");
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
