package com.github.leo_scream.fasta;

import java.util.Objects;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Sequence {
    private final String name;
    private final String data;
    private static final String dataPattern = "^[ACGT]++$";

    private Sequence(final String name, final String data) {
        this.name = name;
        this.data = data;
    }

    /**
     * Create new {@code Sequence} object.
     *
     * @param name sequence name
     * @param data sequence data. Must matches {@link #dataPattern}
     * @return new object of {@code Sequence} class
     * @throws NullPointerException     if {@code name} or {@code data} is null
     * @throws IllegalArgumentException if {@code data} not matches {@link #dataPattern}
     */
    public static Sequence create(final String name, final String data) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(data);
        if (!data.matches(dataPattern)) throw new IllegalArgumentException("Sequence data is not valid: " + data);
        return new Sequence(name, data);
    }
}
