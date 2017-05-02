package com.github.leo_scream.fasta;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class SequenceOccurrences {
    private final Sequence sequence;
    private final Map<String, Long> occurrences;

    private SequenceOccurrences(Sequence sequence, Map<String, Long> occurrences) {
        this.sequence = sequence;
        this.occurrences = occurrences;
    }

    public Sequence sequence() {
        return sequence;
    }

    public Map<String, Long> occurrences() {
        return occurrences;
    }

    public static SequenceOccurrences create(final Sequence sequence, final Set<String> permutations) {
        Objects.requireNonNull(sequence);
        Objects.requireNonNull(permutations);
        final Map<String, Long> occurrences = permutations.stream().collect(
                Collectors.toMap(
                        Function.identity(),
                        permutation -> Pattern.compile(permutation).matcher(sequence.data()).results().count()
                )
        );
        return new SequenceOccurrences(sequence, occurrences);
    }
}
