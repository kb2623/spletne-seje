package org.oosqljet;

public interface SqlMapping<I, O> {
    /**
     *
     * @param in
     * @return
     */
    O inMaping(I in);
    /**
     *
     * @param in
     * @return
     */
    I outMaping(O in);
}
