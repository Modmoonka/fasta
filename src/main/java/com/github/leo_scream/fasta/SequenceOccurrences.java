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

    public SequenceOccurrences(Sequence sequence) {
        Objects.requireNonNull(sequence);
        this.sequence = sequence;
    }

    public Sequence sequence() {
        return sequence;
    }

    public Map<String, Long> occurrences(final Set<String> permutations) {
        return permutations.stream().collect(
                Collectors.toMap(
                        Function.identity(),
                        permutation -> Pattern.compile(permutation).matcher(sequence.data()).results().count()
                )
        );
    }

    public boolean equals(final SequenceOccurrences another) {
        return sequence.equals(another.sequence);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SequenceOccurrences)) return false;
        return this.equals((SequenceOccurrences) o);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + sequence.hashCode();
        return result;
    }
}
