
package com.willtkelly;

import java.io.IOException;
import java.io.InputStream;

public class RomLoader {

    public static byte[] loadRom(String path) throws IOException {
        try (InputStream is = ClassLoader.getSystemResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("Resource not found: " + path);
            }
            return is.readAllBytes();
        }
    }
}
