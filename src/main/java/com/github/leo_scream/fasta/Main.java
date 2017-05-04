package com.github.leo_scream.fasta;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Main {
    public static void main(String[] args) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Path to fasta directory: ");
            final Path directory = Paths.get(reader.readLine());
            System.out.print("K-mer length: ");
            final int kmerLength = Integer.parseInt(reader.readLine());

            if (!directory.toFile().isDirectory()) throw new IOException("Path must be a directory");

            final SortedSet<String> permutations = new TreeSet<>(
                    new Permutations<>(new TreeSet<>(Set.of("A", "C", "G", "T")), new String[kmerLength])
                            .stream()
                            .map(parts -> String.join("", parts))
                            .collect(Collectors.toSet())
            );

            Files.list(directory)
                    .filter(path -> path.toString().endsWith(".fasta"))
                    .map(path -> new Statistics(path, permutations))
                    .forEach(statistics -> {
                        final String filename = statistics.file().path().toString().replace(".fasta", ".tsv");
                        saveToTsv(statistics, filename);
                    });
        } catch (IOException e) {
            System.err.println("Can't work with this path cause: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("K-mer length must be unsighted integer value");
        }
    }

    private static void saveToTsv(final Statistics statistics, final String filepath) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            writer.write("Sequence\t");
            writer.write(String.join("\t", statistics.permutations()));
            writer.newLine();
            statistics.sequenceOccurrences().forEach(
                    sequenceOccurrences -> {
                        try {
                            writer.write(sequenceOccurrences.sequence().data() + "\t");
                            writer.write(
                                    sequenceOccurrences.occurrences(statistics.permutations())
                                            .values()
                                            .stream()
                                            .map(count -> count == 0 ? "0" : "1")
                                            .collect(Collectors.joining("\t"))
                            );
                            writer.newLine();
                        } catch (IOException e) {
                            System.err.println(
                                    "Can't write sequence " + sequenceOccurrences.sequence().name() +
                                            " " + sequenceOccurrences.sequence().data() +
                                            " to file " + filepath + " cause: " + e.getMessage());
                        }
                    }
            );
        } catch (IOException e) {
            System.err.println("Can't write to file " + filepath + " cause: " + e.getMessage());
        }
    }
}
