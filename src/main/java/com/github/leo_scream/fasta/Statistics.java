package com.github.leo_scream.fasta;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Statistics {
    private final Fasta file;
    private final Set<String> permutations;
    private final Set<SequenceOccurrences> sequenceOccurrences;

    private Statistics(Fasta file, Set<String> permutations, Set<SequenceOccurrences> sequenceOccurrences) {
        this.file = file;
        this.permutations = permutations;
        this.sequenceOccurrences = sequenceOccurrences;
    }

    public Fasta file() {
        return file;
    }

    public Set<String> permutations() {
        return permutations;
    }

    public Set<SequenceOccurrences> sequenceOccurrences() {
        return sequenceOccurrences;
    }

    public static Statistics of(final Path path, final Set<String> permutations) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(permutations);
        final Fasta file = Fasta.create(path);
        final Set<SequenceOccurrences> occurrences = file.sequences()
                .map(sequence -> SequenceOccurrences.create(sequence, permutations))
                .collect(Collectors.toSet());
        return new Statistics(file, permutations, occurrences);
    }
}
