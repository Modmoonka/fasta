package com.github.leo_scream.fasta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Fasta {
    private final static String extension = ".fasta";
    private final Path path;
    private final List<Sequence> sequences;

    private Fasta(Path path, List<Sequence> sequences) {
        this.path = path;
        this.sequences = sequences;
    }

    public Path path() {
        return path;
    }

    public Stream<Sequence> sequences() {
        return sequences.stream();
    }

    /**
     * Factory method creates {@code Fasta} from {@code path}.
     *
     * @param path from which file will be created
     * @return new object of {@code Fasta} and read all lines to sequences
     * @throws NullPointerException     if {@code path} is null
     * @throws IllegalArgumentException if {@code path} has no .fasta extension
     */
    public static Fasta create(final Path path) {
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
            System.err.println("Can't read file " + path + " cause: " + e.getMessage());
        }

        return new Fasta(path, sequences);
    }
}
