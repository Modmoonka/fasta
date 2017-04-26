package com.github.leo_scream.fasta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Main {
    public static void main(String[] args) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            Path directory = Paths.get(reader.readLine());
            final String extension = ".fasta";
            final int kmerLength = 9;
            Pattern pattern = Pattern.compile("[ACGT]++");

            if (!directory.toFile().isDirectory()) throw new Exception("Path must be a directory");

            Files.list(directory)
                    .filter(path -> path.toString().endsWith(extension))
                    .map(Main::readLines)
                    .map(lines -> lines.stream()
                            .filter(line -> pattern.matcher(line).matches())
                            .map(sequence -> kmersFromSequence(sequence, kmerLength))
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList())
                    )
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .forEach((kmer, occurrences) -> System.out.println("K-mer { " + kmer + " } : { " + occurrences + " }"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static List<String> readLines(final Path path) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return lines;
    }

    private static List<String> kmersFromSequence(final String sequence, final int k) {
        List<String> sequences = new LinkedList<>();
        for (int i = 0; i < sequence.length() - k; i++) {
            sequences.add(sequence.substring(i, i + k));
        }
        return sequences;
    }
}
