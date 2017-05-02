package com.github.leo_scream.fasta;

import java.util.Objects;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Sequence {
    private final String name;
    private final String data;

    private Sequence(final String name, final String data) {
        this.name = name;
        this.data = data;
    }

    /**
     * @return Sequence name
     */
    public String name() {
        return name;
    }

    /**
     * @return Sequence data
     */
    public String data() {
        return data;
    }

    /**
     * Create new {@code Sequence} object.
     *
     * @param name sequence name
     * @param data sequence data. Must matches {@code dataPattern}
     * @return new object of {@code Sequence} class
     * @throws NullPointerException     if {@code name} or {@code data} is null
     * @throws IllegalArgumentException if {@code data} not matches {@code dataPattern}
     */
    public static Sequence create(final String name, final String data) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(data);
        final String dataPattern = "^[ACGT]++$";
        if (!data.matches(dataPattern)) throw new IllegalArgumentException("Sequence data is not valid: " + data);
        return new Sequence(name, data);
    }
}
