
package com.willtkelly;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class RomLoader {

    public static byte[] loadRom(String path) throws IOException {
        return Files.readAllBytes(Path.of(path));
    }

}
