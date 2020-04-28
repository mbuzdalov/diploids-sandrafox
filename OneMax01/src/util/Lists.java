package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Lists {
    @SafeVarargs
    public static <T> List<T> of(T... args) {
        List<T> rv = new ArrayList<>(args.length);
        Collections.addAll(rv, args);
        return rv;
    }
}
