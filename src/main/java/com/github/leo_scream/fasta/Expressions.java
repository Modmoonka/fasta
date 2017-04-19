package com.github.leo_scream.fasta;

import static java.lang.StrictMath.pow;

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
public class Expressions {
    public static int numberOnPosition(final int number, final int radix, final int position) {
        return (int) (number % pow(radix, position + 1) / pow(radix, position));
    }
}
