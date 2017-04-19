package com.github.leo_scream.fasta;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Permutations<T> {
    private final HashMap<Integer, T[]> cashed;
    private final T[] choices;
    private final int positions;
    private final BigInteger size;

    @SuppressWarnings("unchecked")
    public Permutations() {
        this((T[]) new ArrayList<T>().toArray(), 0);
    }

    @SuppressWarnings("unchecked")
    private Permutations(final T[] choices, final int positions) {
        this.choices = choices;
        this.positions = positions;
        this.size = choices.length == 0 || positions == 0
                ? BigInteger.ZERO
                : BigInteger.valueOf(choices.length).pow(positions);
        this.cashed = new HashMap<>();
    }

    public T[] get(final int index) {
        checkBound(index);
        final T[] permutation;

        if (index < cashed.size()) {
            permutation = cashed.get(index);
        } else {
            permutation = generate(index);
            cashed.put(index, permutation);
        }

        return permutation;
    }

    @SuppressWarnings("unchecked")
    private T[] generate(final int index) {
        return (T[]) IntStream.range(0, positions)
                .map(position -> Expressions.numberOnPosition(index, choices.length, positions - position - 1))
                .mapToObj(choice -> choices[choice])
                .toArray(Object[]::new);
    }

    public Stream<T[]> stream() {
        return cashed.values().stream();
    }

    public BigInteger size() {
        return size;
    }

    private void checkBound(final int index) {
        if (index < 0 || size.compareTo(BigInteger.valueOf(index)) < 0) throw new ArrayIndexOutOfBoundsException();
    }

    @SuppressWarnings("unchecked")
    public <E> Permutations<E> of(final SortedSet<E> choices) {
        Objects.requireNonNull(choices);
        return new Permutations<E>((E[]) choices.toArray(), this.positions);
    }

    public Permutations<T> using(final int positions) {
        if (positions <= 0) throw new IllegalArgumentException("Length using sequences can't be negative.");
        return new Permutations<T>(choices, positions);
    }
}
