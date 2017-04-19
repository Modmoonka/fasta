package com.github.leo_scream.fasta;

import java.lang.reflect.Array;
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
    private final int size;

    @SuppressWarnings("unchecked")
    public Permutations() {
        this((T[]) new ArrayList<T>().toArray(), 0);
    }

    @SuppressWarnings("unchecked")
    private Permutations(final T[] choices, final int positions) {
        this.choices = choices;
        this.positions = positions;
        this.size = choices.length == 0 || positions == 0
                ? 0
                : BigInteger.valueOf(choices.length).pow(positions).intValue();
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

    @SuppressWarnings("unchecked")
    private T[] generate(final int index) {
        return IntStream.range(0, positions)
                .map(position -> Expressions.numberOnPosition(index, choices.length, positions - position - 1))
                .mapToObj(choice -> choices[choice])
                .toArray(size -> (T[]) Array.newInstance(choices.getClass().getComponentType(), size));
    }

    private void checkBound(final int index) {
        if (index < 0 || index > size) throw new ArrayIndexOutOfBoundsException();
    }

    @SuppressWarnings("unchecked")
    public <E> Permutations<E> of(final SortedSet<E> choices) {
        Objects.requireNonNull(choices);
        return new Permutations<>((E[]) choices.toArray(), this.positions);
    }

    public Permutations<T> using(final int positions) {
        if (positions <= 0) throw new IllegalArgumentException("Length using sequences can't be negative.");
        return new Permutations<>(choices, positions);
    }
}
