package com.dexmohq.dexpenses.extract;

import com.dexmohq.dexpenses.PaymentMethod;
import lombok.Value;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 * @author Henrik Drefs
 */
public class PaymentMethodExtractor implements Extractor<PaymentMethod> {

    private final List<ExtractMapping> extractMappings;

    public static final PaymentMethodExtractor INSTANCE = new PaymentMethodExtractor();

    private PaymentMethodExtractor() {
        extractMappings = Arrays.stream(PaymentMethod.values())
                .map(pm -> new ExtractMapping(pm.getPatterns(), pm))
                .collect(toList());
    }

    @Override
    public ExtractedResult<PaymentMethod> extract(String[] lines) {
        for (ExtractMapping extractMapping : extractMappings) {
            final List<Pattern> patterns = extractMapping.getPatterns();
            for (int i = 0; i < lines.length; i++) {
                final String line = lines[i];
                if (patterns.stream().anyMatch(p -> p.matcher(line).find())) {
                    return new ExtractedResult<>(extractMapping.getPaymentMethod(), i);
                }
            }
        }
        return ExtractedResult.empty();
    }

    @Value
    private static class ExtractMapping {
        private final List<Pattern> patterns;
        private final PaymentMethod paymentMethod;
    }
}
