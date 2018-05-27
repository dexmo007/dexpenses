package com.dexmohq.dexpenses.extract;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Henrik Drefs
 */
public class TotalAmountExtractor extends RegexExtractor<MonetaryAmount> {

    private static final CurrencyUnit DEFAULT_CURRENCY = Monetary.getCurrency("EUR");

    private static final Pattern REGEX = Pattern.compile("(summe|[3b]etrag)\\s*(EUR)\\s*(\\d+\\s*,\\s*\\d{2})", Pattern.CASE_INSENSITIVE);
    private static final Pattern WITHOUT_CURRENCY_REGEX = Pattern.compile("(summe|[3b]etrag)[^\\d]*(\\d+\\s*,\\s*\\d{2})", Pattern.CASE_INSENSITIVE);

    private static final Function<Matcher, MonetaryAmount> MONEY_PARSER = matcher -> {
        final String currency = matcher.group(2);
        final CurrencyUnit currencyUnit = Monetary.getCurrency(currency);
        final String amountString = matcher.group(3).replaceAll(" ", "");
        final BigDecimal amount = parseAmount(amountString);
        return Monetary.getDefaultAmountFactory().setCurrency(currencyUnit).setNumber(amount).create();
    };

    private static final Function<Matcher, MonetaryAmount> DEFAULT_CURRENCY_MONEY_PARSER = matcher -> {
        final String amountString = matcher.group(2).replaceAll(" ", "");
        final BigDecimal amount = parseAmount(amountString);
        return Monetary.getDefaultAmountFactory().setCurrency(DEFAULT_CURRENCY).setNumber(amount).create();
    };

    private static final DecimalFormat DECIMAL_FORMAT = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.GERMANY);

    static {
        DECIMAL_FORMAT.setParseBigDecimal(true);
    }

    private static BigDecimal parseAmount(String source) {
        try {
            return (BigDecimal) DECIMAL_FORMAT.parse(source);
        } catch (ParseException e) {
            throw new InternalError(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static final ExtractMapping<MonetaryAmount>[] EXTRACT_MAPPINGS = new ExtractMapping[]{
            new ExtractMapping(REGEX, MONEY_PARSER),
            new ExtractMapping(WITHOUT_CURRENCY_REGEX, DEFAULT_CURRENCY_MONEY_PARSER)
    };

    public TotalAmountExtractor() {
        super(EXTRACT_MAPPINGS);
    }
}
