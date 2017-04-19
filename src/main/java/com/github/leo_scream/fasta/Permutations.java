package com.github.leo_scream.fasta;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Permutations<T> {
    private final HashMap<Integer, T[]> cashed;
    private final T[] choices;
    private final T[] positions;
    private final int size;

    private Permutations(final T[] choices, final T[] positions) {
        this.choices = choices;
        this.positions = positions;
        this.size = choices.length == 0 || positions.length == 0
                ? 0
                : (int) Math.pow(choices.length, positions.length);
        this.cashed = new HashMap<>();
    }

    public T[] get(final int index) {
        checkBound(index);
        final T[] permutation;

        if (cashed.containsKey(index)) {
            permutation = cashed.get(index);
        } else {
            permutation = generate(index);
            cashed.put(index, permutation);
        }

        return permutation;
    }

    public Stream<T[]> stream() {
        return IntStream.range(0, size)
                .mapToObj(this::get);
    }

    public int size() {
        return size;
    }

    private T[] generate(final int index) {
        final T[] permutation = Arrays.copyOf(positions, positions.length);
        for (int i = 0; i < positions.length; i++) {
            permutation[i] = choices[Expressions.numberOnPosition(index, choices.length, positions.length - i - 1)];
        }
        return permutation;
    }

    private void checkBound(final int index) {
        if (index < 0 || index > size) throw new ArrayIndexOutOfBoundsException();
    }

    @SuppressWarnings("unchecked")
    public static <E> Permutations<E> of(final SortedSet<E> choices, final E[] positions) {
        Objects.requireNonNull(choices);
        Objects.requireNonNull(positions);
        return new Permutations<>((E[]) choices.toArray(), positions);
    }
}
