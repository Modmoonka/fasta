package com.github.leo_scream.fasta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Main {
    public static void main(String[] args) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            final Path workingPath = readPath(reader);
            final Path aptamerPath = readAptamer(reader);
            final int kmerLength = readLenght(reader);
            System.out.println("Result files:");

            final SortedMap<String, AtomicInteger> permutations = new TreeMap<>(
                    kmersFromSequence(new String(Files.readAllBytes(aptamerPath)), kmerLength).stream()
                            .collect(Collectors.toMap(Function.identity(), ignored -> new AtomicInteger(0)))
            );

            paths(workingPath)
                    .map(path -> new Statistics(path, permutations))
                    .map(Statistics::save)
                    .forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Something goes wrong, cause: " + e.getMessage());
        }
    }

    private static Path readAptamer(final BufferedReader reader) throws IOException {
        System.out.print("Path to aptamer file: ");
        return Paths.get(reader.readLine());
    }

    private static int readLenght(final BufferedReader reader) throws IOException {
        System.out.print("K-mer length: ");
        return Integer.parseInt(reader.readLine());
    }

    private static Path readPath(final BufferedReader reader) throws IOException {
        System.out.print("Path to fasta directory or file: ");
        return Paths.get(reader.readLine());
    }

    private static Stream<Path> paths(final Path path) throws IOException {
        final Set<Path> paths = new HashSet<>();
        if (path.toFile().isDirectory()) {
            Files.list(path)
                    .filter(item -> item.toString().endsWith(".fasta"))
                    .forEach(paths::add);
        } else if (path.toString().endsWith(".fasta")) {
            paths.add(path);
        }
        return paths.stream();
    }

    private static Set<String> kmersFromSequence(final String aptamer, final int k) {
        Set<String> sequences = new TreeSet<>();
        for (int i = 0; i < aptamer.length() - k; i++) {
            sequences.add(aptamer.substring(i, i + k));
        }
        return sequences;
    }
}
