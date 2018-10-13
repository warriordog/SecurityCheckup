package net.acomputerdog.securitycheckup.util;

import java.util.List;
import java.util.Map;

public interface Informable {
    String getInfo(String key);

    Map<String, String> getInfoMap();

    List<String> getLabeledInfo();
}
