package com.github.leo_scream.fasta

import spock.lang.Specification

/**
 * @author Denis Verkhoturov, mod.satyr@gmail.com
 */
class PermutationsSpecification extends Specification {
    Permutations<String> permutations

    def setup() {
        permutations = Permutations.of([] as SortedSet, new String[0])
    }

    def "Permutations throws NPE if constructed with null choices"() {
        when:
        permutations.of(null, null)

        then:
        thrown(NullPointerException)
    }

    def "Size calculates correctly"() {
        setup:
        permutations = permutations.of(alphabet as SortedSet, positions)

        expect:
        permutations.size() == size

        where:
        alphabet             | positions      || size
        ["A", "C", "G", "T"] | new String [4] || 256
        ["A", "C", "G", "T"] | new String [5] || 1024
        ["X", "Y"]           | new String [3] || 8
    }

    def "i-th permutation of negative index throws AIOBE"() {
        when:
        permutations.get(-1)

        then:
        thrown(ArrayIndexOutOfBoundsException)
    }

    def "i-th permutation of index greater than size throws AIOBE"() {
        when:
        permutations.get(permutations.size() + 1)

        then:
        thrown(ArrayIndexOutOfBoundsException)
    }

    def "i-th permutation of index works correctly"() {
        setup:
        permutations = permutations.of(["A", "C", "G", "T"] as SortedSet, new String[3])

        expect:
        permutations.get(index) == permutation as String[]

        where:
        permutation     || index
        ["A", "A", "A"] || 0
        ["A", "A", "C"] || 1
        ["C", "A", "A"] || 16
        ["C", "A", "G"] || 18
        ["T", "T", "T"] || 63
    }
}
