package com.github.leo_scream.fasta;


import java.nio.file.Path;
import java.util.Objects;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Application {
    private final Path workingPath;

    private Application(final Path workingPath) {
        this.workingPath = workingPath;
    }

    public static Application withWorkingPath(final Path workingPath) {
        Objects.requireNonNull(workingPath);
        return new Application(workingPath);
    }
}
