package com.github.leo_scream.fasta;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Permutations {
    private final String[] alphabet;
    private final SortedSet<String> cashed;
    private final int length;
    private final BigInteger size;

    public Permutations() {
        this(new String[0], 0);
    }

    private Permutations(final String[] alphabet, final int length) {
        this.alphabet = alphabet;
        this.length = length;
        this.size = alphabet.length == 0 || length == 0
                ? BigInteger.ZERO
                : BigInteger.valueOf(alphabet.length).pow(length);
        this.cashed = new TreeSet<>();
    }

    public String get(final int n) {
        checkBound(n);
        return get(n, length - 1);
    }

    private String get(final int n, final int length) {
        int quotient = n / alphabet.length;
        int remainder = n % alphabet.length;
        return quotient == 0
                ? String.join("", Collections.nCopies(length, alphabet[0])) + alphabet[remainder]
                : get(quotient, length - 1) + alphabet[remainder];
    }

    public Stream<String> stream() {
        return cashed.stream();
    }

    public BigInteger size() {
        return size;
    }

    private void checkBound(final int index) {
        if (index < 0 || size.compareTo(BigInteger.valueOf(index)) < 0) throw new ArrayIndexOutOfBoundsException();
    }

    public Permutations with(final SortedSet<String> alphabet) {
        Objects.requireNonNull(alphabet);
        return new Permutations(alphabet.toArray(new String[0]), this.length);
    }

    public Permutations with(final int length) {
        if (length <= 0) throw new IllegalArgumentException("Length of sequences can't be negative.");
        return new Permutations(this.alphabet, length);
    }
}
