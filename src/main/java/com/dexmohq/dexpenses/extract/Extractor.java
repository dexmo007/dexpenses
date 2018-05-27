package com.dexmohq.dexpenses.extract;

/**
 * @author Henrik Drefs
 */
public interface Extractor<T> {

    ExtractedResult<T> extract(String[] lines);

}
