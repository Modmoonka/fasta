package com.github.leo_scream.fasta;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
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

            final Set<String> permutations = Permutations
                    .of(new TreeSet<>(Set.of("A", "C", "G", "T")), new String[kmerLength])
                    .stream()
                    .map(parts -> String.join("", parts))
                    .collect(Collectors.toSet());

            Files.list(directory)
                    .filter(path -> path.toString().endsWith(".fasta"))
                    .map(path -> Statistics.of(path, permutations))
                    .forEach(statistics -> {
                        final String filename = statistics.file().path().toString().replace(".fasta", ".tsv");
                        saveToTcv(statistics, filename);
                    });
        } catch (IOException e) {
            System.err.println("Can't work with this path cause: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("K-mer length must be unsighted integer value");
        }
    }

    private static void saveToTcv(final Statistics statistics, final String filepath) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            writer.write("Sequence\t");
            writer.write(String.join("\t", statistics.permutations()));
            writer.newLine();
            statistics.sequenceOccurrences().forEach(
                    sequenceOccurrences -> {
                        try {
                            writer.write(sequenceOccurrences.sequence().name() + "\t");
                            writer.write(
                                    sequenceOccurrences.occurrences().values().stream()
                                            .map(Object::toString)
                                            .collect(Collectors.joining("\t"))
                            );
                            writer.newLine();
                        } catch (IOException e) {
                            System.err.println(
                                    "Can't write sequence " + sequenceOccurrences.sequence().name() +
                                            " to file " + filepath + " cause: " + e.getMessage());
                        }
                    }
            );
        } catch (IOException e) {
            System.err.println("Can't write to file " + filepath + " cause: " + e.getMessage());
        }
    }
}
