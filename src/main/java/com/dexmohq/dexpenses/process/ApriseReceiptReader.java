package com.dexmohq.dexpenses.process;

import com.asprise.ocr.Ocr;
import com.dexmohq.dexpenses.Test;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApriseReceiptReader extends AbstractFileReceiptReader {

    private final Ocr ocr;

    private final Object ocrLock = new Object();

    public static ApriseReceiptReader load(String language) {
        Ocr.setUp();
        final Ocr ocr = new Ocr();
        ocr.startEngine(language, Ocr.SPEED_FASTEST);
        return new ApriseReceiptReader(ocr);
    }

    @Override
    public String[] read(File imageFile) {
        synchronized (ocrLock) {
            final String text = ocr.recognize(new File[]{imageFile}, Ocr.RECOGNIZE_TYPE_TEXT, Ocr.OUTPUT_FORMAT_PLAINTEXT);
            return text.split("\n");
        }
    }

    @Override
    public void close() {
        ocr.stopEngine();
    }
}
