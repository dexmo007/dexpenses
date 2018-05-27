package com.dexmohq.dexpenses.categorize.el;

import lombok.Value;

import java.util.List;

/**
 * @author Henrik Drefs
 */
@Value
public class CategoryInfo {

    private final List<String> tokens;
    private final String[] headerLines;
    private final String header;

}
