package com.dexmohq.dexpenses.extract;

import com.dexmohq.dexpenses.util.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Henrik Drefs
 */
public class HeaderExtractor implements Extractor<String[]> {

    private static final int MAX_HEADER_LINE = 8;

    private final int line;

    public HeaderExtractor(int dateLine, int timeLine, int totalAmountLine) {
        this.line = MathUtils.min(dateLine, timeLine, totalAmountLine);
    }

    private static final Pattern HEADLINE_REGEX = Pattern.compile("kundenbeleg|h(ae|\u00e4)ndlerbeleg", Pattern.CASE_INSENSITIVE);

    @Override
    public ExtractedResult<String[]> extract(String[] lines) {
        final int headerLimit = MathUtils.min(MAX_HEADER_LINE, lines.length, line);
        final List<String> header = new ArrayList<>();
        for (int i = 0; i < headerLimit; i++) {
            final String line = lines[i];
            if (HEADLINE_REGEX.matcher(line).find()
                    || line.trim().isEmpty()
                    || hasOnlySymbols(line)) {
                continue;
            }
            if (line.trim().equalsIgnoreCase("EUR")) {
                break;
            }
            header.add(line.trim());
        }
        return new ExtractedResult<>(header.toArray(new String[0]), -1);//todo clever lines class that accounts for ranges and unions
    }

    private static boolean hasOnlySymbols(String s) {
        return s.codePoints().noneMatch(Character::isLetterOrDigit);
    }
}
