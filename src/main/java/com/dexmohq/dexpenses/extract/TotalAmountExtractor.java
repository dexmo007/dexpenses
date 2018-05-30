package com.dexmohq.dexpenses.extract;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Henrik Drefs
 */
public class TotalAmountExtractor extends RegexExtractor<MonetaryAmount> {

    private static final CurrencyUnit DEFAULT_CURRENCY = Monetary.getCurrency("EUR");

    private static final Pattern TOTAL_REGEX = Pattern.compile("^\\s*(gesamt)\\s*(EUR)?\\s*(\\d+)\\s*[,.]\\s*(\\d{2})", Pattern.CASE_INSENSITIVE);
    private static final Pattern REGEX = Pattern.compile("^\\s*(summe|(gesamt)?[3b]etrag)\\s*(EUR)\\s*(\\d+)\\s*[,.]\\s*(\\d{2})", Pattern.CASE_INSENSITIVE);
    private static final Pattern WITHOUT_CURRENCY_REGEX = Pattern.compile("^\\s*(summe|(gesamt)?[3b]etrag)[^\\d]*(\\d+)\\s*[,.]\\s*(\\d{2})", Pattern.CASE_INSENSITIVE);

    private static final Function<Matcher, MonetaryAmount> MONEY_PARSER = matcher -> {
        final String currency = matcher.group(3);
        final CurrencyUnit currencyUnit = Monetary.getCurrency(currency);
        final BigDecimal amount = parseAmount(matcher.group(4), matcher.group(5));
        return Monetary.getDefaultAmountFactory().setCurrency(currencyUnit).setNumber(amount).create();
    };

    private static final Function<Matcher, MonetaryAmount> DEFAULT_CURRENCY_MONEY_PARSER = matcher -> {
        final BigDecimal amount = parseAmount(matcher.group(3), matcher.group(4));
        return Monetary.getDefaultAmountFactory().setCurrency(DEFAULT_CURRENCY).setNumber(amount).create();
    };

    private static BigDecimal parseAmount(String ints, String decimal) {
        return new BigDecimal(ints + "." + decimal);
    }

    @SuppressWarnings("unchecked")
    private static final ExtractMapping<MonetaryAmount>[] EXTRACT_MAPPINGS = new ExtractMapping[]{
            new ExtractMapping(TOTAL_REGEX, DEFAULT_CURRENCY_MONEY_PARSER),
            new ExtractMapping(REGEX, MONEY_PARSER),
            new ExtractMapping(WITHOUT_CURRENCY_REGEX, DEFAULT_CURRENCY_MONEY_PARSER)
    };

    public TotalAmountExtractor() {
        super(EXTRACT_MAPPINGS);
    }

}
