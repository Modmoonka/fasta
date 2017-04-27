package com.github.leo_scream.fasta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Main {
    public static void main(String[] args) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            Path directory = Paths.get(reader.readLine());
            final String extension = ".fasta";
            final int kmerLength = 3;
            final String pattern = "^[ACGT]++$";

            if (!directory.toFile().isDirectory()) throw new Exception("Path must be a directory");

            final Permutations<String> permutations = Permutations.of(
                    new TreeSet<>(
                            Arrays.stream(new String[]{"A", "C", "G", "T"}).collect(Collectors.toSet())
                    ),
                    new String[kmerLength]
            );
            final Application fastaApp = Application.create(
                    Files.list(directory).toArray(value -> new Path[0])
            );

            permutations.stream()
                    .map(permutation -> String.join("", permutation));

            Files.list(directory)
                    .filter(path -> path.toString().endsWith(extension))
                    .map(Main::readLines)
                    .map(lines -> lines.stream()
                            .filter(line -> line.matches(pattern))
                            .map(sequence -> kmersFromSequence(sequence, kmerLength))
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList())
                    )
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet().stream().sorted((o1, o2) -> (int) (o2.getValue() - o1.getValue()))
                    .forEach(entry -> System.out.println("{ " + entry.getKey() + " } : { " + entry.getValue() + " }"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static List<String> readLines(final Path path) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private static List<String> kmersFromSequence(final String sequence, final int k) {
        List<String> sequences = new LinkedList<>();
        for (int i = 0; i + k < sequence.length(); i++) {
            sequences.add(sequence.substring(i, i + k));
        }
        return sequences;
    }
}
