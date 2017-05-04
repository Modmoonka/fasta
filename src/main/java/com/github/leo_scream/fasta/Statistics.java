package com.github.leo_scream.fasta;

import java.nio.file.Path;
import java.util.Objects;
import java.util.SortedSet;
import java.util.stream.Stream;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Statistics {
    private final Fasta file;
    private final SortedSet<String> permutations;

    public Statistics(final Path path, SortedSet<String> permutations) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(permutations);
        this.file = new Fasta(path);
        this.permutations = permutations;
    }

    public Fasta file() {
        return file;
    }

    public SortedSet<String> permutations() {
        return permutations;
    }

    public Stream<SequenceOccurrences> sequenceOccurrences() {
        return file.sequences()
                .map(SequenceOccurrences::new);
    }

    public boolean equals(final Statistics another) {
        return file.equals(another.file)
                && permutations.equals(another.permutations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Statistics)) return false;
        return this.equals((Statistics) o);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + permutations.hashCode();
        result = 31 * result + file.hashCode();
        return result;
    }
}
