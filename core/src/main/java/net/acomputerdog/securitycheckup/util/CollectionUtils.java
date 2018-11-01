package net.acomputerdog.securitycheckup.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionUtils {
    public static <T> List<T> createList(T ... items) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, items);
        return list;
    }
}
