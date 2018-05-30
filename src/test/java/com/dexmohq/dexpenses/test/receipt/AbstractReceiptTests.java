package com.dexmohq.dexpenses.test.receipt;

import com.dexmohq.dexpenses.Receipt;
import com.dexmohq.dexpenses.categorize.InvalidCategorizerConfigurationException;
import com.dexmohq.dexpenses.process.ApriseReceiptReader;
import com.dexmohq.dexpenses.process.ReceiptProcessor;
import com.dexmohq.dexpenses.process.ReceiptReader;
import com.dexmohq.dexpenses.util.config.InvalidConfigurationException;
import org.junit.jupiter.api.BeforeAll;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author Henrik Drefs
 */
public abstract class AbstractReceiptTests {

    private static final File CATEGORIZER_FILE = new File(ReceiptTests.class.getResource("/categorizer.json").getFile());
    private static final File DATE_CONFIG_FILE = new File(ReceiptTests.class.getResource("/date_config.json").getFile());
    private static final File TIME_CONFIG_FILE = new File(ReceiptTests.class.getResource("/time_config.json").getFile());

    protected static ReceiptReader receiptReader;
    protected static ReceiptProcessor receiptProcessor;

    @BeforeAll
    static void init() throws InvalidConfigurationException, InvalidCategorizerConfigurationException, IOException {
        receiptReader = ApriseReceiptReader.load("deu");
        receiptProcessor = ReceiptProcessor.load(CATEGORIZER_FILE, DATE_CONFIG_FILE, TIME_CONFIG_FILE);
    }

    protected static Receipt read(String name) throws IOException {
        final File file = new File("receipts/" + name);
        final String[] lines = receiptReader.read(file);
        return receiptProcessor.process(lines);
    }

    protected static MonetaryAmount euros(String amount) {
        return Monetary.getDefaultAmountFactory().setCurrency("EUR").setNumber(new BigDecimal(amount)).create();
    }

}
