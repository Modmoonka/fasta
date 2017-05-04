package com.github.leo_scream.fasta;

import java.util.Arrays;
import java.util.Objects;
import java.util.SortedSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.leo_scream.fasta.Expressions.numberOnPosition;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Permutations<T> {
    private final T[] choices;
    private final T[] positions;
    private final int size;

    @SuppressWarnings("unchecked")
    public Permutations(final SortedSet<T> choices, final T[] positions) {
        Objects.requireNonNull(choices);
        Objects.requireNonNull(positions);
        this.choices = (T[]) choices.toArray();
        this.positions = positions;
        this.size = choices.size() == 0 || positions.length == 0
                ? 0
                : (int) Math.pow(choices.size(), positions.length);
    }

    public int size() {
        return size;
    }

    public T[] get(final int index) {
        checkBound(index);
        final T[] permutation = Arrays.copyOf(positions, positions.length);
        for (int i = 0; i < positions.length; i++) {
            permutation[i] = choices[numberOnPosition(index, choices.length, positions.length - i - 1)];
        }
        return permutation;
    }

    public Stream<T[]> stream() {
        return IntStream.range(0, size)
                .mapToObj(this::get);
    }

    private void checkBound(final int index) {
        if (index < 0 || index > size) throw new ArrayIndexOutOfBoundsException();
    }

    public boolean equals(final Permutations<?> another) {
        return size == another.size
                && Arrays.equals(choices, another.choices)
                && positions.length == another.positions.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permutations)) return false;
        return equals((Permutations<?>) o);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Arrays.hashCode(choices);
        result = 31 * result + positions.length;
        result = 31 * result + size;
        return result;
    }
}
