package com.github.leo_scream.fasta;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.SortedSet;
import java.util.stream.Collectors;
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

    public Path save() {
        final String filename = file.path().toString().replace(file.extension(), ".tsv");
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Sequence\t");
            writer.write(String.join("\t", permutations));
            writer.newLine();
            sequenceOccurrences().forEach(
                    occurrences -> {
                        try {
                            writer.write(occurrences.sequence().data() + "\t");
                            writer.write(
                                    occurrences.occurrences(permutations).values().stream()
                                            .map(count -> count == 0 ? "0" : "1")
                                            .collect(Collectors.joining("\t"))
                            );
                            writer.newLine();
                        } catch (IOException e) {
                            System.err.println(
                                    "Can't write sequence " + occurrences.sequence().name() +
                                            " " + occurrences.sequence().data() +
                                            " to file " + filename + " cause: " + e.getMessage());
                        }
                    }
            );
        } catch (IOException e) {
            System.err.println("Can't write to file " + filename + " cause: " + e.getMessage());
        }
        return Paths.get(filename);
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
