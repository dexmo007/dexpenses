package com.dexmohq.dexpenses.process;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

/**
 * @author Henrik Drefs
 */
public abstract class AbstractFileReceiptReader implements ReceiptReader {

    @Override
    public String[] read(URL imageFile) throws IOException {
        try {
            return read(new File(imageFile.toURI()));
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String[] read(URI imageFile) throws IOException {
        return read(new File(imageFile));
    }

    @Override
    public String[] read(Path imageFile) throws IOException {
        return read(imageFile.toFile());
    }

    @Override
    public String[] read(String imageFile) throws IOException {
        return read(new File(imageFile));
    }

}
