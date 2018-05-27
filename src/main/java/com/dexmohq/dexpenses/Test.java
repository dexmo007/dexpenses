package com.dexmohq.dexpenses;

import com.dexmohq.dexpenses.process.ApriseReceiptReader;
import com.dexmohq.dexpenses.categorize.InvalidCategorizerConfigurationException;
import com.dexmohq.dexpenses.process.ReceiptProcessor;

import java.io.File;
import java.io.IOException;

/**
 * @author Henrik Drefs
 */
public class Test {

    private static final File CATEGORIZER_FILE = new File(Test.class.getResource("/categorizer.json").getFile());

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
        final ReceiptProcessor receiptProcessor = ReceiptProcessor.load(CATEGORIZER_FILE);
        System.out.println(receiptProcessor.process(lines));
    }

}
