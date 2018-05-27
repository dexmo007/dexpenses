package com.dexmohq.dexpenses.process;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

/**
 * @author Henrik Drefs
 */
public interface ReceiptReader extends Closeable {

    String[] read(File imageFile) throws IOException;

    String[] read(URL imageFile) throws IOException;

    String[] read(URI imageFile) throws IOException;

    String[] read(Path imageFile) throws IOException;

    String[] read(String imageFile) throws IOException;

}
