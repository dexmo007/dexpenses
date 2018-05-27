package com.dexmohq.dexpenses.process;

import com.dexmohq.dexpenses.categorize.Categorizer;
import com.dexmohq.dexpenses.categorize.Category;
import com.dexmohq.dexpenses.Receipt;
import com.dexmohq.dexpenses.categorize.InvalidCategorizerConfigurationException;
import com.dexmohq.dexpenses.extract.*;
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
public class ReceiptProcessor {//todo split ocr and parse functionality

    private final Categorizer categorizer;

    public static ReceiptProcessor load(File categorizerFile) throws IOException, InvalidCategorizerConfigurationException {
        final Categorizer categorizer = Categorizer.load(categorizerFile);
        return new ReceiptProcessor(categorizer);
    }

    private static final DateExtractor DATE_EXTRACTOR = new DateExtractor();
    private static final TimeExtractor TIME_EXTRACTOR = new TimeExtractor();
    private static final TotalAmountExtractor TOTAL_AMOUNT_EXTRACTOR = new TotalAmountExtractor();

    public Receipt process(String[] lines) {
        final Receipt result = new Receipt();
        final ExtractedResult<LocalDate> extractedDate = DATE_EXTRACTOR.extract(lines);
        result.setDate(extractedDate.getValue());
        final ExtractedResult<LocalTime> extractedTime = TIME_EXTRACTOR.extract(lines);
        result.setTime(extractedTime.getValue());
        final ExtractedResult<MonetaryAmount> extractedTotalAmount = TOTAL_AMOUNT_EXTRACTOR.extract(lines);
        result.setTotal(extractedTotalAmount.getValue());
        final HeaderExtractor headerExtractor = new HeaderExtractor(extractedDate.getLine(), extractedTime.getLine(), extractedTotalAmount.getLine());
        final ExtractedResult<String[]> extractedHeader = headerExtractor.extract(lines);
        final String[] header = extractedHeader.getValue();
        result.setHeader(header);
        final ExtractedResult<Category> extractedCategory = categorizer.getCategoryExtractor(header).extract(lines);
        result.setCategory(extractedCategory.getValue());
        return result;
    }


}
