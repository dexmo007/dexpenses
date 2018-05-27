package com.dexmohq.dexpenses.process;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;

/**
 * @author Henrik Drefs
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TesseractReceiptReader extends AbstractFileReceiptReader {

    private final ITesseract tesseract;

    public static TesseractReceiptReader load(File tesseractDataDir, String language) {
        final Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(tesseractDataDir.getAbsolutePath());
        tesseract.setLanguage(language);
        return new TesseractReceiptReader(tesseract);
    }

    @Override
    public String[] read(File imageFile) throws IOException {
        try {
            return tesseract.doOCR(imageFile).split("\n");
        } catch (TesseractException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void close() throws IOException {

    }
}
