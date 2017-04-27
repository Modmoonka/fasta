package com.github.leo_scream.fasta;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Application {
    private final List<FastaFile> files;

    private Application(final List<FastaFile> files) {
        this.files = files;
    }

    public void collectStatistics() {
    }

    public static Application create(final Path... paths) {
        Objects.requireNonNull(paths);
        return new Application(
                Arrays.stream(paths)
                        .map(FastaFile::create)
                        .collect(Collectors.toList())
        );
    }
}
