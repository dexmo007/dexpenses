package com.dexmohq.dexpenses.process;

import com.dexmohq.dexpenses.PaymentMethod;
import com.dexmohq.dexpenses.Receipt;
import com.dexmohq.dexpenses.categorize.Categorizer;
import com.dexmohq.dexpenses.categorize.Category;
import com.dexmohq.dexpenses.categorize.InvalidCategorizerConfigurationException;
import com.dexmohq.dexpenses.extract.*;
import com.dexmohq.dexpenses.util.config.InvalidConfigurationException;
import lombok.RequiredArgsConstructor;

import javax.money.MonetaryAmount;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor
public class ReceiptProcessor {

    private final Categorizer categorizer;
    private final Extractor<LocalDate> dateExtractor;
    private final Extractor<LocalTime> timeExtractor;

    public static ReceiptProcessor load(File categorizerFile, File dateConfigFile, File timeConfigFile) throws IOException, InvalidCategorizerConfigurationException, InvalidConfigurationException {
        final Categorizer categorizer = Categorizer.load(categorizerFile);
        final ConfigurableDateExtractor dateExtractor = ConfigurableDateExtractor.load(dateConfigFile);
        final ConfigurableTimeExtractor timeExtractor = ConfigurableTimeExtractor.load(timeConfigFile);
        return new ReceiptProcessor(categorizer, dateExtractor, timeExtractor);
    }

    private static final TotalAmountExtractor TOTAL_AMOUNT_EXTRACTOR = new TotalAmountExtractor();

    public Receipt process(String[] lines) {
        final Receipt result = new Receipt();
        final ExtractedResult<LocalDate> extractedDate = dateExtractor.extract(lines);
        result.setDate(extractedDate.getValue());
        final ExtractedResult<LocalTime> extractedTime = timeExtractor.extract(lines);
        result.setTime(extractedTime.getValue());
        final ExtractedResult<MonetaryAmount> extractedTotalAmount = TOTAL_AMOUNT_EXTRACTOR.extract(lines);
        result.setTotal(extractedTotalAmount.getValue());
        final HeaderExtractor headerExtractor = new HeaderExtractor(extractedDate.getLine(), extractedTime.getLine(), extractedTotalAmount.getLine());
        final ExtractedResult<String[]> extractedHeader = headerExtractor.extract(lines);
        final String[] header = extractedHeader.getValue();
        result.setHeader(header);
        final ExtractedResult<Category> extractedCategory = categorizer.getCategoryExtractor(header).extract(lines);
        result.setCategory(extractedCategory.getValue());
        final ExtractedResult<PaymentMethod> extractedPaymentMethod = PaymentMethodExtractor.INSTANCE.extract(lines);
        result.setPaymentMethod(extractedPaymentMethod.getValue());
        return result;
    }


}
