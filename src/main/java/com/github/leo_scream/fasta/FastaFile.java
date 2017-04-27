package com.github.leo_scream.fasta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class FastaFile {
    private final static String extension = ".fasta";
    private final Path path;
    private final List<Sequence> sequences;

    private FastaFile(Path path, List<Sequence> sequences) {
        this.path = path;
        this.sequences = sequences;
    }

    /**
     * Factory method creates {@code FastaFile} from {@code path}.
     *
     * @param path from which file will be created
     * @return new object of {@code FastaFile} and read all lines to sequences
     * @throws NullPointerException     if {@code path} is null
     * @throws IllegalArgumentException if {@code path} has no .fasta extension
     */
    public static FastaFile create(final Path path) {
        Objects.requireNonNull(path);
        if (!path.toString().endsWith(extension))
            throw new IllegalArgumentException("File on path '" + path + "' has no .fasta extension");

        final List<Sequence> sequences = new LinkedList<>();

        try {
            final Iterator<String> linesIterator = Files.readAllLines(path).iterator();
            while (linesIterator.hasNext()) {
                sequences.add(Sequence.create(linesIterator.next(), linesIterator.next()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new FastaFile(path, sequences);
    }
}
