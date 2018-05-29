package com.dexmohq.dexpenses;

import com.dexmohq.dexpenses.categorize.InvalidCategorizerConfigurationException;
import com.dexmohq.dexpenses.process.ApriseReceiptReader;
import com.dexmohq.dexpenses.process.ReceiptProcessor;
import com.dexmohq.dexpenses.util.config.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;

/**
 * @author Henrik Drefs
 */
public class Test {

    private static final File CATEGORIZER_FILE = new File(Test.class.getResource("/categorizer.json").getFile());

    private static final File DATE_CONFIG_FILE = new File(Test.class.getResource("/date_config.json").getFile());
    private static final File TIME_CONFIG_FILE = new File(Test.class.getResource("/time_config.json").getFile());

    public static void main(String[] args) throws IOException, InvalidCategorizerConfigurationException {
        if (args.length < 1) {
            throw new IllegalArgumentException("receipt file path is required");
        }
        final String receiptFile = args[0];


        final ApriseReceiptReader receiptReader = ApriseReceiptReader.load("deu");
        final String[] lines = receiptReader.read(receiptFile);
        for (String line : lines) {
            System.out.println(line);
        }
        final ReceiptProcessor receiptProcessor;
        try {
            receiptProcessor = ReceiptProcessor.load(CATEGORIZER_FILE, DATE_CONFIG_FILE, TIME_CONFIG_FILE);
        } catch (InvalidConfigurationException e) {
            e.getConstraintViolations().forEach(System.err::println);
            return;
        }
        System.out.println(receiptProcessor.process(lines));
    }

}
